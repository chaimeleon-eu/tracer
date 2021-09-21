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
import es.upv.grycap.tracer.model.Trace;
import es.upv.grycap.tracer.model.TraceCreateDataset;
import es.upv.grycap.tracer.model.TraceCreateModel;
import es.upv.grycap.tracer.model.TraceCreateVersionDataset;
import es.upv.grycap.tracer.model.TraceDataset;
import es.upv.grycap.tracer.model.TraceModel;
import es.upv.grycap.tracer.model.TraceResource;
import es.upv.grycap.tracer.model.TraceUseDatasets;
import es.upv.grycap.tracer.model.TraceUseModels;
import es.upv.grycap.tracer.model.dto.HashType;
import es.upv.grycap.tracer.model.dto.ReqCreateDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqCreateVersionDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.ReqDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqCreateModelDTO;
import es.upv.grycap.tracer.model.dto.ReqResDTO;
import es.upv.grycap.tracer.model.dto.ReqUseDatasetsDTO;
import es.upv.grycap.tracer.model.dto.ReqUseModelsDTO;
import es.upv.grycap.tracer.model.dto.UserAction;
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

		if (request.getUserAction() == UserAction.CREATE_NEW_DATASET) {
			final ReqCreateDatasetDTO req = (ReqCreateDatasetDTO) request;
			List<TraceResource> ltr = getTraceResources(req.getResources());
			return TraceCreateDataset.builder()
					.datasetId(req.getDatasetId())
					.userAction(req.getUserAction())
					.userId(req.getUserId())
					.traceResources(ltr)
					.build();
		} else if (request.getUserAction() == UserAction.CREATE_VERSION_DATASET) {
			final ReqCreateVersionDatasetDTO req = (ReqCreateVersionDatasetDTO) request;
			List<TraceResource> ltr = getTraceResources(req.getResources());
			return TraceCreateVersionDataset.builder()
					.datasetId(req.getDatasetId())
					.userAction(req.getUserAction())
					.userId(req.getUserId())
					.traceResources(ltr)
					.previousIds(req.getPreviousIds())
					.build();
		} else if (request.getUserAction() == UserAction.VISUALIZE_VERSION_DATASET) {
			final ReqDatasetDTO req = (ReqDatasetDTO) request;
			return TraceDataset.builder()
					.datasetId(req.getDatasetId())
					.userAction(req.getUserAction())
					.userId(req.getUserId())
					.build();
		} else if (request.getUserAction() == UserAction.USE_DATASETS_POD) {
			final ReqUseDatasetsDTO req = (ReqUseDatasetsDTO) request;
			return TraceUseDatasets.builder()
					.userAction(request.getUserAction())
					.userId(request.getUserId())
					.datasetsIds(req.getDatasetsIds())
					.build();
		} else if (request.getUserAction() == UserAction.CREATE_MODEL) {
			final ReqCreateModelDTO req = (ReqCreateModelDTO) request;
			return TraceCreateModel.builder()
					.userAction(req.getUserAction())
					.userId(request.getUserId())
					.datasetId(req.getDatasetId())
					.applicationId(req.getApplicationId())
					.modelId(req.getModelId())
					.build();
		} else if (request.getUserAction() == UserAction.USE_MODELS) {
			final ReqUseModelsDTO req = (ReqUseModelsDTO) request;
			return TraceUseModels.builder()
					.userAction(req.getUserAction())
					.userId(request.getUserId())
					.datasetId(req.getDatasetId())
					.applicationId(req.getApplicationId())
					.modelsIds(req.getModelsIds())
					.build();
		} else
			throw new UserActionNotSupported("User action " + request.getUserAction() + " not supported when creating traces from a request.");
	}

	protected List<TraceResource> getTraceResources(final List<ReqResDTO> resReq) {
		List<TraceResource> ltr = new ArrayList<>();
		resReq.forEach(r -> {
			DataHash hd = hashingService.getHashReqResource(r, defaultHashType);
			ltr.add(TraceResource.builder()
					.contentHash(Base64.encodeBase64String(hd.getHash()))
					.contentHashType(hd.getHashType())
					.nameHash(Base64.encodeBase64String(hashingService.getHash(r.getName().getBytes(StandardCharsets.UTF_8), defaultHashType).getHash()))
					.nameHashType(defaultHashType)
					.id(r.getId())
//					.pathHash(r.getPath() != null ?
//							Base64.encodeBase64String(hashingService.getHash(r.getPath().getBytes(StandardCharsets.UTF_8), defaultHashType).getHash())
//							: null)
//					.pathHashType(r.getPath() != null ? defaultHashType :  null)
					.build());

		});
		return ltr;
	}

}
