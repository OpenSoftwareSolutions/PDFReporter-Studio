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
package com.jaspersoft.studio.data.sql.action.union;

import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.model.query.MUnion;

public class ChangeSetOperator extends AAction {

	private static final String CHANGE_TO = "&Change To ";
	private String operator;

	public ChangeSetOperator(String operator, TreeViewer treeViewer) {
		super(CHANGE_TO + operator, treeViewer);
		this.operator = operator;
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection != null && selection.length == 1 && selection[0] instanceof MUnion && !((MUnion) selection[0]).getValue().equals(operator);
	}

	@Override
	public void run() {
		for (Object obj : selection) {
			if (obj instanceof MUnion) {
				((MUnion) obj).setValue(operator);
				break;
			}
		}
		treeViewer.refresh(true);
	}
}
