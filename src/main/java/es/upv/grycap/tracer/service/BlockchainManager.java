package es.upv.grycap.tracer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.IFilterParams;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.model.trace.v1.Trace;

@Service
public interface BlockchainManager {
	
	public void addTrace(ReqDTO entry, String callerUserId);	
	public Transaction<?, ?, ?> getTransactionById(final String transactionId);
	//public List<Trace> getTraceEntriesByUserId(final String userId);
	public List<Trace> getTraces(final FilterParams filterParams);

}
