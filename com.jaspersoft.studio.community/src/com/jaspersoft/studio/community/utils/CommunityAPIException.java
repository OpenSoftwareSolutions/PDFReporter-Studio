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
package com.jaspersoft.studio.community.utils;

import com.jaspersoft.studio.utils.Misc;

/**
 * Custom exception suitable to notify errors during operations with the
 * Community tracker.
 * <p>
 * 
 * An HTTP status code ({@link #getHttpStatusCode()}) and the response body (
 * {@link #getResponseBodyAsString()}) could be set if more error details are
 * needed.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class CommunityAPIException extends Exception {

	private static final long serialVersionUID = 1L;

	private int httpStatusCode = -1;
	private String responseBodyAsString = null;

	public CommunityAPIException() {
		super();
	}

	public CommunityAPIException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public CommunityAPIException(String arg0) {
		super(arg0);
	}
	
	public CommunityAPIException(Throwable arg0) {
		super(arg0);
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getResponseBodyAsString() {
		return responseBodyAsString;
	}

	public void setResponseBodyAsString(String responseBodyAsString) {
		this.responseBodyAsString = responseBodyAsString;
	}
	
	@Override
	public String getMessage() {
		String message = super.getMessage();
		message+= "\nHttp Status Code: " + ((httpStatusCode==-1) ? "<no status code>" : httpStatusCode);
		message+= "\nHttp Response Code: " + Misc.nvl(responseBodyAsString,"<empty body>");
		return message;
	}
	
}
