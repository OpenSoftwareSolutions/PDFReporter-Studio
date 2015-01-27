/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.ireport.jasperserver.ws;

import org.apache.axis.AxisEngine;
import org.apache.axis.configuration.FileProvider;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ResourceConfigurationProvider.java 9821 2007-08-29 18:17:55Z lucian $
 */
public class ResourceConfigurationProvider extends FileProvider {

	public ResourceConfigurationProvider(String resourceName) {
		super(ResourceConfigurationProvider.class.getResourceAsStream(resourceName));
	}

    public void writeEngineConfig(AxisEngine engine) {
    	// nothing
    }

}
