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
package com.jaspersoft.studio.server.protocol;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.fluent.Request;
import org.eclipse.core.runtime.IProgressMonitor;

public class ConnectionManager {
	private static Map<Request, IProgressMonitor> requests = new HashMap<Request, IProgressMonitor>();

	public synchronized static void register(IProgressMonitor monitor, Request req) {
		requests.put(req, monitor);
	}

	public synchronized static void unregister(Request req) {
		requests.remove(req);
	}

	private synchronized static void clean() {
		for (Request r : requests.keySet()) {
			IProgressMonitor m = requests.get(r);
			if (m.isCanceled()) {
				r.abort();
				requests.remove(r);
			}
		}
	}

	private static Thread mct = new Thread(new MonitorCancelThread());
	static {
		mct.start();
	}

	private static class MonitorCancelThread implements Runnable {

		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
					clean();
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}

}
