package es.upv.grycap.tracer.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReqModelDTO extends ReqDTO {

	private static final long serialVersionUID = -188378551328657863L;
	
	/**
	 * The ID of the application used to create the model referenciated by the request
	 */
	@NotBlank(message="The application ID cannot be blank.")
	@NotNull(message="The application ID cannot be null.")
	protected String applicationId;

}
