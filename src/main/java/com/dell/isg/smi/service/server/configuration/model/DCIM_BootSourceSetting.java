/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Muqeeth.Kowkab
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DCIM_BootSourceSetting {
	
	@JsonProperty("BIOSBootString")
	private String biosBootString;
	
	@JsonProperty("BootSourceType")
	private String bootSourceType;
	
	@JsonProperty("BootString")
	private String bootString;
	
	@JsonProperty("CurrentAssignedSequence")
	private String currentAssignedSequence;
	
	@JsonProperty("CurrentEnabledStatus")
	private String currentEnabledStatus;	
	
	@JsonProperty("ElementName")
	private String elementName;
	
	@JsonProperty("FailThroughSupported")
	private String failThroughSupported;
	
	@JsonProperty("InstanceID")
	private String instanceId;
	
	@JsonProperty("PendingAssignedSequence")
	private String pendingAssignedSequence;
	
	@JsonProperty("PendingEnabledStatus")
	private String pendingEnabledStatus;

	/**
	 * @return the biosBootString
	 */
	public String getBiosBootString() {
		return biosBootString;
	}

	/**
	 * @param biosBootString the biosBootString to set
	 */
	public void setBiosBootString(String biosBootString) {
		this.biosBootString = biosBootString;
	}

	/**
	 * @return the bootSourceType
	 */
	public String getBootSourceType() {
		return bootSourceType;
	}

	/**
	 * @param bootSourceType the bootSourceType to set
	 */
	public void setBootSourceType(String bootSourceType) {
		this.bootSourceType = bootSourceType;
	}

	/**
	 * @return the bootString
	 */
	public String getBootString() {
		return bootString;
	}

	/**
	 * @param bootString the bootString to set
	 */
	public void setBootString(String bootString) {
		this.bootString = bootString;
	}

	/**
	 * @return the currentAssignedSequence
	 */
	public String getCurrentAssignedSequence() {
		return currentAssignedSequence;
	}

	/**
	 * @param currentAssignedSequence the currentAssignedSequence to set
	 */
	public void setCurrentAssignedSequence(String currentAssignedSequence) {
		this.currentAssignedSequence = currentAssignedSequence;
	}

	/**
	 * @return the currentEnabledStatus
	 */
	public String getCurrentEnabledStatus() {
		return currentEnabledStatus;
	}

	/**
	 * @param currentEnabledStatus the currentEnabledStatus to set
	 */
	public void setCurrentEnabledStatus(String currentEnabledStatus) {
		this.currentEnabledStatus = currentEnabledStatus;
	}

	/**
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * @param elementName the elementName to set
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	/**
	 * @return the failThroughSupported
	 */
	public String getFailThroughSupported() {
		return failThroughSupported;
	}

	/**
	 * @param failThroughSupported the failThroughSupported to set
	 */
	public void setFailThroughSupported(String failThroughSupported) {
		this.failThroughSupported = failThroughSupported;
	}

	/**
	 * @return the instanceId
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * @return the pendingAssignedSequence
	 */
	public String getPendingAssignedSequence() {
		return pendingAssignedSequence;
	}

	/**
	 * @param pendingAssignedSequence the pendingAssignedSequence to set
	 */
	public void setPendingAssignedSequence(String pendingAssignedSequence) {
		this.pendingAssignedSequence = pendingAssignedSequence;
	}

	/**
	 * @return the pendingEnabledStatus
	 */
	public String getPendingEnabledStatus() {
		return pendingEnabledStatus;
	}

	/**
	 * @param pendingEnabledStatus the pendingEnabledStatus to set
	 */
	public void setPendingEnabledStatus(String pendingEnabledStatus) {
		this.pendingEnabledStatus = pendingEnabledStatus;
	}

}
