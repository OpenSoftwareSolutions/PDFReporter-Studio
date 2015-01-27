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
package com.jaspersoft.studio.server.container;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

import com.jaspersoft.studio.server.Activator;

public class JRSClasspathContainer implements IClasspathContainer {
	public final static Path ID = new Path("com.jaspersoft.server.JRS_CONTAINER"); //$NON-NLS-1$

	// path string that uniquiely identifies this container instance
	private IPath _path;

	public JRSClasspathContainer(IPath path, IJavaProject project) {
		_path = path;
	}

	public IClasspathEntry[] getClasspathEntries() {
		List<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();

		Bundle bundle = Activator.getDefault().getBundle();
		Enumeration<URL> urls = bundle.findEntries("lib/", "js-*.jar", true); //$NON-NLS-1$ //$NON-NLS-2$
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			try {
				URL fileURL = FileLocator.toFileURL(url);
				URI uri = new URI(fileURL.getProtocol(), fileURL.getUserInfo(), fileURL.getHost(), fileURL.getPort(), fileURL.getPath(), fileURL.getQuery(), null);
				// fileURL.toURI();
				Path binpath = new Path(new File(uri).getAbsolutePath());
				Path srcpath = binpath;
				entryList.add(JavaCore.newLibraryEntry(binpath, srcpath, new Path("/"))); //$NON-NLS-1$
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// convert the list to an array and return it
		return entryList.toArray(new IClasspathEntry[entryList.size()]);
	}

	public String getDescription() {
		return "Jaspersoft Server Library"; //$NON-NLS-1$
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return _path;
	}
}
