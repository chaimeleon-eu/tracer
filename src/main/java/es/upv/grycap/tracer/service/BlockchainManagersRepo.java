package es.upv.grycap.tracer.service;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.dto.BlockchainType;
import lombok.Getter;

@Service
public class BlockchainManagersRepo {
	
	@Getter
	protected Map<BlockchainType, BlockchainManager> activeManagers;
	
	@Getter
	protected Map<BlockchainType, BlockchainManager> allManagers;
	
	protected ApplicationContext context;
	
	@PostConstruct
	public void init() {
		allManagers = Map.of(BlockchainType.BIGCHAINDB_PRIVATE, context.getBean(BigchainDbManager.class)
				, BlockchainType.BESU_PRIVATE, context.getBean(BesuManagerV1.class));
		activeManagers = allManagers.entrySet().stream().filter(e -> e.getValue().getBlockchainProperties().isEnabled())
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
	
	@Autowired
	public void context(ApplicationContext context) { this.context = context; }

}
