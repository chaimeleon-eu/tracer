package es.upv.grycap.tracer.model.exceptions;

public class UnknownReqResType extends RuntimeException {

	private static final long serialVersionUID = 6537181959217424049L;
	
	public UnknownReqResType(String msg) {
		super(msg);
	}

}
