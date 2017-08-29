/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.manager;

import com.dell.isg.smi.adapter.server.model.WsmanCredentials;

public interface ITrapManager {

    public boolean configureTraps(WsmanCredentials wsmanCredentials, String string) throws Exception;


    public boolean updateTrapFormat(WsmanCredentials wsmanCredentials, String string) throws Exception;

}
