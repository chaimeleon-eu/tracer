package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespErrorDTO extends RespDTO {

	@JsonIgnore
	private static final long serialVersionUID = 1875532383449224887L;
	
	protected String message;

}
