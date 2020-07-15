package br.com.aegro.production.resources.exceptions;

import br.com.aegro.production.services.exceptions.ResourceNotFoundException;
import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<StandardError> badRequest(IllegalArgumentException e, HttpServletRequest request) {
		String error = "Bad request";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler({ResourceNotFoundException.class, NoSuchElementException.class})
	public ResponseEntity<StandardError> resourceNotFound(RuntimeException e, HttpServletRequest request) {
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler({MongoWriteException.class})
	public ResponseEntity<StandardError> resourceNotFound(MongoWriteException e, HttpServletRequest request) {
		String error = "Database error";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

}
