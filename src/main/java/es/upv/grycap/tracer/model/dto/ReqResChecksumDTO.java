package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReqResChecksumDTO extends ReqResDTO {
	@JsonIgnore
	private static final long serialVersionUID = 3974625444759531622L;
	
	public enum CHECKSUM_TYPE {SHA3_256}
	
	protected String checksum;
	protected CHECKSUM_TYPE checksumType;

}
