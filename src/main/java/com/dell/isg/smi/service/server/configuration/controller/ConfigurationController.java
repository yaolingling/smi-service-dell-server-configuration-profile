/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.controller;

import com.dell.isg.smi.adapter.server.config.ConfigEnum.EXPORT_MODE;
import com.dell.isg.smi.commons.model.common.Credential;
import com.dell.isg.smi.service.server.configuration.NfsYAMLConfiguration;
import com.dell.isg.smi.service.server.configuration.manager.IConfigurationManager;
import com.dell.isg.smi.service.server.configuration.model.BiosSetupRequest;
import com.dell.isg.smi.service.server.configuration.model.ComponentList;
import com.dell.isg.smi.service.server.configuration.model.ConfigureBiosResult;
import com.dell.isg.smi.service.server.configuration.model.MessageKey;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareImageRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerComponent;
import com.dell.isg.smi.service.server.configuration.model.ServerRequest;
import com.dell.isg.smi.service.server.configuration.model.ServiceResponse;
import com.dell.isg.smi.service.server.configuration.model.SystemBiosSettings;
import com.dell.isg.smi.service.server.configuration.model.SystemEraseRequest;
import com.dell.isg.smi.service.server.configuration.utilities.ConfigurationUtils;
import com.dell.isg.smi.service.server.configuration.validators.BiosSetupRequestValidator;
import com.dell.isg.smi.service.server.configuration.validators.ComponentListValidator;
import com.dell.isg.smi.service.server.configuration.validators.CredentialValidator;
import com.dell.isg.smi.service.server.configuration.validators.ServerAndNetworShareImageRequestValidator;
import com.dell.isg.smi.service.server.configuration.validators.ServerAndNetworShareValidator;
import com.dell.isg.smi.service.server.configuration.validators.ServerRequestValidator;
import com.dell.isg.smi.service.server.configuration.validators.SystemEraseValidator;
import com.dell.isg.smi.wsman.model.XmlConfig;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    
    @Autowired
    ComponentListValidator componentListValidator;
    @Autowired
    ServerAndNetworShareValidator serverAndNetworkShareValidator;
    @Autowired
    ServerAndNetworShareImageRequestValidator serverAndNetworkShareImageReqValidator;
    @Autowired
    CredentialValidator credentialValidator;
    @Autowired
    SystemEraseValidator systemEraseValidator;
    @Autowired
    ServerRequestValidator serverRequestValidator;
    @Autowired
    BiosSetupRequestValidator biosSetupRequestValidator;

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class.getName());


    /**
     * Endpoint to export the system configuration in normal mode from the server to a file on a remote share.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Export Configuration", nickname = "export", notes = "This operation allow user to export the system configuration in normal mode from the server to a file on a remote share", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> exportConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
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
    
    /**
     * Endpoint to export the system configuration in clone mode from the server to a file on a remote share.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/clone", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Clone Configuration", nickname = "clone", notes = "This operation allow user to export the system configuration in clone mode from the server to a file on a remote share", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> cloneConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
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

    /**
     * Endpoint to export the system configuration in replace mode from the server to a file on a remote share.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/replace", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Replace Configuration", nickname = "replace", notes = "This operation allow user to export the system configuration in replace mode from the server to a file on a remote share", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> replaceConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
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
    
    /**
     * Endpoint to export the factory setting system configuration from the server to a file on a remote share.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/factory", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Factory Configuration", nickname = "replace", notes = "This operation allow user to export the factory setting system configuration from the server to a file on a remote share", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> factoryConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            XmlConfig config = configurationManager.factoryConfiguration(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in factoryConfiguration : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }
    
    
    
    /**
     * Endpoint to preview the result of import system configuration from a file located on remote share to server.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/preview", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Preview Import Configuration", nickname = "import", notes = "This operation allow user to preview the result of import system configuration from a file located on remote share to server", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> previewConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
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
    
    /**
     * Endpoint to export the attribute registry from the server to a file on a remote share.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/exportInventory", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Export Hardware Inventory", nickname = "export", notes = "This operation allow user to export the hardware inventory for attribute registry from the server to a file on a remote share", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> exportInventory(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            XmlConfig config = configurationManager.exportInventory(request);
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

    /**
     * Endpoint to import the system configuration  from the remote share file on a server.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Import Configuration", nickname = "import", notes = "This operation allow user to import the system configuration from a file located on remote share to server", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> importConfiguration(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
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


    /**
     * Endpoint to take backup of configuration and firmware as Image from Server.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/image/backup", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Image Backup", nickname = "import", notes = "This operation allow user to take backup of configuration and firmware as Image from Server.", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> backupImage(@RequestBody @Valid ServerAndNetworkShareImageRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareImageReqValidator.validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            XmlConfig config = configurationManager.backupServerImage(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in backupImage : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }

    /**
     * Endpoint to restore the backed up server image of configuration and firmware..
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/image/restore", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Image Restore", nickname = "import", notes = "This operation allow user to restore the backed up image of configuration and firmware.", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> restoreImage(@RequestBody @Valid ServerAndNetworkShareImageRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareImageReqValidator.validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            XmlConfig config = configurationManager.restoreServerImage(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in restoreImage : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }

    /**
     * Endpoint to test the access of share from a server.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/testShare", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Test Share", nickname = "import", notes = "This operation allow user to test the export /import share.", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> testShare(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            String result = configurationManager.testShareAccessablity(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, result);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in Test Share : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }
 
    /**
     * Endpoint to delete all configurations from the Lifecycle controller before the system 644 is retired.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/lcwipe", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Life Controller Wipe", nickname = "import", notes = "This operation allow user to to delete all configurations from the Lifecycle controller before the system 644 is retired..", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> lcwipe(@RequestBody @Valid Credential request, BindingResult bindingResult) throws Exception {
        try {
        	credentialValidator.validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            XmlConfig config = configurationManager.wipeLifeController(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in LC Wipe : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }
    
    /**
     * Endpoint to create granular, user selectable, categories to increase flexibility and improve the repurposing aspect of the existing System Wipe feature.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/systemErase", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "System Erase", nickname = "import", notes = "This operation allow user to create granular, user selectable, categories to increase flexibility and improve the repurposing aspect of the existing System Wipe feature. List of components can be BIOS_RESET_DEFULT, EMBEDDED_DIAGNOSTICS_ERASE, OS_DRIVERPACK_ERASE, IDRAC_DEFAULT and LC_DATA_ERASE", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> systemErase(@RequestBody @Valid SystemEraseRequest request, BindingResult bindingResult) throws Exception {
        try {
        	systemEraseValidator.validate(request, bindingResult);
            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            XmlConfig config = configurationManager.systemEraseServer(request);
            String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg, config);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        } catch (Exception e) {
            logger.error("Exception occured in System Erase : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
        }

    }
    
    
    /**
     * Endpoint to get the system configuration for the specified components from server.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/getComponents", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Get Components", nickname = "getComponents", notes = "This operation gives the server system configuration.", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> getComponents(@RequestBody @Valid ServerAndNetworkShareRequest request, BindingResult bindingResult) throws Exception {
        List<ServerComponent> serverComponents = null;
        try {
        	serverAndNetworkShareValidator.validate(request, bindingResult);
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


    /**
     * Endpoint to update the system configuration for the specified server components.
     * @param request - Export configuration request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/updateComponents", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Update Components", nickname = "updateComponents", notes = "This operation updates the server system configuration", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> updateComponents(@ApiParam(value = "ComponentList to Update in the Configuration", required = true) @RequestBody @Valid ComponentList request,
                                                            BindingResult bindingResult) throws Exception {
        List<ServerComponent> updatedComponents = null;
        XmlConfig xmlConfig = null;
        try {
        	componentListValidator.validate(request, bindingResult);

            if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
            ServerAndNetworkShareRequest serverAndNetworkShareRequest = request.getServerAndNetworkShareRequest();
            Boolean initialized = configurationManager.initializeSCPService(serverAndNetworkShareRequest);
            if (initialized) {
                updatedComponents = configurationManager.updateComponents(request, request.isForceUpdate());
                String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
                if (CollectionUtils.isEmpty(updatedComponents)) {
                    requestMsg = messageSource.getMessage("Request.components.already.updated", null, Locale.getDefault());
                } else {
                	xmlConfig = configurationManager.importConfiguration(serverAndNetworkShareRequest);
                }
                ConfigurationUtils.removeRandomFile(serverAndNetworkShareRequest, yamlConfig);
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
     * Endpoint to get the configured BIOS configuration for the specified server.
     * @param request - Server Request.
     * @param bindingResult - Export configuration response binder.
     * @return
     * @throws Exception - On failure 
     */
    @RequestMapping(value = "/getBiosConfiguration", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Get BIOS Configuration", nickname = "getComponents", notes = "This operation gives the configured BIOS Configuration of server.", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> getBiosConfiguration(@RequestBody @Valid ServerRequest request, BindingResult bindingResult) throws Exception {
    	SystemBiosSettings systemBiosSettings = null;
		try {
			serverRequestValidator.validate(request, bindingResult);
			if (null == request || bindingResult.hasErrors()) {
                logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
            }
			systemBiosSettings = configurationManager.getBiosSettings(request);
			String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());
			ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg);
			serviceResponse.setSystemBiosSettings(systemBiosSettings);
			return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
		} catch (Exception e) {
			logger.error("Exception occured in getComponents : ", e);
            String error = e.getMessage();
            String failureMsg = messageSource.getMessage(MessageKey.REQUEST_PROCESS_FAILED.getKey(), null, Locale.getDefault());
            ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, failureMsg, error);
            return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
		}
    	
    }
    
    @RequestMapping(value = "/configureBios", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Configure BIOS Settings", nickname = "configureBios", notes = "This operation allows to configure BIOS Settings", response = ServiceResponse.class)
    public ResponseEntity<ServiceResponse> configureBios(@RequestBody @Valid BiosSetupRequest request, BindingResult bindingResult) throws Exception {
    	try {
    		biosSetupRequestValidator.validate(request, bindingResult);
    		if (null == request || bindingResult.hasErrors()) {    			
    			logger.error("Invalid Request or validation failure");
                ResponseEntity<ServiceResponse> invalidRequestResponse = getInvalidRequestResponse(bindingResult, MessageKey.INVALID_REQUEST);
                return invalidRequestResponse;
    		}
    		ConfigureBiosResult configureBiosResult = configurationManager.configureBios(request);
    		String requestMsg = messageSource.getMessage(MessageKey.REQUEST_SUCCESS.getKey(), null, Locale.getDefault());    		
			ServiceResponse serviceResponse = new ServiceResponse(HttpStatus.OK, requestMsg);
			serviceResponse.setConfigureBiosResult(configureBiosResult);
			return new ResponseEntity<ServiceResponse>(serviceResponse, new HttpHeaders(), serviceResponse.getStatus());
    	} catch (Exception e) {
    		logger.error("Exception occured in configureBios : ", e);
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
