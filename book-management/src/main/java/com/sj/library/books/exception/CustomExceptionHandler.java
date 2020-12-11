package com.sj.library.books.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller advice for managing exceptions
 * 
 * @author Sahil Jain
 *
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> onOtherException(RuntimeException ex) {
		return new ResponseEntity<>("Some error occured : " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
