/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dell.isg.smi.service.server.configuration.model.ServerRequest;
import com.dell.isg.smi.service.server.configuration.utilities.ConfigurationUtils;

/**
 * @author Muqeeth.Kowkab
 *
 */
@Component
public class ServerRequestValidator implements Validator {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerRequestValidator.class.getName());

	@Override
	public boolean supports(Class<?> clazz) {
		return ServerRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {		
		logger.info("Validating ServerRequest");
		
		ServerRequest obj = (ServerRequest) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serverIP", "NotEmpty.serverIP");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serverUsername", "NotEmpty.serverUsername");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serverPassword", "NotEmpty.serverPassword");
		
        String serverIPAddress = obj.getServerIP();
        
        if (!ConfigurationUtils.validateIPAddress(serverIPAddress)) {
            errors.rejectValue("serverIP", "NotReachable.serverIP");
        }      
		
	}

}
