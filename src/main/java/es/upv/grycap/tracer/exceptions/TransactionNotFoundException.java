package es.upv.grycap.tracer.exceptions;

public class TransactionNotFoundException extends BigchaindbException {

	private static final long serialVersionUID = -7168889348954010076L;
	
    public TransactionNotFoundException() {
        super();
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionNotFoundException(Throwable cause) {
        super(cause);
    }

    protected TransactionNotFoundException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
