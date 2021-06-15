package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RespCreateDatasetDTO extends RespDTO {

	@JsonIgnore
	private static final long serialVersionUID = -7934268486575243907L;

}
