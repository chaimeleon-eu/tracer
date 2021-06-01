package es.upv.grycap.tracer.service;

import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.dto.EntryReqDTO;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;

@Service
public interface BlockchainManager {
	
	public void addEntry(EntryReqDTO entry);	
	public Transaction<?, ?, ?> getTransactionById(final String entry);

}
