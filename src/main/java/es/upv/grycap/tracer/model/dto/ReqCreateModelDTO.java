package es.upv.grycap.tracer.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateModelDTO extends ReqModelDTO {

	@JsonIgnore
	private static final long serialVersionUID = -188378551328657863L;
	
	@NotBlank(message="The model ID cannot be blank.")
	@NotNull(message="The model ID cannot be null.")
	protected String modelId;
}
