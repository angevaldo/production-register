package br.com.aegro.production.resources.exceptions;

import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<StandardError> badRequest(MethodArgumentNotValidException ex, HttpServletRequest request) {
		String msg = "Invalid argument";
		HttpStatus httpSts = HttpStatus.BAD_REQUEST;
		String defaultMsg = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		StandardError standardErr = new StandardError(Instant.now(), httpSts.value(), msg, defaultMsg, request.getRequestURI());
		return status(httpSts).body(standardErr);
	}

	@ExceptionHandler({HttpMessageNotReadableException.class})
	public ResponseEntity<StandardError> badRequest(HttpMessageNotReadableException ex, HttpServletRequest request) {
		String msg = "Object not valid";
		HttpStatus httpSts = HttpStatus.BAD_REQUEST;
		StandardError standardErr = new StandardError(Instant.now(), httpSts.value(), msg, ex.getMessage(), request.getRequestURI());
		return status(httpSts).body(standardErr);
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<StandardError> badRequest(IllegalArgumentException ex, HttpServletRequest request) {
		String msg = "Illegal Argument";
		HttpStatus httpSts = HttpStatus.BAD_REQUEST;
		StandardError standardErr = new StandardError(Instant.now(), httpSts.value(), msg, ex.getMessage(), request.getRequestURI());
		return status(httpSts).body(standardErr);
	}

	@ExceptionHandler({ObjectNotFoundException.class})
	public ResponseEntity<StandardError> resourceNotFound(ObjectNotFoundException ex, HttpServletRequest request) {
		String msg = "Object not found";
		HttpStatus httpSts = HttpStatus.NOT_FOUND;
		StandardError standardErr = new StandardError(Instant.now(), httpSts.value(), msg, ex.getMessage(), request.getRequestURI());
		return status(httpSts).body(standardErr);
	}

	@ExceptionHandler({NoSuchElementException.class})
	public ResponseEntity<StandardError> resourceNotFound(NoSuchElementException ex, HttpServletRequest request) {
		String msg = "Element not found";
		HttpStatus httpSts = HttpStatus.NOT_FOUND;
		StandardError standardErr = new StandardError(Instant.now(), httpSts.value(), msg, ex.getMessage(), request.getRequestURI());
		return status(httpSts).body(standardErr);
	}

	@ExceptionHandler({MongoWriteException.class})
	public ResponseEntity<StandardError> resourceNotFound(MongoWriteException ex, HttpServletRequest request) {
		String msg = "Database error";
		HttpStatus httpSts = HttpStatus.CONFLICT;
		StandardError standardErr = new StandardError(Instant.now(), httpSts.value(), msg, ex.getMessage(), request.getRequestURI());
		return status(httpSts).body(standardErr);
	}

}
