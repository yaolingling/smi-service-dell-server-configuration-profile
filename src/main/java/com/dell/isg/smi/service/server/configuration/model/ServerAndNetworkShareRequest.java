/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.List;

import io.swagger.annotations.ApiModel;

//import com.wordnik.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Muqeeth_Kowkab
 *
 */

@ApiModel(value = "ServerAndNetworkShareRequest", description = "Captures input request of server and network share details ")
public class ServerAndNetworkShareRequest {

    @ApiModelProperty(value = "Server IP address.", dataType = "string", required = true)
    private String serverIP;

    @ApiModelProperty(value = "Server username.", dataType = "string", required = true)
    private String serverUsername;

    @ApiModelProperty(value = "Server password.", dataType = "string", required = true)
    private String serverPassword;

    @ApiModelProperty(value = "Type of share: NFS=0, CIFS=2", required = true)
    private int shareType;

    @ApiModelProperty(value = "The shared directory name on the server being exported. Not the location.", dataType = "string")
    private String shareName;

    @ApiModelProperty(value = "The IP address of the target export server", dataType = "string")
    private String shareAddress;

    @ApiModelProperty(value = "The target output file name.", dataType = "string")
    private String fileName;

    @ApiModelProperty(value = "Username of share server in the scenario of share type CIFS", dataType = "string")
    private String shareUsername;

    @ApiModelProperty(value = "Password of share server in the scenario of share type CIFS.", dataType = "string")
    private String sharePassword;

    @ApiModelProperty(hidden = true)
    private String filePathName;

    @ApiModelProperty(value = "To identify the component for Import. It identifies the one or more FQDDs. Selective list of FQDDs should be given in comma separated format. Default = ALL")
    private List<String> componentNames;

    @ApiModelProperty(value = "Type of the host shut down before perform the import operation. Graceful=0, Forced =1, NoReboot=2. Only applies in importing the configuration to server", dataType = "uint16")
    private int shutdownType;

    @ApiModelProperty(hidden = true)
    private boolean randomFile;
    
    @ApiModelProperty(hidden = true)
    private String sharePath;


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


    /**
     * @return the shareType
     */
    public int getShareType() {
        return shareType;
    }


    /**
     * @param shareType the shareType to set
     */
    public void setShareType(int shareType) {
        this.shareType = shareType;
    }


    /**
     * @return the shareName
     */
    public String getShareName() {
        return shareName;
    }


    /**
     * @param shareName the shareName to set
     */
    public void setShareName(String shareName) {
        this.shareName = shareName;
    }


    /**
     * @return the shareAddress
     */
    public String getShareAddress() {
        return shareAddress;
    }


    /**
     * @param shareAddress the shareAddress to set
     */
    public void setShareAddress(String shareAddress) {
        this.shareAddress = shareAddress;
    }


    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }


    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /**
     * @return the shareUsername
     */
    public String getShareUsername() {
        return shareUsername;
    }


    /**
     * @param shareUsername the shareUsername to set
     */
    public void setShareUsername(String shareUsername) {
        this.shareUsername = shareUsername;
    }


    /**
     * @return the sharePassword
     */
    public String getSharePassword() {
        return sharePassword;
    }


    /**
     * @param sharePassword the sharePassword to set
     */
    public void setSharePassword(String sharePassword) {
        this.sharePassword = sharePassword;
    }


    /**
     * @return the filePathName
     */
    public String getFilePathName() {
        return filePathName;
    }


    /**
     * @param filePathName the filePathName to set
     */
    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }


    /**
     * @return the componentNames
     */
    public List<String> getComponentNames() {
        return componentNames;
    }


    /**
     * @param componentNames the componentNames to set
     */
    public void setComponentNames(List<String> componentNames) {
        this.componentNames = componentNames;
    }


    /**
     * @return the shutdownType
     */
    public int getShutdownType() {
        return shutdownType;
    }


    /**
     * @param shutdownType the shutdownType to set
     */
    public void setShutdownType(int shutdownType) {
        this.shutdownType = shutdownType;
    }


    /**
     * @return the randomFile
     */
    public boolean isRandomFile() {
        return randomFile;
    }


    /**
     * @param randomFile the randomFile to set
     */
    public void setRandomFile(boolean randomFile) {
        this.randomFile = randomFile;
    }


	/**
	 * @return the sharePath
	 */
	public String getSharePath() {
		return sharePath;
	}


	/**
	 * @param sharePath the sharePath to set
	 */
	public void setSharePath(String sharePath) {
		this.sharePath = sharePath;
	}
}
