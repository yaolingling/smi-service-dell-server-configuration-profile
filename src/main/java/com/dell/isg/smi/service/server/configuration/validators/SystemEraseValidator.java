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

import com.dell.isg.smi.commons.model.common.Credential;
import com.dell.isg.smi.service.server.configuration.model.SystemEraseRequest;

/**
 * @author Muqeeth_Kowkab
 *
 */
@Component
public class SystemEraseValidator implements Validator {
	
	@Autowired
    CredentialValidator credentialValidator;

	private static final Logger logger = LoggerFactory.getLogger(SystemEraseValidator.class.getName());	

	@Override
	public boolean supports(Class<?> clazz) {
		return SystemEraseRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.info("Validating System Erase request");
		SystemEraseRequest obj = (SystemEraseRequest) target;

		Credential credential = obj.getCredential();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "credential", "Missing.object.Credential");
		
		if (null != credential) {
			try {
				errors.pushNestedPath("credential");
				ValidationUtils.invokeValidator(credentialValidator, credential, errors);
			} finally {
				errors.popNestedPath();
			}
		}
	}

}
