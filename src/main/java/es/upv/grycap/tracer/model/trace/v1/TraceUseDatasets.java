package es.upv.grycap.tracer.model.trace.v1;

import java.util.List;

import javax.validation.constraints.NotEmpty;
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
public class TraceUseDatasets extends Trace {

	@JsonIgnore
	private static final long serialVersionUID = 4774388914989813495L;
	
	/**
	 * The list of IDs used by the traced action
	 */
	@NotNull(message="The list of datasets IDS cannot be null.")
	@NotEmpty(message="The list of datasets IDS cannot be empty.")
	protected List<String> datasetsIds;

}
