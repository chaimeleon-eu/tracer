package es.upv.grycap.tracer.model.trace.v1;

import java.util.List;

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
public class TraceCreateDataset extends TraceDataset {
	
	@JsonCreator
	public TraceCreateDataset(@JsonProperty("id") String id) {
		super(id);
	}
	
	/**
	 * The resources' information
	 */
	protected List<TraceResource> traceResources;
}
