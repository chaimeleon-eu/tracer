package es.upv.grycap.tracer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.BlockchainProperties;
import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.IFilterParams;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.v1.Trace;

@Service
public interface BlockchainManager {
	
	public ITransaction<?> generateTransaction(final ReqDTO entry, String callerUserId);
	public TraceCacheOpResult submitTransaction(final ITransaction<?> transaction);	
	
	//public ITransaction getTransactionById(final String transactionId);
	public TraceCacheOpResult getTransactionStatusById(final String transactionId);
	//public List<Trace> getTraceEntriesByUserId(final String userId);
	public List<TraceSummaryBase> getTraces(final FilterParams filterParams);
	public TraceBase getTraceById(String traceId);
	public BlockchainProperties getBlockchainProperties();
	public BlockchainType getType();

}
