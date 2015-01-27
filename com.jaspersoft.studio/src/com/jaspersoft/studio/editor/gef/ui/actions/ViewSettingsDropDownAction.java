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
package com.jaspersoft.studio.editor.gef.ui.actions;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.action.snap.SizeGridAction;
import com.jaspersoft.studio.editor.action.snap.SnapToGridAction;
import com.jaspersoft.studio.editor.action.snap.SnapToGuidesAction;
import com.jaspersoft.studio.messages.Messages;

/**
 * Dropdown action to contribute a settings menu with the menu items taken from the global View menu.
 * <p>
 * 
 * The following items currently compose the menu:
 * <ul>
 * <li>Rulers</li>
 * <li>Snap To Guides</li>
 * <li>Show Grid</li>
 * <li>Snap To Grid</li>
 * <li>Snap To Geometry</li>
 * <li>Grid Size...</li>
 * <li>Decorating contributions</li>
 * </ul>
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 */
public class ViewSettingsDropDownAction extends Action implements IMenuCreator {
	private Menu menu;
	private ActionRegistry actionRegistry;

	public ViewSettingsDropDownAction(ActionRegistry actionRegistry) {
		setText(Messages.ViewSettingsDropDownAction_settingsName);
		setMenuCreator(this);
		this.actionRegistry = actionRegistry;
	}

	@Override
	public void dispose() {
		if (menu != null) {
			menu.dispose();
			menu = null;
		}
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}

	@Override
	public Menu getMenu(Control parent) {
		if (menu != null) {
			menu.dispose();
		}
		menu = new Menu(parent);
		addActionToMenu(menu, actionRegistry.getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
		addActionToMenu(menu, actionRegistry.getAction(SnapToGuidesAction.ID));
		new MenuItem(menu, SWT.SEPARATOR);
		addActionToMenu(menu, actionRegistry.getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		addActionToMenu(menu, actionRegistry.getAction(SnapToGridAction.ID));
		addActionToMenu(menu, actionRegistry.getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
		addActionToMenu(menu, actionRegistry.getAction(SizeGridAction.ID));
		new MenuItem(menu, SWT.SEPARATOR);
		for (String id : JaspersoftStudioPlugin.getDecoratorManager().getActionIDs())
			addActionToMenu(menu, actionRegistry.getAction(id));
		new MenuItem(menu, SWT.SEPARATOR);
		for (String id : JaspersoftStudioPlugin.getEditorSettingsManager().getActionIDs())
			addActionToMenu(menu, actionRegistry.getAction(id));
		return menu;
	}

	/*
	 * Adds an item to the existing menu using, using the contributed action.
	 */
	private void addActionToMenu(Menu parent, IAction action) {
		//If the action is a menu creator the create it as submenu
		if (action instanceof IMenuCreator){
			IMenuCreator creator = (IMenuCreator) action;
			creator.getMenu(parent);
		} else {
			ActionContributionItem item = new ActionContributionItem(action);
			item.fill(parent, -1);
		}
	}

	@Override
	public void run() {
		// Do Nothing
	}
}
