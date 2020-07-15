package br.com.aegro.production.resources.exceptions;

import br.com.aegro.production.services.exceptions.ResourceNotFoundException;
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

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<StandardError> badRequest(MethodArgumentNotValidException ex, HttpServletRequest request) {
		String error = "Invalid argument";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		StandardError e = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
		return ResponseEntity.status(status).body(e);
	}

	@ExceptionHandler({HttpMessageNotReadableException.class})
	public ResponseEntity<StandardError> badRequest(HttpMessageNotReadableException ex, HttpServletRequest request) {
		String error = "Resource not valid";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError e = new StandardError(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(e);
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<StandardError> badRequest(IllegalArgumentException ex, HttpServletRequest request) {
		String error = "Illegal Argument";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError e = new StandardError(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(e);
	}

	@ExceptionHandler({ResourceNotFoundException.class})
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError e = new StandardError(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(e);
	}

	@ExceptionHandler({NoSuchElementException.class})
	public ResponseEntity<StandardError> resourceNotFound(NoSuchElementException ex, HttpServletRequest request) {
		String error = "Element not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError e = new StandardError(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(e);
	}

	@ExceptionHandler({MongoWriteException.class})
	public ResponseEntity<StandardError> resourceNotFound(MongoWriteException ex, HttpServletRequest request) {
		String error = "Database error";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError e = new StandardError(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(e);
	}

}
