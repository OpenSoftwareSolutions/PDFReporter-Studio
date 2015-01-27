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
package com.jaspersoft.studio.toolbars;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.gef.parts.EditableFigureEditPart;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.subreport.MSubreport;

/**
 * Toolbars button to open inside the editor a report 
 * starting from a selected subreport element
 * 
 * @author Orlandin Marco
 *
 */
public class SubreportContributionItem extends CommonToolbarHandler{
	
	private SelectionAdapter pushButtonPressed = new SelectionAdapter() {
		
		public void widgetSelected(SelectionEvent e) {
			List<Object> selection = getSelectionForType(MSubreport.class);
			if (selection.size() != 1)
				return;
			ANode subreportModel = (ANode) selection.get(0);
			Object value = subreportModel.getValue();
			IEditorPart editorPart = (IEditorPart)getWorkbenchPart();
			EditableFigureEditPart.openEditor(value, editorPart, subreportModel);
		}
	};
	
	@Override
	protected Control createControl(Composite parent) {
		super.createControl(parent);
		ToolBar buttons = new ToolBar(parent, SWT.FLAT | SWT.WRAP);
		
		ToolItem changeImage = new ToolItem(buttons, SWT.PUSH);
		changeImage.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/blue-folder-open-document.png"));
		changeImage.setToolTipText("Open Subreport");
		changeImage.addSelectionListener(pushButtonPressed);
		
		return buttons;
	}
	
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) return false;
		List<Object> selection = getSelectionForType(MSubreport.class);
		return selection.size() == 1;
	}
}
