/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dell.isg.smi.adapter.server.config.ConfigEnum.EXPORT_MODE;
import com.dell.isg.smi.service.server.configuration.NfsYAMLConfiguration;
import com.dell.isg.smi.service.server.configuration.manager.IConfigurationManager;
import com.dell.isg.smi.service.server.configuration.model.ComponentList;
import com.dell.isg.smi.service.server.configuration.model.MessageKey;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerComponent;
import com.dell.isg.smi.service.server.configuration.model.ServiceResponse;
import com.dell.isg.smi.service.server.configuration.utilities.ConfigurationUtils;
import com.dell.isg.smi.service.server.configuration.validators.ComponentListValidator;
import com.dell.isg.smi.service.server.configuration.validators.ServerAndNetworShareValidator;
import com.dell.isg.smi.wsman.model.XmlConfig;

import io.swagger.annotations.ApiOperation;

/**
 * @author Muqeeth_Kowkab
 *
 */

@RestController
@RequestMapping("/api/1.0/server/configuration")
public class ConfigurationController {

    @Autowired
    ResourceBundleMessageSource messageSource;

    @Autowired
    IConfigurationManager configurationManager;
    
    @Autowired
    NfsYAMLConfiguration yamlConfig;

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class.getName());


    @RequestMapping(value = "/export", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Export Configuration", nickname = "export", notes = "This operation allow user to export the system configuration in normal mode from the server to a file on a remote share", response = ServiceResponse.class)
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "serverAndNetworkShareRequest", value = "ServerAndNetworkShareRequest", required = true, dataType =
    // "com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest", paramType = "Body") })
    // @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
    // @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Failure") })

    public ResponseEntity<ServiceResponse> exportConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
            new ServerAndNetworShareValidator().validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
    		String mode = EXPORT_MODE.NORMAL.getValue();
            XmlConfig config = configurationManager.exportConfiguration(request, mode);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in exportConfiguration : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }
    
    @RequestMapping(value = "/clone", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Clone Configuration", nickname = "clone", notes = "This operation allow user to export the system configuration in clone mode from the server to a file on a remote share", response = ServiceResponse.class)
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "serverAndNetworkShareRequest", value = "ServerAndNetworkShareRequest", required = true, dataType =
    // "com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest", paramType = "Body") })
    // @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
    // @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Failure") })

    public ResponseEntity<ServiceResponse> cloneConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
            new ServerAndNetworShareValidator().validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            String mode = EXPORT_MODE.CLONE.getValue();
            XmlConfig config = configurationManager.exportConfiguration(request, mode);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in cloneConfiguration : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }

    @RequestMapping(value = "/replace", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Replace Configuration", nickname = "replace", notes = "This operation allow user to export the system configuration in replace mode from the server to a file on a remote share", response = ServiceResponse.class)
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "serverAndNetworkShareRequest", value = "ServerAndNetworkShareRequest", required = true, dataType =
    // "com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest", paramType = "Body") })
    // @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
    // @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Failure") })

    public ResponseEntity<ServiceResponse> replaceConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
            new ServerAndNetworShareValidator().validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            String mode = EXPORT_MODE.REPLACE.getValue();
            XmlConfig config = configurationManager.exportConfiguration(request, mode);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in replaceConfiguration : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }
    
    @RequestMapping(value = "/preview", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Preview Import Configuration", nickname = "import", notes = "This operation allow user to get the preview of the import system configuration from a file located on remote share to server", response = ServiceResponse.class)
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "serverAndNetworkShareRequest", value = "ServerAndNetworkShareRequest", required = true, dataType =
    // "com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest", paramType = "Body") })
    // @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
    // @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Failure") })

    public ResponseEntity<ServiceResponse> previewConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
            new ServerAndNetworShareValidator().validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            Object result = configurationManager.previewConfiguration(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, result);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in previewConfiguration : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }
    
    @RequestMapping(value = "/exportRegistry", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Export Attribute Configuration", nickname = "export", notes = "This operation allow user to export the hardware inventory for attribute registry from the server to a file on a remote share", response = ServiceResponse.class)
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "serverAndNetworkShareRequest", value = "ServerAndNetworkShareRequest", required = true, dataType =
    // "com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest", paramType = "Body") })
    // @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
    // @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Failure") })

    public ResponseEntity<ServiceResponse> exportRegistry(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
            new ServerAndNetworShareValidator().validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            XmlConfig config = configurationManager.exportRegistry(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in exportRegistry : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }

    @RequestMapping(value = "/import", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Import Configuration", nickname = "import", notes = "This operation allow user to import the system configuration from a file located on remote share to server", response = ServiceResponse.class)
    // @ApiImplicitParams({
    // @ApiImplicitParam(name = "serverAndNetworkShareRequest", value = "ServerAndNetworkShareRequest", required = true, dataType =
    // "com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest", paramType = "Body") })
    // @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
    // @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Failure") })

    public ResponseEntity<ServiceResponse> importConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
            new ServerAndNetworShareValidator().validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            XmlConfig config = configurationManager.importConfiguration(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in importConfiguration : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }


    @RequestMapping(value = "/getComponents", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Get Components", nickname = "getComponents", notes = "This operation gives the server system configuration.", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> getComponents(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        List<ServerComponent> serverComponents = null;
        try {
            // System.out.println(ReflectionToStringBuilder.toString(request, new CustomRecursiveToStringStyle(99)));
            new ServerAndNetworShareValidator().validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            Boolean initialized = configurationManager.initializeSCPService(request);
            if (initialized) {
                serverComponents = configurationManager.getComponents(request);
                String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
                if (CollectionUtils.isEmpty(serverComponents)) {                    
                    requestMsg = messageSource.getMessage("Request.No.ServerComponents", null, Locale.getDefault());
                }
                ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg);
                serviceResponse.setServerComponents(serverComponents);
                ConfigurationUtils.removeRandomFile(request, yamlConfig);
                return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
            } else {
                logger.error("Failed to mount the network share and initializing failed.");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.FAILED_INIT);
                return invalidRequestResponse;
            }

        } catch (Exception e) {
            logger.error("Exception occured in getComponents : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }
    }


    @RequestMapping(value = "/updateComponents", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Update Components", nickname = "updateComponents", notes = "This operation updates the server system configuration", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> updateComponents(@RequestBody @Valid ComponentList request, BindingResult bindingResult) throws Exception {
        List<ServerComponent> updatedComponents = null;
        try {
            // System.out.println(ReflectionToStringBuilder.toString(request, new CustomRecursiveToStringStyle(99)));

            new ComponentListValidator().validate(request, bindingResult);

            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            ServerAndNetworkShareRequest serverAndNetworkShareRequest = request.getServerAndNetworkShareRequest();
            Boolean initialized = configurationManager.initializeSCPService(serverAndNetworkShareRequest);
            if (initialized) {
                updatedComponents = configurationManager.updateComponents(request);
                XmlConfig xmlConfig = configurationManager.importConfiguration(serverAndNetworkShareRequest);
                ConfigurationUtils.removeRandomFile(serverAndNetworkShareRequest, yamlConfig);

                String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
                if (CollectionUtils.isEmpty(updatedComponents)) {
                    requestMsg = "All Components are already updated. Nothing to update.";
                }
                ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg);
                serviceResponse.setServerComponents(updatedComponents);
                serviceResponse.setXmlConfig(xmlConfig);
                return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
            } else {
                logger.error("Failed to mount the network share and initializing failed.");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.FAILED_INIT);
                return invalidRequestResponse;
            }

        } catch (Exception e) {
            logger.error("Exception occured in updateComponents : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }
    }


    /**
     * Gets the invalid request response.
     *
     * @param bindingResult the binding result
     * @param messageKey the MessageKey
     * @return the invalid request response
     */
    private ResponseEntity<ServiceResponse> getInvalidRequestResponse(BindingResult bindingResult, MessageKey messageKey) {

        List<ObjectError> allObjErrors = bindingResult.getAllErrors();
        List<String> errors = new ArrayList<String>();

        if (CollectionUtils.isNotEmpty(allObjErrors)) {
            for (ObjectError objError : allObjErrors) {
                if (objError instanceof FieldError) {
                    String errorMsg = messageSource.getMessage(objError, null);
                    logger.error("Error message for validation failure: " + errorMsg);
                    errors.add(errorMsg);
                }
            }
        }

        String invalidReqMsg = messageSource.getMessage(messageKey.getKey(), null, Locale.getDefault());
        ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.BAD_REQUEST, invalidReqMsg, errors);
        return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
    }

}
