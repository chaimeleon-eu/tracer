package es.upv.grycap.tracer.model.trace;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "version",
	    visible = true
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = es.upv.grycap.tracer.model.trace.v1.Trace.class, name = "V1")
})
public abstract class TraceBase implements ITraceResponse {
	
	/**
	 * The ID if the user (person, application, service etc.) that invoked the tracer service with the intention of registering this trace
	 */
	@Getter	@Setter
	protected String callerId;
	

	/**
	 * The actual version a a trace, used to serialize/deserialize the child classes
	 */
    @Getter
	protected TraceVersion version;
	
	/**
	 * The id of a trace can be any string.
	 * It is not defined as an exact format on purpose, to allow flexibility of changing it in future versions. 
	 */
	@Getter	@Setter
	protected String id;
	
	public TraceBase(TraceVersion version) {
		this.version = version;
	}
	
	public abstract TraceSummaryBase toSummary();

}
