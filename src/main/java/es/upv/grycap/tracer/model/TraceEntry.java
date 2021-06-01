package es.upv.grycap.tracer.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TraceEntry implements Serializable {

	private static final long serialVersionUID = 5757503237019236987L;
	
	protected String userId;
	protected String datasetId;
	
	protected UserAction type;

}
