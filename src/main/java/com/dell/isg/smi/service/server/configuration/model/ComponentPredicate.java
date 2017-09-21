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
