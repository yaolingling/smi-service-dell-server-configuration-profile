/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "nfs_script_file")
public class NfsYAMLConfiguration {

    private String script_name;
    private String script_directory;


    /**
     * @return the script_name
     */
    public String getScript_name() {
        return script_name;
    }


    /**
     * @param script_name the script_name to set
     */
    public void setScript_name(String script_name) {
        this.script_name = script_name;
    }


    /**
     * @return the script_directory
     */
    public String getScript_directory() {
        return script_directory;
    }


    /**
     * @param script_directory the script_directory to set
     */
    public void setScript_directory(String script_directory) {
        this.script_directory = script_directory;
    }
}
