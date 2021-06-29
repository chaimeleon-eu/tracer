package es.upv.grycap.tracer.service;

import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.TraceCreateDataset;
import es.upv.grycap.tracer.model.dto.RespCreateDatasetDTO;

@Service
public class TraceEntryResponseDTOHandler {
	
	public RespCreateDatasetDTO getEntryRespDTO(final TraceCreateDataset traceEntry) {
		return RespCreateDatasetDTO.builder().build();
	}

}
