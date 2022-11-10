package es.upv.grycap.tracer.service;

import java.io.IOException;

import javax.activation.UnsupportedDataTypeException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import es.upv.grycap.tracer.exceptions.BesuException;
import es.upv.grycap.tracer.exceptions.BigchaindbException;
import lombok.extern.slf4j.Slf4j;

/**
*
* @author Andy S Alic (asalic)
*/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	

	public static final String ERROR_MSG = "There's been an internal error. That's all we know";
	
	@ExceptionHandler({BesuException.class, BigchaindbException.class})
    public ResponseEntity<?> handleExternalBlockchainException(Exception e, HttpServletResponse response) throws IOException {
		log.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
	

	@ExceptionHandler({HttpClientErrorException.class, EntityNotFoundException.class, BadRequest.class, UnsupportedDataTypeException.class})
    public ResponseEntity<?> handleExternalDirectMessageException(Exception e, HttpServletResponse response) throws IOException {
		log.error(e.getMessage(), e);
		HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
		if (e instanceof HttpClientErrorException || e instanceof EntityNotFoundException || e instanceof BadRequest || e instanceof UnsupportedDataTypeException) { 
			status = HttpStatus.BAD_REQUEST;
		}
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), status);
    }
	
	@ExceptionHandler({java.net.ConnectException.class, InternalServerError.class,NullPointerException.class})
    public ResponseEntity<?> handleInternalServerError(Exception e, HttpServletResponse response) throws IOException {
		log.error(e.getMessage(), e);
		return new ResponseEntity<>(ERROR_MSG, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log(request, ex);
		return new ResponseEntity<>(ERROR_MSG, null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	protected void log(WebRequest req, Throwable ex) {
		//String requestId = MDC.get(mdcTokenKey);
		log.error(ex.getMessage(), ex);
	}
}
