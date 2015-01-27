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
package com.jaspersoft.studio.server.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.jaspersoft.studio.server.Activator;

public class HttpUtils31 {
	public static HttpMethod get(HttpClient client, String url) throws HttpException, IOException {
		HttpMethod method = new GetMethod(url);
		method.setRequestHeader("Accept", "application/json");

		System.out.println(method.getURI());
		int statusCode = client.executeMethod(method);
		if (statusCode != HttpStatus.SC_OK)
			System.err.println("Method failed: " + method.getStatusLine());
		return method;
	}

	public static IProxyService getProxyService() {
		if (Activator.getDefault() != null) {
			BundleContext bc = Activator.getDefault().getBundle().getBundleContext();
			ServiceReference serviceReference = bc.getServiceReference(IProxyService.class.getName());
			return (IProxyService) bc.getService(serviceReference);
		}
		return null;
	}

	public static void setupProxy(HttpClient c, URL arg2, HostConfiguration config) {
		IProxyService proxyService = getProxyService();
		if (proxyService == null)
			return;
		IProxyData[] proxyDataForHost;
		try {
			proxyDataForHost = proxyService.select(arg2.toURI());
			for (IProxyData data : proxyDataForHost) {
				if (data.isRequiresAuthentication()) {
					String userId = data.getUserId();
					Credentials proxyCred = new UsernamePasswordCredentials(userId, data.getPassword());
					// if the username is in the form "user\domain"
					// then use NTCredentials instead.
					int domainIndex = userId.indexOf("\\");
					if (domainIndex > 0) {
						String domain = userId.substring(0, domainIndex);
						if (userId.length() > domainIndex + 1) {
							String user = userId.substring(domainIndex + 1);
							proxyCred = new NTCredentials(user, data.getPassword(), data.getHost(), domain);
						}
					}
					c.getState().setProxyCredentials(AuthScope.ANY, proxyCred);
				}
				config.setProxy(data.getHost(), data.getPort());
			}
			// Close the service and close the service tracker
			proxyService = null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
