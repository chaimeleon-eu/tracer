package es.upv.grycap.tracer.service.caching;

import java.util.function.Supplier;

import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.service.BlockchainManager;

public class TraceCacheOpUpdater implements Supplier<TraceCacheOpResult> {
	

	protected final BlockchainManager manager;
	
	protected String transactionId;
	
	public TraceCacheOpUpdater(final BlockchainManager manager, String transactionId) {
		this.manager = manager;
		this.transactionId = transactionId;
	}

	@Override
	public TraceCacheOpResult get() {
		return manager.getTransactionStatusById(transactionId);
	}

}
