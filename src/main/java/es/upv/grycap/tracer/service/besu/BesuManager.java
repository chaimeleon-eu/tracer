package es.upv.grycap.tracer.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.BesuException;
import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.model.BesuProperties;
import es.upv.grycap.tracer.model.BesuTransaction;
import es.upv.grycap.tracer.model.BlockchainProperties;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.besu.BesuDeployedContract;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.v1.FilterParams;
import es.upv.grycap.tracer.model.trace.v1.Trace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BesuManager<T extends Contract> implements BlockchainManager {
	
	public static final BigInteger GAS_LIMIT = BigInteger.valueOf(3000000);
	
	protected final BesuProperties props;
	
	protected Credentials credentials;
	
	//protected String walletAddress;
	
	protected T contract;
	
	protected Web3j web3j;
	
	@Autowired
	public BesuManager(@Autowired final BesuProperties props) {
		this.props = props;
	}
	
	
	
	@PostConstruct
	public void init() {
		web3j = Web3j.build(new HttpService(props.getUrl()));
		
		try {
			File[] files = new File(props.getWallet().getPath()).listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
				
			});
			if (files == null || files.length == 0) {
				throw new BesuException("No files found in Besu's wallet directory " + props.getWallet().getPath());
			} else if (files.length > 1) {
				throw new BesuException("Multiple files found in Besu's wallet directory " + props.getWallet().getPath() + " when there only one must exist at on this path");
			} else {
				log.info("Try to load existing wallet at " + files[0].getAbsolutePath());
				credentials = WalletUtils.loadCredentials(props.getWallet().getPassword(), files[0].getAbsolutePath());
			}
		} catch (IOException | CipherException | BesuException e) {
			log.error(Util.getFullStackTrace(e));
			log.info("Try to create a new wallet");
			try {
				Files.createDirectories(Paths.get(props.getWallet().getPath()));
				String walletName = WalletUtils.generateNewWalletFile(props.getWallet().getPassword(), 
						new File(props.getWallet().getPath()));
				credentials = WalletUtils.loadCredentials(props.getWallet().getPassword(), 
						Paths.get(props.getWallet().getPath(), walletName).toAbsolutePath().toString());
			} catch (Exception ex) {
				throw new Error("Unable to load or create new wallet");
			}
		}
		if (credentials == null) {
			throw new BesuException("Unable to load credentials");
		}
		
		try {
			contract = getDeployedContract();
		} catch (IOException e) {
			log.error(Util.getFullStackTrace(e));
			throw UncheckedExceptionFactory.get(e);
		}
	}
	
	protected T getDeployedContract() throws StreamReadException, DatabindException, IOException {
		Path p = Paths.get(props.getContract().getDeployed());
		BesuDeployedContract dc = null;
		T contract = null;
		EthGetCode code = null;
		ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		if (Files.exists(p)) {
			String dcs = Files.readString(p);
			log.info("Existing deployed contract: " + dcs);
			dc = om.readValue(dcs, BesuDeployedContract.class);
			code = web3j.ethGetCode(dc.getAddress(), DefaultBlockParameterName.LATEST).send();
			if (!code.getCode().equals(dc.getCode())) {
				log.warn("Code in deployed contract ' "
						+ dcs
						+ " ' loaded from "
						+ p.toAbsolutePath().toString()
						+ " differs from what was found at deployed contract's address: "
						+  code.getCode());
				log.info("Deploying contract " + dcs);
				contract = deployContract();
				log.info("Replace old contract at " + p.toAbsolutePath().toString());
				Files.delete(p);
				log.info("Reload contract code from address : " + contract.getContractAddress());
				code = web3j.ethGetCode(contract.getContractAddress(), DefaultBlockParameterName.LATEST).send();
				dc = new BesuDeployedContract(contract.getContractAddress(), code.getCode(), 
						getContractClass().getCanonicalName());
				om.writeValue(p.toFile(), dc);
			} else {
				log.info("Loading contract from address " + dc.getAddress());
				contract = loadContract(dc.getAddress());
			}
		} else {
			log.info("No contract found at " + p.toAbsolutePath().toString());
			contract = deployContract();
			log.info("Reload contract code from address : " + contract.getContractAddress());
			code = web3j.ethGetCode(contract.getContractAddress(), DefaultBlockParameterName.LATEST).send();
			dc = new BesuDeployedContract(contract.getContractAddress(), code.getCode(),
					getContractClass().getCanonicalName());
			p.toFile().getParentFile().mkdirs();
			om.writeValue(p.toFile(), dc);
			log.info("Contract written on " + p.toAbsolutePath().toString());
		}
		return contract;
	}
	
	protected ContractGasProvider getGasProvider() {
		return new StaticGasProvider(BigInteger.ZERO, GAS_LIMIT);
	}
	
//	protected BigInteger getGasPrice() {
//		return BigInteger.ZERO;
//	}
//	
//	protected BigInteger getGasLimit() {
//		return BigInteger.valueOf(Integer.MAX_VALUE);
//	}
	
	protected abstract T deployContract();
	
	protected abstract T loadContract(String address);
	
	protected abstract Class<T> getContractClass();
//	{
//		EthGetTransactionCount nonceResp = web3j
//	            .ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
//	            .send();//.getTransactionCount();
//		if (nonceResp.hasError()) {
//			log.error(nonceResp.getError().toString());
//			throw new BesuException(nonceResp.getError().toString());
//		} else {
//			BigInteger nonce = nonceResp.getTransactionCount();
////			RawTransaction rawTransaction = RawTransaction.createContractTransaction(
////					nonce,
////			        GAS_PRICE, GAS_LIMIT,
////			        BigInteger.ZERO,
////			        compiledCode);
//			TransactionManager txManager = new RawTransactionManager(web3j, credentials);
//			// Send transaction
//			String txHash = txManager.sendTransaction(
//					GAS_PRICE, GAS_LIMIT, 
//			    contractAddress, 
//			    compiledCode, 
//			    BigInteger.ZERO).getTransactionHash();
//
//			// Wait for transaction to be mined
//			TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
//			    web3j, 
//			    TransactionManager.DEFAULT_POLLING_FREQUENCY, 
//			    TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
//			TransactionReceipt txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
//	
//			// get contract address
//			return  web3j.ethGetTransactionReceipt(transactionHash).send();
//		}
//	}
	
	//protected abstract T deployLoadContract();
	
//	
//	protected ChaimeleonTracingV1 loadContract() {
//		
//	}

	@Override
	public BlockchainType getType() {
		return BlockchainType.BESU_PRIVATE;
	}
	
	@Override
	public BlockchainProperties getBlockchainProperties() {
		return props;
	}

}
