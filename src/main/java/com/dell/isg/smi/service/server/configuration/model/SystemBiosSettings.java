/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Muqeeth.Kowkab
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class SystemBiosSettings {
	
	private List<Attribute> attributes;
	
	private Map<String, ArrayList<String>> possibleValuesForAttributes;
	
	private List<DCIM_BootSourceSetting> bootSourceSettings;

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
	 * @return the possibleValuesForAttributes
	 */
	public Map<String, ArrayList<String>> getPossibleValuesForAttributes() {
		return possibleValuesForAttributes;
	}

	/**
	 * @param possibleValuesForAttributes the possibleValuesForAttributes to set
	 */
	public void setPossibleValuesForAttributes(Map<String, ArrayList<String>> possibleValuesForAttributes) {
		this.possibleValuesForAttributes = possibleValuesForAttributes;
	}

	/**
	 * @return the bootSourceSettings
	 */
	public List<DCIM_BootSourceSetting> getBootSourceSettings() {
		return bootSourceSettings;
	}

	/**
	 * @param bootSourceSettings the bootSourceSettings to set
	 */
	public void setBootSourceSettings(List<DCIM_BootSourceSetting> bootSourceSettings) {
		this.bootSourceSettings = bootSourceSettings;
	}
	
		
}
