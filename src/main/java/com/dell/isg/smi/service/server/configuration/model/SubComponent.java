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

@ApiModel(value = "SubComponent", description = "This has the attributes of the SubComponent and the Nested Components")
@JsonInclude(Include.NON_DEFAULT)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "attributes", "nestedComponents" })
public class SubComponent {

    @ApiModelProperty(value = "Fully Qualified Device Descriptor (FQDD).", dataType = "string", required = false)
    @XmlAttribute(name = "FQDD", required = true)
    protected String fqdd;

    @ApiModelProperty(value = "List of Attributes", required = false)
    @XmlElement(name = "Attribute")
    protected List<Attribute> attributes;

    @ApiModelProperty(value = "List of Nested Component.", required = false)
    @XmlElement(name = "Component")
    protected List<NestedComponent> nestedComponents;


    // public List<NestedComponent> getComponent() {
    // if (component == null) {
    // component = new ArrayList<NestedComponent>();
    // }
    // return this.component;
    // }

    /**
     * @return the nestedComponents
     */
    public List<NestedComponent> getNestedComponents() {
        if (nestedComponents == null) {
            nestedComponents = new ArrayList<NestedComponent>();
        }
        return this.nestedComponents;
    }


    /**
     * @param nestedComponents the nestedComponents to set
     */
    public void setNestedComponents(List<NestedComponent> nestedComponents) {
        this.nestedComponents = nestedComponents;
    }


    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        return this.attributes;
    }


    /**
     * @param attribute the attribute to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
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
