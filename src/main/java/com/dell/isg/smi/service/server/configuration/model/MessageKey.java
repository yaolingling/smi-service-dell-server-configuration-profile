/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

/**
 * @author Muqeeth_Kowkab
 *
 */
public enum MessageKey {

    INVALID_REQUEST("Invalid.request"), REQUEST_SUBMITTED("Request.submitted"), REQUEST_PROCESS_FAILED("Request.process.failed"), FAILED_INIT("Failed.Initialization"), REQUEST_SUCCESS("Request.success");

    private String messageKey;


    MessageKey(String messageKey) {
        this.messageKey = messageKey;
    }


    public String getMessageKey() {
        return messageKey;
    }


    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }


    public String getKey() {
        return this.messageKey;
    }

}
