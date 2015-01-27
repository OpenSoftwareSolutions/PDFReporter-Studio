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
package com.jaspersoft.studio.server.protocol.restv2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Response;

import org.eclipse.core.runtime.IProgressMonitor;
import org.glassfish.jersey.apache.connector.ApacheConnector;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;

import com.jaspersoft.studio.server.utils.UrlUtil;

public class JSSApacheConnector extends ApacheConnector {
	private ClientRequest lastRequest;

	public JSSApacheConnector(Configuration arg0) {
		super(arg0);
	}

	@Override
	public ClientResponse apply(ClientRequest req) throws ProcessingException {
		System.out.println("call: " + req.getUri());
		req.setUri(UrlUtil.fixUri(req.getUri()));
		lastRequest = req;
		return super.apply(req);
	}

	public void closeLastRequest() {
		if (lastRequest != null)
			lastRequest.close();
	}

	private Response doWait(Future<Response> rf, IProgressMonitor monitor) throws Exception {
		try {
			if (monitor != null)
				while (!rf.isDone() && !rf.isCancelled()) {
					if (monitor.isCanceled())
						rf.cancel(true);
					Thread.sleep(5);
				}
			return rf.get();
		} catch (InterruptedException e) {
			throw e;
		} catch (ExecutionException e) {
			throw e;
		}
	}

	public Response get(Builder builder, IProgressMonitor monitor) throws Exception {
		return doWait(builder.async().get(), monitor);
	}

	public Response delete(Builder builder, IProgressMonitor monitor) throws Exception {
		return doWait(builder.async().delete(), monitor);
	}

	public Response post(Builder builder, Entity<?> entity, IProgressMonitor monitor) throws Exception {
		return doWait(builder.async().post(entity), monitor);
	}

	public Response put(Builder builder, Entity<?> entity, IProgressMonitor monitor) throws Exception {
		builder.header("Content-Lenght", 0);
		return doWait(builder.async().put(entity), monitor);
	}
}
