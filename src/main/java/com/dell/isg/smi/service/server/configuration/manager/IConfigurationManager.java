/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.manager;

import java.util.List;

import com.dell.isg.smi.commons.model.common.Credential;
import com.dell.isg.smi.service.server.configuration.model.ComponentList;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareImageRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerComponent;
import com.dell.isg.smi.service.server.configuration.model.SystemEraseRequest;
import com.dell.isg.smi.wsman.model.XmlConfig;

public interface IConfigurationManager {

	public XmlConfig importConfiguration(ServerAndNetworkShareRequest serverAndNetworkShareRequest) throws Exception;

	public XmlConfig exportConfiguration(ServerAndNetworkShareRequest serverAndNetworkShareRequest, String exportMode)
			throws Exception;

	public List<ServerComponent> getComponents(ServerAndNetworkShareRequest request) throws Exception;

	public List<ServerComponent> updateComponents(ComponentList request) throws Exception;

	public Boolean initializeSCPService(ServerAndNetworkShareRequest request) throws Exception;

	public Object previewConfiguration(ServerAndNetworkShareRequest request) throws Exception;

	public XmlConfig exportInventory(ServerAndNetworkShareRequest request) throws Exception;

	public XmlConfig factoryConfiguration(ServerAndNetworkShareRequest request) throws Exception;

	public XmlConfig backupServerImage(ServerAndNetworkShareImageRequest request) throws Exception;

	public XmlConfig restoreServerImage(ServerAndNetworkShareImageRequest request) throws Exception;

	public String testShareAccessablity(ServerAndNetworkShareRequest request) throws Exception;

	public XmlConfig wipeLifeController(Credential request) throws Exception;

	public XmlConfig systemEraseServer(SystemEraseRequest request) throws Exception;

}
