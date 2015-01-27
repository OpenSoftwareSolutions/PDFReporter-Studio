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
package com.jaspersoft.studio.editor.preview.view;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.editor.preview.actions.export.AExportAction;
import com.jaspersoft.studio.editor.preview.actions.export.ExportMenuAction;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class APreview {
	private Control control;
	protected JasperReportsConfiguration jContext;

	public APreview(Composite parent, JasperReportsConfiguration jContext) {
		this.jContext = jContext;
		control = createControl(parent);
	}

	protected abstract Control createControl(Composite parent);

	public Control getControl() {
		return control;
	}

	private boolean contributed = false;

	public boolean isContributed2ToolBar() {
		return contributed;
	}

	public void contribute2ToolBar(IToolBarManager tmanager) {
		contributed = true;
	}

	public void dispose() {

	}

	public void setEnabled(boolean enabled) {

	}

	protected void setDefaultExporter(ExportMenuAction exmenu, AExportAction expAction) {
		if (expAction == null)
			return;
		for (IContributionItem ic : exmenu.getMenuManager().getItems()) {
			if (ic instanceof ActionContributionItem) {
				ActionContributionItem aic = (ActionContributionItem) ic;
				if (aic.getAction().getClass().equals(expAction.getClass()))
					exmenu.setDefaultAction(aic.getAction());
			}
		}
	}
}
