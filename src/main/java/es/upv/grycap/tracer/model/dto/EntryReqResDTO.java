package es.upv.grycap.tracer.model.dto;

import lombok.Setter;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class EntryReqResDTO implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	protected String id;

}
