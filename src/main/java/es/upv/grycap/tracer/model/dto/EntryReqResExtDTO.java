package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EntryReqResExtDTO extends EntryReqResDTO {

	@JsonIgnore
	private static final long serialVersionUID = 5113631743786156067L;
	protected String url;
}
