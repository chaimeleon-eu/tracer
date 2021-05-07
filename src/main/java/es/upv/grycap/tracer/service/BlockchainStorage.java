package es.upv.grycap.tracer.service;

import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.dto.EntryReqDTO;

@Service
public interface BlockchainStorage {
	
	public void addEntry(EntryReqDTO entry);

}
