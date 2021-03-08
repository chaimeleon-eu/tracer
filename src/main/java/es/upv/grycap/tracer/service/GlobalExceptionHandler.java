package es.upv.grycap.tracer.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
*
* @author Andy S Alic (asalic)
*/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	

	public static final String ERROR_MSG = "There's been an internal error. That's all we know";

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
