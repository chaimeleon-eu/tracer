package es.upv.grycap.tracer.model.trace.v1;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
//@NoArgsConstructor
public class TraceUpdateDataset extends TraceDataset {
	
	@JsonCreator
	public TraceUpdateDataset(@JsonProperty("id") String id, @JsonProperty("timestamp") String timestamp) {
		super(id, timestamp);
	}
	
	/**
	 * The details about the updated dataset, such as the performed action 
	 * or the field that has been changed.
	 */
	protected TraceUpdateDetails details; 
	
	@Override
	public TraceSummaryBase toSummary() {
		TraceSummary trs = (TraceSummary) super.toSummary();
		trs.setDetails(details.name());
		log.info("Show details: " + details.name());
		return trs;
	}

}
