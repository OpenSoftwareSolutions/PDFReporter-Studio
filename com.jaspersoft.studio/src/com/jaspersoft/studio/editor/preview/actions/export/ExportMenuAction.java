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
package com.jaspersoft.studio.editor.preview.actions.export;

import net.sf.jasperreports.eclipse.viewer.IReportViewer;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.services.IDisposable;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;

public class ExportMenuAction extends AReportViewerAction implements IMenuCreator {

	private static final ImageDescriptor ICON = JaspersoftStudioPlugin.getInstance().getImageDescriptor(
			"icons/resources/save.GIF"); //$NON-NLS-1$
	private static final ImageDescriptor DISABLED_ICON = JaspersoftStudioPlugin.getInstance().getImageDescriptor(
			"icons/resources/save.GIF"); //$NON-NLS-1$

	private MenuManager menuManager = new MenuManager();
	private Menu menu;
	private IAction defaultAction;

	/**
	 * @see AReportViewerAction#AbstractReportViewerAction(IReportViewer)
	 */
	public ExportMenuAction(IReportViewer viewer) {
		super(viewer, AS_DROP_DOWN_MENU);

		setText(Messages.ExportMenuAction_title);
		setToolTipText(Messages.ExportMenuAction_tooltip);
		setImageDescriptor(ICON);
		setDisabledImageDescriptor(DISABLED_ICON);
		setMenuCreator(this);
	}

	@Override
	protected boolean calculateEnabled() {
		return getReportViewer().hasReport();
	}

	@Override
	public void run() {

		if (defaultAction != null && defaultAction.isEnabled())
			defaultAction.run();
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	@Override
	public void dispose() {
		for (IContributionItem item : menuManager.getItems())
			if (item instanceof ActionContributionItem && ((ActionContributionItem) item).getAction() instanceof IDisposable)
				((IDisposable) ((ActionContributionItem) item).getAction()).dispose();
		menuManager.dispose();
	}

	public Menu getMenu(Control parent) {
		if (menu == null)
			menu = menuManager.createContextMenu(parent);
		return menu;
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

	public IAction getDefaultAction() {
		return defaultAction;
	}

	public void setDefaultAction(IAction defaultAction) {
		if (this.defaultAction != null)
			this.defaultAction.setChecked(false);
		this.defaultAction = defaultAction;
		this.defaultAction.setChecked(true);
	}
}
