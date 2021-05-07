package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EntryReqResDataDTO extends EntryReqResDTO {

	@JsonIgnore
	private static final long serialVersionUID = -4334306034695127681L;
	protected String data;

}
