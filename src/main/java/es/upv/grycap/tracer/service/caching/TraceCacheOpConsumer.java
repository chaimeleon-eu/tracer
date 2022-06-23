package es.upv.grycap.tracer.service.caching;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.model.ReqCacheEntryDetailed;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.persistence.IReqCacheDetailedRepo;
import es.upv.grycap.tracer.service.BlockchainManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TraceCacheOpConsumer implements Consumer<TraceCacheOpResult> {
	
	protected final BlockchainManager manager;
	protected final UUID reqCacheEntryId;
	
	protected final IReqCacheDetailedRepo reqCacheDetailedRepo;
	
	protected final Executor executorCacheSubmitter;
	
	protected final int retryDelay;
	
	public TraceCacheOpConsumer(final BlockchainManager manager, UUID reqCacheEntryId, final IReqCacheDetailedRepo reqCacheDetailedRepo, 
			final Executor executorCacheSubmitter, int retryDelay) {
		this.reqCacheEntryId = reqCacheEntryId;
		this.reqCacheDetailedRepo = reqCacheDetailedRepo;
		this.executorCacheSubmitter = executorCacheSubmitter;
		this.retryDelay = retryDelay;
		this.manager = manager;
	}

	@Override
	public void accept(TraceCacheOpResult result) {
		update(result);
		
	}
	
	@Transactional
	protected void update(TraceCacheOpResult result) {
		Optional<ReqCacheEntryDetailed> rceO = reqCacheDetailedRepo.findById(reqCacheEntryId);
		// If the entry has been added successfully to the blockchain,
		// remove from cache
		
		if (rceO.isPresent()) {
			ReqCacheEntryDetailed rce = rceO.get();
			if (result.getStatus() != ReqCacheStatus.BLOCKCHAIN_SUCCESS) {
				rce.setModificationDate(Instant.now());
				rce.setStatus(result.getStatus());
				rce.setStatusMessage(result.getMsg());
				if (result.getTransactionId() != null) {
					// if waiting for the blockchain to complete
					rce.setTransactionId(result.getTransactionId());
				}
				reqCacheDetailedRepo.saveAndFlush(rce);
				Util.loopRequestFutures(result.getStatus(), retryDelay,
						manager, rce,reqCacheDetailedRepo, executorCacheSubmitter);
			} else {
				reqCacheDetailedRepo.delete(rce);
			}
		}
	}
	
}
