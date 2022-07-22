package es.upv.grycap.tracer.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
public class RespCreateDatasetDTO extends RespDTO {

	@JsonIgnore
	private static final long serialVersionUID = -7934268486575243907L;

	@NotBlank(message="Dataset ID cannot be blank.")
	@NotNull(message="Dataset ID cannot be null.")
	protected String datasetId;
}
