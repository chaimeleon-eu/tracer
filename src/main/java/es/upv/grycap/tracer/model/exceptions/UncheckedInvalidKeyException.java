package es.upv.grycap.tracer.model.exceptions;

import java.security.InvalidKeyException;

public class UncheckedInvalidKeyException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UncheckedInvalidKeyException(InvalidKeyException ex) {
		super(ex);
	}

}
