package es.upv.grycap.tracer.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import es.upv.grycap.tracer.model.dto.HashType;
import es.upv.grycap.tracer.model.dto.ReqCreateDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.ReqResContentType;
import es.upv.grycap.tracer.model.dto.ReqResDTO;
import es.upv.grycap.tracer.model.dto.ReqResHashDTO;
import es.upv.grycap.tracer.model.trace.v1.UserAction;
import es.upv.grycap.tracer.service.caching.CachingManager;

public class BlockchainManagerProxyTest extends BlockchainManagerProxy{

	public BlockchainManagerProxyTest(BlockchainManagersRepo blockchainManagersRepo, CachingManager cachingManager,
			 UserManager userManager, int requestParseLimit) {
		super(null, null, null, null, 0);
	}
	
	private ReqDTO generateReqCreateDatasetDTO(long count) {// numResMin, long numResMax) {
		//long count = Math.round(Math.random() * (numResMax - numResMin)) + numResMin;
		List<ReqResDTO> resources = new ArrayList<>();
		for (long idx=0; idx< count; ++idx) {
			resources.add(ReqResHashDTO.builder()
					.contentType(ReqResContentType.HASH)
					.description(null)
					.hash(Base64.getEncoder().encodeToString(
							HashingService.getHash(UUID.randomUUID().toString().getBytes(), HashType.SHA3_256).getHash()))
					.hashType(HashType.SHA3_256)
					.id(UUID.randomUUID().toString())
					.name(Long.toString(idx))
					.build());
		}
		
		return ReqCreateDatasetDTO.builder()
				.blockchains(null)
				.datasetId(UUID.randomUUID().toString())
				.resources(resources)
				.userAction(UserAction.CREATE_NEW_DATASET)
				.userId(UUID.randomUUID().toString())
				.build();
	}
	
	

}
