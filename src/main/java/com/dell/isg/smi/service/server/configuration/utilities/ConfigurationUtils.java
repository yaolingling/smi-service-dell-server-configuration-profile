/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.utilities;

import java.io.File;
import java.net.InetAddress;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dell.isg.smi.commons.model.credential.PasswordCredential;
import com.dell.isg.smi.commons.model.fileshare.FileShare;
import com.dell.isg.smi.commons.model.fileshare.FileShareTypeEnum;
import com.dell.isg.smi.commons.utilities.fileshare.FileShareService;
import com.dell.isg.smi.service.server.configuration.NfsYAMLConfiguration;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest;
import com.dell.isg.smi.service.server.configuration.model.SystemConfiguration;

/**
 * @author mkowkab
 *
 */
public class ConfigurationUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationUtils.class.getName());
    private static JAXBContext jc;


    /**
     * This Method unmarshal the XML file and returns the SystemConfiguration Object
     * 
     * @param filePathName- The filePathName of the System Config file
     * @return SystemConfiguration object
     * @throws Exception
     */
    public static SystemConfiguration getSystemConfiguration(String filePathName) throws Exception {
        SystemConfiguration systemConfig = null;
        try {
            jc = JAXBContext.newInstance(SystemConfiguration.class);
            Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            File file = new File(filePathName);
            systemConfig = (SystemConfiguration) jaxbUnmarshaller.unmarshal(file);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to getSystemConfiguration: " + e.getMessage());
            throw e;
        }
        return systemConfig;
    }


    /**
     * This Method mount's the directory in the Docker container
     * 
     * @param request
     * @param yamlConfig
     * @return
     * @throws Exception
     */
    public static Boolean mount(ServerAndNetworkShareRequest request, NfsYAMLConfiguration yamlConfig) throws Exception {
        Boolean result = true;
        try {
            if (null != request && null != yamlConfig && !fileExist("/shares/remote/" + request.getShareName())) {
                logger.info("Mount doesn't exist. Creating a mount for " + request.getFilePathName());
                FileShareService fileShareService = new FileShareService();
                FileShare fileShare = new FileShare();
                fileShare.setAddress(request.getShareAddress());
                fileShare.setName(request.getShareName()); // Share name will be used as mount location folder name
                fileShare.setPath(request.getShareName()); // this is the path of remote share
                int shareType = request.getShareType();
                
                FileShareTypeEnum type = null;
                if (shareType==0) {
                	type = FileShareTypeEnum.NFS;
                } else if (shareType==2) {
					type = FileShareTypeEnum.CIFS;
					PasswordCredential passwordCreds = new PasswordCredential();
	                passwordCreds.setUsername(request.getShareUsername());
	                passwordCreds.setPassword(request.getSharePassword());
	                fileShare.setPasswordCredential(passwordCreds);
				}        
                
                fileShare.setType(type);
                fileShare.setScriptName(yamlConfig.getScript_name());
                fileShare.setScriptDirectory(yamlConfig.getScript_directory());
                result = fileShareService.mount(fileShare);
                logger.info("ScriptFileName: " + yamlConfig.getScript_name());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to mountNFS: " + e.getMessage());
            result = false;
        }
        logger.info("ConfigurationUtils: mountNFS:" + result);
        return result;

    }


    /**
     * This method generates the unique filename
     * 
     * @return RandomAlphanumeric String
     */
    public static String getUniqueFileName() {
        String fileName = RandomStringUtils.randomAlphanumeric(10) + ".xml";
        return fileName;
    }


    /**
     * This Method validates the IP address
     * 
     * @param ipAddress the IP to be validated
     * @return true if reachable otherwise false
     */
    public static boolean validateIPAddress(String ipAddress) {
        try {
            if (StringUtils.isNotBlank(ipAddress)) {
                return InetAddress.getByName(ipAddress).isReachable(2000);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean fileExist(String filePathName) {
        File file = new File(filePathName);
        return file.exists();
    }


    public static void removeRandomFile(ServerAndNetworkShareRequest request, NfsYAMLConfiguration yamlConfig) {
        try {
            if (null != request && request.isRandomFile()) {
                String filePathName = request.getFilePathName();
                if (StringUtils.isNotBlank(filePathName) && fileExist(filePathName)) {
                    File file = new File(filePathName);
                    file.delete();
                    logger.info("Removed the random file " + filePathName);
                }
            }
            if (null != request && null != yamlConfig) {            	
            	String shareName = request.getShareName();
            	if (StringUtils.isNotBlank(shareName)) {
            		FileShareService fileShareService = new FileShareService();
            		FileShare fileShare = new FileShare();
            		fileShare.setName(shareName);
            		String scriptName = yamlConfig.getScript_name();
            		String scriptDirectory = yamlConfig.getScript_directory();
            		fileShare.setScriptName(scriptName);
            		fileShare.setScriptDirectory(scriptDirectory);
            		Boolean unmounted = fileShareService.unmount(fileShare);
            		logger.info("unmounted " + shareName + " status: " + unmounted);            		
            	}
            }

        } catch (Exception e) {
            logger.error("Could not remove the random file/unmount failed.");
        }

    }
}
