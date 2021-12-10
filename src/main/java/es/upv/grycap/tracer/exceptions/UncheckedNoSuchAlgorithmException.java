package es.upv.grycap.tracer.exceptions;

import java.security.NoSuchAlgorithmException;

public class UncheckedNoSuchAlgorithmException extends RuntimeException {

	private static final long serialVersionUID = 3053456746440531739L;
	
	public UncheckedNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
		super(ex);
	}

}
