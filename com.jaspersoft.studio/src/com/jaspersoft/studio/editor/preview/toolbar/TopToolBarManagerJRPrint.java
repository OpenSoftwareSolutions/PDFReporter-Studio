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

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.editor.preview.actions.SwitchViewsAction;
import com.jaspersoft.studio.editor.preview.view.AViewsFactory;

public class TopToolBarManagerJRPrint extends ATopToolBarManager {

	public TopToolBarManagerJRPrint(PreviewJRPrint container, Composite parent) {
		super(container, parent);

	}

	protected SwitchViewsAction pvModeAction;

	public IContributionItem[] getContributions() {
		if (tbManager == null)
			return new IContributionItem[0];
		return tbManager.getItems();
	}

	protected void fillToolbar(IToolBarManager tbManager) {
		if (pvModeAction == null) {
			AViewsFactory viewFactory = container.getViewFactory();
			pvModeAction = new SwitchViewsAction(container.getRightContainer(), viewFactory.getLabel(container
					.getDefaultViewerKey()), true, viewFactory);
		}
		tbManager.add(pvModeAction);

		tbManager.add(new Separator());
	}

	/**
	 * Set the text of the action
	 */
	public void setActionText(String text) {
		pvModeAction.setText(text);
	}
}
