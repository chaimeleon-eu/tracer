package es.upv.grycap.tracer.model.exceptions;

public class UserActionNotSupported extends RuntimeException {

	private static final long serialVersionUID = 5783438813428720754L;
	
	public UserActionNotSupported(String message) {
		super(message);
	}

}
