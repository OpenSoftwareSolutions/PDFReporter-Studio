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
package com.jaspersoft.studio.community.requests;

/**
 * Request data for file upload to the community tracker.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class FileUploadRequest {
	private String filename;
	private byte[] encodedFileData;
	
	public FileUploadRequest(String filename, byte[] encodedFileData) {
		super();
		this.filename = filename;
		this.encodedFileData = encodedFileData.clone();
	}

	public String getAsJSON(){
		StringBuffer jsonBuf = new StringBuffer();
		jsonBuf.append("{"); //$NON-NLS-1$
		jsonBuf.append("\"file\": \"").append(new String(encodedFileData)).append("\","); //$NON-NLS-1$ //$NON-NLS-2$
		jsonBuf.append("\"filename\": \"").append(filename).append("\" }"); //$NON-NLS-1$ //$NON-NLS-2$
		return jsonBuf.toString();
	}
}
