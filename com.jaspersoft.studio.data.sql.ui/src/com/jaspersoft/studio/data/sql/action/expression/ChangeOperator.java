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
package com.jaspersoft.studio.data.sql.action.expression;

import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpressionGroup;
import com.jaspersoft.studio.model.ANode;

public class ChangeOperator extends AAction {

	public ChangeOperator(TreeViewer treeViewer) {
		super("Change to" + AMKeyword.OR_OPERATOR, treeViewer);
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection != null && selection.length == 1 && selection[0] instanceof ANode && isColumn((ANode) selection[0]);
	}

	protected boolean isColumn(ANode element) {
		boolean b = element instanceof MExpressionGroup && !element.isFirst();
		if (b)
			setMenuText((MExpressionGroup) element);
		return b;
	}

	protected void setMenuText(MExpressionGroup msel) {
		if (msel.getValue().equals(AMKeyword.AND_OPERATOR))
			setText("Change to " + AMKeyword.OR_OPERATOR);
		else
			setText("Change to " + AMKeyword.AND_OPERATOR);
	}

	@Override
	public void run() {
		for (Object obj : selection) {
			if (obj instanceof MExpressionGroup) {
				MExpressionGroup msel = (MExpressionGroup) obj;
				if (msel.getValue().equals(AMKeyword.AND_OPERATOR))
					msel.setValue(AMKeyword.OR_OPERATOR);
				else
					msel.setValue(AMKeyword.AND_OPERATOR);
				setMenuText(msel);
				selectInTree(obj);
				break;
			}
		}
	}
}
