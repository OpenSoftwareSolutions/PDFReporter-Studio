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
package com.jaspersoft.studio.data.sql.model.metadata.keys;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.utils.Misc;

public class PrimaryKey implements Serializable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private String pkName;
	private MSQLColumn[] columns;

	public PrimaryKey(String pkName) {
		super();
		this.pkName = pkName;
	}

	public void setColumns(MSQLColumn[] columns) {
		this.columns = columns;
	}

	private String sql;

	public String toSqlString() {
		if (sql == null) {
			StringBuffer sb = new StringBuffer("PRIMARY KEY ");
			sb.append(Misc.nvl(pkName));
			sb.append(" (");
			String sep = "";
			for (MSQLColumn c : columns) {
				sb.append(sep);
				sep = ", ";
				sb.append(c.getValue());
			}
			sb.append(")");
			sql = sb.toString();
		}
		return sql;
	}
}
