package es.upv.grycap.tracer.service.caching;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.activation.UnsupportedDataTypeException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.CaseNotHandledException;
import es.upv.grycap.tracer.exceptions.UncheckedJsonProcessingException;
import es.upv.grycap.tracer.exceptions.UncheckedUnsupportedDataTypeException;
import es.upv.grycap.tracer.model.IReqCacheEntry;
import es.upv.grycap.tracer.model.TraceCacheDetailed;
import es.upv.grycap.tracer.model.TraceCacheSummary;
import es.upv.grycap.tracer.model.TracerRoles;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.persistence.ITraceCacheDetailedRepo;
import es.upv.grycap.tracer.persistence.ITraceCacheSummaryRepo;
import es.upv.grycap.tracer.service.BlockchainManager;
import es.upv.grycap.tracer.service.BlockchainManagersRepo;
import es.upv.grycap.tracer.service.UserManager;

/**
 * Manage the requests cache, including submission of a trace to a blockchain and update it's status. 
 * A cached entry is removed only when the submission has been successfully executed (the blockchain(s) 
 * have accepted it)
 * 
 * @author Andy S Alic
 *
 */
@Service
public class CachingManager {
	
	protected BlockchainManagersRepo blockchainManagersRepo;
	
	protected UserManager userManager;
	
	/**
	 * Launches the threads that submit to a blockchain / update the status of a trace 
	 */
	protected ExecutorService executorCache;
	
	protected ITraceCacheSummaryRepo reqCacheSummaryRepo;
	protected ITraceCacheDetailedRepo reqCacheDetailedRepo;
	
	protected int retryDelay;
	
	@Autowired
	public CachingManager(@Autowired BlockchainManagersRepo blockchainManagersRepo, 
			@Autowired UserManager userManager, @Autowired ITraceCacheSummaryRepo reqCacheSummaryRepo,
			@Autowired ITraceCacheDetailedRepo reqCacheDetailedRepo,
			@Value("${tracer.cache.threads}") int cacheThreads,
			@Value("${tracer.cache.retry-delay}") int retryDelay) {
		this.blockchainManagersRepo = blockchainManagersRepo;
		this.userManager = userManager;
		this.reqCacheSummaryRepo = reqCacheSummaryRepo;
		this.reqCacheDetailedRepo = reqCacheDetailedRepo;
		this.retryDelay = retryDelay;
		executorCache = Executors.newFixedThreadPool(cacheThreads);
		
	}
	
	@PostConstruct
	public void init() {

		List<TraceCacheDetailed> reqs = reqCacheDetailedRepo.findAllByStatuses(
				List.of(ReqCacheStatus.WAITING, ReqCacheStatus.BLOCKCHAIN_WAITING,
						ReqCacheStatus.BLOCKCHAIN_UNAVAILABLE));//example);
		
		//final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		reqs.forEach(reqCache -> {
			BlockchainManager mgr = blockchainManagersRepo.getActiveManagers().get(reqCache.getBlockchainType());
			Util.loopRequestFutures(reqCache.getStatus(), retryDelay, mgr, reqCache, reqCacheDetailedRepo, executorCache);
			
		});
	}
	
    @PreDestroy
    public void destroy() {
    	executorCache.shutdown();
    }
    
    public TraceCacheSummary addTraceCache(final Authentication authentication, 
    		final TraceBase req, final BlockchainManager mgr) {
		final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		String reqStr;
		try {
			reqStr = mapper.writeValueAsString(req);
			final Instant now = Instant.now();
			final TraceCacheDetailed rce = TraceCacheDetailed.builder()
					.blockchainType(mgr.getType())
					.creationDate(now)
					.callerId(req.getCallerId())
					.id(UUID.randomUUID())
					.modificationDate(now)
					.trace(reqStr)
					.status(ReqCacheStatus.WAITING)
					.traceId(req.getId())
					.build();
			//rces.add(rce);
			reqCacheDetailedRepo.saveAndFlush(rce);
			
			CompletableFuture.supplyAsync(new TraceCacheOpSubmitter(mgr, rce.getTrace(), rce.getCallerId()), 
					executorCache)
					.thenAccept(new TraceCacheOpConsumer(mgr, rce.getId(), reqCacheDetailedRepo, executorCache, retryDelay));
			
			return rce;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new UncheckedJsonProcessingException(e);
		}
	}
    
	public List<? extends IReqCacheEntry> getReqsCache(final Authentication authentication, boolean detailed) {
    	if (userManager.isAdmin(authentication)) {
	    	// if admin return everything
    		if (detailed)
    			return reqCacheDetailedRepo.findAll();
    		else 
    			return reqCacheSummaryRepo.findAll();
    	} else {
    		// otherwise return only those with the same caller ID
    		if (detailed) {
    			Example<TraceCacheDetailed> ex = Example.of(
    					TraceCacheDetailed.builder().callerId(userManager.getCallerUserId(authentication)).build());
    			return reqCacheDetailedRepo.findAll(ex);
    		} else {
    			Example<TraceCacheSummary> ex = Example.of(
    					TraceCacheSummary.builder().callerId(userManager.getCallerUserId(authentication)).build());
    			return reqCacheSummaryRepo.findAll(ex);
    		}
    	}
		
	}
	@Transactional
	public IReqCacheEntry deleteReqCache(final Authentication authentication, UUID id) {
		// TODO: Careful with removing entity while a thread updates its status 
		Optional<TraceCacheDetailed> rce = reqCacheDetailedRepo.findById(id);
		rce.ifPresentOrElse(e -> {reqCacheDetailedRepo.delete(e); reqCacheDetailedRepo.flush();}, 
				() -> {throw new EntityNotFoundException("Request cache with id "+ id + " not found.");});
		
		return rce.orElse(null);
	}

}
