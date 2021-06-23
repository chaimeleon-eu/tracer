package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.HashType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReqResChecksumDTO extends ReqResDTO {
	@JsonIgnore
	private static final long serialVersionUID = 3974625444759531622L;
	
	
	protected String hash;
	protected HashType hashType;

}
