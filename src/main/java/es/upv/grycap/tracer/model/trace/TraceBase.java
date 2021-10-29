package es.upv.grycap.tracer.model.trace;

import es.upv.grycap.tracer.model.trace.v1.Trace;
import es.upv.grycap.tracer.model.trace.v1.UserAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class TraceBase {
	
	protected int version;
	
	public TraceBase(int version) {
		this.version = version;
	}

}
