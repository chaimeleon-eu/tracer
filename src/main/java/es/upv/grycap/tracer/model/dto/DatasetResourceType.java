package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum DatasetResourceType {

	@JsonProperty
	IMAGING_DATA,
	@JsonProperty
	PATIENT_INFO;
}
