/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dell.isg.smi.adapter.server.model.WsmanCredentials;
import com.dell.isg.smi.commons.elm.exception.RuntimeCoreException;
import com.dell.isg.smi.commons.elm.utilities.CustomRecursiveToStringStyle;
import com.dell.isg.smi.commons.model.common.Credential;
import com.dell.isg.smi.commons.model.common.ResponseString;
import com.dell.isg.smi.service.server.configuration.manager.ITrapManager;
import com.dell.isg.smi.service.server.exception.BadRequestException;
import com.dell.isg.smi.service.server.exception.EnumErrorCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/1.0/server/configuration/trap")
public class TrapConfigController {

    private static final Logger logger = LoggerFactory.getLogger(TrapConfigController.class);

    @Autowired
    ITrapManager trapManagerImpl;


    @RequestMapping(value = "/configureTraps/{trapDestination}", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "/configureTraps/{trapDestination}", nickname = "traps", notes = "This operation allow user to configure server traps throu wsman.", response = ResponseString.class)
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "Credential", value = "Credential", required = true, dataType = "Credential.class", paramType = "Body", defaultValue = "no default") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ResponseString.class), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
    public ResponseString configureTraps(@RequestBody Credential credential, @PathVariable("trapDestination") String trapDestination) {
        logger.trace("Credential for configuring traps : ", credential.getAddress(), credential.getUserName());
        String result = "Failed to configure the traps.";
        if (credential == null || StringUtils.isEmpty(credential.getAddress())) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorCode(EnumErrorCode.IOIDENTITY_INVALID_INPUT);
            throw badRequestException;
        }

        if (trapDestination == null || StringUtils.isEmpty(trapDestination)) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorCode(EnumErrorCode.IOIDENTITY_INVALID_INPUT);
            throw badRequestException;
        }
        try {
            WsmanCredentials wsmanCredentials = new WsmanCredentials(credential.getAddress(), credential.getUserName(), credential.getPassword());
            boolean status = trapManagerImpl.configureTraps(wsmanCredentials, trapDestination);
            if (status) {
                result = "Successfully to configure the traps.";
            }
        } catch (Exception e) {
            logger.error("Exception occured in trap configuration : ", e);
            RuntimeCoreException runtimeCoreException = new RuntimeCoreException(e);
            runtimeCoreException.setErrorCode(EnumErrorCode.ENUM_SERVER_ERROR);
            throw runtimeCoreException;
        }
        logger.trace("Trap Response : ", ReflectionToStringBuilder.toString(result, new CustomRecursiveToStringStyle(99)));
        return new ResponseString(result);
    }


    @RequestMapping(value = "/updateTrapFormat/{trapFormat}", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "/updateTrapFormat/{trapFormat}", nickname = "trapFormat", notes = "This operation allow user to update server traps throu wsman.", response = ResponseString.class)
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "Credential", value = "Credential", required = true, dataType = "Credential.class", paramType = "Body", defaultValue = "no default") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ResponseString.class), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
    public ResponseString updateTrapFormat(@RequestBody Credential credential, @PathVariable("trapFormat") String trapFormat) {
        logger.trace("Credential for configuring trap format : ", credential.getAddress(), credential.getUserName());
        String result = "Failed to update snmp trap format.";
        if (credential == null || StringUtils.isEmpty(credential.getAddress())) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorCode(EnumErrorCode.IOIDENTITY_INVALID_INPUT);
            throw badRequestException;
        }

        if (trapFormat == null || StringUtils.isEmpty(trapFormat)) {
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.setErrorCode(EnumErrorCode.IOIDENTITY_INVALID_INPUT);
            throw badRequestException;
        }

        try {
            WsmanCredentials wsmanCredentials = new WsmanCredentials(credential.getAddress(), credential.getUserName(), credential.getPassword());
            boolean status = trapManagerImpl.updateTrapFormat(wsmanCredentials, trapFormat);
            if (status) {
                result = "Successfully  update snmp trap format.";
            }
        } catch (Exception e) {
            logger.error("Exception occured in trap configuration : ", e);
            RuntimeCoreException runtimeCoreException = new RuntimeCoreException(e);
            runtimeCoreException.setErrorCode(EnumErrorCode.ENUM_SERVER_ERROR);
            throw runtimeCoreException;
        }
        logger.trace("Trap format response : ", ReflectionToStringBuilder.toString(result, new CustomRecursiveToStringStyle(99)));
        return new ResponseString(result);
    }
}
