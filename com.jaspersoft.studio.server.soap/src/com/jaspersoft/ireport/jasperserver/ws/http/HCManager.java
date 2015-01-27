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
package com.jaspersoft.ireport.jasperserver.ws.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.eclipse.core.runtime.IProgressMonitor;

public class HCManager {
	private Map<IProgressMonitor, HttpMethod> cmap = new HashMap<IProgressMonitor, HttpMethod>();

	public void cancel(HttpMethod method) {
		HttpClient client = null;
		HttpConnectionManager cmanager = client.getHttpConnectionManager();

		for (IProgressMonitor m : cmap.keySet()) {
			if (m.isCanceled()) {
				method.abort();
				cmap.remove(m);
			}
		}

	}
}
