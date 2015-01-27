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
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.AMultiSelectionAction;
import com.jaspersoft.studio.data.sql.action.expression.CreateExpression;
import com.jaspersoft.studio.data.sql.model.query.MHaving;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.data.sql.ui.gef.parts.ColumnEditPart;
import com.jaspersoft.studio.model.ANode;

public class CreateHavingFromColumn extends AMultiSelectionAction {
	private CreateExpression ce;
	private SQLQueryDesigner designer;

	public CreateHavingFromColumn(SQLQueryDesigner designer, TreeViewer treeViewer) {
		super("Create &Having Condition", treeViewer);
		this.designer = designer;
	}

	protected boolean isGoodNode(ANode element) {
		return element instanceof MSelectColumn;
	}

	@Override
	public void run() {
		MHaving mhaving = null;
		for (Object obj : selection) {
			obj = convertObject(obj);
			if (obj instanceof MSelectColumn) {
				MSelectColumn msc = (MSelectColumn) obj;
				if (mhaving == null)
					mhaving = Util.getKeyword(msc, MHaving.class);

				if (ce == null)
					ce = new CreateExpression(designer, treeViewer);
				ce.run(mhaving, msc);
				break;
			}
		}
	}

	protected ANode convertObject(Object obj) {
		if (obj instanceof ColumnEditPart)
			return ((ColumnEditPart) obj).getmSelectColumn();
		return super.convertObject(obj);
	}
}
