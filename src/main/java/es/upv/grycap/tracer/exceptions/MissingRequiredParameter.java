package es.upv.grycap.tracer.exceptions;

public class MissingRequiredParameter  extends RuntimeException {

	private static final long serialVersionUID = 3489823199640754412L;
	
	public MissingRequiredParameter(String message) {
		super(message);
	}

}
