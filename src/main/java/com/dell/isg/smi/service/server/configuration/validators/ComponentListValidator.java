/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.service.server.configuration.validators;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dell.isg.smi.service.server.configuration.model.ComponentList;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerComponent;

/**
 * @author Muqeeth_Kowkab
 *
 */
@Component
public class ComponentListValidator implements Validator {
	
	@Autowired
	ServerAndNetworShareValidator serverAndNetworkShareValidator;

    private static final Logger logger = LoggerFactory.getLogger(ComponentListValidator.class.getName());


    @Override
    public boolean supports(Class<?> clazz) {
        return ComponentList.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
    	logger.info("Validating ComponentListValidator request");

        ComponentList obj = (ComponentList) target;
        
        ServerAndNetworkShareRequest serverAndNetworkShareRequest = obj.getServerAndNetworkShareRequest();
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serverAndNetworkShareRequest", "Missing.object.ServerAndNetworkShareRequest");
        
        
        if (null != serverAndNetworkShareRequest) {
        	try {
                errors.pushNestedPath("serverAndNetworkShareRequest");
                ValidationUtils.invokeValidator(serverAndNetworkShareValidator, obj.getServerAndNetworkShareRequest(), errors);
            } finally {
                errors.popNestedPath();
            }
        }       
        
        List<ServerComponent> serverComponents = obj.getServerComponents();
        
        if (CollectionUtils.isEmpty(serverComponents)) {
        	errors.rejectValue("serverComponents", "Missing.object.ServerComponent");
        }       
    }

}
