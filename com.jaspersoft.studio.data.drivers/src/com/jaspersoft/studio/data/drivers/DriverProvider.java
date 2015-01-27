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
package com.jaspersoft.studio.data.drivers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.osgi.framework.Bundle;

import com.jaspersoft.studio.utils.jasper.IDriverProvider;

public class DriverProvider implements IDriverProvider {

	private static List<URL> urlist;

	@Override
	public URL[] getDriversURL() {
		if (urlist == null) {
			urlist = new ArrayList<URL>();

			Bundle bundle = Activator.getDefault().getBundle();
			Enumeration<URL> urls = bundle.findEntries("lib/", "*.jar", true);
			while (urls.hasMoreElements())
				urlist.add(urls.nextElement());
		}
		return urlist.toArray(new URL[urlist.size()]);
	}

}
