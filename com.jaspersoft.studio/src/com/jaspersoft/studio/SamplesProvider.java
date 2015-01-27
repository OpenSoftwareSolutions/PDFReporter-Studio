/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import net.sf.jasperreports.samples.ISamplesProvider;

public class SamplesProvider implements ISamplesProvider {

	@Override
	public Set<URL> getSamples() {
		Set<URL> paths = new HashSet<URL>();
		Enumeration<?> en = JaspersoftStudioPlugin.getInstance().getBundle().findEntries("lib", "*.jar", true); //$NON-NLS-1$ //$NON-NLS-2$
		while (en.hasMoreElements())
			paths.add((URL) en.nextElement());
		return paths;
	}

}
