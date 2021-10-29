package es.upv.grycap.tracer.model.trace.v1;

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
public class TraceCreateModel extends TraceModel {

	@JsonIgnore
	private static final long serialVersionUID = -6536948640006655142L;
	
	@NotBlank(message="The model ID cannot be blank.")
	@NotNull(message="The model ID cannot be null.")
	protected String modelId;

}
