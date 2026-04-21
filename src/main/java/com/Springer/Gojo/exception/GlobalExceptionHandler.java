package com.Springer.Gojo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Springer.Gojo.Dto.Exception.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Resource/User Not Found (404)
	@ExceptionHandler({UserNotFoundException.class, ResourceNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
		log.atWarn()
	       .setMessage("Lookup failed")
	       .addKeyValue("errorDetail", ex.getMessage())
	       .log();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new  ErrorResponse(false, ex.getMessage(), 404));
	}

    // Conflict / Already Exists (409)
	@ExceptionHandler({EmailAlreadyExistsException.class, IllegalStateException.class})
	public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
		log.atWarn()
	       .setMessage("Lookup failed")
	       .addKeyValue("errorDetail", ex.getMessage())
	       .log();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(false, ex.getMessage(), 409));
	}

	// Unauthorized / Invalid Credentials (401)
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex){
		log.atWarn()
	       .setMessage("Invalid Credentials")
	       .addKeyValue("errorDetail", ex.getMessage())
	       .log();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(false,ex.getMessage(), 401));
	}

	// Catch All (500)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		log.atError()
	       .setCause(ex) // Automatically handles the full stack trace
	       .setMessage("Internal system failure occurred")
	       .addKeyValue("exceptionType", ex.getClass().getSimpleName())
	       .log();
		return new ResponseEntity<>(new ErrorResponse(false,"An internal error occurred. Please contact support.", 500), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Forbidden (403)
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleForbidden(UnauthorizedException ex) {
		log.atWarn()
	       .setMessage("Lookup failed")
	       .addKeyValue("errorDetail", ex.getMessage())
	       .log();
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(false, ex.getMessage(), 403));
	}
	
	// Bad Request/ Validation
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex){
		log.atWarn()
	       .setMessage("Lookup failed")
	       .addKeyValue("errorDetail", ex.getMessage())
	       .log();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(false, ex.getMessage(), 400));
	}
	
	// For DTO validation fails
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
		
		log.atWarn()
			.setMessage("Validation failed for incoming request")
			.addKeyValue("errorCount", ex.getBindingResult().getErrorCount())
			.addKeyValue("fields", ex.getBindingResult().getFieldErrors().stream()
										.map(FieldError::getField).toList())
			.log();
		
		Map<String,String> errors = new HashMap<>();
		
		ex.getBindingResult()
		  .getFieldErrors()
		  .forEach(error -> {errors.put(error.getField(), error.getDefaultMessage());
		  });
	
		ErrorResponse response = new ErrorResponse(false, "Validation Failed", 400, errors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
