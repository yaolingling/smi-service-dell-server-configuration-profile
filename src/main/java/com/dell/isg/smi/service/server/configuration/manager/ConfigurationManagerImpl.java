/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.manager;

import com.dell.isg.smi.adapter.server.config.ConfigEnum.EXPORT_MODE;
import com.dell.isg.smi.adapter.server.config.ConfigEnum.SHARE_TYPES;
import com.dell.isg.smi.adapter.server.config.IConfigAdapter;
import com.dell.isg.smi.adapter.server.inventory.IInventoryAdapter;
import com.dell.isg.smi.adapter.server.model.NetworkShare;
import com.dell.isg.smi.adapter.server.model.WsmanCredentials;
import com.dell.isg.smi.commons.model.common.Credential;
import com.dell.isg.smi.service.server.configuration.NfsYAMLConfiguration;
import com.dell.isg.smi.service.server.configuration.model.Attribute;
import com.dell.isg.smi.service.server.configuration.model.BiosSetupRequest;
import com.dell.isg.smi.service.server.configuration.model.ComponentList;
import com.dell.isg.smi.service.server.configuration.model.ComponentPredicate;
import com.dell.isg.smi.service.server.configuration.model.ConfigureBiosResult;
import com.dell.isg.smi.service.server.configuration.model.DCIM_BIOSEnumeration;
import com.dell.isg.smi.service.server.configuration.model.DCIM_BootSourceSetting;
import com.dell.isg.smi.service.server.configuration.model.NestedComponent;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareImageRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerComponent;
import com.dell.isg.smi.service.server.configuration.model.ServerRequest;
import com.dell.isg.smi.service.server.configuration.model.SubComponent;
import com.dell.isg.smi.service.server.configuration.model.SystemBiosSettings;
import com.dell.isg.smi.service.server.configuration.model.SystemConfiguration;
import com.dell.isg.smi.service.server.configuration.model.SystemEraseRequest;
import com.dell.isg.smi.service.server.configuration.utilities.ConfigurationUtils;
import com.dell.isg.smi.wsman.model.XmlConfig;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Muqeeth_Kowkab
 *
 */

@Component
public class ConfigurationManagerImpl implements IConfigurationManager {

