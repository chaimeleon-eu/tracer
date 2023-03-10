package es.upv.grycap.tracer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.BlockchainProperties;
import es.upv.grycap.tracer.model.IFilterParams;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.TracesFilteredPagination;
import es.upv.grycap.tracer.model.trace.v1.FilterParams;
import es.upv.grycap.tracer.model.trace.v1.Trace;

@Service
public interface BlockchainManager {
	
	//public ITransaction<T> generateTransaction(final TraceBase entry, String callerUserId);
	//public TraceCacheOpResult submitTransaction(final ITransaction<T> transaction);	
	
	public TraceCacheOpResult submitTrace(final TraceBase entry, String callerUserId);
	
	//public ITransaction getTransactionById(final String transactionId);
	public TraceCacheOpResult getTransactionStatusById(final String transactionId);
	//public List<Trace> getTraceEntriesByUserId(final String userId);
	public TracesFilteredPagination getTraces(final FilterParams filterParams, Integer offset, Integer limit);
	public TraceBase getTraceById(String traceId);
	public BlockchainProperties getBlockchainProperties();
	public BlockchainType getType();

}
