package es.upv.grycap.tracer.exceptions;

public class UnhandledException extends RuntimeException {

	private static final long serialVersionUID = -7526685250810154119L;
	
	public UnhandledException(String msg) {
		super(msg);
	}

}
