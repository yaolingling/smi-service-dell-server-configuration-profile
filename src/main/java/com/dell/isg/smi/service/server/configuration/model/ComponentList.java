/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.dell.isg.smi.service.server.configuration.model.ServerComponent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Muqeeth_Kowkab
 *
 */

@ApiModel(value = "ComponentList", description = "This has ServerAndNetworkShareRequest for Network and Server Details and List of ServerComponents")
public class ComponentList {

    @ApiModelProperty(value = "ServerAndNetworkShareRequest has the details of Network and DELL Servers", required = true)
    @NotNull(message = "serverAndNetworkShareRequest cannot be null")
    private ServerAndNetworkShareRequest serverAndNetworkShareRequest;

    @ApiModelProperty(value = "List of ServerComponents", required = true)
    private List<ServerComponent> serverComponents;

    @ApiModelProperty(value = "Force Update of Attributes in Configuration if found")
    private boolean forceUpdate;


    /**
     * @return the serverAndNetworkShareRequest
     */
    public ServerAndNetworkShareRequest getServerAndNetworkShareRequest() {
        return serverAndNetworkShareRequest;
    }


    /**
     * @param serverAndNetworkShareRequest the serverAndNetworkShareRequest to set
     */
    public void setServerAndNetworkShareRequest(ServerAndNetworkShareRequest serverAndNetworkShareRequest) {
        this.serverAndNetworkShareRequest = serverAndNetworkShareRequest;
    }


    /**
     * @return the serverComponents
     */
    public List<ServerComponent> getServerComponents() {
        return serverComponents;
    }


    /**
     * @param serverComponents the serverComponents to set
     */
    public void setServerComponents(List<ServerComponent> serverComponents) {
        this.serverComponents = serverComponents;
    }

    public boolean isForceUpdate()
    {
        return forceUpdate;
    }

    public void setForceUpdate(final boolean forceUpdate)
    {
        this.forceUpdate = forceUpdate;
    }
}
