package es.upv.grycap.tracer.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import es.upv.grycap.tracer.model.trace.v1.TraceUpdateDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReqUpdateDataset extends ReqDatasetDTO {
	
	/**
	 * The details about the updated dataset, such as the performed action 
	 * or the field that has been changed.
	 */
	@NotBlank(message="The update details cannot be blank.")
	@NotNull(message="The update details cannot be null.")
	protected TraceUpdateDetails updateDetails; 
}
