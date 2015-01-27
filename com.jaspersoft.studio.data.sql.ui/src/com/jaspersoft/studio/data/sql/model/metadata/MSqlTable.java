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
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.data.sql.model.AMSQLObject;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IDragable;

public class MSqlTable extends AMSQLObject implements IDragable, INotInMetadata {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private boolean isNotInMetadata = false;

	public MSqlTable(MTables parent, String value, boolean isNotInMetadata) {
		super(parent, value, null);
		this.isNotInMetadata = isNotInMetadata;
	}

	protected MSqlTable(MTables parent, String value, String image) {
		super(parent, value, image);
	}

	public MSqlTable(ANode parent, String value, ResultSet rs) {
		super(parent, value, "icons/table.png");
		tooltip = "";
		if (rs != null) {
			setType(rs);
			setRemarks(rs);
		}
	}

	public boolean isNotInMetadata() {
		return isNotInMetadata;
	}

	protected void setType(ResultSet rs) {
		try {
			type = rs.getString("TABLE_TYPE");
			tooltip += "\n" + type;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void setRemarks(ResultSet rs) {
		try {
			remarks = rs.getString("REMARKS");
			if (remarks != null)
				tooltip += "\n" + remarks;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String type;

	public String getType() {
		return type;
	}

	private String remarks;

	public String getRemarks() {
		return remarks;
	}

	public MSqlSchema getSchema() {
		if (getParent() != null && getParent().getParent() != null && getParent().getParent() instanceof MSqlSchema)
			return (MSqlSchema) getParent().getParent();
		return null;
	}

	public boolean isCurrentSchema() {
		return getParent() != null && getParent().getParent() != null && getParent().getParent() instanceof MSqlSchema && ((MSqlSchema) getParent().getParent()).isCurrent();
	}

}
