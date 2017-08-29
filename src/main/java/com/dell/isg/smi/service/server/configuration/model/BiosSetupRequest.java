/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Muqeeth.Kowkab
 *
 */
@ApiModel(value = "BiosSetupRequest", description = "Captures input details to configure BIOS of Dell Server")
public class BiosSetupRequest {
	
	@ApiModelProperty(value = "Dell Server Credentials", required = true, position=1)
	private ServerRequest serverRequest;
	
	@ApiModelProperty(value = "List of BIOS Attribute Object. Which include Attibute Name and Attribute Value", required = true, position=2)
	private List<Attribute> attributes;
	
	@ApiModelProperty(value = "List of Boot devices to be ordered in the sequence.", required = false, position=3)
	private List<String> biosBootSequenceOrder;
	
	@ApiModelProperty(value = "List of Hard-Disk Drive devices to be ordered in the sequence.", required = false, position=4)
	private List<String> hddSequenceOrder;
	
	@ApiModelProperty(value = "List of boot devices to enable", required = false, position=5)
	private List<String> enableBootDevices;
	
	@ApiModelProperty(value = "List of boot devices to disable", required = false, position=6)
	private List<String> disableBootDevices;

	/**
	 * @return the serverRequest
	 */
	public ServerRequest getServerRequest() {
		return serverRequest;
	}

	/**
	 * @param serverRequest the serverRequest to set
	 */
	public void setServerRequest(ServerRequest serverRequest) {
		this.serverRequest = serverRequest;
	}

	/**
	 * @return the attributes
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the biosBootSequenceOrder
	 */
	public List<String> getBiosBootSequenceOrder() {
		return biosBootSequenceOrder;
	}

	/**
	 * @param biosBootSequenceOrder the biosBootSequenceOrder to set
	 */
	public void setBiosBootSequenceOrder(List<String> biosBootSequenceOrder) {
		this.biosBootSequenceOrder = biosBootSequenceOrder;
	}

	/**
	 * @return the hddSequenceOrder
	 */
	public List<String> getHddSequenceOrder() {
		return hddSequenceOrder;
	}

	/**
	 * @param hddSequenceOrder the hddSequenceOrder to set
	 */
	public void setHddSequenceOrder(List<String> hddSequenceOrder) {
		this.hddSequenceOrder = hddSequenceOrder;
	}

	/**
	 * @return the enableBootDevices
	 */
	public List<String> getEnableBootDevices() {
		return enableBootDevices;
	}

	/**
	 * @param enableBootDevices the enableBootDevices to set
	 */
	public void setEnableBootDevices(List<String> enableBootDevices) {
		this.enableBootDevices = enableBootDevices;
	}

	/**
	 * @return the disableBootDevices
	 */
	public List<String> getDisableBootDevices() {
		return disableBootDevices;
	}

	/**
	 * @param disableBootDevices the disableBootDevices to set
	 */
	public void setDisableBootDevices(List<String> disableBootDevices) {
		this.disableBootDevices = disableBootDevices;
	}

}
