/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.manager;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.dell.isg.smi.adapter.server.IServerAdapter;
import com.dell.isg.smi.adapter.server.config.ConfigEnum.EXPORT_MODE;
import com.dell.isg.smi.adapter.server.config.ConfigEnum.SHARE_TYPES;
import com.dell.isg.smi.adapter.server.model.NetworkShare;
import com.dell.isg.smi.adapter.server.model.WsmanCredentials;
import com.dell.isg.smi.service.server.configuration.NfsYAMLConfiguration;
import com.dell.isg.smi.service.server.configuration.model.ComponentList;
import com.dell.isg.smi.service.server.configuration.model.ComponentPredicate;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerComponent;
import com.dell.isg.smi.service.server.configuration.model.SystemConfiguration;
import com.dell.isg.smi.service.server.configuration.utilities.ConfigurationUtils;
import com.dell.isg.smi.wsman.model.XmlConfig;

/**
 * @author Muqeeth_Kowkab
 *
 */

@Component
public class ConfigurationManagerImpl implements IConfigurationManager {

	@Autowired
	NfsYAMLConfiguration yamlConfig;

	@Autowired
	IServerAdapter serverAdapterImpl;

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class.getName());

	public ConfigurationManagerImpl() {
	}

	@Override
	public XmlConfig importConfiguration(ServerAndNetworkShareRequest request) throws Exception {

		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		NetworkShare networkShare = new NetworkShare();
		networkShare.setShareType(SHARE_TYPES.valueOf(String.valueOf(request.getShareType())));
		networkShare.setShareName(request.getShareName());
		networkShare.setShareAddress(request.getShareAddress());
		networkShare.setFileName(request.getFileName());
		networkShare.setSharePassword(request.getShareUsername());
		networkShare.setSharePassword(request.getSharePassword());

		return serverAdapterImpl.applyServerConfig(wsmanCredentials, networkShare, request.getShutdownType());
	}
	
	@Override
	public Object previewConfiguration(ServerAndNetworkShareRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		NetworkShare networkShare = new NetworkShare();
		networkShare.setShareType(SHARE_TYPES.valueOf(String.valueOf(request.getShareType())));
		networkShare.setShareName(request.getShareName());
		networkShare.setShareAddress(request.getShareAddress());
		networkShare.setFileName(request.getFileName());
		networkShare.setSharePassword(request.getShareUsername());
		networkShare.setSharePassword(request.getSharePassword());
		
		XmlConfig config = serverAdapterImpl.previewImportServerConfig(wsmanCredentials, networkShare);
		Object result = serverAdapterImpl.previewConfigResults(wsmanCredentials, config.getJobID());
		
		return result;
	}

	@Override
	public XmlConfig exportConfiguration(ServerAndNetworkShareRequest request, String exportMode) throws Exception {

		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		NetworkShare networkShare = new NetworkShare();
		networkShare.setShareType(SHARE_TYPES.valueOf(String.valueOf(request.getShareType())));
		networkShare.setShareName(request.getShareName());
		networkShare.setShareAddress(request.getShareAddress());
		networkShare.setFileName(request.getFileName());
		networkShare.setSharePassword(request.getShareUsername());
		networkShare.setSharePassword(request.getSharePassword());

		String components = StringUtils.join(request.getComponentNames(), ",");

		return serverAdapterImpl.exportServerConfig(wsmanCredentials, networkShare, components, exportMode);
	}
	
	@Override
	public XmlConfig factoryConfiguration(ServerAndNetworkShareRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		NetworkShare networkShare = new NetworkShare();
		networkShare.setShareType(SHARE_TYPES.valueOf(String.valueOf(request.getShareType())));
		networkShare.setShareName(request.getShareName());
		networkShare.setShareAddress(request.getShareAddress());
		networkShare.setFileName(request.getFileName());
		networkShare.setSharePassword(request.getShareUsername());
		networkShare.setSharePassword(request.getSharePassword());
		
		XmlConfig config = serverAdapterImpl.exportFactorySetting(wsmanCredentials, networkShare);
		return config;
	}
	
	@Override
	public XmlConfig exportRegistry(ServerAndNetworkShareRequest request) throws Exception {
		WsmanCredentials wsmanCredentials = new WsmanCredentials(request.getServerIP(), request.getServerUsername(),
				request.getServerPassword());

		NetworkShare networkShare = new NetworkShare();
		networkShare.setShareType(SHARE_TYPES.valueOf(String.valueOf(request.getShareType())));
		networkShare.setShareName(request.getShareName());
		networkShare.setShareAddress(request.getShareAddress());
		networkShare.setFileName(request.getFileName());
		networkShare.setSharePassword(request.getShareUsername());
		networkShare.setSharePassword(request.getSharePassword());
		
		XmlConfig config = serverAdapterImpl.exportHardwareInventory(wsmanCredentials, networkShare);
		return config;
	}

	@Override
	public List<ServerComponent> getComponents(ServerAndNetworkShareRequest request) throws Exception {
		List<ServerComponent> components = extractComponents(request);
		return components;
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
	public List<ServerComponent> updateComponents(ComponentList request) throws Exception {
		return updateConfigurationFile(request);
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
	private List<ServerComponent> updateConfigurationFile(ComponentList request) throws Exception {
		List<ServerComponent> updatedServerComponents = null;
		try {
			if (null != request) {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				String filePathName = request.getServerAndNetworkShareRequest().getFilePathName();
				logger.info("updateConfigurationFile: File Name from updated request:: " + filePathName);
				File xml = new File(filePathName);
				Document document = db.parse(xml);
				JAXBContext jaxbContext = JAXBContextFactory.createContext(new Class[] { SystemConfiguration.class },
						null);

				Binder<Node> binder = jaxbContext.createBinder();
				binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				binder.setProperty(Marshaller.JAXB_FRAGMENT, true);

				Node xmlNode = document.getDocumentElement();

				SystemConfiguration systemConfig = (SystemConfiguration) binder.unmarshal(document);

				updatedServerComponents = updateXMLSystemConfiguration(systemConfig, request);

				if (CollectionUtils.isNotEmpty(updatedServerComponents)) {
					xmlNode = binder.updateXML(systemConfig);
					binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					document.setNodeValue(xmlNode.getNodeValue());

					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(document);
					StreamResult result = new StreamResult(xml);
					transformer.transform(source, result);
					logger.info("updateConfigurationFile: updated writing to: " + filePathName);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return updatedServerComponents;

	}

	/**
	 * This Method updates the XML SystemConfiguration for a given
	 * SystemConfiguration object and ComponentList request
	 * 
	 * @param systemConfig-
	 *            The SystemConfiguration object
	 * @param request-
	 *            The ComponentList
	 * @return the updated list of ServerComponents
	 * @throws Exception
	 */
	private List<ServerComponent> updateXMLSystemConfiguration(SystemConfiguration systemConfig, ComponentList request)
			throws Exception {
		List<ServerComponent> updatedServerComponents = new LinkedList<ServerComponent>();
		try {
			if (null != systemConfig && null != request) {
				List<ServerComponent> serverComponents = systemConfig.getServerComponents();

				List<ServerComponent> requestServerComponents = request.getServerComponents();
				ComponentPredicate predicate = new ComponentPredicate();
				CollectionUtils.filter(requestServerComponents, predicate.updateServerComponents(serverComponents));
				if (null != requestServerComponents) {
					updatedServerComponents.addAll(requestServerComponents);
				}
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

}
