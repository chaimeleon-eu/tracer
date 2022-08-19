package es.upv.grycap.tracer.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.BesuException;
import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.model.BesuProperties;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.besu.ChaimeleonTracingV1;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.v1.FilterParams;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BesuManagerV1 extends BesuManager<ChaimeleonTracingV1> {
	
	protected final BigInteger PG_SIZE = BigInteger.valueOf(50);

	@Autowired
	public BesuManagerV1(@Autowired TraceFiltering traceFiltering,
			@Autowired final BesuProperties props) {
		super(props);
	}

	@Override
	protected ChaimeleonTracingV1 deployContract() {
		try {
			return ChaimeleonTracingV1.deploy(
					web3j, credentials, getGasProvider()).send();
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
			throw new BesuException(e);
		}
	}

	@Override
	protected ChaimeleonTracingV1 loadContract(String address) {
		return ChaimeleonTracingV1.load(address, web3j, credentials, getGasProvider());
	}

	@Override
	public TraceCacheOpResult submitTrace(final TraceBase entry, String callerUserId) {
		ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
		
		String resultMsg = null;
		ReqCacheStatus resultStatus = null;
		String tId = null;
		try {
			TransactionReceipt receipt = contract.addTrace(om.writeValueAsString(entry)).send();
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
			if (transactionReceipt.hasError()) {
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
			BigInteger tracesCount = contract.getTracesCount().send();
			List<TraceBase> traces = new ArrayList<>();
			for (BigInteger idx=BigInteger.ZERO; idx.compareTo(tracesCount) == -1; idx.add(idx)) {
				traces.addAll(getTracesSubArray(idx, idx.add(PG_SIZE)));
			}
			BigInteger numEls = tracesCount.mod(PG_SIZE);
			traces.addAll(getTracesSubArray(tracesCount.subtract(numEls), numEls));
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
			BigInteger tracesCount = contract.getTracesCount().send();
			for (BigInteger idx=BigInteger.ZERO; idx.compareTo(tracesCount) == -1; idx.add(idx)) {
				trace = getTracesSubArray(idx, idx.add(PG_SIZE)).stream().filter(t -> t.getId().equals(traceId))
					.findFirst().orElse(null);
				if (trace != null) {
					break;
				}
			}
			if (trace != null) {
			BigInteger numEls = tracesCount.mod(PG_SIZE);
			trace = getTracesSubArray(tracesCount.subtract(numEls), numEls)
					.stream().filter(t -> t.getId().equals(traceId))
					.findFirst().orElse(null);
			}
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
		}
		return trace;
	}
	
	protected List<TraceBase> getTracesSubArray(BigInteger startPos, BigInteger maxNumElems) throws Exception {
		ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
		List<String> tracesStr = contract.getTracesSubarray(startPos, maxNumElems).send();
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
	}


}
