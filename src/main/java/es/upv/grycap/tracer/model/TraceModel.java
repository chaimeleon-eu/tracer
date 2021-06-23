package es.upv.grycap.tracer.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TraceModel extends Trace {
	
	@JsonIgnore
	private static final long serialVersionUID = 1632046976434313965L;
	

	@NotNull
	protected String modelId;

}
