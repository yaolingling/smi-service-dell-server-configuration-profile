/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Muqeeth_Kowkab
 *
 */
@Component
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
                        CollectionUtils.filter(requestSubComponents, updateSubComponents(serverSubComponents));
                    }
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

    /**
     * filter the request server components.
     *
     * @param serverComponents the server components
     * @return the Predicate
     */
	public Predicate<ServerComponent> filterRequestServerComponents(List<ServerComponent> serverComponents) {		
		return new Predicate<ServerComponent>() {

			@Override
			public boolean evaluate(ServerComponent requestServerComponent) {
				if (null == requestServerComponent || null == serverComponents) {
					return false;
				}
				for (ServerComponent sc: serverComponents) {
					String requestFQDD = requestServerComponent.getFQDD();
					String serverFQDD = sc.getFQDD();
					if (StringUtils.equals(requestFQDD, serverFQDD)) {
						return true;
					}
				}				
				return false;
			}			
		};
	}


	public Predicate<String> filterInstanceIds(List<DCIM_BootSourceSetting> serverBootSourceSettings, List<String> filteredInstanceIds) {
		return new Predicate<String>() {

			@Override
			public boolean evaluate(String bootDevice) {
				if (StringUtils.isBlank(bootDevice) || null == serverBootSourceSettings) {
					return false;
				}
				for (DCIM_BootSourceSetting dcimBootSource: serverBootSourceSettings) {					
					String serverBootSourceType = dcimBootSource.getBootSourceType();
					if (StringUtils.equals(serverBootSourceType, "IPL") || StringUtils.equals(serverBootSourceType, "BCV")) {
						String serverInstanceId = dcimBootSource.getInstanceId();
						String[] parsedStrings = StringUtils.split(serverInstanceId, "#");
						
						if (StringUtils.equals(bootDevice, serverInstanceId)
								|| (StringUtils.equals(parsedStrings[2], bootDevice)
										&& (StringUtils.equals(parsedStrings[1], "BootSeq")
												|| (StringUtils.equals(parsedStrings[1], "HddSeq"))))) {
							logger.info("filterInstanceId: " + parsedStrings[2]);
							filteredInstanceIds.add(serverInstanceId);
							return true;
						}
					}					
				}
				return false;
			}			
		};
	}


	public Predicate<Attribute> filterAttributes(SystemBiosSettings serverBiosSettings,
			Map<String, String> filteredAttributes) {
		return new Predicate<Attribute>() {

			@Override
			public boolean evaluate(Attribute requestAttribute) {
				if (null == requestAttribute || null == serverBiosSettings) {
					return false;
				}
				Map<String, ArrayList<String>> possibleValuesMap = serverBiosSettings.getPossibleValuesForAttributes();
				ArrayList<String> serverPossibleValues = possibleValuesMap.get(requestAttribute.getName());
				List<Attribute> serverAttributes = serverBiosSettings.getAttributes();

				if (CollectionUtils.isNotEmpty(serverPossibleValues)
						&& serverPossibleValues.contains(requestAttribute.getValue()) && null != serverAttributes) {

					for (Attribute serverAttribute : serverAttributes) {
						String serverAttributeName = serverAttribute.getName();
						String serverAttributeValue = serverAttribute.getValue();

						if (StringUtils.equals(serverAttributeName, requestAttribute.getName())
								&& !StringUtils.equals(serverAttributeValue, requestAttribute.getValue())) {
							logger.info("filterAttributes:: Request AttributeName: " + requestAttribute.getName());
							filteredAttributes.put(requestAttribute.getName(), requestAttribute.getValue());
							return true;
						}
					}
				}
				return false;
			}

		};
	}


	public Predicate<String> filterEnableDisableDevices(List<DCIM_BootSourceSetting> serverBootSourceSettings,
			boolean isEnabled) {
		return new Predicate<String>() {

			@Override
			public boolean evaluate(String bootDevice) {
				if (StringUtils.isBlank(bootDevice) || null == serverBootSourceSettings) {
					return false;
				}
				for (DCIM_BootSourceSetting dcimBootSource : serverBootSourceSettings) {

					String serverBootSourceType = dcimBootSource.getBootSourceType();

					if (StringUtils.equals(serverBootSourceType, "IPL")) {
						String serverInstanceId = dcimBootSource.getInstanceId();
						String[] parsedStrings = StringUtils.split(serverInstanceId, "#");

						if (StringUtils.equals(bootDevice, serverInstanceId)
								|| (StringUtils.equals(parsedStrings[2], bootDevice))) {
							if (isEnabled && StringUtils.contains(dcimBootSource.getCurrentEnabledStatus(), "0")) {
								logger.info("filterEnableDisableDevices: " + parsedStrings[2] + " To Enable");
								return true;
							} else if (!isEnabled
									&& StringUtils.contains(dcimBootSource.getCurrentEnabledStatus(), "1")) {
								logger.info("filterEnableDisableDevices: " + parsedStrings[2] + " To Disable");
								return true;
							}
						}
					}
				}
				return false;
			}

		};
	}

}
