package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqResFileDataDTO extends ReqResDTO {
	@JsonIgnore
	private static final long serialVersionUID = 8131165912167657628L;

	/**
	 * base 64 encoded data
	 */
	protected String data;

}
