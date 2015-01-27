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

import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.model.query.orderby.AMOrderByMember;
import com.jaspersoft.studio.model.ANode;

public class OrderByDesc extends AAction {

	public OrderByDesc(TreeViewer treeViewer) {
		super("Set Ascending", treeViewer);
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection != null && selection.length == 1 && selection[0] instanceof ANode && isColumn((ANode) selection[0]);
	}

	protected boolean isColumn(ANode element) {
		boolean b = element instanceof AMOrderByMember;
		if (b)
			setMenuText((AMOrderByMember<?>) element);
		return b;
	}

	protected void setMenuText(AMOrderByMember<?> msel) {
		if (msel.isDesc())
			setText("Set Ascending");
		else
			setText("Set Descending");
	}

	@Override
	public void run() {
		for (Object obj : selection) {
			if (obj instanceof AMOrderByMember) {
				AMOrderByMember<?> msel = (AMOrderByMember<?>) obj;
				msel.setDesc(!msel.isDesc());
				setMenuText(msel);
				selectInTree(obj);
				break;
			}
		}
	}
}
