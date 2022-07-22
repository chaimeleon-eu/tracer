package es.upv.grycap.tracer.model.dto;

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
public class ReqUseModelsDTO extends ReqModelDTO {

	@JsonIgnore
	private static final long serialVersionUID = 3813731216746508363L;
	
	/**
	 * A list of models IDs that were used in the repo
	 */
	@NotEmpty(message="List of models IDs cannot be empty.")
	@NotNull(message="List of models IDs cannot be null.")
	protected List<String> modelsIds;

}
