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
package com.jaspersoft.ireport.jasperserver.ws;

/**
 * 
 * @author gtoffoli
 */
public class JServer {

	private int timeout;
	private String name;
	private String url;
	private String username;
	private String password;
	private boolean chunked;
	private boolean mime;

	private WSClient wSClient = null;

	private boolean loaded = false;
	private boolean loading = false;

	private String locale = null;

	/** Creates a new instance of JServer */
	public JServer() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setWSClient(null);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		setWSClient(null);
	}

	public boolean isMime() {
		return mime;
	}

	public void setMime(boolean mime) {
		this.mime = mime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		setWSClient(null);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		setWSClient(null);
	}

	public String toString() {
		return "" + getName();
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public WSClient getWSClient() throws Exception {
		if (wSClient == null) {
			setWSClient(new WSClient(this));
		}
		return wSClient;
	}

	public void setWSClient(WSClient wSClient) {
		this.wSClient = wSClient;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isChunked() {
		return chunked;
	}

	public void setChunked(boolean chunked) {
		this.chunked = chunked;
	}

}
