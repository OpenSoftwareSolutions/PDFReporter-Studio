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
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.utils.Misc;

public class ForeignKey implements Serializable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private String fkName;
	private MSQLColumn[] srcColumns;
	private MSQLColumn[] destColumns;
	private MSqlTable tbl;

	public ForeignKey(String fkName, MSqlTable tbl) {
		super();
		this.fkName = fkName;
		this.tbl = tbl;
	}

	public void setColumns(MSQLColumn[] srcColumns, MSQLColumn[] destColumns) {
		this.srcColumns = srcColumns;
		this.destColumns = destColumns;
	}

	public String getFkName() {
		return fkName;
	}

	public MSQLColumn[] getSrcColumns() {
		return srcColumns;
	}

	public MSQLColumn[] getDestColumns() {
		return destColumns;
	}

	@Override
	public boolean equals(Object obj) {
		return fkName.equals(obj);
	}

	@Override
	public int hashCode() {
		return fkName.hashCode();
	}

	public MSqlTable getTable() {
		return tbl;
	}

	private String sql;

	public String toSqlString() {
		if (sql == null) {
			StringBuffer sb = new StringBuffer("CONSTRAINT ");
			sb.append(Misc.nvl(fkName));
			sb.append(" FOREIGN KEY (");
			String sep = "";
			if (srcColumns != null)
				for (MSQLColumn c : srcColumns) {
					sb.append(sep);
					sep = ", ";
					sb.append(c.getValue());
				}
			sb.append(") REFERENCES ");
			if (destColumns != null && destColumns.length > 0) {
				MSqlTable destTable = (MSqlTable) destColumns[0].getParent();
				sb.append(destTable.toSQLString()).append(" (");
				sep = "";
				for (MSQLColumn c : destColumns) {
					sb.append(sep);
					sep = ", ";
					sb.append(c.getValue());
				}
			}
			sb.append(")");
			sql = sb.toString();
		}
		return sql;
	}
}
