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

import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.model.ANode;

public class SelectDistinct extends AAction {

	private static final String CHANGE_TO = "&Change To ";

	public SelectDistinct(TreeViewer treeViewer) {
		super(CHANGE_TO + AMKeyword.SELECT_DISTINCT_KEYWORD, treeViewer);
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection != null && selection.length == 1 && selection[0] instanceof ANode && isColumn((ANode) selection[0]);
	}

	protected boolean isColumn(ANode element) {
		boolean b = element instanceof MSelect;
		if (b)
			setMenuText((MSelect) element);
		return b;
	}

	protected void setMenuText(MSelect msel) {
		if (msel.getValue().equals(AMKeyword.SELECT_KEYWORD))
			setText(CHANGE_TO + AMKeyword.SELECT_DISTINCT_KEYWORD);
		else
			setText(CHANGE_TO + AMKeyword.SELECT_KEYWORD);
	}

	@Override
	public void run() {
		for (Object obj : selection) {
			if (obj instanceof MSelect) {
				MSelect msel = (MSelect) obj;
				if (msel.getValue().equals(AMKeyword.SELECT_KEYWORD))
					msel.setValue(AMKeyword.SELECT_DISTINCT_KEYWORD);
				else
					msel.setValue(AMKeyword.SELECT_KEYWORD);
				setMenuText(msel);
				selectInTree(obj);
				break;
			}
		}
	}
}
