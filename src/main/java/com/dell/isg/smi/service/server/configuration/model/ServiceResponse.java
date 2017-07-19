/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.dell.isg.smi.service.server.configuration.model.ServerComponent;
import com.dell.isg.smi.wsman.model.XmlConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_DEFAULT)
@ApiModel(value = "ServiceResponse", description = "This has the detailed description of the result of microservice call. It includes Message, errors if any like Validation errors, error message, XMLConfig the result returned from the DELL Servers for the Server Configuration transaction.")
public class ServiceResponse {

    @ApiModelProperty(hidden = true)
    private HttpStatus status;

    @ApiModelProperty(value = "Response Message.", dataType = "string", required = false)
    private String message;

    @ApiModelProperty(value = "List of Errors.", required = false)
    private List<String> errors;

    @ApiModelProperty(value = "Error message", dataType = "string", required = false)
    private String error;

    @ApiModelProperty(value = "The XMLConfig. Which has the details of JobID, Message and Result returned from DELL Server", required = false)
    private XmlConfig xmlConfig;

    @ApiModelProperty(value = "The ServerComponent. Which has the FQDD, Attributes and nested components", dataType = "string", required = false)
    private List<ServerComponent> serverComponents;
    
    @ApiModelProperty(value = "The Result. Which has the details of preview import configuration Result returned from DELL Server", required = false)
    private Object result;

	public ServiceResponse(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }


    public ServiceResponse(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }


    public ServiceResponse(HttpStatus status, String message, XmlConfig xmlConfig) {
        super();
        this.status = status;
        this.message = message;
        this.setXmlConfig(xmlConfig);
    }


    public ServiceResponse(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.error = error;
    }
    
    public ServiceResponse(HttpStatus status, String message, Object result) {
        super();
        this.status = status;
        this.message = message;
        this.setXmlConfig(xmlConfig);
    }


    /**
     * @return the status
     */
    public HttpStatus getStatus() {
        return status;
    }


    /**
     * @param status the status to set
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }


    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }


    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * @return the errors
     */
    public List<String> getErrors() {
        return errors;
    }


    /**
     * @param errors the errors to set
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }


    public String getError() {
        return error;
    }


    public void setError(String error) {
        this.error = error;
    }


    public XmlConfig getXmlConfig() {
        return xmlConfig;
    }


    public void setXmlConfig(XmlConfig xmlConfig) {
        this.xmlConfig = xmlConfig;
    }
    
    public Object getResult() {
		return result;
	}


	public void setResult(Object result) {
		this.result = result;
	}


    /**
     * @return the serverComponents
     */
    public List<ServerComponent> getServerComponents() {
        return serverComponents;
    }


    /**
     * @param serverComponents the serverComponents to set
     */
    public void setServerComponents(List<ServerComponent> serverComponents) {
        this.serverComponents = serverComponents;
    }

}
