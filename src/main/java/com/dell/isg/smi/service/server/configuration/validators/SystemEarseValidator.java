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

import com.dell.isg.smi.commons.model.common.Credential;
import com.dell.isg.smi.service.server.configuration.model.SystemEarseRequest;

/**
 * @author Muqeeth_Kowkab
 *
 */
@Component
public class SystemEarseValidator implements Validator {

	private static final Logger logger = LoggerFactory.getLogger(SystemEarseValidator.class.getName());

	private CredentialValidator credentialValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return SystemEarseRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		logger.info("Validating System Earse request");
		SystemEarseRequest obj = (SystemEarseRequest) target;

		Credential credential = obj.getCredential();
		ValidationUtils.invokeValidator(credentialValidator, credential, errors);
	}

}
