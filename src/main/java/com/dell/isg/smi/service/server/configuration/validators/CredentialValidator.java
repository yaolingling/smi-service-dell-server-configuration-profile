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
import com.dell.isg.smi.service.server.configuration.utilities.ConfigurationUtils;

/**
 * @author Muqeeth_Kowkab
 *
 */
@Component
public class CredentialValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(CredentialValidator.class.getName());


    @Override
    public boolean supports(Class<?> clazz) {
        return Credential.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        logger.info("Validating Credential request");
        Credential obj = (Credential) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.address");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.password");

        String address = obj.getAddress();

        if (!ConfigurationUtils.validateIPAddress(address)) {
            errors.rejectValue("address", "NotReachable.address");
        }
    }

}
