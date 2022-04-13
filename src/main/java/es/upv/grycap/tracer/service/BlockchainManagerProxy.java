package es.upv.grycap.tracer.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.RespTraceDTO;
import es.upv.grycap.tracer.model.dto.RespTracesDTO;
import es.upv.grycap.tracer.persistence.ITraceReqCacheRepository;

@Service
public class BlockchainManagerProxy {
	
	protected HashingService hashingService;
	
	protected ITraceReqCacheRepository tracesReqsCache;
	
	protected int requestParseLimit;
	
	protected Map<BlockchainType, BlockchainManager> managers;
	
	protected ApplicationContext context;
	
	protected ExecutorService executorCache;
	
	@Autowired
	public BlockchainManagerProxy(@Autowired HashingService hashingService, 
			@Autowired ITraceReqCacheRepository tracesReqsCache,
			@Value("${tracer.request-parse-limit}") int requestParseLimit) {
		this.hashingService = hashingService;
		this.tracesReqsCache = tracesReqsCache;
		this.requestParseLimit = requestParseLimit;
	}
	
	@PostConstruct
	public void init() {
		managers = Map.of(BlockchainType.BIGCHAINDB_PRIVATE, context.getBean(BigchainDbManager.class)
				, BlockchainType.BESU_PRIVATE, context.getBean(BesuManager.class));
		executorCache = Executors.newFixedThreadPool(10);
	}
	
    @PreDestroy
    public void destroy() {
    	executorCache.shutdown();
    }
	
	@Autowired
	public void context(ApplicationContext context) { this.context = context; }

	public void addTrace(, String callerUserId) {
		ReqDTO entry = 
	}

	public RespTracesDTO getTraces(FilterParams filterParams) {
		if (filterParams.getBlockchains() == null) {
			return new RespTracesDTO(managers.entrySet().stream()
					.filter(e -> e.getValue().getBlockchainProperties().isEnabled())
				.map(e -> new RespTraceDTO(e.getKey(), e.getValue().getTraces(filterParams)))
				.collect(Collectors.toList()));
		} else {
			return new RespTracesDTO(filterParams.getBlockchains().stream()
					.filter(bc -> managers.containsKey(bc) && managers.get(bc).getBlockchainProperties().isEnabled())
				.map(bc -> new RespTraceDTO(bc, managers.get(bc).getTraces(filterParams)))
				.collect(Collectors.toList()));
		}
				
	}
	
	public BlockchainType[] getAllBlockchainTypes() {
		return BlockchainType.values();
	}
	
	public List<BlockchainType> getEnabledBlockchainTypes() {
		return managers.values().stream()
				.filter(bm -> bm.getBlockchainProperties().isEnabled())
				.map(bm -> bm.getBlockchainProperties().getType())
				.collect(Collectors.toList());
	}

}
