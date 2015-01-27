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
 * Represents data that is returned from the web services calls.
 *
 */

public class FileContent {
	private byte[] bytes;
	private String mimeType;
	private String name;

	public FileContent() {
	}

	public void setData(byte[] bytes) { this.bytes = bytes; }
	public void setMimeType(String mimeType) { this.mimeType = mimeType; }

	public byte[] getData() { return bytes; }
	public String getMimeType() { return mimeType; }

	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
}
