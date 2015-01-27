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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.RetargetAction;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;

public class DecoratorManager {
	public void init() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "decorators"); //$NON-NLS-1$  
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IElementDecorator)
					nodeFactory.add((IElementDecorator) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private List<IElementDecorator> nodeFactory = new ArrayList<IElementDecorator>();

	public void setupFigure(ComponentFigure fig, FigureEditPart editPart) {
		for (IElementDecorator f : nodeFactory)
			f.setupFigure(fig, editPart);
	}

	public void registerActions(ActionRegistry registry, List<String> selectionActions, GraphicalViewer gviewer,
			AbstractVisualEditor part) {
		for (IElementDecorator f : nodeFactory)
			f.registerActions(registry, selectionActions, gviewer, part);
	}

	public void buildContextMenu(ActionRegistry registry, EditPartViewer viewer, IMenuManager menu) {
		for (IElementDecorator f : nodeFactory)
			f.buildContextMenu(registry, viewer, menu);
	}

	public List<RetargetAction> buildMenuActions() {
		List<RetargetAction> actions = new ArrayList<RetargetAction>();
		for (IElementDecorator f : nodeFactory)
			actions.addAll(Arrays.asList(f.buildMenuActions()));
		return actions;
	}

	public void contribute2Menu(ActionRegistry registry, MenuManager menuManager) {
		for (IElementDecorator f : nodeFactory)
			f.contribute2Menu(registry, menuManager);
	}

	public List<String> getActionIDs() {
		List<String> ids = new ArrayList<String>();
		for (IElementDecorator f : nodeFactory) {
			ids.addAll(f.getActionIDs());
		}
		return ids;
	}
}
