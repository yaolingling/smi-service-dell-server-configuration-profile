/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mkowkab
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DCIM_BIOSEnumeration {
	
	@JsonProperty("DisplayOrder")
	private String displayOrder;
	
	@JsonProperty("InstanceID")
	private String instanceId;
	
	@JsonProperty("PendingValue")
	private String pendingValue;
	
	@JsonProperty("PossibleValuesDescription")
	@JsonFormat(with=JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private ArrayList<String>  possibleValuesDescription;
	
	@JsonProperty("CurrentValue")
	private String currentValue;
	
	@JsonProperty("FQDD")
	private String fqdd;
	
	@JsonProperty("GroupDisplayName")
	private String groupDisplayName;
	
	@JsonProperty("PossibleValues")
	@JsonFormat(with=JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private ArrayList<String> possibleValues;
	
	@JsonProperty("Dependency")
	private String dependency;
	
	@JsonProperty("GroupID")
	private String groupId;
	
	@JsonProperty("AttributeDisplayName")
	private String attributeDisplayName;
	
	@JsonProperty("AttributeName")
	private String attributeName;
	
	@JsonProperty("IsReadOnly")
	private String isReadOnly;

	/**
	 * @return the displayOrder
	 */
	public String getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
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
	 * @return the pendingValue
	 */
	public String getPendingValue() {
		return pendingValue;
	}

	/**
	 * @param pendingValue the pendingValue to set
	 */
	public void setPendingValue(String pendingValue) {
		this.pendingValue = pendingValue;
	}

	/**
	 * @return the possibleValuesDescription
	 */
	public ArrayList<String> getPossibleValuesDescription() {
		return possibleValuesDescription;
	}

	/**
	 * @param possibleValuesDescription the possibleValuesDescription to set
	 */
	public void setPossibleValuesDescription(ArrayList<String> possibleValuesDescription) {
		this.possibleValuesDescription = possibleValuesDescription;
	}

	/**
	 * @return the currentValue
	 */
	public String getCurrentValue() {
		return currentValue;
	}

	/**
	 * @param currentValue the currentValue to set
	 */
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * @return the fqdd
	 */
	public String getFqdd() {
		return fqdd;
	}

	/**
	 * @param fqdd the fqdd to set
	 */
	public void setFqdd(String fqdd) {
		this.fqdd = fqdd;
	}

	/**
	 * @return the groupDisplayName
	 */
	public String getGroupDisplayName() {
		return groupDisplayName;
	}

	/**
	 * @param groupDisplayName the groupDisplayName to set
	 */
	public void setGroupDisplayName(String groupDisplayName) {
		this.groupDisplayName = groupDisplayName;
	}


	/**
	 * @return the possibleValues
	 */
	public ArrayList<String> getPossibleValues() {
		return possibleValues;
	}

	/**
	 * @param possibleValues the possibleValues to set
	 */
	public void setPossibleValues(ArrayList<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

	/**
	 * @return the dependency
	 */
	public String getDependency() {
		return dependency;
	}

	/**
	 * @param dependency the dependency to set
	 */
	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the attributeDisplayName
	 */
	public String getAttributeDisplayName() {
		return attributeDisplayName;
	}

	/**
	 * @param attributeDisplayName the attributeDisplayName to set
	 */
	public void setAttributeDisplayName(String attributeDisplayName) {
		this.attributeDisplayName = attributeDisplayName;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return the isReadOnly
	 */
	public String getIsReadOnly() {
		return isReadOnly;
	}

	/**
	 * @param isReadOnly the isReadOnly to set
	 */
	public void setIsReadOnly(String isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	

}
