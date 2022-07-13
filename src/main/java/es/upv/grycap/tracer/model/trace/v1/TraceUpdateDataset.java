package es.upv.grycap.tracer.model.trace.v1;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraceUpdateDataset extends TraceDataset {
	
	/**
	 * The details about the updated dataset, such as the performed action 
	 * or the field that has been changed.
	 */
	@NotBlank(message="The user action details cannot be blank.")
	@NotNull(message="The user action details cannot be null.")
	protected TraceUpdateDetails updateDetails; 

}
