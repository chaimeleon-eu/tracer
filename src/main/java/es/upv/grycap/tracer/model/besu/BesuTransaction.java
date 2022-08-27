package es.upv.grycap.tracer.model.besu;

import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.Getter;

import org.web3j.protocol.core.methods.request.Transaction;


public class BesuTransaction implements ITransaction<TraceBase> {
	
	@Getter
	protected TraceBase trace;
	
	public BesuTransaction(final TraceBase trace) {
		this.trace = trace;
	}

	@Override
	public String getId() {
		return trace.getId();
	}

	@Override
	public TraceBase getTransaction() {
		return trace;
	}


}
