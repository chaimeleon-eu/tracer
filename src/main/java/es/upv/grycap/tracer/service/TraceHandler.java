package es.upv.grycap.tracer.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.DataHash;
import es.upv.grycap.tracer.model.TracerTime;
import es.upv.grycap.tracer.model.dto.HashType;
import es.upv.grycap.tracer.model.dto.ReqCreateDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.ReqDatasetDTO;
import es.upv.grycap.tracer.model.dto.ReqCreateModelDTO;
import es.upv.grycap.tracer.model.dto.ReqResDTO;
import es.upv.grycap.tracer.model.dto.ReqUpdateDataset;
import es.upv.grycap.tracer.model.dto.ReqUseDatasetsDTO;
import es.upv.grycap.tracer.model.dto.ReqUseModelsDTO;
import es.upv.grycap.tracer.exceptions.TraceException;
import es.upv.grycap.tracer.exceptions.UncheckedInterruptedException;
import es.upv.grycap.tracer.exceptions.UserActionNotSupported;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.v1.Trace;
import es.upv.grycap.tracer.model.trace.v1.TraceCreateDataset;
import es.upv.grycap.tracer.model.trace.v1.TraceCreateModel;
import es.upv.grycap.tracer.model.trace.v1.TraceDataset;
import es.upv.grycap.tracer.model.trace.v1.TraceResource;
import es.upv.grycap.tracer.model.trace.v1.TraceUpdateDataset;
import es.upv.grycap.tracer.model.trace.v1.TraceUseDatasets;
import es.upv.grycap.tracer.model.trace.v1.TraceUseModels;
import es.upv.grycap.tracer.model.trace.v1.UserAction;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TraceHandler {

	//protected HashingService hashingService;

	protected HashType defaultHashType;
	
	protected TimeManager timeManager;
	

	@Autowired
	public TraceHandler(@Value("${tracer.hashAlgorithm}") String defaultHashAlgorithmId,
			@Autowired TimeManager timeManager
			//@Autowired HashingService hashingService
			) {
		this.defaultHashType = HashType.fromAlgorithmId(defaultHashAlgorithmId);
		this.timeManager = timeManager;
		//this.hashingService = hashingService;
	}

	public TraceBase fromRequest(final ReqDTO request, String callerId) {
		Trace ds = null;
		String id = generateId();
		if (request.getUserAction() == UserAction.CREATE_DATASET) {
			final ReqCreateDatasetDTO req = (ReqCreateDatasetDTO) request;
			List<TraceResource> ltr = getTraceResources(req.getResources());
			TraceCreateDataset dsTmp = new TraceCreateDataset(id);
			dsTmp.setDatasetId(req.getDatasetId());
			dsTmp.setTraceResources(ltr);
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.UPDATE_DATASET) {
			final ReqUpdateDataset req = (ReqUpdateDataset) request;
			TraceUpdateDataset dsTmp = new TraceUpdateDataset(id);
			dsTmp.setDatasetId(req.getDatasetId());
			dsTmp.setDetails(req.getDetails());
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.USE_DATASETS) {
			final ReqUseDatasetsDTO req = (ReqUseDatasetsDTO) request;
			TraceUseDatasets dsTmp = new TraceUseDatasets(id);
			dsTmp.setDatasetsIds(req.getDatasetsIds());
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.CREATE_MODEL) {
			final ReqCreateModelDTO req = (ReqCreateModelDTO) request;
			TraceCreateModel dsTmp = new TraceCreateModel(id);
			dsTmp.setDatasetsIds(req.getDatasetsIds());
			dsTmp.setApplicationId(req.getApplicationId());
			dsTmp.setModelId(req.getModelId());
			ds = dsTmp;
		} else if (request.getUserAction() == UserAction.USE_MODELS) {
			final ReqUseModelsDTO req = (ReqUseModelsDTO) request;
			TraceUseModels dsTmp = new TraceUseModels(id);
			//dsTmp.setDatasetId(req.getDatasetId());
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
			DataHash hd = HashingService.getHashReqResource(r, defaultHashType);
			ltr.add(TraceResource.builder()
					.contentHash(Base64.encodeBase64String(hd.getHash()))
					.contentHashType(hd.getHashType())
					//.nameHash(Base64.encodeBase64String(HashingService.getHash(r.getName().getBytes(StandardCharsets.UTF_8), defaultHashType).getHash()))
					//.nameHashType(defaultHashType)
					.id(r.getId())
//					.pathHash(r.getPath() != null ?
//							Base64.encodeBase64String(hashingService.getHash(r.getPath().getBytes(StandardCharsets.UTF_8), defaultHashType).getHash())
//							: null)
//					.pathHashType(r.getPath() != null ? defaultHashType :  null)
					.build());

		});
		return ltr;
	}
	
	protected synchronized String generateId() {
		return Long.toString(timeManager.getTime());
	}

}
