/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Muqeeth.Kowkab
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class ConfigureBiosResult {
	
	@ApiModelProperty(value = "JobID", dataType = "string", required = false, position=1)
	private String configBiosMessage;
	
	@ApiModelProperty(value = "JobID", dataType = "string", required = false, position=2)
	private String jobId;
	
	@ApiModelProperty(value = "BIOS Update Attributes Result Message", dataType = "string", required = false, position=3)
	private String biosUpdateAttributesResult;
	
	@ApiModelProperty(value = "BIOS Updated Attributes", required = false, position=4)
	private Map<String, String> updatedAttributes;
	
	@ApiModelProperty(value = "BIOS BootOrder Sequence Change Message", dataType = "string", required = false, position=5)
	private String changeBootOrderSequenceMessage;	
	
	@ApiModelProperty(value = "BIOS HDD Sequence Change Result Message", dataType = "string", required = false, position=6)
	private String changeHddSequenceMessage;
	
	@ApiModelProperty(value = "BIOS Boot Devices Enable Result Message", dataType = "string", required = false, position=7)
	private String enableBootDevicesResult;
	
	@ApiModelProperty(value = "BIOS Boot Devices Disable Result Message", dataType = "string", required = false, position=8)
	private String disableBootDevicesResult;

	/**
	 * @return the configBiosMessage
	 */
	public String getConfigBiosMessage() {
		return configBiosMessage;
	}

	/**
	 * @param configBiosMessage the configBiosMessage to set
	 */
	public void setConfigBiosMessage(String configBiosMessage) {
		this.configBiosMessage = configBiosMessage;
	}

	/**
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * @return the biosUpdateAttributesResult
	 */
	public String getBiosUpdateAttributesResult() {
		return biosUpdateAttributesResult;
	}

	/**
	 * @param biosUpdateAttributesResult the biosUpdateAttributesResult to set
	 */
	public void setBiosUpdateAttributesResult(String biosUpdateAttributesResult) {
		this.biosUpdateAttributesResult = biosUpdateAttributesResult;
	}

	/**
	 * @return the updatedAttributes
	 */
	public Map<String, String> getUpdatedAttributes() {
		return updatedAttributes;
	}

	/**
	 * @param updatedAttributes the updatedAttributes to set
	 */
	public void setUpdatedAttributes(Map<String, String> updatedAttributes) {
		this.updatedAttributes = updatedAttributes;
	}

	/**
	 * @return the changeBootOrderSequenceMessage
	 */
	public String getChangeBootOrderSequenceMessage() {
		return changeBootOrderSequenceMessage;
	}

	/**
	 * @param changeBootOrderSequenceMessage the changeBootOrderSequenceMessage to set
	 */
	public void setChangeBootOrderSequenceMessage(String changeBootOrderSequenceMessage) {
		this.changeBootOrderSequenceMessage = changeBootOrderSequenceMessage;
	}

	/**
	 * @return the changeHddSequenceMessage
	 */
	public String getChangeHddSequenceMessage() {
		return changeHddSequenceMessage;
	}

	/**
	 * @param changeHddSequenceMessage the changeHddSequenceMessage to set
	 */
	public void setChangeHddSequenceMessage(String changeHddSequenceMessage) {
		this.changeHddSequenceMessage = changeHddSequenceMessage;
	}

	/**
	 * @return the enableBootDevicesResult
	 */
	public String getEnableBootDevicesResult() {
		return enableBootDevicesResult;
	}

	/**
	 * @param enableBootDevicesResult the enableBootDevicesResult to set
	 */
	public void setEnableBootDevicesResult(String enableBootDevicesResult) {
		this.enableBootDevicesResult = enableBootDevicesResult;
	}

	/**
	 * @return the disableBootDevicesResult
	 */
	public String getDisableBootDevicesResult() {
		return disableBootDevicesResult;
	}

	/**
	 * @param disableBootDevicesResult the disableBootDevicesResult to set
	 */
	public void setDisableBootDevicesResult(String disableBootDevicesResult) {
		this.disableBootDevicesResult = disableBootDevicesResult;
	}
	
	

}
