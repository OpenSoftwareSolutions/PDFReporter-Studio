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

import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectSubQuery;
import com.jaspersoft.studio.model.ANode;

public class CreateSubSelect extends AAction {

	public CreateSubSelect(TreeViewer treeViewer) {
		super("Add &Sub Query", treeViewer);
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection != null && selection.length == 1 && isInSelect(selection[0]);
	}

	public static boolean isInSelect(Object element) {
		return element instanceof MSelect || (element instanceof ANode && ((ANode) element).getParent() instanceof MSelect);
	}

	@Override
	public void run() {
		Object sel = selection[0];
		MSelect mselect = null;
		int index = 0;
		if (sel instanceof MSelect)
			mselect = (MSelect) sel;
		else if (sel instanceof ANode && ((ANode) sel).getParent() instanceof MSelect) {
			mselect = (MSelect) ((ANode) sel).getParent();
			index = mselect.getChildren().indexOf(sel) + 1;
		}

		MSelectSubQuery msq = new MSelectSubQuery(mselect, index);
		Util.createSelect(msq);

		selectInTree(msq);
		treeViewer.expandToLevel(msq, 1);
	}

}
