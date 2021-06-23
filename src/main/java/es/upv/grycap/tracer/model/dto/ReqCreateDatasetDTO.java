package es.upv.grycap.tracer.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateDatasetDTO extends ReqDTO {

	@JsonIgnore
	private static final long serialVersionUID = 276930127687010771L;
	

	protected List<ReqResDTO> resources;

}
