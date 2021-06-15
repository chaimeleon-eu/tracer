package es.upv.grycap.tracer.model.exceptions;

public class UncheckedInterruptedException extends RuntimeException {

	private static final long serialVersionUID = -1541603321496850123L;
	
	public UncheckedInterruptedException(InterruptedException ex) {
		super(ex);
	}

}
