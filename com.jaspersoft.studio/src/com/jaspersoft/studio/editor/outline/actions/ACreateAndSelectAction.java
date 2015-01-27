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
package com.jaspersoft.studio.editor.outline.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

public abstract class ACreateAndSelectAction extends ACreateAction {

	public ACreateAndSelectAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	public void run() {
		super.run();
		ISelection s = getSelection();
		if (s instanceof StructuredSelection) {
			Object obj = ((StructuredSelection) s).getFirstElement();
			if (obj instanceof EditPart) {
				EditPart editPart = (EditPart) obj;
				List children = editPart.getChildren();
				if (children != null && !children.isEmpty()) {
					int last = children.size() - 1;
					StructuredSelection newselection = new StructuredSelection(children.get(last));
					setSelection(newselection);
					getWorkbenchPart().getSite().getSelectionProvider().setSelection(newselection);
				}
			}
		}
	}
}
