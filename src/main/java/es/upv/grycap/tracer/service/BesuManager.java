package es.upv.grycap.tracer.service;

import java.util.List;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.BesuException;
import es.upv.grycap.tracer.model.BesuProperties;
import es.upv.grycap.tracer.model.BesuTransaction;
import es.upv.grycap.tracer.model.BlockchainProperties;
import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.v1.Trace;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BesuManager implements BlockchainManager {
	
	protected final BesuProperties props;
	
	protected Credentials credentials;
	
	@Autowired
	public BesuManager(@Autowired final BesuProperties props) {
		this.props = props;
	}
	
	@PostConstruct
	public void init() {
		
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
		if (credentials != null) {
			String accountAddress = credentials.getAddress();
			log.info("Account address: " + accountAddress);
		} else {
			throw new Error("Unable to load credentials");
		}
	}
	
	public ITransaction<?> generateTransaction(final TraceBase entry, String callerUserId) {
		Transaction transaction = Transaction.createContractTransaction(
				 credentials.getAddress(),
	              BigInteger.ZERO,
	              Transaction.DEFAULT_GAS,  // we use default gas limit
	              "0x...<smart contract code to execute>"
	      );
		return new BesuTransaction(transaction);	
	}
//	
//	protected ChaimeleonTracingV1 loadContract() {
//		
//	}
	
	@Override
	public TraceCacheOpResult submitTransaction(final ITransaction<?> transaction) {
//		 Web3j web3 = Web3j.build(new HttpService(props.getUrl()));
//		 EthGetTransactionReceipt transactionReceipt =
//	             org.web3j.ethGetTransactionReceipt(transactionHash).send();
//
//		if (transactionReceipt.getTransactionReceipt.isPresent()) {
//		    String contractAddress = transactionReceipt.get().getContractAddress();
//		} else {
//		    // try again
//		}
//		 
//		 
//		 
//		 EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
//				 credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
//
//	     BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//
//	      org.web3j.protocol.core.methods.response.EthSendTransaction
//	              transactionResponse = parity.ethSendTransaction(ethSendTransaction)
//	              .send();
//
//	      String transactionHash = transactionResponse.getTransactionHash();
			return null;
	}


//	@Override
//	public Transaction<?, ?, ?> getTransactionById(String transactionId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public List<TraceSummaryBase> getTraces(FilterParams filterParams) {
		return List.of();
	}

	@Override
	public BlockchainProperties getBlockchainProperties() {
		return props;
	}

	@Override
	public TraceCacheOpResult getTransactionStatusById(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockchainType getType() {
		return BlockchainType.BESU_PRIVATE;
	}

	@Override
	public TraceBase getTraceById(String traceId) {
		// TODO Auto-generated method stub
		return null;
	}

}
