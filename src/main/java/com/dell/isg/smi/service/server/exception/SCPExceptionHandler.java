/**
 * 
 */
package com.dell.isg.smi.service.server.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dell.isg.smi.service.server.configuration.model.ServiceResponse;

/**
 * @author mkowkab
 *
 */
@ControllerAdvice
public class SCPExceptionHandler{
	
	 @Autowired
	    ResourceBundleMessageSource messageSource;

	@ExceptionHandler(value={IllegalArgumentException.class, IllegalStateException.class, HttpMessageNotReadableException.class})
	protected ResponseEntity<ServiceResponse> handleException(RuntimeException ex) {
		String errorMessage = messageSource.getMessage("Invalid.request", null, Locale.getDefault());
		ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.BAD_REQUEST, errorMessage);
		return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());		
	}
}
