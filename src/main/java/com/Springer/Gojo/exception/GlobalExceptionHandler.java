package com.Springer.Gojo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Springer.Gojo.Dto.Exception.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Resource/User Not Found (404)
	@ExceptionHandler({UserNotFoundException.class, ResourceNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new  ErrorResponse(false, ex.getMessage(), 404));
	}

    // Conflict / Already Exists (409)
	@ExceptionHandler({EmailAlreadyExistsException.class, IllegalStateException.class})
	public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(false, ex.getMessage(), 409));
	}

	// Unauthorized / Invalid Credentials (401)
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(false,ex.getMessage(), 401));
	}

	// Catch All (500)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		return new ResponseEntity<>(new ErrorResponse(false,ex.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Forbidden (403)
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleForbidden(UnauthorizedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(false, ex.getMessage(), 403));
	}
	
	// Bad Request/ Validation
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(false, ex.getMessage(), 400));
	}
	
	// For DTO validation fails
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
		
		Map<String,String> errors = new HashMap<>();
		
		ex.getBindingResult()
		  .getFieldErrors()
		  .forEach(error -> {errors.put(error.getField(), error.getDefaultMessage());
		  });
	
		ErrorResponse response = new ErrorResponse(false, "Validation Failed", 400, errors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
