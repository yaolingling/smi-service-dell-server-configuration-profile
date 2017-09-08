/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dell.isg.smi.service.server.configuration.model.BiosSetupRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerRequest;
import com.dell.isg.smi.service.server.configuration.utilities.ConfigurationUtils;

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
		
		String scheduledStartTime = obj.getScheduledStartTime();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "scheduledStartTime", "Missing.ScheduledStartTime");
		String untilTime = obj.getUntilTime();
		
		if (StringUtils.isNotBlank(scheduledStartTime) && !StringUtils.equals(scheduledStartTime, "TIME_NOW")
				&& !ConfigurationUtils.validateTime(scheduledStartTime)){			
			errors.rejectValue("scheduledStartTime", "Invalid.ScheduledStartTime.Format");
		}
		if (StringUtils.isNotBlank(untilTime) && !ConfigurationUtils.validateTime(untilTime)) {
			errors.rejectValue("untilTime", "Invalid.UntilTime.Format");
		}
		
		if (StringUtils.isNotBlank(scheduledStartTime) && StringUtils.isNotBlank(untilTime)
				&& !ConfigurationUtils.validateEndTimeAfterStartTime(scheduledStartTime, untilTime)) {
			errors.rejectValue("untilTime", "Invalid.StartEndTime.Range");
		}
		
		int rebootJobType = obj.getRebootJobType();
		
		if (rebootJobType <=0 || rebootJobType >3) {
			errors.rejectValue("untilTime", "Invalid.RebootJobType");
		}
		
		
	}

}
