package es.upv.grycap.tracer.model.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReqCreateModelDTO extends ReqModelDTO {

	@JsonIgnore
	private static final long serialVersionUID = -188378551328657863L;
	
	
	/**
	 * The ID of the model
	 */
	@NotBlank(message="The model ID cannot be blank.")
	@NotNull(message="The model ID cannot be null.")
	protected String modelId;
	/**
	 * The list of models IDs used to create this model
	 */
	@NotBlank(message="The application ID cannot be blank.")
	@NotNull(message="The application ID cannot be null.")
	protected List<String> datasetsIds;
}
