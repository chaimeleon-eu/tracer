package es.upv.grycap.tracer.model.trace.v1;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TraceDataset extends Trace {

	@JsonIgnore
	private static final long serialVersionUID = -5620792989961648202L;
	protected String datasetId;

}
