package es.upv.grycap.tracer.service.caching;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.upv.grycap.tracer.exceptions.UncheckedJsonMappingException;
import es.upv.grycap.tracer.exceptions.UncheckedJsonProcessingException;
import es.upv.grycap.tracer.model.TraceCacheDetailed;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.persistence.ITraceCacheDetailedRepo;
import es.upv.grycap.tracer.persistence.ITraceCacheSummaryRepo;
import es.upv.grycap.tracer.service.BlockchainManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TraceCacheOpSubmitter implements Supplier<TraceCacheOpResult> {
	
	//protected final Map<BlockchainType, BlockchainManager> managers;
	protected final ITraceCacheDetailedRepo reqCacheDetailedRepo;
	protected final BlockchainManager manager;
	protected String trace;
	//protected ReqDTO request;
	//protected String requestBody;
	protected String callerUserId;
	protected UUID reqCacheEntryId;
	
	public TraceCacheOpSubmitter(final ITraceCacheDetailedRepo reqCacheDetailedRepo, 
			final BlockchainManager manager, UUID reqCacheEntryId, String trace, String callerUserId) {
		this.reqCacheDetailedRepo = reqCacheDetailedRepo;
		this.manager = manager;
		this.trace = trace;
		this.reqCacheEntryId = reqCacheEntryId;
//		request = null;
//		this.requestBody = requestBody;
		this.callerUserId = callerUserId;
	}
	
//	public TraceCacheOpSubmitter(final IReqCacheSummaryRepo reqCacheSummaryRepo, 
//			final BlockchainManager manager, ReqDTO request, String callerUserId) {
//		this.reqCacheSummaryRepo = reqCacheSummaryRepo;
//		this.manager = manager;
//		this.request = request;
//		this.requestBody = null;
//		this.callerUserId = callerUserId;
//	}

	@Override
	public TraceCacheOpResult get() {
		try {
			ObjectMapper om = new ObjectMapper();
			TraceBase request = om.readValue(trace, TraceBase.class);
			updateStatus();
			return manager.submitTrace(request, callerUserId);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return new TraceCacheOpResult(ExceptionUtils.getStackTrace(e), ReqCacheStatus.ERROR, null);
		}
//		if (request.getBlockchains() == null) {
//			managers.values().forEach(mgr -> mgr.addTrace(request, callerUserId));
//		} else {
//			request.getBlockchains().forEach(bc -> managers.get(bc).addTrace(request, callerUserId));
//		}
		
	}
	
	@Transactional
	protected void updateStatus() {
        final TraceCacheDetailed rce = reqCacheDetailedRepo.getById(reqCacheEntryId);
        if (rce != null) {
            rce.setStatus(ReqCacheStatus.BLOCKCHAIN_WAITING);
            reqCacheDetailedRepo.saveAndFlush(rce);
        }
	}

}
