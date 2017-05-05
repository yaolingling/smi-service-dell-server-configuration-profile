/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.service.server.configuration.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dell.isg.smi.service.server.configuration.model.ComponentList;

/**
 * @author Muqeeth_Kowkab
 *
 */
public class ComponentListValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(ComponentListValidator.class.getName());


    @Override
    public boolean supports(Class<?> clazz) {
        return ComponentList.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {

        ComponentList obj = (ComponentList) target;

        try {
            errors.pushNestedPath("serverAndNetworkShareRequest");
            ValidationUtils.invokeValidator(new ServerAndNetworShareValidator(), obj.getServerAndNetworkShareRequest(), errors);
        } finally {
            errors.popNestedPath();
        }
    }

}
