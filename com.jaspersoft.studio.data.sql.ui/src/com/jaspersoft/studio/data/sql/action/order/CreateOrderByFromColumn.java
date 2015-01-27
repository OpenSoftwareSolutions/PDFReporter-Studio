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
package com.jaspersoft.studio.data.sql.action.order;

import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.AMultiSelectionAction;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderBy;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByExpression;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectExpression;
import com.jaspersoft.studio.data.sql.ui.gef.parts.ColumnEditPart;
import com.jaspersoft.studio.model.ANode;

public class CreateOrderByFromColumn extends AMultiSelectionAction {

	public CreateOrderByFromColumn(TreeViewer treeViewer) {
		super("Add To &Order By", treeViewer);
	}

	protected boolean isGoodNode(ANode element) {
		return element instanceof MSelectColumn || element instanceof MSelectExpression;
	}

	@Override
	public void run() {
		ANode gbc = null;
		MOrderBy morderby = null;
		for (Object obj : selection) {
			obj = convertObject(obj);
			if (obj instanceof MSelectColumn) {
				MSelectColumn msc = (MSelectColumn) obj;
				if (morderby == null)
					morderby = Util.getKeyword(msc, MOrderBy.class);
				gbc = new MOrderByColumn(morderby, msc);
			} else if (obj instanceof MSelectExpression) {
				MSelectExpression msc = (MSelectExpression) obj;
				if (morderby == null)
					morderby = Util.getKeyword(msc, MOrderBy.class);
				gbc = new MOrderByExpression(morderby, msc);
			}
		}
		selectInTree(gbc);
	}

	protected ANode convertObject(Object obj) {
		if (obj instanceof ColumnEditPart)
			return ((ColumnEditPart) obj).getmSelectColumn();
		return super.convertObject(obj);
	}
}
