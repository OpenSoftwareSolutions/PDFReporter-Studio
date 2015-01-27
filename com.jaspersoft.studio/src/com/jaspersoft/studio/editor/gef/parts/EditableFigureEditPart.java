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
package com.jaspersoft.studio.editor.gef.parts;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;

import com.jaspersoft.studio.editor.IJROBjectEditor;
import com.jaspersoft.studio.model.ANode;

public class EditableFigureEditPart extends FigureEditPart {
	
	
	@Override
	public void performRequest(Request req) {
		if (RequestConstants.REQ_OPEN.equals(req.getType())) {
			Object value = ((ANode) getModel()).getValue();
			IEditorPart editorPart = ((DefaultEditDomain) getViewer().getEditDomain()).getEditorPart();
			openEditor(value, editorPart, ((ANode) getModel()));
		}
		super.performRequest(req);
	}

	public static void openEditor(final Object val, IEditorPart editor, ANode node) {
		if (editor.getEditorSite() instanceof MultiPageEditorSite) {
			final MultiPageEditorPart mpep = ((MultiPageEditorSite) editor.getEditorSite()).getMultiPageEditor();
			if (mpep instanceof IJROBjectEditor)
				doOpenEditor(val, (IJROBjectEditor) mpep, node);
		} else {
			editor = Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if (editor instanceof IJROBjectEditor)
				doOpenEditor(val, (IJROBjectEditor) editor, node);
		}
	}

	public static void doOpenEditor(final Object val, final IJROBjectEditor editor, final ANode node) {
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				((IJROBjectEditor) editor).openEditor(val, node);
			}
		});
	}
}
