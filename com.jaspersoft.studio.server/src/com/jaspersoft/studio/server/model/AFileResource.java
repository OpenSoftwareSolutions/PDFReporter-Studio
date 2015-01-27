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
package com.jaspersoft.studio.server.model;

import java.io.File;
import java.io.IOException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRConstants;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;

public abstract class AFileResource extends MResource {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public AFileResource(ANode parent, ResourceDescriptor rd, int index) {
		super(parent, rd, index);
	}

	public abstract String getDefaultFileExtension();

	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		if (file != null)
			try {
				getValue().setData(Base64.encodeBase64(net.sf.jasperreports.eclipse.util.FileUtils.getBytes(file)));
				getValue().setHasData(true);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		getValue().setData(null);
		getValue().setHasData(false);
	}

	public String getHFFileSize() {
		if (file != null && file.exists())
			return FileUtils.byteCountToDisplaySize(file.length());
		return "";
	}

	public String getFileName() {
		if (file != null)
			try {
				return file.getCanonicalPath();
			} catch (IOException e) {
				UIUtils.showError(e);
			}
		return "";
	}

}
