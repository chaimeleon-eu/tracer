package es.upv.grycap.tracer.model.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
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
public class ReqDatasetDTO extends ReqDTO {

	@JsonIgnore
	private static final long serialVersionUID = -2378736133026750401L;
	
	/**
	 * The ID of the dataset that was just created 
	 */
	@NotBlank(message="Dataset ID cannot be blank.")
	@NotNull(message="Dataset ID cannot be null.")
	protected String datasetId;

}
