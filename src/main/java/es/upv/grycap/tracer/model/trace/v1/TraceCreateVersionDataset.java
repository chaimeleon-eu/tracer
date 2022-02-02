package es.upv.grycap.tracer.model.trace.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
//@SuperBuilder
@NoArgsConstructor
public class TraceCreateVersionDataset extends TraceCreateDataset {
	
	@JsonIgnore
	private static final long serialVersionUID = -7540394263110535067L;
	
	protected String previousId;

}
