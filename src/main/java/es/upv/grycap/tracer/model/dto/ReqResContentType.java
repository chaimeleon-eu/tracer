package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ReqResContentType {
	/**
	 * 
	 */
	@JsonProperty
	FILE_DATA,
	/**
	 * A link to an external server with the resource.
	 * HTTP(S) or FTP(S) supported.
	 */
	@JsonProperty
	HTTP_FTP,
	
	/**
	 * The actual hash of the file.
	 * it will be stored into the blockchain without any further check
	 */
	@JsonProperty
	HASH
}
