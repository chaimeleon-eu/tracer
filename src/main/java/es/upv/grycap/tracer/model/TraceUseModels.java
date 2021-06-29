package es.upv.grycap.tracer.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
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
public class TraceUseModels extends TraceModel {

	@JsonIgnore
	private static final long serialVersionUID = 4598094156983695180L;
	@NotEmpty(message="List of models IDs cannot be empty.")
	@NotNull(message="List of models IDs cannot be null.")
	protected List<String> modelsIds;
}
