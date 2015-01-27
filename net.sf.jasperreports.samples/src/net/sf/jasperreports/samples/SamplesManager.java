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
package net.sf.jasperreports.samples;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class SamplesManager {
	public void init() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(Activator.PLUGIN_ID, "samples"); //$NON-NLS-1$  
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("resources"); //$NON-NLS-1$
				if (o instanceof ISamplesProvider)
					paths.add((ISamplesProvider) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private List<ISamplesProvider> paths = new ArrayList<ISamplesProvider>();

	public Set<URL> getURLs() {
		Set<URL> urls = new HashSet<URL>();
		for (ISamplesProvider sp : paths) {
			Set<URL> samples = sp.getSamples();
			if (samples != null)
				urls.addAll(samples);
		}
		return urls;
	}

}
