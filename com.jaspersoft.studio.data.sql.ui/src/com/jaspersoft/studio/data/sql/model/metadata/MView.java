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

import java.sql.ResultSet;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.model.IDragable;

public class MView extends MSqlTable implements IDragable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MView(MTables parent, String value, ResultSet rs) {
		super(parent, "View", "icons/table.png");
		setRemarks(rs);
	}

}
