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
package com.jaspersoft.studio.data.sql.action.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.DeleteAction;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupBy;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderBy;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class DeleteTable extends DeleteAction<MFromTable> {

	public DeleteTable(SQLQueryDesigner designer, TreeViewer treeViewer) {
		super(designer, treeViewer, "Table", MFromTable.class);
		setId(getClass().getCanonicalName());
	}

	@Override
	protected boolean isGoodNode(ANode element) {
		return element instanceof MFromTable;
	}

	@Override
	protected void doDeleteMore(ANode parent, MFromTable todel) {
		if (parent.getRoot() != null)
			for (INode n : parent.getRoot().getChildren()) {
				if (n instanceof MSelect) {
					List<ANode> toRemove = new ArrayList<ANode>();
					for (INode gb : n.getChildren()) {
						MSelectColumn gbc = (MSelectColumn) gb;
						if (gbc.getMFromTable() != null && gbc.getMFromTable().equals(todel))
							toRemove.add(gbc);
					}
					((MSelect) n).removeChildren(toRemove);
				} else if (n instanceof MGroupBy) {
					List<ANode> toRemove = new ArrayList<ANode>();
					for (INode gb : n.getChildren()) {
						MGroupByColumn gbc = (MGroupByColumn) gb;
						if (gbc.getMFromTable() != null && gbc.getMFromTable().equals(todel))
							toRemove.add(gbc);
					}
					((MGroupBy) n).removeChildren(toRemove);
				} else if (n instanceof MOrderBy) {
					List<ANode> toRemove = new ArrayList<ANode>();
					for (INode gb : n.getChildren()) {
						if (gb instanceof MOrderByColumn) {
							MOrderByColumn gbc = (MOrderByColumn) gb;
							if (gbc.getMFromTable() != null && gbc.getMFromTable().equals(todel))
								toRemove.add(gbc);
						}
					}
					((MOrderBy) n).removeChildren(toRemove);
				}
			}
	}
}
