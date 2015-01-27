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

import javax.activation.DataSource;

/**
 * @author Lucian Chirita
 *
 */
public class RequestAttachment {

	private DataSource dataSource;
	private String contentID;
	
	public RequestAttachment() {
		this(null, null);
	}
	
	public RequestAttachment(DataSource dataSource) {
		this(dataSource, null);
	}
	
	public RequestAttachment(DataSource dataSource, String contentID) {
		this.dataSource = dataSource;
		this.contentID = contentID;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getContentID() {
		return contentID;
	}

	public void setContentID(String contentID) {
		this.contentID = contentID;
	}
	
	
	
}
