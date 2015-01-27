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
package com.jaspersoft.studio.editor.preview.toolbar;

import net.sf.jasperreports.eclipse.viewer.action.AReportAction;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.services.IDisposable;

import com.jaspersoft.studio.data.widget.DatasourceComboItem;
import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.editor.preview.view.APreview;

public abstract class ATopToolBarManager {
	protected PreviewJRPrint container;

	public ATopToolBarManager(PreviewJRPrint container, Composite parent) {
		this.container = container;
		createToolBar(parent);
	}

	protected IToolBarManager tbManager;
	protected ToolBar topToolBar;

	protected void createToolBar(Composite parent) {
		topToolBar = new ToolBar(parent, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		tbManager = new ToolBarManager(topToolBar);
		removeAll();
		fillToolbar(tbManager);

		refreshToolbar();
	}

	protected abstract void fillToolbar(IToolBarManager tbManager);

	public void removeAll() {
		for (IContributionItem it : tbManager.getItems()) {	
			if (it instanceof ActionContributionItem && ((ActionContributionItem) it).getAction() instanceof IDisposable)
				((IDisposable) ((ActionContributionItem) it).getAction()).dispose();
			else if (it instanceof ContributionItem)
				it.dispose();
		}
		tbManager.removeAll();
	}

	public void refreshToolbar() {
		tbManager.update(true);
		if (!topToolBar.isDisposed())
			topToolBar.pack();
		topToolBar.getParent().layout(true);
	}

	public void contributeItems(APreview contributor) {
		removeAll();
		fillToolbar(tbManager);
		contributor.contribute2ToolBar(tbManager);
		refreshToolbar();
	}

	public void setFocus() {
	}

	public void setEnabled(boolean enabled) {
		for (IContributionItem ti : tbManager.getItems()) {
			if (ti instanceof ToolItem)
				((ToolItem) ti).setEnabled(enabled);
			else if (ti instanceof ActionContributionItem) {
				IAction action = ((ActionContributionItem) ti).getAction();
				if (action instanceof AReportAction && enabled)
					action.setEnabled(((AReportAction) action).isActionEnabled());
				else
					action.setEnabled(enabled);
			} else if (ti instanceof DatasourceComboItem)
				((DatasourceComboItem) ti).setEnabled(enabled);
		}
		refreshToolbar();
	}
}
