package es.upv.grycap.tracer.model;

import es.upv.grycap.tracer.model.dto.ITransaction;
import lombok.Getter;

import org.web3j.protocol.core.methods.request.Transaction;


public class BesuTransaction implements ITransaction {
	
	@Getter
	protected Transaction transaction;
	
	public BesuTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public String getId() {
		return transaction.getValue();
	}


}
