package es.upv.grycap.tracer.service.besu;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.BesuException;
import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.exceptions.UnhandledException;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.besu.BesuProperties;
import es.upv.grycap.tracer.model.besu.ChaimeleonTracer_V1;
import es.upv.grycap.tracer.model.besu.BesuProperties.ContractInfo;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.v1.FilterParams;
import es.upv.grycap.tracer.service.TimeManager;
import es.upv.grycap.tracer.service.TraceFiltering;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerChaimeleonTracer_V1 extends HandlerBesuContract<ChaimeleonTracer_V1> {
	
	public static enum ResultCode {
		SUCCESS(0), PARAMS_ERROR(1);
		
		@Getter
		protected long id;
		
		private ResultCode(long id) {
			this.id = id;
		}
		
		public static ResultCode fromId(BigInteger id) {
			long idL = id.longValueExact();
			for (ResultCode v: ResultCode.values()) {
				if (idL == v.getId())
					return v;
			}
			throw new UnhandledException("Unhandled result code received from contract " + id);
		}
	}
	
	protected final BigInteger PG_SIZE = BigInteger.valueOf(50);

	public HandlerChaimeleonTracer_V1( BlockchainType btype, String url, final BesuProperties props, final Credentials credentials, final TimeManager timeManager) {
		super(btype, url, props, credentials, timeManager);
	}

	@Override
	protected ChaimeleonTracer_V1 loadContract(String address) {
		return ChaimeleonTracer_V1.load(address, web3j, credentials, getGasProvider());
	}

	@Override
	public TraceCacheOpResult submitTrace(final TraceBase entry, String callerUserId) {
		ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
		
		String resultMsg = null;
		ReqCacheStatus resultStatus = null;
		String tId = null;
		
		try {
			TransactionReceipt receipt = contract.addTrace(BigInteger.valueOf(timeManager.getTime()), om.writeValueAsString(entry)).send();
			tId = receipt.getTransactionHash();
			if (!receipt.isStatusOK()) {
				resultMsg = receipt.getStatus();
				resultStatus = ReqCacheStatus.BLOCKCHAIN_SUBMISSION_ERROR;
			} else {
				resultStatus = ReqCacheStatus.BLOCKCHAIN_SUCCESS;
			}
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
			resultMsg = Util.getFullStackTrace(e);
			resultStatus = ReqCacheStatus.ERROR;
		}
		return new TraceCacheOpResult(resultMsg, resultStatus, tId);
	}

	@Override
	public TraceCacheOpResult getTransactionStatusById(String tId) {
		
		String resultMsg = null;
		ReqCacheStatus resultStatus = null;
		try {
			EthGetTransactionReceipt transactionReceipt = 
				    web3j.ethGetTransactionReceipt(tId).send();
			if (!transactionReceipt.getTransactionReceipt().isPresent()) {
				resultMsg = "Transaction with hash '" 
						+ tId + "' not found on the blockchain";
				resultStatus = ReqCacheStatus.BLOCKCHAIN_NOT_FOUND;
			} else if (transactionReceipt.hasError()) {
				resultMsg = "Message: '" 
						+ transactionReceipt.getError().getMessage()
						+ "', data: '"
						+ transactionReceipt.getError().getData()
						+ "', code: " + transactionReceipt.getError().getCode();
				resultStatus = ReqCacheStatus.BLOCKCHAIN_ERROR;				
			} else {
				if (transactionReceipt.getResult().isStatusOK()) {
					resultStatus = ReqCacheStatus.BLOCKCHAIN_SUCCESS;
				} else {
					resultMsg = transactionReceipt.getResult().getLogs().stream()
							.map(l -> l.getLogIndex() + ". " + l.getData())
							.collect(Collectors.joining(",\n"));
					resultStatus = ReqCacheStatus.BLOCKCHAIN_ERROR;	
				}
			}
		} catch (IOException e) {
			log.error(Util.getFullStackTrace(e));
			resultMsg = Util.getFullStackTrace(e);
			resultStatus = ReqCacheStatus.BLOCKCHAIN_ERROR;
		}
		return new TraceCacheOpResult(resultMsg, resultStatus, tId);
	}

	@Override
	public List<TraceSummaryBase> getTraces(FilterParams filterParams) {
		try {
			BigInteger tracesCount = contract.getTracesCount().send().component1();
			Collection<TraceBase> traces = new ArrayList<>();
			BigInteger steps = tracesCount.divide(PG_SIZE);
			for (BigInteger idx=BigInteger.ZERO; idx.compareTo(steps) == -1; idx.add(BigInteger.ONE)) {
				traces.addAll(getTracesSubArray(idx.multiply(PG_SIZE), PG_SIZE));
			}
			BigInteger numEls = tracesCount.mod(PG_SIZE);
			if (numEls.compareTo(BigInteger.ZERO) == 1)
				traces.addAll(getTracesSubArray(tracesCount.subtract(numEls), numEls));
			traces = filterParams.filterTraces(btype, traces);
			return traces.stream().map(e -> e.toSummary()).toList();
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
			return List.of();
		}
	}
	
	@Override
	public TraceBase getTraceById(String traceId) {
		TraceBase trace = null;

		try {
			BigInteger tracesCount = contract.getTracesCount().send().component1();
			ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
			Tuple3<List<String>, BigInteger, String> result = 
					contract.getTracesByValue(traceId, BigInteger.ZERO, tracesCount.subtract(BigInteger.ONE)).send();
			ResultCode rc = ResultCode.fromId(result.component2());
			if (rc == ResultCode.SUCCESS) {
				List<String> tracesStr = result.component1();
				trace = tracesStr.stream()
						.map(ts -> {
							try {
								return om.readValue(ts, TraceBase.class);
							} catch (JsonProcessingException e) {
								log.error(Util.getFullStackTrace(e));
								throw UncheckedExceptionFactory.get(e);
							}
						})
						.filter(t -> t.getId().equals(traceId))
						.findFirst().orElse(null);
			} else {
				String msg = result.component3();
				log.error("Error from the blockchain code " + rc.name() 
					+ " with message '" + msg + "'");
			}
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
		}
		return trace;
	}

//	@Override
//	public TraceBase getTraceById(String traceId) {
//		TraceBase trace = null;
//
//		try {
//			BigInteger tracesCount = contract.getTracesCount().send();
//			BigInteger steps = tracesCount.divide(PG_SIZE);
//			for (BigInteger idx=BigInteger.ZERO; idx.compareTo(steps) == -1; idx.add(BigInteger.ONE)) {
//				trace = getTracesSubArray(idx.multiply(PG_SIZE), PG_SIZE)
//						.stream().filter(t -> t.getId().equals(traceId))
//						.findFirst().orElse(null);
//				if (trace != null) {
//					break;
//				}
//			}
//			if (trace == null) {
//				BigInteger numEls = tracesCount.mod(PG_SIZE);
//				if (numEls.compareTo(BigInteger.ZERO) == 1)
//					trace = getTracesSubArray(tracesCount.subtract(numEls), numEls)
//							.stream().filter(t -> t.getId().equals(traceId))
//							.findFirst().orElse(null);
//			}
//		} catch (Exception e) {
//			log.error(Util.getFullStackTrace(e));
//		}
//		return trace;
//	}
	
	@Override
	protected ChaimeleonTracer_V1 deployContract() {
		try {
			return ChaimeleonTracer_V1.deploy(
					web3j, credentials, getGasProvider()).send();
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
			throw new BesuException(e);
		}
	}
	
	protected List<TraceBase> getTracesSubArray(BigInteger startPos, BigInteger maxNumElems) throws Exception {
		ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
		Tuple3<List<String>,BigInteger,String> result = contract.getTracesSubarray(startPos, maxNumElems).send();
		ResultCode rc = ResultCode.fromId(result.component2());
		if (rc == ResultCode.SUCCESS) {
			List<String> tracesStr = result.component1();
			List<TraceBase> traces = tracesStr.stream()
					.map(ts -> {
						try {
							return om.readValue(ts, TraceBase.class);
						} catch (JsonProcessingException e) {
							log.error(Util.getFullStackTrace(e));
							throw UncheckedExceptionFactory.get(e);
						}
					})
					.collect(Collectors.toList());
			return traces;
		} else {
			String msg = result.component3();
			log.error("Error from the blockchain code " + rc.name() 
				+ " with message '" + msg + "'");
			return List.of();
			
		}
	}

	@Override
	protected Class<ChaimeleonTracer_V1> getContractClass() {
		return ChaimeleonTracer_V1.class;
	}

	@Override
	public List<TraceSummaryBase> getTracesByValue(String value, BigInteger startPos, BigInteger endPos) {
		try {
			Tuple3<List<String>,BigInteger,String> result = contract.getTracesByValue(value, startPos, endPos).send();
			ResultCode rc = ResultCode.fromId(result.component2());
			if (rc == ResultCode.SUCCESS) {
				List<String> tracesStr = result.component1();
				ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
				List<TraceSummaryBase> traces = tracesStr.stream()
						.map(ts -> {
							try {
								TraceBase tb = om.readValue(ts, TraceBase.class);
								return tb.toSummary();
							} catch (JsonProcessingException e) {
								log.error(Util.getFullStackTrace(e));
								throw UncheckedExceptionFactory.get(e);
							}
						})
						.collect(Collectors.toList());
				return traces;
			} else {
				String msg = result.component3();
				log.error("Error from the blockchain code " + rc.name() 
					+ " with message '" + msg + "'");
				return null;
				
			}
				
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
			return List.of();
		}
	}

	@Override
	public BigInteger getTracesCount() {
		try {
			Tuple3<BigInteger,BigInteger,String> result = contract.getTracesCount().send();
			ResultCode rc = ResultCode.fromId(result.component2());
			if (rc == ResultCode.SUCCESS) {
				return result.component1();
			} else {
				String msg = result.component3();
				log.error("Error from the blockchain code " + rc.name() 
					+ " with message '" + msg + "'");
				return null;
				
			}
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
			return null;
		}
	}


}
