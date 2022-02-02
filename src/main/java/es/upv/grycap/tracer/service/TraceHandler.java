package es.upv.grycap.tracer.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.DataHash;
import es.upv.grycap.tracer.model.dto.HashType;
import es.upv.grycap.tracer.model.dto.ReqCreateDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqCreateVersionDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.ReqDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqCreateModelDTO;
import es.upv.grycap.tracer.model.dto.ReqResDTO;
import es.upv.grycap.tracer.model.dto.ReqUseDatasetsDTO;
import es.upv.grycap.tracer.model.dto.ReqUseModelsDTO;
import es.upv.grycap.tracer.exceptions.UserActionNotSupported;
import es.upv.grycap.tracer.model.trace.v1.Trace;
import es.upv.grycap.tracer.model.trace.v1.TraceCreateDataset;
import es.upv.grycap.tracer.model.trace.v1.TraceCreateModel;
import es.upv.grycap.tracer.model.trace.v1.TraceCreateVersionDataset;
import es.upv.grycap.tracer.model.trace.v1.TraceDataset;
import es.upv.grycap.tracer.model.trace.v1.TraceResource;
import es.upv.grycap.tracer.model.trace.v1.TraceUseDatasets;
import es.upv.grycap.tracer.model.trace.v1.TraceUseModels;
import es.upv.grycap.tracer.model.trace.v1.UserAction;

@Service
public class TraceHandler {

	protected HashingService hashingService;

	protected HashType defaultHashType;

	@Autowired
	public TraceHandler(@Value("${tracer.hashAlgorithm}") String defaultHashAlgorithmId,
			@Autowired HashingService hashingService) {
		this.defaultHashType = HashType.fromAlgorithmId(defaultHashAlgorithmId);
		this.hashingService = hashingService;
	}

	public Trace fromRequest(final ReqDTO request, String callerId) {
		Trace ds = null; 
		if (request.getUserAction() == UserAction.CREATE_NEW_DATASET) {
			final ReqCreateDatasetDTO req = (ReqCreateDatasetDTO) request;
			List<TraceResource> ltr = getTraceResources(req.getResources());
			TraceCreateDataset dsTmp = new TraceCreateDataset();
			dsTmp.setDatasetId(req.getDatasetId());
			dsTmp.setTraceResources(ltr);
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.CREATE_VERSION_DATASET) {
			final ReqCreateVersionDatasetDTO req = (ReqCreateVersionDatasetDTO) request;
			List<TraceResource> ltr = getTraceResources(req.getResources());
			TraceCreateVersionDataset dsTmp = new TraceCreateVersionDataset();
			dsTmp.setDatasetId(req.getDatasetId());
			dsTmp.setTraceResources(ltr);
			dsTmp.setPreviousId(req.getPreviousId());
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.VISUALIZE_VERSION_DATASET) {
			final ReqDatasetDTO req = (ReqDatasetDTO) request;
			TraceDataset dsTmp = new TraceDataset();
			dsTmp.setDatasetId(req.getDatasetId());
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.USE_DATASETS_POD) {
			final ReqUseDatasetsDTO req = (ReqUseDatasetsDTO) request;
			TraceUseDatasets dsTmp = new TraceUseDatasets();
			dsTmp.setDatasetsIds(req.getDatasetsIds());
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.CREATE_MODEL) {
			final ReqCreateModelDTO req = (ReqCreateModelDTO) request;
			TraceCreateModel dsTmp = new TraceCreateModel();
			dsTmp.setDatasetId(req.getDatasetId());
			dsTmp.setApplicationId(req.getApplicationId());
			dsTmp.setModelId(req.getModelId());
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.USE_MODELS) {
			final ReqUseModelsDTO req = (ReqUseModelsDTO) request;
			TraceUseModels dsTmp = new TraceUseModels();
			dsTmp.setDatasetId(req.getDatasetId());
			dsTmp.setApplicationId(req.getApplicationId());
			dsTmp.setModelsIds(req.getModelsIds());
			ds = dsTmp;
		} else
			throw new UserActionNotSupported("User action " + request.getUserAction() + " not supported when creating traces from a request.");

		// ds should've been initted or error should've been thrown at this stage
		ds.setCallerId(callerId);
		ds.setUserAction(request.getUserAction());
		ds.setUserId(request.getUserId());
		return ds;
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
