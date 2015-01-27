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
package com.jaspersoft.studio.server.model.server;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.repo.Resource;

import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;

public class ServerProfile implements Resource, Cloneable, Serializable {

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private boolean supportsDateRanges;
	private String name;
	private String url;
	private String user;
	private String organisation;
	private String jrVersion = JRXmlWriterHelper.LAST_VERSION;
	private int timeout = 60000;
	private boolean chunked;
	private boolean mime = true;
	private String projectPath;
	private boolean useOnlySOAP = false;
	private boolean syncDA = false;
	private String locale;
	private String timeZone;
	private boolean useSSO = false;
	private String ssoUuid;

	public String getSsoUuid() {
		return ssoUuid;
	}

	public void setSsoUuid(String ssoUuid) {
		this.ssoUuid = ssoUuid;
	}

	public boolean isUseSSO() {
		return useSSO;
	}

	public void setUseSSO(boolean useSSO) {
		this.useSSO = useSSO;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isSyncDA() {
		return syncDA;
	}

	public void setSyncDA(boolean syncDA) {
		this.syncDA = syncDA;
	}

	public boolean isUseOnlySOAP() {
		return useOnlySOAP;
	}

	public void setUseOnlySOAP(boolean useOnlySOAP) {
		this.useOnlySOAP = useOnlySOAP;
	}

	public boolean isMime() {
		return mime;
	}

	public void setMime(boolean mime) {
		this.mime = mime;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getJrVersion() {
		return jrVersion;
	}

	public void setJrVersion(String jrVersion) {
		this.jrVersion = jrVersion;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	private String pass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		if (!url.endsWith("/"))
			url += "/";
		return url;
	}

	private URL url_;

	public URL getURL() throws MalformedURLException {
		if (url_ == null)
			url_ = new URL(getUrl());
		return url_;
	}

	public void setUrl(String url) {
		this.url = url;
		this.url_ = null;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
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

	public boolean isSupportsDateRanges() {
		return supportsDateRanges;
	}

	public void setSupportsDateRanges(boolean supportsDateRanges) {
		this.supportsDateRanges = supportsDateRanges;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
