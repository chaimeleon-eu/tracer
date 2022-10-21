package es.upv.grycap.tracer.exceptions;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.activation.UnsupportedDataTypeException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UncheckedExceptionFactory {
	
	public static RuntimeException get(Exception ex) {
		if (ex instanceof JsonProcessingException)
			return new UncheckedJsonProcessingException((JsonProcessingException)ex);
		else if (ex instanceof InvalidKeyException)
			return new UncheckedInvalidKeyException((InvalidKeyException)ex);
		else if (ex instanceof NoSuchAlgorithmException)
			return new UncheckedNoSuchAlgorithmException((NoSuchAlgorithmException)ex);
		else if (ex instanceof SignatureException)
			return new UncheckedSignatureException((SignatureException)ex);
		else if (ex instanceof UnsupportedDataTypeException)
			return new UncheckedUnsupportedDataTypeException((UnsupportedDataTypeException)ex);
		else if (ex instanceof IOException) 
			return new UncheckedIOException((IOException) ex);
		else 
			throw new UnhandledException("Exception of type " + ex.getClass().getCanonicalName() + " not handled");
	}

}