	private static final String MESSAGE = "Message";

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class.getName());

	private static final int TIME_DELAY_MILLISECONDS = 30000;

	private static final int POLL_JOB_RETRY = 12;

	private static final String BIOS_SETUP_1_1 = "BIOS.Setup.1-1";

	private static final String COMPLETED_WITH_NO_ERROR = "Completed with no error";

	private static final String THE_COMMAND_WAS_SUCCESSFUL = "The command was successful";

	private static final String BOOTSOURCE_BCV = "BCV";

	private static final String BOOTSOURCE_IPL = "IPL";

	private static final String JOB_ID = "JobId";

	@Autowired
	NfsYAMLConfiguration yamlConfig;

	@Autowired
	IConfigAdapter configAdapter;

	@Autowired
	IInventoryAdapter inventoryAdapter;

	@Autowired
	Configuration jsonPathConfiguration;
	
	@Autowired
	ComponentPredicate componentPredicate;
	
	@Autowired
    ResourceBundleMessageSource messageSource;



	public ConfigurationManagerImpl() {
	}

	@Override
	public XmlConfig importConfiguration(ServerAndNetworkShareRequest request) throws Exception {

		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());
		
		return configAdapter.applyServerConfig(wsmanCredentials, getNetworkShare(request), request.getShutdownType());
	}

	@Override
	public Object previewConfiguration(ServerAndNetworkShareRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		XmlConfig config = configAdapter.previewImportServerConfig(wsmanCredentials, getNetworkShare(request));
		Object result = configAdapter.previewConfigResults(wsmanCredentials, config.getJobID());

		return result;
	}

	@Override
	public XmlConfig exportConfiguration(ServerAndNetworkShareRequest request, String exportMode) throws Exception {

		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		String components = StringUtils.join(request.getComponentNames(), ",");

		return configAdapter.exportServerConfig(wsmanCredentials, getNetworkShare(request), components, exportMode);
	}

	@Override
	public XmlConfig factoryConfiguration(ServerAndNetworkShareRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		XmlConfig config = configAdapter.exportFactorySetting(wsmanCredentials, getNetworkShare(request));
		return config;
	}

	@Override
	public XmlConfig exportInventory(ServerAndNetworkShareRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		XmlConfig config = configAdapter.exportHardwareInventory(wsmanCredentials, getNetworkShare(request));
		return config;
	}

	@Override
	public List<ServerComponent> getComponents(ServerAndNetworkShareRequest request) throws Exception {
		List<ServerComponent> components = extractComponents(request);
		return components;
	}

	@Override
	public XmlConfig wipeLifeController(Credential request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getAddress(), request.getUserName(),
				request.getPassword());
		XmlConfig config = configAdapter.wipeLifeController(wsmanCredentials);
		return config;
	}

	@Override
	public XmlConfig systemEraseServer(SystemEraseRequest request) throws Exception {
		Credential credential = request.getCredential();
		WsmanCredentials wsmanCredentials = new WsmanCredentials(credential.getAddress(), credential.getUserName(),
				credential.getPassword());
		XmlConfig config = configAdapter.performSystemErase(wsmanCredentials, request.getComponentNames());
		return config;
	}

	@Override
	public String testShareAccessablity(ServerAndNetworkShareRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		String result = configAdapter.verifyServerNetworkShareConnectivity(wsmanCredentials, getNetworkShare(request));
		return result;
	}

	@Override
	public XmlConfig backupServerImage(ServerAndNetworkShareImageRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		NetworkShare networkShare = new NetworkShare();
		networkShare.setShareType(getShareTypeEnum(request.getShareType()));
		networkShare.setShareName(request.getShareName());
		networkShare.setShareAddress(request.getShareAddress());
		networkShare.setFileName(request.getFileName());
		networkShare.setShareUserName(request.getShareUsername());
		networkShare.setSharePassword(request.getSharePassword());

		XmlConfig config = configAdapter.backupServerImage(wsmanCredentials, networkShare, request.getPassPhrase(),
				request.getImageName(), request.getWorkgroup(), request.getScheduleStartTime(), request.getUntilTime());

		return config;
	}

	@Override
	public XmlConfig restoreServerImage(ServerAndNetworkShareImageRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		NetworkShare networkShare = new NetworkShare();
		networkShare.setShareType(getShareTypeEnum(request.getShareType()));
		networkShare.setShareName(request.getShareName());
		networkShare.setShareAddress(request.getShareAddress());
		networkShare.setFileName(request.getFileName());
		networkShare.setShareUserName(request.getShareUsername());
		networkShare.setSharePassword(request.getSharePassword());

		XmlConfig config = configAdapter.restoreServerImage(wsmanCredentials, networkShare, request.getPassPhrase(),
				request.getImageName(), request.getWorkgroup(), request.getScheduleStartTime(), request.getUntilTime(), request.getPreserveVDConfig());
		return config;
	}

	private SHARE_TYPES getShareTypeEnum(int type) {
		switch (type) {
		case 0:
			return SHARE_TYPES.NFS;
		case 2:
			return SHARE_TYPES.CIFS;
		}
		return null;
	}

	/**
	 * This Method extracts the List of ServerComponents for a given Server and
	 * Network share request
	 * 
	 * @param request
	 * @return List of ServerComponents
	 * @throws Exception
	 */
	private List<ServerComponent> extractComponents(ServerAndNetworkShareRequest request) throws Exception {
		List<ServerComponent> serverComponents = null;
		try {
			SystemConfiguration systemConfig = ConfigurationUtils.getSystemConfiguration(request.getFilePathName());
			serverComponents = getServerComponents(systemConfig, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return serverComponents;
	}

	/**
	 * This method gives the list of ServerComponents for a given
	 * SystemConfiguration and ServerAndNetworkSharerequest
	 * 
	 * @param systemConfig-
	 *            The SystemConfiguration object of XML Configuration file
	 * @param request-
	 *            The ServerAndNetworkShareRequest
	 * @return List of ServerComponents in a SystemConfiguration
	 * @throws Exception
	 */
	private List<ServerComponent> getServerComponents(SystemConfiguration systemConfig,
			ServerAndNetworkShareRequest request) throws Exception {
		List<ServerComponent> extractedComponents = new LinkedList<ServerComponent>();

		try {
			List<ServerComponent> serverComponents = systemConfig.getServerComponents();
			List<String> componentNames = request.getComponentNames();
			List<String> temp = new LinkedList<String>();

			if (CollectionUtils.isNotEmpty(componentNames)) {
				for (String reqCompName : componentNames) {
					if (StringUtils.isNotBlank(reqCompName)) {
						temp.add(reqCompName);
					}
				}

				if (CollectionUtils.isNotEmpty(temp)) {
					for (String componentName : temp) {
						for (ServerComponent serverComponent : serverComponents) {
							if (StringUtils.equals(componentName, serverComponent.getFQDD())) {
								extractedComponents.add(serverComponent);
							}
						}
					}
				} else {
					extractedComponents.addAll(serverComponents);
				}

			} else {
				extractedComponents.addAll(serverComponents);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return extractedComponents;
	}

	@Override
	public List<ServerComponent> updateComponents(ComponentList request, boolean forceUpdate) throws Exception {
		return updateConfigurationFile(request, forceUpdate);
	}

	/**
	 * This Method updates the XML Configuration File for a given Component
	 * Lists to be updated
	 * 
	 * @param request-
	 *            The ComponentList request
	 * @return the list of updated ServerComponents
	 * @throws Exception
	 */
	private List<ServerComponent> updateConfigurationFile(ComponentList request, boolean forceUpdate) throws Exception {
		List<ServerComponent> updatedServerComponents = null;
		try {
			if (null != request)
			{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				String filePathName = request.getServerAndNetworkShareRequest().getFilePathName();
				logger.info("updateConfigurationFile: File Name from updated request:: " + filePathName);
				File xml = new File(filePathName);
				Document document = db.parse(xml);
				document = updateConfigurationXMLXPath(document, request.getServerComponents(), forceUpdate);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(xml);
				transformer.transform(source, result);
				logger.info("updateConfigurationFile: updated writing to: " + filePathName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return updatedServerComponents;

	}

	/*
	 * (non-Javadoc) initializes the SCP Micro Service
	 */
	@Override
	public Boolean initializeSCPService(ServerAndNetworkShareRequest request) throws Exception {
		updateRequestDetails(request);
		Boolean mounted = ConfigurationUtils.mount(request, yamlConfig);
		if (mounted && !ConfigurationUtils.fileExist(request.getFilePathName())) {
			logger.info("initializeSCPService: File doesn't exist. Exporting the config file from ther server");
			String mode = EXPORT_MODE.NORMAL.getValue();
			exportConfiguration(request, mode);
		} else {
			logger.info("initializeSCPService: File already exist. Skipping export of file to "
					+ request.getFilePathName());
		}
		return mounted;
	}

	/**
	 * This Method updates the network share filepathName, In case of blank
	 * ShareAddress file name will be unique otherwise uses the requested
	 * filePathName
	 * 
	 * @param serverAndNetworkShareRequest
	 * @throws Exception
	 */
	private void updateRequestDetails(ServerAndNetworkShareRequest serverAndNetworkShareRequest) throws Exception {
		try {
			String filePathName = "";
			if (null != serverAndNetworkShareRequest) {
				String fileName = serverAndNetworkShareRequest.getFileName();
				if (StringUtils.isBlank(fileName)) {
					logger.info(
							"updateRequestDetails: No FileName in Request. Generating Random FileName and updating the request");
					fileName = ConfigurationUtils.getUniqueFileName();
					serverAndNetworkShareRequest.setFileName(fileName);
					serverAndNetworkShareRequest.setRandomFile(true);
				}
				String randomShareName = ConfigurationUtils.getUniqueFileName();
				serverAndNetworkShareRequest.setSharePath(randomShareName);
				filePathName = "/shares/remote/" + randomShareName + "/" + fileName;
				serverAndNetworkShareRequest.setFilePathName(filePathName);
				logger.info("updateRequestDetails: FilePathName: " + filePathName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Gets the network share.
	 *
	 * @param request the ServerAndNetworkShareRequest
	 * @return the network share
	 */
	private NetworkShare getNetworkShare(ServerAndNetworkShareRequest request) {
		NetworkShare networkShare = new NetworkShare();
		networkShare.setShareType(getShareTypeEnum(request.getShareType()));
		networkShare.setShareName(request.getShareName());
		networkShare.setShareAddress(request.getShareAddress());
		networkShare.setFileName(request.getFileName());
		networkShare.setShareUserName(request.getShareUsername());
		networkShare.setSharePassword(request.getSharePassword());
		return networkShare;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SystemBiosSettings getBiosSettings(ServerRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());
		Map<String, Object> result = (Map<String, Object>) inventoryAdapter.collectBios(wsmanCredentials);
		SystemBiosSettings biosSettings = extractDCIMBIOS(result);
		List<DCIM_BootSourceSetting> bootSourceSettings = getBootSourceSettings(wsmanCredentials);
		biosSettings.setBootSourceSettings(bootSourceSettings);
		
		return biosSettings;
	}

	@SuppressWarnings("unchecked")
	private List<DCIM_BootSourceSetting> getBootSourceSettings(WsmanCredentials wsmanCredentials) throws Exception {
		List<DCIM_BootSourceSetting> dcimBootSourceSettings = null;
		try {			
			Map<String, Object> result = (Map<String, Object>) inventoryAdapter.collectBoot(wsmanCredentials);
			
			if (null != result) {
				TypeRef<List<DCIM_BootSourceSetting>> typeRef = new TypeRef<List<DCIM_BootSourceSetting>>() {};
				dcimBootSourceSettings = JsonPath.using(jsonPathConfiguration).parse(result.get("DCIM_BootSourceSetting")).read("$[*]", typeRef);
			}	
		} catch (Exception e) {
			throw e;
		}
		return dcimBootSourceSettings;
	}

	private SystemBiosSettings extractDCIMBIOS(Map<String, Object>  result) throws Exception {
		SystemBiosSettings systemBiosSetting = null;
		try {
			if (null != result) {
				TypeRef<List<DCIM_BIOSEnumeration>> typeRef = new TypeRef<List<DCIM_BIOSEnumeration>>() {};
				List<DCIM_BIOSEnumeration> dcimBIOSEnumerations = JsonPath.using(jsonPathConfiguration).parse(result.get("DCIM_BIOSEnumeration")).read("$[*]", typeRef);
				
				systemBiosSetting = getSystemBios(dcimBIOSEnumerations);
			
			}
		} catch (Exception e) {
			throw e;
		}
		return systemBiosSetting;
	}

	private SystemBiosSettings getSystemBios(List<DCIM_BIOSEnumeration> dcimBIOSEnumerations) throws Exception {
		SystemBiosSettings systemBiosSetting = new SystemBiosSettings();
		try {
			if (CollectionUtils.isNotEmpty(dcimBIOSEnumerations)) {
				List<Attribute> systemAttributes = new LinkedList<Attribute>();
				Map<String, ArrayList<String>> attributePossibleValuesMap = new HashMap<String, ArrayList<String>>();
				
				
				for (DCIM_BIOSEnumeration dcimBios: dcimBIOSEnumerations) {
					String attributeName = dcimBios.getAttributeName();
					String attributeValue = dcimBios.getCurrentValue();
					ArrayList<String> possibleValues = dcimBios.getPossibleValues();
					
					Attribute attribute = new Attribute();				
					attribute.setName(attributeName);
					attribute.setValue(attributeValue);
					systemAttributes.add(attribute);			
					
					attributePossibleValuesMap.put(attributeName, possibleValues);				
				}
				
				if (CollectionUtils.isNotEmpty(systemAttributes) && null != attributePossibleValuesMap) {
					systemBiosSetting.setAttributes(systemAttributes);
					systemBiosSetting.setPossibleValuesForAttributes(attributePossibleValuesMap);
				}				
			}			
		} catch (Exception e) {
			throw e;
		}
		return systemBiosSetting;
	}

	@Override
	public ConfigureBiosResult configureBios(BiosSetupRequest request) throws Exception {
		ConfigureBiosResult biosResult = new ConfigureBiosResult();
		try {
			
			if (null != request && null != request.getServerRequest()) {
				ServerRequest serverRequest = request.getServerRequest();
				WsmanCredentials wsmanCredentials = new WsmanCredentials(serverRequest.getServerIP(),
						serverRequest.getServerUsername(), serverRequest.getServerPassword());

				SystemBiosSettings serverBiosSettings = getBiosSettings(request.getServerRequest());
				
				Map<String, String> filteredAttributesToUpdate = getFilteredAttributesToUpdate(request.getAttributes(), serverBiosSettings);
				
				String biosAtrributesUpdateResult = updateAttributes(wsmanCredentials, filteredAttributesToUpdate);
				biosResult.setBiosUpdateAttributesResult(biosAtrributesUpdateResult);
				biosResult.setUpdatedAttributes(filteredAttributesToUpdate);
				
				List<DCIM_BootSourceSetting> serverBootSourceSettings = serverBiosSettings.getBootSourceSettings();	
				
				List<String> filteredInstanceIdsForBootSeq = getInstanceIds(request.getBiosBootSequenceOrder(), serverBootSourceSettings);				
				List<String> filteredInstanceIdsForHddSeq = getInstanceIds(request.getHddSequenceOrder(), serverBootSourceSettings);			
				String changeBootOrderSeqResult = changeBootOrderSequence(wsmanCredentials, filteredInstanceIdsForBootSeq, BOOTSOURCE_IPL);
				biosResult.setChangeBootOrderSequenceMessage(changeBootOrderSeqResult);				
				String changeHddSeqResult = changeBootOrderSequence(wsmanCredentials, filteredInstanceIdsForHddSeq, BOOTSOURCE_BCV);
				biosResult.setChangeHddSequenceMessage(changeHddSeqResult);
				
				List<String> devicesToEnable = request.getEnableBootDevices();				
				CollectionUtils.filter(devicesToEnable, componentPredicate.filterEnableDisableDevices(serverBootSourceSettings, true));
				List<String> filteredInstanceIdsForBootEnable = getInstanceIds(devicesToEnable, serverBootSourceSettings);
				
				List<String> devicesToDisable = request.getDisableBootDevices();
				CollectionUtils.filter(devicesToDisable, componentPredicate.filterEnableDisableDevices(serverBootSourceSettings, false));
				List<String> filteredInstanceIdsForBootDisable = getInstanceIds(devicesToDisable, serverBootSourceSettings);
				
				String bootSourceEnableResult = changeBootSourceState(wsmanCredentials, filteredInstanceIdsForBootEnable, true, BOOTSOURCE_IPL);
				biosResult.setEnableBootDevicesResult(bootSourceEnableResult);
				String bootSourceDisableResult = changeBootSourceState(wsmanCredentials, filteredInstanceIdsForBootDisable, false, BOOTSOURCE_IPL);
				biosResult.setDisableBootDevicesResult(bootSourceDisableResult);
			
				
				
				if (StringUtils.equals(biosAtrributesUpdateResult, THE_COMMAND_WAS_SUCCESSFUL)
						|| StringUtils.equals(changeBootOrderSeqResult, COMPLETED_WITH_NO_ERROR)
						|| StringUtils.equals(changeHddSeqResult, COMPLETED_WITH_NO_ERROR)
						|| StringUtils.equals(bootSourceEnableResult, COMPLETED_WITH_NO_ERROR)
						|| StringUtils.equals(bootSourceDisableResult, COMPLETED_WITH_NO_ERROR)) {
					Map<String, String> cmdResult = configAdapter.createTargetConfigJob(wsmanCredentials, BIOS_SETUP_1_1,
							request.getRebootJobType(), request.getScheduledStartTime(), request.getUntilTime());

					if (null != cmdResult && !cmdResult.isEmpty()) {

						if (cmdResult.containsKey(JOB_ID) && StringUtils.isNotBlank(cmdResult.get(JOB_ID))) {
							String jobId = cmdResult.get(JOB_ID);
							logger.info("configureBios: result: JobId: " + jobId);
							biosResult.setJobId(jobId);
							if (StringUtils.equals(request.getScheduledStartTime(), "TIME_NOW")) {
								XmlConfig xmlConfig = (XmlConfig) configAdapter.pollJobStatus(wsmanCredentials, jobId, TIME_DELAY_MILLISECONDS, POLL_JOB_RETRY);
								biosResult.setXmlConfig(xmlConfig);
							} else {
								XmlConfig xmlConfig = new XmlConfig();
								xmlConfig.setJobID(jobId);
								xmlConfig.setResult(XmlConfig.JobStatus.SUCCESS.toString());
								xmlConfig.setMessage(messageSource.getMessage("Request.ConfigureBios.Success.WithScheduledTime", null, Locale.getDefault()));
								biosResult.setXmlConfig(xmlConfig);
							}
						} else if (cmdResult.containsKey(MESSAGE) && StringUtils.isNotBlank(cmdResult.get(MESSAGE))) {
							logger.info("No JobID retrieved from CreateTargedConfigJob");
							String messageFromConfigJob = cmdResult.get(MESSAGE);
							biosResult = new ConfigureBiosResult();
							biosResult.setConfigBiosMessage(messageFromConfigJob);
							XmlConfig xmlConfig = new XmlConfig();
							xmlConfig.setResult(XmlConfig.JobStatus.FAILURE.toString());
							xmlConfig.setMessage(messageFromConfigJob);
						}
					}
				}
			}		
		} catch (Exception e) {
			throw e;
		}
		return biosResult;
		
	}

	/**
	 * Gets the attributes to update.
	 *
	 * @param attributes the attributes
	 * @param serverBiosSettings the configured ServerBiosSettings
	 * @return the attributes to update
	 * @throws Exception the exception
	 */
	private Map<String, String> getFilteredAttributesToUpdate(List<Attribute> attributes,
			SystemBiosSettings serverBiosSettings) throws Exception {
		Map<String, String> filteredAttributes = new HashMap<String, String>();
		try {
			CollectionUtils.select(attributes, componentPredicate.filterAttributes(serverBiosSettings, filteredAttributes));
		} catch (Exception e) {
			throw e;
		}
		return filteredAttributes;
		
	}

	/**
	 * Gets the instance ids.
	 *
	 * @param devices the devices
	 * @param serverBootSourceSettings the server boot source settings
	 * @return the instance ids
	 * @throws Exception the exception
	 */
	private List<String> getInstanceIds(List<String> devices,
			List<DCIM_BootSourceSetting> serverBootSourceSettings) throws Exception {
		List<String> filteredInstanceIdsForDevices = new LinkedList<String>();
		try {
			CollectionUtils.select(devices, componentPredicate.filterInstanceIds(serverBootSourceSettings, filteredInstanceIdsForDevices));						
		} catch (Exception e) {
			throw e;
		}
		return filteredInstanceIdsForDevices;		
	}
	
	/**
	 * Update attributes.
	 *
	 * @param wsmanCredentials the wsman credentials
	 * @param attributesToUpdate the attributes to update
	 * @return the string Result
	 * @throws Exception the exception
	 */
	private String updateAttributes(WsmanCredentials wsmanCredentials, Map<String, String> attributesToUpdate)
			throws Exception {
		String result = messageSource.getMessage("No.Bios.Attributes", null, Locale.getDefault());
		try {
			if (null != wsmanCredentials && null != attributesToUpdate && !attributesToUpdate.isEmpty()) {
				result = configAdapter.updateBiosAttributes(wsmanCredentials, attributesToUpdate, false);
				logger.info("UpdateAttribute Result: " + result);
			}

		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * Change boot source state.
	 *
	 * @param wsmanCredentials the wsman credentials
	 * @param instanceIdList the instance id list
	 * @param isEnabled the is enabled
	 * @param instanceType the instance type
	 * @return the string Result
	 * @throws Exception the exception
	 */
	private String changeBootSourceState(WsmanCredentials wsmanCredentials, List<String> instanceIdList, boolean isEnabled,
			String instanceType) throws Exception {
		String result = messageSource.getMessage("No.BootDevices.EnableDisable", null, Locale.getDefault());
		try {
			if (null != wsmanCredentials && CollectionUtils.isNotEmpty(instanceIdList)
					&& StringUtils.isNotBlank(instanceType)) {
				result = configAdapter.changeBootSourceState(wsmanCredentials, instanceIdList, isEnabled, instanceType);
				logger.info("ChangeBootSourceState Result: " + result);
				if (StringUtils.equals(result, "0")) {
					result = COMPLETED_WITH_NO_ERROR;
				} else {
					result = "Failed. Check the logs for the detailed failure.";
				}
			}
		} catch (Exception e) {
			throw e;
		}		
		return result;
	}

	/**
	 * Change boot order sequence.
	 *
	 * @param wsmanCredentials the wsman credentials
	 * @param instanceIdList the instance id list
	 * @param instanceType the BootSourceType type
	 * @return the string Result
	 * @throws Exception the exception
	 */
	private String changeBootOrderSequence(WsmanCredentials wsmanCredentials, List<String> instanceIdList, String instanceType)
			throws Exception {
		String result = messageSource.getMessage("No.Devices.ChangeSequenceOrder", null, Locale.getDefault());
		try {
			if (null != wsmanCredentials && CollectionUtils.isNotEmpty(instanceIdList)
					&& StringUtils.isNotBlank(instanceType)) {
				result = configAdapter.changeBootOrder(wsmanCredentials, instanceType, instanceIdList);
				logger.info("changeBootOrderSequence Result: " + result);
				if (StringUtils.equals(result, "0")) {
					result = COMPLETED_WITH_NO_ERROR;
				} else {
					result = "Failed. Check the logs for the detailed failure.";
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	private Document updateConfigurationXMLXPath(Document xmlDocument, final List<ServerComponent> serverComponents, boolean forceUpdate)
			throws XPathExpressionException
	{
		Document updatedDocument = xmlDocument.getOwnerDocument();
		final String xpathFindComponent = "//SystemConfiguration/Component[@FQDD='%s']";
		final String xpathFindComponentAttribute = "//SystemConfiguration/Component[@FQDD='%s']/Attribute[@Name='%s']";
		final String xpathFindComponentAttributeComment = "//SystemConfiguration/Component[@FQDD='%s']//comment()[contains(.,'Name=\"%s\"')]";
		final String xpathFindComponentSubComponentAttribute = "//SystemConfiguration/Component[@FQDD='%s']/Component[@FQDD='%s']/Attribute[@Name='%s']";
		final String xpathFindComponentSubComponentAttributeComment = "//SystemConfiguration/Component[@FQDD='%s']/Component[@FQDD='%s']//comment()[contains(.,'Name=\"%s\"')]";
        final String xpathFindComponentSubComponentNestedComponentAttribute = "//SystemConfiguration/Component[@FQDD='%s']/Component[@FQDD='%s']/Component[@FQDD='%s']/Attribute[@Name='%s']";
        final String xpathFindComponentSubComponentNestedComponentAttributeComment = "//SystemConfiguration/Component[@FQDD='%s']/Component[@FQDD='%s']/Component[@FQDD='%s']//comment()[contains(.,'Name=\"%s\"')]";

		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		if (CollectionUtils.isNotEmpty(serverComponents)) {
			for (ServerComponent component : serverComponents) {
				if (CollectionUtils.isNotEmpty(component.getAttributes())) {
						String attributeExpression = String.format(xpathFindComponentAttribute, component.getFQDD(), "%s");
						String commentedElementExpression = String.format(xpathFindComponentAttributeComment, component.getFQDD(), "%s");
						updateAttributesInDocument(xmlDocument, component.getAttributes(), xpath, attributeExpression, commentedElementExpression, forceUpdate);
				}
				if (CollectionUtils.isNotEmpty(component.getSubComponents())) {
					for (SubComponent subComponent : component.getSubComponents()) {
						if (CollectionUtils.isNotEmpty(subComponent.getAttributes()))
                        {
                            String attributeExpression = String.format(xpathFindComponentSubComponentAttribute, component.getFQDD(), subComponent.getFQDD(), "%s");
                            String commentedElementExpression = String.format(xpathFindComponentSubComponentAttributeComment, component.getFQDD(), subComponent.getFQDD(), "%s");
                            updateAttributesInDocument(xmlDocument, subComponent.getAttributes(), xpath, attributeExpression, commentedElementExpression, forceUpdate);
                        }
                        if (CollectionUtils.isNotEmpty(subComponent.getNestedComponents())) {
                            for (NestedComponent nestedComponent : subComponent.getNestedComponents()) {
                                String attributeExpression = String.format(xpathFindComponentSubComponentNestedComponentAttribute, component.getFQDD(), subComponent.getFQDD(), nestedComponent.getFQDD(), "%s");
                                String commentedElementExpression = String.format(xpathFindComponentSubComponentNestedComponentAttributeComment, component.getFQDD(), subComponent.getFQDD(), nestedComponent.getFQDD(), "%s");
                                updateAttributesInDocument(xmlDocument, nestedComponent.getAttributes(), xpath, attributeExpression, commentedElementExpression, forceUpdate);
                            }
                        }
					}
				}
			}
		}
		return xmlDocument;
	}

	private void updateAttributesInDocument(final Document xmlDocument,final List<Attribute> attributes, final XPath xpath, String attributeExpression, String attributeCommentExpression, boolean forceUpdate)
			throws XPathExpressionException
	{
		if (CollectionUtils.isNotEmpty(attributes)) {
			for (Attribute attribute : attributes) {
				final String expression = String.format(attributeExpression, attribute.getName());
				Node attributeElement = (Node) xpath.evaluate(expression, xmlDocument, XPathConstants.NODE);
				if (attributeElement != null) {
					attributeElement.setTextContent(attribute.getValue());
				} else if (forceUpdate) {
					final String commentExpression = String.format(attributeCommentExpression, attribute.getName());
					Node commentNode = (Node) xpath.evaluate(commentExpression, xmlDocument, XPathConstants.NODE);
					if (commentNode != null) {
						final Node parent = commentNode.getParentNode();
						Element newElement = xmlDocument.createElement("Attribute");
						newElement.setAttribute("Name", attribute.getName());
						newElement.setTextContent(attribute.getValue());
						parent.replaceChild(newElement, commentNode);
					}
				}
			}
		}
	}

}
