package es.upv.grycap.tracer.model.exceptions;

import java.security.SignatureException;

public class UncheckedSignatureException extends RuntimeException {

	private static final long serialVersionUID = 4550411257742860632L;
	
	public UncheckedSignatureException(SignatureException ex) {
		super(ex);
	}

}
