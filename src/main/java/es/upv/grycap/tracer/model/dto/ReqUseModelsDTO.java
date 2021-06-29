package es.upv.grycap.tracer.model.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUseModelsDTO extends ReqModelDTO {

	@JsonIgnore
	private static final long serialVersionUID = 3813731216746508363L;
	
	@NotEmpty(message="List of models IDs cannot be empty.")
	@NotNull(message="List of models IDs cannot be null.")
	protected List<String> modelsIds;

}
