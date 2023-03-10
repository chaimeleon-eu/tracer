package es.upv.grycap.tracer.exceptions;

public class BesuException extends RuntimeException {

	private static final long serialVersionUID = -4121029866780186576L;
	
	public BesuException(String msg) {
		super(msg);
	}
	
	public BesuException(Exception e) {
		super(e);
	}

}
