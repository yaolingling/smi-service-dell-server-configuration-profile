/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Muqeeth_Kowkab
 *
 */
@ApiModel(value = "ServerRequest", description = "Captures input details of PowerEdge server ")
public class ServerRequest {
	
	@ApiModelProperty(value = "Server IP address.", dataType = "string", required = true, position=1)
    private String serverIP;

    @ApiModelProperty(value = "Server username.", dataType = "string", required = true, position=2)
    private String serverUsername;

    @ApiModelProperty(value = "Server password.", dataType = "string", required = true, position=3)
    private String serverPassword;

	/**
	 * @return the serverIP
	 */
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * @param serverIP the serverIP to set
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * @return the serverUsername
	 */
	public String getServerUsername() {
		return serverUsername;
	}

	/**
	 * @param serverUsername the serverUsername to set
	 */
	public void setServerUsername(String serverUsername) {
		this.serverUsername = serverUsername;
	}

	/**
	 * @return the serverPassword
	 */
	public String getServerPassword() {
		return serverPassword;
	}

	/**
	 * @param serverPassword the serverPassword to set
	 */
	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}

}
