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

@ApiModel(value = "NestedComponent", description = "This has FQDD of the nested component and its respective list of Attribute")
@JsonInclude(Include.NON_DEFAULT)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "attributes" })
public class NestedComponent {

    @ApiModelProperty(value = "Fully Qualified Device Descriptor (FQDD).", dataType = "string", required = false)
    @XmlAttribute(name = "FQDD", required = true)
    protected String fqdd;

    @ApiModelProperty(value = "List of Attributes", required = false)
    @XmlElement(name = "Attribute", required = true)
    protected List<Attribute> attributes;


    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        return this.attributes;
    }


    /**
     * @param attributes the attribute to set
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

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof NestedComponent))
        {
            return false;
        }

        final NestedComponent that = (NestedComponent) o;

        if (!fqdd.equals(that.fqdd))
        {
            return false;
        }
        return getAttributes().equals(that.getAttributes());
    }

    @Override
    public int hashCode()
    {
        int result = fqdd.hashCode();
        result = 31 * result + getAttributes().hashCode();
        return result;
    }
}
