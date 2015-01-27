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
package com.jaspersoft.studio;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.Util;
import org.eclipse.osgi.service.datalocation.Location;

/**
 * Provide the methods to retrieve the path to the configuration file, the 
 * path is cached after the first request
 * 
 * @author Orlandin Marco
 *
 */
public class ConfigurationPathProvider {
	
	/**
	 * Where the path is cached
	 */
	private static String cachedPath = null;
	
	/**
	 * Get the path and cache it values
	 */
	private static void intializePath(){
		String path = null;
		Location configArea = Platform.getInstallLocation();
		String product = Platform.getProduct().getName();
		if (configArea != null) {
			if (Util.isMac()) {
				path = configArea.getURL().toExternalForm() + "/" + product + ".app/Contents/MacOS/";
				path = path + product + ".ini";
			}
			else path = configArea.getURL().toExternalForm() + product + ".ini"; //$NON-NLS-1$
		}
		cachedPath = path;
	}
	
	/**
	 * 
	 * Return the path of the configuration file and cache it. 
	 * 
	 * @return String represented a Path in URL format to the configuration file
	 */
	public static String getPath(){
		if (cachedPath == null) intializePath();
		return cachedPath;
	}
}
