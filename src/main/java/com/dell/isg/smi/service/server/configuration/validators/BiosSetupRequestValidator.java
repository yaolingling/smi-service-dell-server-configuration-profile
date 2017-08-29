/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dell.isg.smi.service.server.configuration.model.BiosSetupRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerRequest;

/**
 * @author Muqeeth.Kowkab
 *
 */
@Component
public class BiosSetupRequestValidator implements Validator {
	
	private static final Logger logger = LoggerFactory.getLogger(BiosSetupRequestValidator.class.getName());
	
	@Autowired
	ServerRequestValidator serverRequestValidator;


	@Override
	public boolean supports(Class<?> clazz) {
		return BiosSetupRequest.class.isAssignableFrom(clazz);
	}


	@Override
	public void validate(Object target, Errors errors) {
		logger.info("Validating BiosSetup request");
		
		BiosSetupRequest obj = (BiosSetupRequest) target;
		
		ServerRequest serverRequest = obj.getServerRequest();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serverRequest", "Missing.object.serverRequest");
		
		if (null != serverRequest) {
			try {
				errors.pushNestedPath("serverRequest");
				ValidationUtils.invokeValidator(serverRequestValidator, obj.getServerRequest(), errors);
			} finally {
				errors.popNestedPath();
			}
		}
	}

}
