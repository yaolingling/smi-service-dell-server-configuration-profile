/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.service.server.configuration.model;

import java.util.List;
import com.dell.isg.smi.commons.model.common.Credential;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Muqeeth_Kowkab
 *
 */

public class SystemEarseRequest {
	
	@ApiModelProperty(value = "Credential", required = false)
	private Credential credential;
	
	@ApiModelProperty(value = "List of components to be earsed. BIOS_RESET_DEFULT, EMBEDDED_DIAGNOSTICS_ERASE, OS_DRIVERPACK_ERASE, IDRAC_DEFAULT and LC_DATA_ERASE")
    private String[] componentNames;
	
	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public String[] getComponentNames() {
		return componentNames;
	}

	public void setComponentNames(String[] componentNames) {
		this.componentNames = componentNames;
	}

}
