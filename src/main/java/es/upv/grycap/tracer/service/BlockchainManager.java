package es.upv.grycap.tracer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.model.trace.v1.Trace;

@Service
public interface BlockchainManager {
	
	public void addEntry(ReqDTO entry, String callerUserId);	
	public Transaction<?, ?, ?> getTransactionById(final String transactionId);
	public List<Trace> getTraceEntriesByUserId(final String userId);
	public List<Trace> getTraceEntries();

}
