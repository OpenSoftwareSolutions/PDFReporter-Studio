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
package com.jaspersoft.studio.utils.jasper;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.util.CompositeClassloader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

public class DriversManager {
	public void init() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "drivers"); //$NON-NLS-1$  
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IDriverProvider)
					nodeFactory.add((IDriverProvider) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private List<IDriverProvider> nodeFactory = new ArrayList<IDriverProvider>();

	private URL[] getDriversURL() {
		List<URL> urls = new ArrayList<URL>();
		for (IDriverProvider f : nodeFactory)
			urls.addAll(Arrays.asList(f.getDriversURL()));

		return urls.toArray(new URL[urls.size()]);
	}

	private static URLClassLoader classloader;

	public ClassLoader getClassLoader(ClassLoader cl) {
		if (classloader == null) {
			URL[] urls = getDriversURL();
			if (urls != null && urls.length > 0)
				classloader = new URLClassLoader(urls);
		}
		if (classloader == null)
			return cl;
		return new CompositeClassloader(cl, classloader);
	}
}
