package es.upv.grycap.tracer.service.caching;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	//protected final IReqCacheDetailedRepo reqCacheDetailedRepo;
	protected final BlockchainManager manager;
	protected String trace;
	//protected ReqDTO request;
	//protected String requestBody;
	protected String callerUserId;
	
	public TraceCacheOpSubmitter(//final IReqCacheDetailedRepo reqCacheDetailedRepo, 
			final BlockchainManager manager, String trace, String callerUserId) {
		//this.reqCacheDetailedRepo = reqCacheDetailedRepo;
		this.manager = manager;
		this.trace = trace;
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
		//final ReqCacheEntryDetailed rce = reqCacheDetailedRepo.getById(reqCacheEntryId);
		ITransaction<?> tr = null;
		try {
			ObjectMapper om = new ObjectMapper();
			TraceBase request = om.readValue(trace, TraceBase.class);
			tr = manager.generateTransaction(request, callerUserId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String trId = tr == null ? null : tr.getId();
			return new TraceCacheOpResult(ExceptionUtils.getStackTrace(e), ReqCacheStatus.ERROR, trId);
		}

		try {
			return manager.submitTransaction(tr);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			String trId = tr == null ? null : tr.getId();
			return new TraceCacheOpResult(ExceptionUtils.getStackTrace(e), ReqCacheStatus.BLOCKCHAIN_ERROR, trId);
			
		}
//		if (request.getBlockchains() == null) {
//			managers.values().forEach(mgr -> mgr.addTrace(request, callerUserId));
//		} else {
//			request.getBlockchains().forEach(bc -> managers.get(bc).addTrace(request, callerUserId));
//		}
		
	}

}
