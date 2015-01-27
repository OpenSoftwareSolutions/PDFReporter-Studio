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

import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.model.ANode;

public abstract class AMultiSelectionAction extends AAction {

	public AMultiSelectionAction(String text, TreeViewer treeViewer) {
		super(text, treeViewer);
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		if (selection == null)
			return false;
		else {
			for (Object s : selection) {
				s = convertObject(s);
				if (s == null)
					return false;
				if (!isGoodNode((ANode) s))
					return false;
			}
		}
		return true;
	}

	protected ANode convertObject(Object obj) {
		if (obj instanceof ANode)
			return (ANode) obj;
		return null;
	}

	protected abstract boolean isGoodNode(ANode element);
}
