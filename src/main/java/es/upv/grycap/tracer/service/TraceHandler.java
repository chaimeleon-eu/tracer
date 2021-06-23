package es.upv.grycap.tracer.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.DataHash;
import es.upv.grycap.tracer.model.HashType;
import es.upv.grycap.tracer.model.Trace;
import es.upv.grycap.tracer.model.TraceCreateDataset;
import es.upv.grycap.tracer.model.TraceModel;
import es.upv.grycap.tracer.model.TraceResource;
import es.upv.grycap.tracer.model.UserAction;
import es.upv.grycap.tracer.model.dto.ReqCreateDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.ReqModelDTO;
import es.upv.grycap.tracer.model.dto.ReqResDTO;
import es.upv.grycap.tracer.model.exceptions.UserActionNotSupported;

@Service
public class TraceHandler {
	
	@Autowired
    private Validator validator;
	
	@Autowired
	protected HashingService hashingService;
	
	protected HashType defaultHashType;
	
	@Autowired
	public TraceHandler(@Value("${tracer.hashAlgorithm}") String defaultHashAlgorithmId) {
		this.defaultHashType = HashType.fromAlgorithmId(defaultHashAlgorithmId);
	}
	
	public Trace fromRequest(final ReqDTO request) {
		
		if (request.getUserAction() == UserAction.CREATE_DATASET 
				|| request.getUserAction() == UserAction.CREATE_VERSION_DATASET) {
			final ReqCreateDatasetDTO req = (ReqCreateDatasetDTO) request;
			List<TraceResource> ltr = new ArrayList<>();
			List<ReqResDTO> resReq = req.getResources();
			resReq.forEach(r -> {
				DataHash hd = hashingService.getHashReqResource(r, defaultHashType);
				ltr.add(TraceResource.builder()
						.contentHash(Base64.encodeBase64String(hd.getHash()))
						.contentHashType(hd.getHashType())
						.nameHash(Base64.encodeBase64String(hashingService.getHash(r.getName().getBytes(StandardCharsets.UTF_8), defaultHashType).getHash()))
						.nameHashType(defaultHashType)
						.id(r.getId())
						.pathHash(r.getPath() != null ? 
								Base64.encodeBase64String(hashingService.getHash(r.getPath().getBytes(StandardCharsets.UTF_8), defaultHashType).getHash())
								: null)
						.pathHashType(r.getPath() != null ? defaultHashType :  null)
						.build());
				
			});
			return TraceCreateDataset.builder()
					.datasetId(req.getDatasetId())
					.userAction(req.getUserAction())
					.userId(req.getUserId())
					.traceResources(ltr)
					.build();
		} else if (request.getUserAction() == UserAction.VISUALIZE_VERSION_DATASET 
				|| request.getUserAction() == UserAction.USE_DATASET_POD) {

			return Trace.builder()
					.datasetId(request.getDatasetId())
					.userAction(request.getUserAction())
					.userId(request.getUserId())
					.build();
		}  else if (request.getUserAction() == UserAction.CREATE_MODEL_POD 
				|| request.getUserAction() == UserAction.USE_MODEL_POD) {
			final ReqModelDTO req = (ReqModelDTO) request;
			return TraceModel.builder()
					.datasetId(req.getDatasetId())
					.userAction(req.getUserAction())
					.userId(request.getUserId())
					.modelId(req.getModelId())
					.build();
		} else 
			throw new UserActionNotSupported("User action " + request.getUserAction() + " not supported when creating traces from a request.");
	}

}
