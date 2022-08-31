package es.upv.grycap.tracer.model.trace.v1;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
//@SuperBuilder
//@NoArgsConstructor
public class TraceModel extends Trace {
	
	@JsonCreator
	public TraceModel(@JsonProperty("id") String id) {
		super(id);
	}
	
	/**
	 * The ID of the application using the model
	 */
	@NotBlank(message="The application ID cannot be blank.")
	@NotNull(message="The application ID cannot be null.")
	protected String applicationId;

}
