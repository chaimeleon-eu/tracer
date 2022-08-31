package es.upv.grycap.tracer.model.trace.v1;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
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
public class TraceUseModels extends TraceModel {
	
	@JsonCreator
	public TraceUseModels(@JsonProperty("id") String id) {
		super(id);
	}
	
	/**
	 * The list of models IDs that were used when this action was executed	
	 */
	@NotEmpty(message="List of models IDs cannot be empty.")
	@NotNull(message="List of models IDs cannot be null.")
	protected List<String> modelsIds;
}
