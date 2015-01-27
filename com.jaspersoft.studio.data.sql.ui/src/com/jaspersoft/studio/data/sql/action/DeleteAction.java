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
package com.jaspersoft.studio.data.sql.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.model.ANode;

public class DeleteAction<T extends ANode> extends AMultiSelectionAction {
	protected String name;
	protected Class<T> type;
	private SQLQueryDesigner designer;

	public DeleteAction(SQLQueryDesigner designer, TreeViewer treeViewer, String name, Class<T> type) {
		super("&Delete " + name, treeViewer);
		this.name = name;
		this.type = type;
		this.designer = designer;
	}

	protected boolean isGoodNode(ANode element) {
		return type.isAssignableFrom(element.getClass());
	}

	@Override
	public void run() {
		final List<T> lst = new ArrayList<T>();
		for (Object obj : selection) {
			if (type.isAssignableFrom(obj.getClass()))
				lst.add((T) obj);
		}
		if (UIUtils.showConfirmation("Delete " + name, "Are you sure you want to delete the " + name.toLowerCase() + "(s)?"))
			doDelete(lst);
	}

	protected void doDelete(final List<T> lst) {
		ANode mfrom = null;
		int indx = 0;
		for (T ftbl : lst) {
			if (mfrom == null)
				mfrom = (ANode) ftbl.getParent();
			indx = mfrom.getChildren().indexOf(ftbl);
			mfrom.removeChild(ftbl);
			doDeleteMore(mfrom, ftbl);
		}
		ANode toSelect = mfrom;
		if (indx - 1 > 0)
			toSelect = (ANode) mfrom.getChildren().get(Math.min(mfrom.getChildren().size() - 1, indx));
		selectInTree(toSelect);
		designer.refreshQueryText();
	}

	protected void doDeleteMore(ANode parent, T todel) {
	}
}
