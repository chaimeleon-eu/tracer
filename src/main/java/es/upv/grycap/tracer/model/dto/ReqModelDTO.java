package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqModelDTO extends ReqDTO {

	@JsonIgnore
	private static final long serialVersionUID = -188378551328657863L;

	protected String modelId;
}
