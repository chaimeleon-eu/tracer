package es.upv.grycap.tracer.service.besu;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.BesuException;
import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.besu.BesuDeployedContract;
import es.upv.grycap.tracer.model.besu.BesuProperties;
import es.upv.grycap.tracer.model.besu.BesuProperties.ContractInfo;
import es.upv.grycap.tracer.model.besu.BesuProperties.ContractInfoEnable;
import es.upv.grycap.tracer.model.besu.BesuProperties.Gas;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.v1.FilterParams;
import es.upv.grycap.tracer.service.TimeManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public abstract class HandlerBesuContract<T extends Contract> {
	
	public static final BigInteger GAS_LIMIT = BigInteger.valueOf(9007199254740990L);
	
	public static final BigInteger GAS_PRICE = BigInteger.valueOf(0);
	
	protected final String url;
	
	protected final Credentials credentials;
	
	protected final TimeManager timeManager;
	
	protected ContractInfo contractInfo;
	
	protected Web3j web3j;
	
	protected AtomicReference<T> contract;
	
	protected BlockchainType btype;
	
	protected StaticGasProvider gasProvider;
	
	protected AtomicBoolean inited;
	
	protected int retryConnect;
	
	public HandlerBesuContract( BlockchainType btype, String url, final BesuProperties props, final Credentials credentials, final TimeManager timeManager) {
		this.url = url;
		this.credentials = credentials;
		this.timeManager = timeManager;
		this.btype = btype;
		this.retryConnect = props.getRetryConnect();
		for (ContractInfo ci: props.getContracts()) {
			if (ci.getName().equalsIgnoreCase(getContractName())) {
				this.contractInfo = ci;
			}
		}
		inited = new AtomicBoolean(false);
		contract =  new AtomicReference<>();
		contract.set(null);
		if (contractInfo == null) {
			log.warn("Contract " + getContractName() + " has been disabled due to no configuration entry found in the application yaml");
			contractInfo = new ContractInfo();
			contractInfo.setEnable(new ContractInfoEnable(false, false));
			contractInfo.setGas(new Gas(GAS_PRICE, GAS_LIMIT));
		} else {
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
	        builder.connectTimeout(100, TimeUnit.SECONDS);
	        builder.readTimeout(100, TimeUnit.SECONDS);
	        HttpService service = new HttpService(url, builder.build(), false);
			web3j = Web3j.build(service);
			
		}
		log.info("Init gas provider with price " + contractInfo.getGas().getPrice() + " and limit " + contractInfo.getGas().getLimit());
		gasProvider = new StaticGasProvider(contractInfo.getGas().getPrice(), contractInfo.getGas().getLimit());
	}
	
	public void init() {
		if (contractInfo.getEnable().isEnabled()) {
			CompletableFuture<T> initC = CompletableFuture.supplyAsync(
					() -> {
					T contracTmpt = null;
					boolean getContract = false;
					boolean unableToConnectMsg = true;
					while (!getContract) {
							
						try {
							Thread.sleep(retryConnect * 1000);
							contracTmpt = getDeployedContract();
							getContract = true;
						}  catch (ConnectException | NoRouteToHostException e) {
							if (unableToConnectMsg) {
								log.warn("Unable to connect to besu network (retry until connected every " + retryConnect + " seconds): " + e.getMessage());
								unableToConnectMsg = false;
							}
						} catch (IOException e) {
							log.error(Util.getFullStackTrace(e));
							getContract = true;
							//throw UncheckedExceptionFactory.get(e);
						} catch (InterruptedException e) {
							log.error(Util.getFullStackTrace(e));
							getContract = true;
						}
					}
					return contracTmpt;
				});
			initC.thenAcceptAsync((res) -> {
				log.info("Setting contract");
				if (res == null) {
					log.warn("Contract is null, check log for errors, disabling operations with blockchain handler " + getContractClass().getCanonicalName());
					contractInfo.setEnable(new ContractInfoEnable(false, false));
				} else {
					contract.set(res);						
				}
				inited.set(true);
			});
					
		}
	}
	
	public boolean isInited() {
		return inited.get();
	}
	
	public boolean canAdd() {
		return contractInfo.getEnable().isAdd();
	}
	
	public boolean canRead() {
		return contractInfo.getEnable().isRead();
	}
	
	public boolean isEnabled() {
		return canAdd() && canRead();
	}
	
	public String getContractName() {
		return getContractClass().getSimpleName();
	}
	
	public abstract TraceCacheOpResult submitTrace(final TraceBase entry, String callerUserId);
	public abstract TraceCacheOpResult getTransactionStatusById(String tId);
	public abstract List<TraceSummaryBase> getTraces(FilterParams filterParams);
	public abstract TraceBase getTraceById(String traceId);
	public abstract List<TraceSummaryBase> getTracesByValue(String value, BigInteger startPos, BigInteger endPos);
	public abstract BigInteger getTracesCount();
	
	protected T getDeployedContract() throws StreamReadException, DatabindException, IOException {
		Path p = Paths.get(contractInfo.getDeployed());
		BesuDeployedContract dc = null;
		T contract = null;
		EthGetCode code = null;
		ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		if (Files.exists(p)) {
			String dcs = Files.readString(p);
			dc = om.readValue(dcs, BesuDeployedContract.class);
			code = web3j.ethGetCode(dc.getAddress(), DefaultBlockParameterName.LATEST).send();
			log.info("Existing deployed contract: " + dc.getAddress());
			if (!code.getCode().equals(dc.getCode())) {
				log.warn("Code in deployed contract ' "
						+ dcs
						+ " ' loaded from "
						+ p.toAbsolutePath().toString()
						+ " differs from what was found at deployed contract's address: "
						+  code.getCode());
				log.info("Deploying contract " + dcs +  " @ " + url);
				contract = deployContract();
				Path newPath = Paths.get(p.toAbsolutePath().toString() + "." + timeManager.getTime() + ".bak");
				log.info("Store old contract at " + p.toAbsolutePath().toString() + "  in " + newPath.toAbsolutePath().toString());
				Files.move(p, newPath);
				log.info("Reload contract code from address : " + contract.getContractAddress());
				code = web3j.ethGetCode(contract.getContractAddress(), DefaultBlockParameterName.LATEST).send();
				dc = new BesuDeployedContract(contract.getContractAddress(), code.getCode(), 
						getContractClass().getCanonicalName(), Instant.ofEpochMilli(timeManager.getTime()));
				om.writeValue(p.toFile(), dc);
			} else {
				log.info("Loading contract from address " + dc.getAddress() + " @ " +  url);
				contract = loadContract(dc.getAddress());
			}
		} else {
			log.info("No contract found at " + p.toAbsolutePath().toString());
			if (canAdd()) {
				log.info("Contract is allowed to add new traces (from app's properties), deploy it on the blockchain @ " + url);
				contract = deployContract();
				log.info("Reload contract code from address : " + contract.getContractAddress());
				code = web3j.ethGetCode(contract.getContractAddress(), DefaultBlockParameterName.LATEST).send();
				dc = new BesuDeployedContract(contract.getContractAddress(), code.getCode(),
						getContractClass().getCanonicalName(), Instant.ofEpochMilli(timeManager.getTime()));
				p.toFile().getParentFile().mkdirs();
				om.writeValue(p.toFile(), dc);
				log.info("Contract stored on " + p.toAbsolutePath().toString());
			} else {
				log.info("Contract is not allowed to add new traces (from app's properties), don't deploy it on the blockchain");				
			}
		}
		return contract;
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

}
