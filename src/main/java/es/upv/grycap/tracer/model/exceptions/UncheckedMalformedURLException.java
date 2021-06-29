package es.upv.grycap.tracer.model.exceptions;

import java.net.MalformedURLException;

public class UncheckedMalformedURLException extends RuntimeException {

	private static final long serialVersionUID = 2494763259316401818L;
	
	public UncheckedMalformedURLException(MalformedURLException ex) {
		super(ex);
	}

}
