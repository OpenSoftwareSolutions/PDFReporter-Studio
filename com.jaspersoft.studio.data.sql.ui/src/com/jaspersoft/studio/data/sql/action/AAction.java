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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

public abstract class AAction extends Action {
	protected Object[] selection;
	protected TreeViewer treeViewer;

	public AAction(String text, TreeViewer treeViewer) {
		super(text);
		this.treeViewer = treeViewer;
	}

	public boolean calculateEnabled(ISelection iselection) {
		List<Object> lst = new ArrayList<Object>();
		if (iselection instanceof TreeSelection) {
			for (TreePath tp : ((TreeSelection) iselection).getPaths())
				lst.add(tp.getLastSegment());
		}
		return calculateEnabled(lst.toArray());
	}

	public boolean calculateEnabled(Object[] selection) {
		this.selection = selection;
		return true;
	}

	public void selectInTree(Object sel) {
		treeViewer.refresh(true);
		treeViewer.setSelection(new TreeSelection(new TreePath(new Object[] { sel })));
		treeViewer.reveal(sel);
	}
}
