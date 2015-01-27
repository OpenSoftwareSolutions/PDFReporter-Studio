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

import java.lang.reflect.Field;
import java.net.URL;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.CommonsHTTPSender;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;

import com.jaspersoft.studio.server.util.HttpUtils31;

public class JSSCommonsHTTPSender extends CommonsHTTPSender {
	private static final long serialVersionUID = 8881188152022966420L;

	public JSSCommonsHTTPSender() {
		super();
		try {
			Field field = CommonsHTTPSender.class.getDeclaredField("httpChunkStream");
			field.setAccessible(true);
			field.setBoolean(this, false);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected HostConfiguration getHostConfiguration(HttpClient c, MessageContext arg1, URL arg2) {
		HostConfiguration config = super.getHostConfiguration(c, arg1, arg2);
		HttpUtils31.setupProxy(c, arg2, config);
		return config;
	}
}
