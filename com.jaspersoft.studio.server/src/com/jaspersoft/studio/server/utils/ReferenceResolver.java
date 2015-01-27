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
package com.jaspersoft.studio.server.utils;

import org.eclipse.core.runtime.IProgressMonitor;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.model.MReference;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.IConnection;

public class ReferenceResolver {
	public static ResourceDescriptor resolveReference(MResource res, ResourceDescriptor reference, IProgressMonitor monitor) throws Exception {
		INode n = res.getRoot();
		if (n != null && n instanceof MServerProfile) {
			MServerProfile sp = (MServerProfile) res.getRoot();
			return resolveReference(sp.getWsClient(monitor), reference, monitor);
		}
		return null;
	}

	public static ResourceDescriptor resolveReference(MReference res, IProgressMonitor monitor) throws Exception {
		INode n = res.getRoot();
		if (n != null && n instanceof MServerProfile) {
			MServerProfile sp = (MServerProfile) res.getRoot();
			return resolveReference(sp.getWsClient(monitor), res.getValue(), monitor);
		}
		return null;
	}

	public static ResourceDescriptor resolveReference(IConnection wsc, ResourceDescriptor refrd, IProgressMonitor monitor) throws Exception {
		ResourceDescriptor rd = new ResourceDescriptor();
		rd.setUriString(refrd.getReferenceUri());
		rd.setIsNew(false);
		rd.setWsType(refrd.getWsType());

		rd = wsc.get(monitor, rd, null);
		if (monitor != null && monitor.isCanceled())
			return rd;
		if (rd.getIsReference())
			resolveReference(wsc, rd, monitor);
		return rd;
	}
}
