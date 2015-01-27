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

import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;

public class CreateSubQueryTable extends AAction {

	public CreateSubQueryTable(SQLQueryDesigner designer, TreeViewer treeViewer) {
		super("Add &Sub Query", treeViewer);
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection == null || (selection != null && selection.length == 1 && isInFrom(selection[0]));
	}

	public static boolean isInFrom(Object element) {
		return element instanceof MFrom || (element instanceof MFromTable && ((MFromTable) element).getParent() instanceof MFrom);
	}

	@Override
	public void run() {
		Object sel = selection[0];
		MFrom mfrom = null;
		int index = 0;
		if (sel instanceof MFrom)
			mfrom = (MFrom) sel;
		else if (sel instanceof ANode && ((ANode) sel).getParent() instanceof MFrom) {
			mfrom = (MFrom) ((ANode) sel).getParent();
			index = mfrom.getChildren().indexOf((MFromTable) sel) + 1;
		}

		MQueryTable mtable = new MQueryTable(null);
		MFromTable msq = new MFromTable(mfrom, mtable, index);
		msq.setAlias("sq");
		mtable.setSubquery(Util.createSelect(msq));

		selectInTree(msq);
		treeViewer.expandToLevel(msq, 1);
	}

}
