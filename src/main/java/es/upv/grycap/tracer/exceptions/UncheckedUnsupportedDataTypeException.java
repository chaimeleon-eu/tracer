package es.upv.grycap.tracer.exceptions;

import javax.activation.UnsupportedDataTypeException;

public class UncheckedUnsupportedDataTypeException extends RuntimeException {

	private static final long serialVersionUID = -1030105525775443309L;
	
	public UncheckedUnsupportedDataTypeException(UnsupportedDataTypeException e) {
		super(e);
	}
	
	public UncheckedUnsupportedDataTypeException(String msg) {
		super(msg);
	}

}
