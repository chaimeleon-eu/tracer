package es.upv.grycap.tracer.service.caching;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.activation.UnsupportedDataTypeException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.CaseNotHandledException;
import es.upv.grycap.tracer.exceptions.InvalidFieldNameException;
import es.upv.grycap.tracer.exceptions.InvalidFieldvalueException;
import es.upv.grycap.tracer.exceptions.OperationNotSupportedException;
import es.upv.grycap.tracer.exceptions.UncheckedJsonProcessingException;
import es.upv.grycap.tracer.exceptions.UncheckedUnsupportedDataTypeException;
import es.upv.grycap.tracer.model.IReqCacheEntry;
import es.upv.grycap.tracer.model.TraceCacheDetailed;
import es.upv.grycap.tracer.model.TraceCacheSummary;
import es.upv.grycap.tracer.model.TracerRoles;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.request.cache.UpdateField;
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
    
    /**
     * These are the statuses of an existing trace cache that allow the field update
     */
    protected final Set<ReqCacheStatus> UPDATE_CACHE_ALLOWED_STATUS = Set.of(ReqCacheStatus.BLOCKCHAIN_ERROR,
            ReqCacheStatus.BLOCKCHAIN_NOT_FOUND, ReqCacheStatus.ERROR, ReqCacheStatus.BLOCKCHAIN_SUBMISSION_ERROR);
	
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
				List.of(ReqCacheStatus.WAITING, //ReqCacheStatus.BLOCKCHAIN_WAITING,
						ReqCacheStatus.BLOCKCHAIN_UNAVAILABLE));//example);
		
		//final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		reqs.forEach(reqCache -> {
			submitCacheLooper(reqCache);
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
			
			CompletableFuture.supplyAsync(new TraceCacheOpSubmitter(reqCacheDetailedRepo, mgr, rce.getId(), rce.getTrace(), rce.getCallerId()), 
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
		
		rce.ifPresentOrElse(e -> {
				if (e.getCallerId().equals(userManager.getCallerUserId(authentication)) 
						|| userManager.isAdmin(authentication)) {
					reqCacheDetailedRepo.delete(e); 
					reqCacheDetailedRepo.flush();
				} else {
					throw new AccessDeniedException("You are not authorized to delete this resource.");
				}
			}, 
				() -> {throw new EntityNotFoundException("Request cache with id "+ id + " not found.");});
		
		return rce.orElse(null);
	}
	
	public IReqCacheEntry updateReqCacheBlockchainCommit(final Authentication authentication, UUID id, Collection<UpdateField> fields, boolean runBlockchainCommit) {
	    TraceCacheDetailed rce = null;
	    if (!fields.isEmpty()) {
	        rce = (TraceCacheDetailed) this.updateReqCache(authentication, id, fields);
	    }
        if (runBlockchainCommit) {
            final UUID idTC = rce == null ? id : rce.getId();
            Optional<TraceCacheDetailed> rceNew = reqCacheDetailedRepo.findById(idTC);
            if (rceNew.isPresent()) {
                submitCacheLooper(rceNew.get());
                rce = rceNew.get();
            } else {
                throw new EntityNotFoundException("Request cache with id " + idTC + " not found.");
            }
        }
        if (rce == null) {
            Optional<TraceCacheDetailed> rceNew = reqCacheDetailedRepo.findById(id);
            if (rceNew.isPresent()) {
                return rceNew.get();
            } else {
                throw new EntityNotFoundException("Request cache with id " + id + " not found.");
            }
        }
        return rce;	    
	}
	
	
	
    @Transactional
    public IReqCacheEntry updateReqCache(final Authentication authentication, UUID id, Collection<UpdateField> fields) {
        Optional<TraceCacheDetailed> rce = reqCacheDetailedRepo.findById(id);
        
        rce.ifPresentOrElse(e -> {
                if (e.getCallerId().equals(userManager.getCallerUserId(authentication)) 
                        || userManager.isAdmin(authentication)) {
                    
                    if (!UPDATE_CACHE_ALLOWED_STATUS.contains(e.getStatus())) {
                        throw new OperationNotSupportedException("Cannot update a trace when its status is '" + e.getStatus() + "', only when in one of the following: "
                                + UPDATE_CACHE_ALLOWED_STATUS.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", ")));
                    }

                    try {
                        Object ft = null;
                        for (UpdateField f: fields) {
                            String fieldName = f.getFieldName();
                            String fieldValue = f.getFieldValue();
                            switch (fieldName) {
                                case "statusMessage": Util.<TraceCacheDetailed, String>updateFieldByName(e, fieldName, fieldValue);break;
                                case "id": ft = UUID.fromString(fieldValue);Util.<TraceCacheDetailed, UUID>updateFieldByName(e, fieldName, (UUID)ft);break;
                                //case "creationDate", "modificationDate": ft = ZonedDateTime.parse(fieldValue).toInstant();Util.<TraceCacheDetailed, Instant>updateFieldByName(e, fieldName, (Instant)ft);break;
                                case "status": ft = ReqCacheStatus.valueOf(fieldValue);Util.<TraceCacheDetailed, ReqCacheStatus>updateFieldByName(e, fieldName, (ReqCacheStatus)ft);break;  
                                case "blockchainType": ft = BlockchainType.valueOf(fieldValue);Util.<TraceCacheDetailed, BlockchainType>updateFieldByName(e, fieldName, (BlockchainType)ft);break;
                                case "callerId", "traceId": {
                                    if (fieldValue != null) {
                                        Util.<TraceCacheDetailed, String>updateFieldByName(e, fieldName, fieldValue);
                                        final ObjectMapper mapper = new ObjectMapper();
                                        TraceBase tb = mapper.readValue(e.getTrace(), TraceBase.class);
                                        if (fieldName.equals("traceId")) {
                                            Util.<TraceBase, String>updateFieldByName(tb, "id", fieldValue);
                                        } else {
                                            Util.<TraceBase, String>updateFieldByName(tb,  fieldName, fieldValue);
                                        }
                                        e.setTrace(mapper.writeValueAsString(tb));
                                    } else {
                                         throw new InvalidFieldvalueException("Field '" + fieldName + "' mustn't be null");
                                    }
                                    break;
                                }
                               case "trace": {
                                   // check if it is a trace
                                   final ObjectMapper mapper = new ObjectMapper();
                                   TraceBase tb = mapper.readValue(fieldValue, TraceBase.class);
                                   Util.<TraceCacheDetailed, String>updateFieldByName(e, fieldName, fieldValue);
                                   e.setCallerId(tb.getCallerId());
                                   e.setTraceId(tb.getId());
                                   break;
                               }
                               default: throw new InvalidFieldNameException("Field '" + fieldName + "' not handled.");
                            }

                        }
                    } catch (Exception ex) {
                        throw new InvalidFieldvalueException("Invalid field value: " + ex.getMessage());
                    }
                    e.setModificationDate(Instant.now());
                    reqCacheDetailedRepo.save(e);
                    reqCacheDetailedRepo.flush();
                } else {
                    throw new AccessDeniedException("You are not authorized to delete this resource.");
                }
            }, 
                () -> {throw new EntityNotFoundException("Request cache with id "+ id + " not found.");});
        
        return rce.orElse(null);
    }
    
    protected void submitCacheLooper(TraceCacheDetailed reqCache) {
        if (reqCache.getStatus() == ReqCacheStatus.WAITING || reqCache.getStatus() == ReqCacheStatus.BLOCKCHAIN_UNAVAILABLE) {
            BlockchainManager mgr = blockchainManagersRepo.getActiveManagers().get(reqCache.getBlockchainType());
            Util.loopRequestFutures(reqCache.getStatus(), retryDelay, mgr, reqCache, reqCacheDetailedRepo, executorCache);
        }
    }

}
