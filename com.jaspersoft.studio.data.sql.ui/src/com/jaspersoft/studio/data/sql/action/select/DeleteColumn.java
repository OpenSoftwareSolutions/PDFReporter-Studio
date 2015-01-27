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
package com.jaspersoft.studio.data.sql.action.select;

import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.DeleteAction;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupBy;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderBy;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByExpression;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectExpression;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class DeleteColumn extends DeleteAction<ANode> {

	public DeleteColumn(SQLQueryDesigner designer, TreeViewer treeViewer) {
		super(designer, treeViewer, "Column", ANode.class);
	}

	@Override
	protected boolean isGoodNode(ANode element) {
		return element instanceof MSelectColumn || element instanceof MSelectExpression;
	}

	@Override
	protected void doDeleteMore(ANode parent, ANode todel) {
		for (INode n : parent.getRoot().getChildren()) {
			if (n instanceof MGroupBy) {
				for (INode gb : n.getChildren()) {
					MGroupByColumn gbc = (MGroupByColumn) gb;
					if (gbc.getMSelectColumn() != null && gbc.getMSelectColumn().equals(todel))
						gbc.setMSelectColumn(null);
				}
			} else if (n instanceof MOrderBy) {
				for (INode gb : n.getChildren()) {
					if (gb instanceof MOrderByColumn) {
						MOrderByColumn gbc = (MOrderByColumn) gb;
						if (gbc.getMSelectColumn() != null && gbc.getMSelectColumn().equals(todel))
							gbc.setMSelectColumn(null);
					} else if (gb instanceof MOrderByExpression) {
						MOrderByExpression gbc = (MOrderByExpression) gb;
						if (gbc.getMSelectionExpression() != null && gbc.getMSelectionExpression().equals(todel))
							gbc.setMSelectionExpression(null);
					}
				}
			}
		}
	}
}
