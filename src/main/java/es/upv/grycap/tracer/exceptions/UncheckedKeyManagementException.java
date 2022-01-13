package es.upv.grycap.tracer.exceptions;

import java.security.KeyManagementException;

public class UncheckedKeyManagementException extends RuntimeException {

	private static final long serialVersionUID = 252196461512366514L;
	
	public UncheckedKeyManagementException(KeyManagementException ex) {
		super(ex);
	}

}
