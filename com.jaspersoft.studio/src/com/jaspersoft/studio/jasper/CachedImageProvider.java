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
package com.jaspersoft.studio.jasper;

import java.awt.Image;

import net.sf.jasperreports.chartthemes.simple.FileImageProvider;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JasperReportsContext;

public class CachedImageProvider extends FileImageProvider {
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private Image image;

	public CachedImageProvider() {
		super();
	}

	public CachedImageProvider(String file) {
		super(file);
	}

	public CachedImageProvider(FileImageProvider p) {
		super(p.getFile());
	}

	private long last = -1;

	@Override
	public Image getImage(JasperReportsContext jasperReportsContext) {
		if (image == null && System.currentTimeMillis() - last > 1000) {
			try {
				image = super.getImage(jasperReportsContext);
				last = -1;
			} catch (Exception e) {
				last = System.currentTimeMillis();
				return null;
			}
		}
		return image;
	}

	@Override
	public void setFile(String file) {
		super.setFile(file);
		if (image != null) {
			image.flush();
			image = null;
		}
	}
}
