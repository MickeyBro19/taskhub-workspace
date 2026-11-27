package com.mickey.taskhub.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(
			NotFoundException ex,
			HttpServletRequest request
	                                              ) {
		
		return build(
				HttpStatus.NOT_FOUND,
				ex.getMessage(),
				request
		            );
	}
	
	private ResponseEntity<ApiError> build(
			HttpStatus status,
			String message,
			HttpServletRequest request
	                                      ) {
		
		return ResponseEntity
				.status(status)
				.body(new ApiError(
						Instant.now(),
						status.value(),
						status.getReasonPhrase(),
						message,
						request.getRequestURI()
				));
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiError> handleBadRequest(
			BadRequestException ex,
			HttpServletRequest request
	                                                ) {
		
		return build(
				HttpStatus.BAD_REQUEST,
				ex.getMessage(),
				request
		            );
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ApiError> handleConflict(
			ConflictException ex,
			HttpServletRequest request
	                                              ) {
		
		return build(
				HttpStatus.CONFLICT,
				ex.getMessage(),
				request
		            );
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(
			MethodArgumentNotValidException ex,
			HttpServletRequest request
	                                                ) {
		
		String message = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.findFirst()
				.map(error -> error.getField() + ": "
						+ error.getDefaultMessage())
				.orElse("Invalid request");
		
		return build(
				HttpStatus.BAD_REQUEST,
				message,
				request
		            );
	}
}
