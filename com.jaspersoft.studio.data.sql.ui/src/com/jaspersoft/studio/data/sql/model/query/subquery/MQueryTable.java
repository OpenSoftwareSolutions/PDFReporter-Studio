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
package com.jaspersoft.studio.data.sql.model.query.subquery;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.metadata.MTables;
import com.jaspersoft.studio.data.sql.model.query.AMQueryAliased;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.model.INode;

public class MQueryTable extends MSqlTable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MQueryTable(MTables parent) {
		super(parent, "(...)", (String) null);
	}

	@Override
	public List<INode> getChildren() {
		if (subquery != null) {
			List<INode> children = new ArrayList<INode>();
			for (INode n : subquery.getChildren()) {
				if (n instanceof MSelectColumn || ((AMQueryAliased<?>) n).getAlias() != null)
					children.add(new MQueryColumn(null, (AMQueryAliased<?>) n));
			}
			return children;
		}
		return super.getChildren();
	}

	private MSelect subquery;

	public MSelect getSubquery() {
		return subquery;
	}

	public void setSubquery(MSelect subquery) {
		this.subquery = subquery;
	}

	@Override
	public boolean isNotInMetadata() {
		return true;
	}

	@Override
	public boolean isCurrentSchema() {
		return true;
	}
}
