package es.upv.grycap.tracer.model.trace.v1;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
//@SuperBuilder
//@NoArgsConstructor
public class TraceUseDatasets extends Trace {
	
	@JsonCreator
	public TraceUseDatasets(@JsonProperty("id") String id, @JsonProperty("timestamp") String timestamp) {
		super(id, timestamp);
	}
	
	/**
	 * The list of IDs used by the traced action
	 */
	@NotNull(message="The list of datasets IDS cannot be null.")
	@NotEmpty(message="The list of datasets IDS cannot be empty.")
	protected List<String> datasetsIds;

}
