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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.httpclient.HttpConnectionManager;

public class IdleConnectionMonitorThread extends Thread {
	private final Set<HttpConnectionManager> connMgr = new HashSet<HttpConnectionManager>();
	private volatile boolean shutdown;

	public IdleConnectionMonitorThread() {
		super();
	}

	public synchronized void addConnectionManager(HttpConnectionManager hcm) {
		connMgr.add(hcm);
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000);
					for (HttpConnectionManager m : connMgr) {
						// Close expired connections
						// m.closeExpiredConnections();
						// Optionally, close connections
						// that have been idle longer than 30 sec
						m.closeIdleConnections(30000);
					}
				}
			}
		} catch (InterruptedException ex) {
			// terminate
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}
