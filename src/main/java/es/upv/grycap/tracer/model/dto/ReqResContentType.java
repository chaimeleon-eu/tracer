package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ReqResContentType {
	@JsonProperty
	FILE_DATA, 
	@JsonProperty
	HTTP_FTP, 
	@JsonProperty
	HASH
}
