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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.data.sql.model.AMSQLObject;
import com.jaspersoft.studio.data.sql.model.metadata.keys.ForeignKey;
import com.jaspersoft.studio.data.sql.model.metadata.keys.PrimaryKey;
import com.jaspersoft.studio.data.sql.text2model.ConvertUtil;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IDragable;

public class MSQLColumn extends AMSQLObject implements IDragable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MSQLColumn(ANode parent, String value, ResultSet rs) {
		super(parent, value, null);
		try {
			if (rs != null) {
				typeName = rs.getString("TYPE_NAME");
				columnSize = rs.getInt("COLUMN_SIZE");
				scale = rs.getInt("DECIMAL_DIGITS");
				precission = rs.getInt("NUM_PREC_RADIX");
				nullable = rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
				tooltip = formatedType();
				remarks = rs.getString("REMARKS");
				if (remarks != null)
					tooltip += "\n" + remarks;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private List<ForeignKey> foreignKeys;
	private PrimaryKey primaryKey;
	private String remarks;
	private String typeName;
	private int columnSize;
	private int precission;
	private int scale;
	private boolean nullable;

	public String getRemarks() {
		return remarks;
	}

	@Override
	public String getToolTip() {
		String tt = ConvertUtil.cleanDbNameFull(super.getToolTip());
		if (primaryKey != null)
			tt += "\n" + primaryKey.toSqlString();
		if (foreignKeys != null) {
			for (ForeignKey fk : foreignKeys)
				tt += "\n" + fk.toSqlString();
		}
		return tt;
	}

	@Override
	public ImageDescriptor getImagePath() {
		if (primaryKey != null)
			return JasperReportsPlugin.getDefault().getImageDescriptor("icons/key.png");
		if (foreignKeys != null && !foreignKeys.isEmpty())
			return JasperReportsPlugin.getDefault().getImageDescriptor("icons/key--arrow.png");
		return super.getImagePath();
	}

	public String getTypeName() {
		return formatedType();
	}

	private String formatedType() {
		if (typeName == null)
			return "";
		String tname = "\n" + typeName;
		if (typeName.equalsIgnoreCase("VARCHAR") || typeName.equalsIgnoreCase("CHAR") || typeName.equalsIgnoreCase("CHARACTER") || typeName.equalsIgnoreCase("NATIONAL CHARACTER")
				|| typeName.equalsIgnoreCase("NCHAR") || typeName.equalsIgnoreCase("CHARACTER VARYING") || typeName.equalsIgnoreCase("NATIONAL CHARACTER VARYING") || typeName.equalsIgnoreCase("NVARCHAR")
				|| typeName.equalsIgnoreCase("BIT") || typeName.equalsIgnoreCase("BIT VARYING") || typeName.equalsIgnoreCase(" TEXT") || typeName.equalsIgnoreCase("STRING")
				|| typeName.equalsIgnoreCase("BINARY") || typeName.equalsIgnoreCase("VARBINARY") || typeName.equalsIgnoreCase("LONGVARBINARY") || typeName.equalsIgnoreCase("NVARCHAR2"))
			tname += "(" + columnSize + ")";
		else if (typeName.equalsIgnoreCase("NUMERIC") || typeName.equalsIgnoreCase("DECIMAL") || typeName.equalsIgnoreCase("NUMBER"))
			tname += "(" + precission + ", " + scale + ")";
		if (!nullable)
			tname += " NOT NULL";
		return tname;
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	public void addForeignKey(ForeignKey fk) {
		if (foreignKeys == null)
			foreignKeys = new ArrayList<ForeignKey>();
		foreignKeys.add(fk);
	}
}
