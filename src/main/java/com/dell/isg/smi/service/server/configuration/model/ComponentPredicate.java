/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Muqeeth_Kowkab
 *
 */
public class ComponentPredicate {
    private static final Logger logger = LoggerFactory.getLogger(ComponentPredicate.class.getName());


    /**
     * Update server components.
     *
     * @param serverComponents the server components
     * @return the Predicate
     */
    public Predicate<ServerComponent> updateServerComponents(List<ServerComponent> serverComponents) {
        return new Predicate<ServerComponent>() {

            @Override
            public boolean evaluate(ServerComponent requestServerComponent) {
                if (null == requestServerComponent || null == serverComponents) {
                    return false;
                }
                List<SubComponent> requestSubComponents = requestServerComponent.getSubComponents();
                for (ServerComponent serverComponent : serverComponents) {
                    String serverFQDD = serverComponent.getFQDD();
                    String requestFQDD = requestServerComponent.getFQDD();

                    List<SubComponent> serverSubComponents = serverComponent.getSubComponents();

                    if (StringUtils.equals(serverFQDD, requestFQDD)) {
                        CollectionUtils.filter(requestServerComponent.getAttributes(), updateAttributes(serverComponent.getAttributes()));
                    }
                    CollectionUtils.filter(requestSubComponents, updateSubComponents(serverSubComponents));
                }
                if (CollectionUtils.isNotEmpty(requestSubComponents) || CollectionUtils.isNotEmpty(requestServerComponent.getAttributes())) {
                    return true;
                } else {
                    return false;
                }
            }

        };
    }


    /**
     * Update attributes.
     *
     * @param serverAttributes the server attributes
     * @return the Predicate
     */
    public Predicate<Attribute> updateAttributes(List<Attribute> serverAttributes) {
        return new Predicate<Attribute>() {
            @Override
            public boolean evaluate(Attribute requestAttribute) {
                if (null == requestAttribute || null == serverAttributes) {
                    return false;
                }
                String reqAttributeName = requestAttribute.getName();
                String reqAttributeValue = requestAttribute.getValue();

                for (Attribute serverAttribute : serverAttributes) {
                    String serverAttributeName = serverAttribute.getName();
                    String serverAttributeValue = serverAttribute.getValue();
                    if (StringUtils.equals(serverAttributeName, reqAttributeName) && !StringUtils.equals(reqAttributeValue, serverAttributeValue)) {
                        serverAttributeValue = serverAttribute.getValue();
                        serverAttribute.setValue(reqAttributeValue);
                        logger.info("updateAttributes: Updated Attribute value for : " + serverAttributeName);
                        return true;
                    }
                }
                return false;
            }
        };
    }


    /**
     * This Method updates the server SubComponent's w.r.t Request SubComponent
     * 
     * @param serverSubComponents- ServerSubComponents to update
     * @return the Predicate
     */
    public Predicate<SubComponent> updateSubComponents(List<SubComponent> serverSubComponents) {
        return new Predicate<SubComponent>() {

            @Override
            public boolean evaluate(SubComponent requestSubComponent) {
                if (null == requestSubComponent || null == serverSubComponents) {
                    logger.info("No SubComponents to update.");
                    return false;
                }
                List<Attribute> requestAttributes = requestSubComponent.getAttributes();
                String requestFQDD = requestSubComponent.getFQDD();
                List<NestedComponent> requestNestedComponents = requestSubComponent.getNestedComponents();

                for (SubComponent serverSubComponent : serverSubComponents) {
                    String serverFQDD = serverSubComponent.getFQDD();

                    if (StringUtils.equals(serverFQDD, requestFQDD)) {
                        logger.info("updateSubComponents: " + serverFQDD);
                        CollectionUtils.filter(requestAttributes, updateAttributes(serverSubComponent.getAttributes()));
                        List<NestedComponent> serverNestedComponents = serverSubComponent.getNestedComponents();

                        CollectionUtils.filter(requestNestedComponents, updateNestedComponents(serverNestedComponents));

                    }

                }
                if (CollectionUtils.isNotEmpty(requestAttributes) || CollectionUtils.isNotEmpty(requestNestedComponents)) {
                    return true;
                } else {
                    return false;
                }
            }


            /**
             * Update nested components.
             *
             * @param serverNestedComponents the server nested components
             * @return the Predicate
             */
            private Predicate<NestedComponent> updateNestedComponents(List<NestedComponent> serverNestedComponents) {
                return new Predicate<NestedComponent>() {

                    @Override
                    public boolean evaluate(NestedComponent requestNestedComponent) {
                        if (null == requestNestedComponent || null == serverNestedComponents) {
                            return false;
                        }
                        for (NestedComponent serverNestedComponent : serverNestedComponents) {
                            String serverFQDD = serverNestedComponent.getFQDD();
                            String requestFQDD = requestNestedComponent.getFQDD();

                            if (StringUtils.equals(serverFQDD, requestFQDD)) {
                                logger.info("updateNestedComponents: " + serverFQDD);
                                CollectionUtils.filter(requestNestedComponent.getAttributes(), updateAttributes(serverNestedComponent.getAttributes()));
                            }
                        }
                        if (CollectionUtils.isNotEmpty(requestNestedComponent.getAttributes())) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                };
            }

        };
    }

}
