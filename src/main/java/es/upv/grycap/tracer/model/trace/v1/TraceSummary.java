package es.upv.grycap.tracer.model.trace.v1;

import java.time.Instant;

import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.TraceVersion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraceSummary extends TraceSummaryBase {
	
	public static final TraceVersion VERSION = TraceVersion.V1;
	
	/**
	 * The ID of the user (person, application, service etc.) that performed the traced action
	 */
	protected String userId;
	/**
	 * The ID if the user (person, application, service etc.) that invoked the tracer service with the intention of registering this trace
	 */
	protected String callerId;
	/**
	 * The action of a user (person, application, service etc.) represented by this trace
	 */
	protected UserAction userAction;
	
	/**
	 * The ID of the trace, independent of any storage solutions
	 */
	protected String id;

	
	public TraceSummary() {
		super(VERSION);
	}

}
