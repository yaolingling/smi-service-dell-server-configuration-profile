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

import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareImageRequest;
import com.dell.isg.smi.service.server.configuration.utilities.ConfigurationUtils;

/**
 * @author Muqeeth_Kowkab
 *
 */
@Component
public class ServerAndNetworShareImageRequestValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(ServerAndNetworShareImageRequestValidator.class.getName());


    @Override
    public boolean supports(Class<?> clazz) {
        return ServerAndNetworkShareImageRequest.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        logger.info("Validating Server and network details request");
        ServerAndNetworkShareImageRequest obj = (ServerAndNetworkShareImageRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serverIP", "NotEmpty.serverIP");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serverUsername", "NotEmpty.serverUsername");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serverPassword", "NotEmpty.serverPassword");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shareType", "NotEmpty.shareType");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shareName", "NotEmpty.shareName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shareAddress", "NotEmpty.shareAddress");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "imageName", "NotEmpty.imageName");
        
        
        int shareType = obj.getShareType();
        
        if (shareType ==2) {
        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shareUsername", "NotEmpty.shareUsername");
        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sharePassword", "NotEmpty.sharePassword");
        }
        

        String serverIPAddress = obj.getServerIP();
        String shareAddress = obj.getShareAddress();

        if (!ConfigurationUtils.validateIPAddress(serverIPAddress)) {
            errors.rejectValue("serverIP", "NotReachable.ipAddress");
        }

        if (!ConfigurationUtils.validateIPAddress(shareAddress)) {
            errors.rejectValue("shareAddress", "NotReachable.ipAddress");
        }
    }

}
