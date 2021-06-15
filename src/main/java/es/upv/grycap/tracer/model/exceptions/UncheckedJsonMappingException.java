package es.upv.grycap.tracer.model.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;

public class UncheckedJsonMappingException extends RuntimeException {

	private static final long serialVersionUID = -6908594096684445527L;
	
	public UncheckedJsonMappingException(JsonMappingException ex) {
		super(ex);
	}

}
