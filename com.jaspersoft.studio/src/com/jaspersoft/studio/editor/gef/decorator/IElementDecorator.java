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
package com.jaspersoft.studio.editor.gef.decorator;

import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.RetargetAction;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;

/**
 * @author slavic
 * 
 */
public interface IElementDecorator {
	/**
	 * method to setup decorator on setup figure in edit part, when figure parameters are changed
	 * 
	 * @param fig
	 * @param jConfig
	 */
	public void setupFigure(ComponentFigure fig, FigureEditPart editPart);

	/**
	 * register and create all decorator actions in editors ActionRegistry
	 * 
	 * @param registry
	 * @param selectionActions
	 * @param gviewer
	 * @param part
	 */
	public void registerActions(ActionRegistry registry, List<String> selectionActions, GraphicalViewer gviewer,
			AbstractVisualEditor part);

	/**
	 * decide and add action to context menu, right click on an element
	 * 
	 * @param registry
	 * @param viewer
	 * @param menu
	 */
	public void buildContextMenu(ActionRegistry registry, EditPartViewer viewer, IMenuManager menu);

	/**
	 * create RetargetActions that we show in global menu
	 * 
	 * @return
	 */
	public RetargetAction[] buildMenuActions();

	/**
	 * add actions to global menu
	 * 
	 * @param registry
	 * @param menuManager
	 */
	public void contribute2Menu(ActionRegistry registry, MenuManager menuManager);
	
	/**
	 * Returns the list of Action IDs.
	 * 
	 * @return list of id
	 */
	public List<String> getActionIDs();
	
}
