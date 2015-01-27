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
package com.jaspersoft.studio.doc.handlers;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.editor.JrxmlEditor;

/**
 * 
 * Handler to switch the report editor into the preview tab
 * 
 * @author Oralndin Marco
 *
 */
public class SwitchToPreviewHandler extends Action {
	
		@Override
		public void run() {
			JrxmlEditor editor = (JrxmlEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			editor.setActiveEditor(editor.getEditor(2));
		}
}
