package es.upv.grycap.tracer.model.trace;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "version",
	    visible = true
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = es.upv.grycap.tracer.model.trace.v1.TraceSummary.class, name = "V1")
})
public abstract class TraceSummaryBase implements ITraceResponse {

	

	/**
	 * The actual version a a trace, used to serialize/deserialize the child classes
	 */
    @Getter
	protected TraceVersion version;
	
	/**
	 * The id of a trace can be any string.
	 * It is not defined as an exact format on purpose, to allow flexibility of changing it in future versions. 
	 */
	@Getter
	@Setter
	protected String id;
	
	/**
	 * The creation date of the trace
	 */
	@Getter	@Setter
	protected Instant timestamp;
	
	public TraceSummaryBase(TraceVersion version) {
		this.version = version;
	}
}
