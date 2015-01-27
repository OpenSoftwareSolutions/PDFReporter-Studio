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
package com.jaspersoft.studio.utils;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * Utility class for Velocity Template Engine operations.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class VelocityUtils {
	
	private VelocityUtils(){
		// prevent instantiation...
	}
	
	/*
	 * @return a simple "naked" {@link VelocityEngine} instance not yet initialized
	 */
	private static VelocityEngine getSimpleVelocityEngine(){
		return new VelocityEngine();
	}

	/**
	 * Returns a "standard" pre-configured {@link VelocityEngine} instance already initialized.
	 * <p>
	 * 
	 * Here are the properties set:
	 * <ul>
	 * 	<li>to load resources (i.e: templates) from classpath</li>
	 * 	<li>to use {@link NullLogChute} logger in order to prevent Velocity from
	 * 	trying to produce the velocity.log file inside the JSS installation folder.
	 * 	It has been verified as problematic in Windows because of folder permissions.</li>
	 * </ul>
	 * 
	 * @return the configured {@link VelocityEngine} instance
	 */
	public static VelocityEngine getConfiguredVelocityEngine(){
		VelocityEngine ve = getSimpleVelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
		ve.init();
		return ve;
	}
}
