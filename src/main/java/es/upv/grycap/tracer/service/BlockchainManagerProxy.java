package es.upv.grycap.tracer.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.activation.UnsupportedDataTypeException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bitcoinj.wallet.RiskAnalysis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.upv.grycap.tracer.exceptions.CaseNotHandledException;
import es.upv.grycap.tracer.exceptions.UncheckedJsonProcessingException;
import es.upv.grycap.tracer.exceptions.UncheckedUnsupportedDataTypeException;
import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.IReqCacheEntry;
import es.upv.grycap.tracer.model.TraceCacheDetailed;
import es.upv.grycap.tracer.model.TraceCacheSummary;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.TracerRoles;
import es.upv.grycap.tracer.model.dto.BlockchainProvider;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.RespTraceDTO;
import es.upv.grycap.tracer.model.dto.RespTracesDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.persistence.ITraceCacheDetailedRepo;
import es.upv.grycap.tracer.persistence.ITraceCacheSummaryRepo;
import es.upv.grycap.tracer.service.caching.CachingManager;
import es.upv.grycap.tracer.service.caching.TraceCacheOpSubmitter;

@Service
public class BlockchainManagerProxy {
	
	//protected HashingService hashingService;
	protected int requestParseLimit;
	
	protected UserManager userManager;
	protected CachingManager cachingManager;
	
	protected BlockchainManagersRepo blockchainManagersRepo;
	protected TraceHandler traceHandler;
	
	@Autowired
	public BlockchainManagerProxy(@Autowired BlockchainManagersRepo blockchainManagersRepo, @Autowired CachingManager cachingManager, 
			//@Autowired HashingService hashingService, 
			@Autowired UserManager userManager,
			@Autowired TraceHandler traceHandler,
			@Value("${tracer.request-parse-limit}") int requestParseLimit) {
		this.blockchainManagersRepo = blockchainManagersRepo;
		this.cachingManager = cachingManager;
		//this.hashingService = hashingService;
		this.userManager = userManager;
		this.traceHandler = traceHandler;
		this.requestParseLimit = requestParseLimit;
	}

	public Collection<TraceCacheSummary> addTrace(final Authentication authentication, final ReqDTO req) {
		String callerUserId = userManager.getCallerUserId(authentication);
		final TraceBase tr = traceHandler.fromRequest(req, callerUserId);
		final Collection<TraceCacheSummary> rces = new ArrayList<>();
		Collection<BlockchainManager> managers = null;
		if (req.getBlockchains() != null) {
			managers = blockchainManagersRepo.getActiveManagers().entrySet().stream()
					.filter(e -> req.getBlockchains().contains(e.getKey()))
					.map(m -> m.getValue())
					.collect(Collectors.toList());
		} else {
			managers = blockchainManagersRepo.getActiveManagers().values();
		}
		managers.forEach(mgr -> {
						rces.add(cachingManager.addTraceCache(authentication, tr, mgr));
					});
		return rces;
	}
	
	public List<? extends IReqCacheEntry> getReqsCache(final Authentication authentication, boolean detailed) {
		return cachingManager.getReqsCache(authentication, detailed);
	}
	
	public  IReqCacheEntry deleteReqCache(final Authentication authentication, UUID id) {
		return cachingManager.deleteReqCache(authentication, id);
	}

	public RespTracesDTO<?> getTraces(FilterParams filterParams) {
		if (filterParams.getBlockchains() == null) {
			return new RespTracesDTO<TraceSummaryBase>(blockchainManagersRepo.getActiveManagers().entrySet().stream()
					.filter(e -> e.getValue().getBlockchainProperties().isEnabled())
				.map(e -> new RespTraceDTO<TraceSummaryBase>(e.getKey(), e.getValue().getTraces(filterParams)))
				.collect(Collectors.toList()));
		} else {
			return new RespTracesDTO<TraceSummaryBase>(filterParams.getBlockchains().stream()
					.filter(bc -> blockchainManagersRepo.getActiveManagers().containsKey(bc) && 
							blockchainManagersRepo.getActiveManagers().get(bc).getBlockchainProperties().isEnabled())
				.map(bc -> new RespTraceDTO<TraceSummaryBase>(bc, blockchainManagersRepo.getActiveManagers().get(bc).getTraces(filterParams)))
				.collect(Collectors.toList()));
		}				
	}
	
	public RespTracesDTO<?> getTraceById(String traceId) {
			return new RespTracesDTO<TraceBase>(blockchainManagersRepo.getActiveManagers().entrySet().stream()
					.filter(e -> e.getValue().getBlockchainProperties().isEnabled())
				.map(e -> new RespTraceDTO<TraceBase>(e.getKey(), 
						Arrays.asList(e.getValue().getTraceById(traceId)).stream().filter(t -> t != null).toList()))
				.collect(Collectors.toList()));				
	}
	
	public List<BlockchainProvider> getProviders() {
		return blockchainManagersRepo.getAllManagers().values().stream().map(bm -> BlockchainProvider.builder().enabled(bm.getBlockchainProperties().isEnabled())
				.name(bm.getBlockchainProperties().getName()).type(bm.getType()).build()).collect(Collectors.toList());
	}
	
	public BlockchainType[] getAllBlockchainTypes() {
		return BlockchainType.values();
	}
	
	public List<BlockchainType> getEnabledBlockchainTypes() {
		return blockchainManagersRepo.getActiveManagers().values().stream()
				.filter(bm -> bm.getBlockchainProperties().isEnabled())
				.map(bm -> bm.getType())
				.collect(Collectors.toList());
	}

}
