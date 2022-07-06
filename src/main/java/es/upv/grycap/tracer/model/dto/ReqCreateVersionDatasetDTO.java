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
public class ReqCreateVersionDatasetDTO extends ReqCreateDatasetDTO {

	@JsonIgnore
	private static final long serialVersionUID = 7901674838690058395L;

	/**
	 * The id of the previous version of the dataset.
	 */
	@NotBlank(message="Previous version id of the dataset can't be blank.")
	@NotNull(message="Previous version id of the dataset can't be null.")
	protected String previousId;

}
