/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Muqeeth_Kowkab
 *
 */
@ApiModel(value = "ServerComponent", description = "This has the Parent Fully Qualified Device Descriptor (FQDD), Attributes and SubComponents")
@JsonInclude(Include.NON_DEFAULT)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "attributes", "subComponents" })
public class ServerComponent {

    @ApiModelProperty(value = "Fully Qualified Device Descriptor (FQDD).", dataType = "string", required = true)
    @XmlAttribute(name = "FQDD", required = true)
    protected String fqdd;

    @ApiModelProperty(value = "List of Attributes", required = true)
    @XmlElement(name = "Attribute", required = true)
    protected List<Attribute> attributes;

    @ApiModelProperty(value = "List of SubComponent", required = false)
    @XmlElement(name = "Component")
    protected List<SubComponent> subComponents;


    /**
     * @param attribute the attributes to set
     */
    public void setAttributes(List<Attribute> attribute) {
        this.attributes = attribute;
    }


    /**
     * @return the Attributes
     */
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        return this.attributes;
    }


    /**
     * @return the innerComponents
     */
    public List<SubComponent> getSubComponents() {
        if (subComponents == null) {
            subComponents = new ArrayList<SubComponent>();
        }
        return this.subComponents;
    }


    /**
     * @param innerComponents the innerComponents to set
     */
    public void setInnerComponents(List<SubComponent> subComponents) {
        this.subComponents = subComponents;
    }


    /**
     * Gets the value of the fqdd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFQDD() {
        return fqdd;
    }


    /**
     * Sets the value of the fqdd property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setFQDD(String value) {
        this.fqdd = value;
    }

}
