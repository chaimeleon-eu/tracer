package es.upv.grycap.tracer.exceptions;

public class OperationNotSupportedException extends RuntimeException {
    

    public OperationNotSupportedException(String msg) {
        super(msg);
    }
    
    public OperationNotSupportedException(Exception e) {
        super(e);
    }

}
