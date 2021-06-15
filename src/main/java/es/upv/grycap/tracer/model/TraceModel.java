package es.upv.grycap.tracer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TraceModel extends Trace {
	
	
	private static final long serialVersionUID = 1632046976434313965L;
	protected String modelId;

}
