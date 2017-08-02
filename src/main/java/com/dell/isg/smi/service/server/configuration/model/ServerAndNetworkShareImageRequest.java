/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import io.swagger.annotations.ApiModel;

//import com.wordnik.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Muqeeth_Kowkab
 *
 */

@ApiModel(value = "ServerAndNetworkShareImageRequest", description = "Captures input request of server and network share details for image backup and restore. ")
public class ServerAndNetworkShareImageRequest {

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

    @ApiModelProperty(value = "The name of the backup file. The parameter shall be required, if the ShareType parameter has value 0 (NFS), 2 (CIFS), or not specified.", dataType = "string")
    private String imageName;
    
    @ApiModelProperty(value = "The passPrase to extarct the image . The parameter shall be required, if the ShareType parameter has value 0 (NFS), or 2 (CIFS) or not specified.", dataType = "string")
    private String passPhrase;

    @ApiModelProperty(value = "Workgroup for the share.", dataType = "string")
    private String workgroup;
    
    @ApiModelProperty(value = "Start time for the job execution in format: yyyymmddhhmmss.The string TIME_NOW means immediate.", dataType = "string")
    private String scheduleStartTime;
    
    @ApiModelProperty(value = "End time for the job execution in format: yyyymmddhhmmss. :If this parameter is not NULL, then ScheduledStartTime parameter shall also be specified.", dataType = "string")
    private String untilTime;
    
    @ApiModelProperty(value = "Whether to preserve the VD (Virtual Disk) configuration.", dataType = "string")
    private String preserveVDConfig;
    

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
        return imageName;
    }


    /**
     * @param filePathName the filePathName to set
     */
    public void setFilePathName(String filePathName) {
        this.imageName = filePathName;
    }


	public String getImageName() {
		return imageName;
	}


	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	public String getPassPhrase() {
		return passPhrase;
	}


	public void setPassPhrase(String passPhrase) {
		this.passPhrase = passPhrase;
	}


	public String getWorkgroup() {
		return workgroup;
	}


	public void setWorkgroup(String workgroup) {
		this.workgroup = workgroup;
	}


	public String getScheduleStartTime() {
		return scheduleStartTime;
	}


	public void setScheduleStartTime(String scheduleStartTime) {
		this.scheduleStartTime = scheduleStartTime;
	}


	public String getUntilTime() {
		return untilTime;
	}


	public void setUntilTime(String untilTime) {
		this.untilTime = untilTime;
	}


	public String getPreserveVDConfig() {
		return preserveVDConfig;
	}


	public void setPreserveVDConfig(String preserveVDConfig) {
		this.preserveVDConfig = preserveVDConfig;
	}

}
