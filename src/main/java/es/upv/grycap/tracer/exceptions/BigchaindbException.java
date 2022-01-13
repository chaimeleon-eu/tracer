package es.upv.grycap.tracer.exceptions;

public class BigchaindbException extends RuntimeException {

	private static final long serialVersionUID = -8762663498200410656L;
	
    public BigchaindbException() {
        super();
    }

    public BigchaindbException(String message) {
        super(message);
    }

    public BigchaindbException(String message, Throwable cause) {
        super(message, cause);
    }

    public BigchaindbException(Throwable cause) {
        super(cause);
    }

    protected BigchaindbException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
