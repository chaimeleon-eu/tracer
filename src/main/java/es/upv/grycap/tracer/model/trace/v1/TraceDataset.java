package es.upv.grycap.tracer.model.trace.v1;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
//@SuperBuilder
@NoArgsConstructor
public class TraceDataset extends Trace {

	/**
	 * The id of the dataset referenced by this trace
	 */
	@NotBlank(message="The dataset ID cannot be blank.")
	@NotNull(message="The dataset ID cannot be null.")
	protected String datasetId;

}
