package es.upv.grycap.tracer.model.trace.v1;

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
//@SuperBuilder
@NoArgsConstructor
public class TraceCreateModel extends TraceModel {

	@JsonIgnore
	private static final long serialVersionUID = -6536948640006655142L;
	
	/**
	 * The ID of the created model
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
