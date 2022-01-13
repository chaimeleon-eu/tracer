package es.upv.grycap.tracer.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UncheckedJsonProcessingException extends RuntimeException {

	private static final long serialVersionUID = -373746328072520793L;
	
	public UncheckedJsonProcessingException(JsonProcessingException ex) {
		super(ex);
	}

}
