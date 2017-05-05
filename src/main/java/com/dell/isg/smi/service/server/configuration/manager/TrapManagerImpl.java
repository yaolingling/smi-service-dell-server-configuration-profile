/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dell.isg.smi.adapter.server.IServerAdapter;
import com.dell.isg.smi.adapter.server.model.WsmanCredentials;

@Component
public class TrapManagerImpl implements ITrapManager {

    @Autowired
    IServerAdapter serverAdapterImpl;


    @Override
    public boolean configureTraps(WsmanCredentials wsmanCredentials, String trapDestination) throws Exception {
        return serverAdapterImpl.configureTraps(wsmanCredentials, trapDestination);
    }


    @Override
    public boolean updateTrapFormat(WsmanCredentials wsmanCredentials, String trapFormat) throws Exception {
        return serverAdapterImpl.updateTrapFormat(wsmanCredentials, trapFormat);
    }

}
