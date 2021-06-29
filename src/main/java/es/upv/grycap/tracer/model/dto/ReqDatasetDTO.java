package es.upv.grycap.tracer.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqDatasetDTO extends ReqDTO {

	@JsonIgnore
	private static final long serialVersionUID = -2378736133026750401L;
	
	@NotBlank(message="Dataset ID cannot be blank.")
	@NotNull(message="Dataset ID cannot be null.")
	protected String datasetId;

}
