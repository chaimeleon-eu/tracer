package es.upv.grycap.tracer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TraceUseDatasets extends Trace {

	@JsonIgnore
	private static final long serialVersionUID = 4774388914989813495L;
	
	protected List<String> datasetsIds;

}
