package es.upv.grycap.tracer.service.besu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.BesuException;
import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.model.BlockchainProperties;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.besu.BesuDeployedContract;
import es.upv.grycap.tracer.model.besu.BesuProperties;
import es.upv.grycap.tracer.model.besu.BesuTransaction;
import es.upv.grycap.tracer.model.besu.BesuProperties.ContractInfo;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.TraceVersion;
import es.upv.grycap.tracer.model.trace.v1.FilterParams;
import es.upv.grycap.tracer.model.trace.v1.Trace;
import es.upv.grycap.tracer.service.BlockchainManager;
import es.upv.grycap.tracer.service.TimeManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BesuManager implements BlockchainManager {
	
	protected final BesuProperties props;
	
	protected final TimeManager timeManager;
	
	protected Credentials credentials;
	
	//protected String walletAddress;
	protected List<HandlerBesuContract<? extends Contract>> enabledContracts;
	
	protected Map<String, HandlerBesuContract<? extends Contract>> handlersByContractName;
	
	@Autowired
	public BesuManager(@Autowired final BesuProperties props, @Autowired final TimeManager timeManager) {
		this.props = props;
		this.timeManager = timeManager;
	}
	
	
	
	@PostConstruct
	public void init() {

		enabledContracts = new ArrayList<>();
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
		
		Reflections reflections = new Reflections(this.getClass().getPackageName());    
		//Set<Class<? extends HandlerBesuContract>> classes 
		handlersByContractName = reflections.getSubTypesOf(HandlerBesuContract.class).stream()
				.map(cl -> {
					Constructor<? extends HandlerBesuContract> cons;
					try {
						cons = cl.getConstructor(BlockchainType.class, String.class, 
								BesuProperties.class, Credentials.class, TimeManager.class);
						HandlerBesuContract<?> obj = cons.newInstance(getType(), props.getUrl(), props, credentials, timeManager);
						obj.init();
						if (obj.isEnabled()) {
							log.info("Contract " + obj.getContractName() 
									+ " initialized with "
									+ (obj.canAdd() ? (obj.canRead() ? "add/read" : "add") 
											     : (obj.canRead() ? "read" : "no ops") )
									+ " support");
							return obj;
						} else
							return null;
					} catch (Exception e) {
						log.error(Util.getFullStackTrace(e));						
						return null;
					}
					
				})
				.filter(handler -> handler != null)
				.collect(Collectors.toUnmodifiableMap(HandlerBesuContract::getContractName, Function.identity()));
		//HandlerBesuContract<?> hbs = handlersByContractName.values().iterator().next();

//		log.info("traces count: " + hbs.getTracesCount());
//		hbs.getTracesByValue("CREATE_DATASET", BigInteger.valueOf(0), BigInteger.valueOf(0))
//		.forEach(tsb -> log.info(tsb.getId()));
		
//		// load contracts
//		props.getContracts().forEach(c -> {
//			if (c.isEnabled()) {
//				enabledContracts.add(handlersByContractName.get(c.getName()));
//				log.info("Contract " + c.getName() + " has been enabled");
//			} else
//				log.info("Contract " + c.getName() + " is disabled");
//		});
	}
	
	
	
	
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



	@Override
	public TraceCacheOpResult submitTrace(TraceBase entry, String callerUserId) {
		TraceCacheOpResult result = checkContractsInited();
		if (result != null)
			return result;
		for (HandlerBesuContract<? extends Contract> handler: handlersByContractName.values()) {
				if (handler.canAdd()) {
					log.info("Add trace to contract " + handler.getContractName());
					result = handler.submitTrace(entry, callerUserId);
				} else {
					log.info("Contract " + handler.getContractName() + " cannot add traces.");
				} 
			
		}
		return result;
	}



	@Override
	public TraceCacheOpResult getTransactionStatusById(String transactionId) {
		TraceCacheOpResult result = null;
		for (HandlerBesuContract<? extends Contract> handler: handlersByContractName.values()) {
			if (handler.isInited()) {
				if (handler.canRead()) {
					log.info("Searching transaction with ID '" 
							+ transactionId
							+ "' on contract " + handler.getContractName());
					result = handler.getTransactionStatusById(transactionId);
					if (result.getStatus() != ReqCacheStatus.BLOCKCHAIN_NOT_FOUND) {
						break;
					}
				}
			} else {
				log.warn("Contract " + handler.getContractName() + " not inited, skipping.");
			}
		}
		if (result == null) {
			result = new TraceCacheOpResult("Transaction not found on the blockchain",
					ReqCacheStatus.BLOCKCHAIN_NOT_FOUND, transactionId);
		}
		return result;
	}



	@Override
	public List<TraceSummaryBase> getTraces(FilterParams filterParams, Integer offset, Integer limit) {
		List<TraceSummaryBase> result = new ArrayList<>();
		for (HandlerBesuContract<? extends Contract> handler: handlersByContractName.values()) {
			if (handler.isInited()) {
				if (handler.canRead()) {
	//				if (filterParams.hasBlockchains() && !filterParams.containsBlockchain(getType()))
	//					continue;
					log.info("Searching traces on contract " + handler.getContractName());
					List<TraceSummaryBase> partialResult = handler.getTraces(filterParams, offset, limit);
					result.addAll(partialResult);
				}
			} else {
				log.warn("Contract " + handler.getContractName() + " not inited, skipping.");
			}
		}
		return result;
	}



	@Override
	public TraceBase getTraceById(String traceId) {
		TraceBase trace = null;
		for (HandlerBesuContract<? extends Contract> handler: handlersByContractName.values()) {
			if (handler.isInited()) {
				if (handler.canRead()) {
					log.info("Searching trace ID '" + traceId
							+ "' on contract " + handler.getContractName());
					trace = handler.getTraceById(traceId);
					if (trace != null) {
						break;
					}
				}
			} else {
				log.warn("Contract " + handler.getContractName() + " not inited, skipping.");
			}
		}
		return trace;
	}
	
	protected TraceCacheOpResult checkContractsInited() {

		for (HandlerBesuContract<? extends Contract> handler: handlersByContractName.values()) {
			if (!handler.isInited()) {
				return new TraceCacheOpResult("Contract " + handler.getContractName() 
					+ " is not inited. Cannot continue untill all enabled contracts are inited.",
						ReqCacheStatus.BLOCKCHAIN_UNAVAILABLE, null);
			}
		}
		return null;
	}

}
