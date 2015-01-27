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
package com.jaspersoft.studio.data.sql.model.metadata;

import net.sf.jasperreports.engine.JRConstants;

import org.apache.commons.lang.WordUtils;

import com.jaspersoft.studio.data.sql.model.MDBObjects;

public class MTables extends MDBObjects {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MTables(MSqlSchema parent, String value) {
		super(parent, value, "icons/table.png");
	}

	public String getTableCatalog() {
		return ((MSqlSchema) getParent()).getTableCatalog();
	}

	public String getTableSchema() {
		return (String) getParent().getValue();
	}

	@Override
	public String getDisplayText() {
		return WordUtils.capitalizeFully(getValue());
	}
}
