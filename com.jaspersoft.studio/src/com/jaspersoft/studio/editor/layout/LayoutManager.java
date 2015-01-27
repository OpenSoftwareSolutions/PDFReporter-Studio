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
package com.jaspersoft.studio.editor.layout;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.editor.action.layout.LayoutAction;

public class LayoutManager {
	public static ILayout getLayout(JRPropertiesHolder[] elements, JasperDesign jDesign, String uuid) {
		for (JRPropertiesHolder pholder : elements) {
			if (pholder == null || pholder.getPropertiesMap() == null)
				continue;
			String prop = pholder.getPropertiesMap().getProperty(ILayout.KEY);
			if (prop != null)
				return instLayout(prop);
		}
		if (uuid != null && jDesign != null) {
			String prop = jDesign.getPropertiesMap().getProperty(ILayout.KEY + "." + uuid);
			if (prop != null)
				return instLayout(prop);
		}
		return new FreeLayout();
	}

	public static ILayout instLayout(String prop) {
		try {
			return (ILayout) Class.forName(prop).newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		}
		return new FreeLayout();
	}

	private static final Class<?>[] layouts = new Class<?>[] { HorizontalRowLayout.class, VerticalRowLayout.class };
	private static ILayout[] LAYOUTNAMES;

	public static void addActions(ActionRegistry registry, IWorkbenchPart part, List<String> selectionActions) {
		for (Class<?> id : layouts) {
			IAction action = new LayoutAction(part, id);
			registry.registerAction(action);
			selectionActions.add(action.getId());
		}
	}

	public static void addMenu(MenuManager submenu, ActionRegistry actionRegistry) {
		for (Class<?> id : layouts) {
			IAction action = actionRegistry.getAction(id.getName());
			if (action.isEnabled())
				submenu.add(action);
		}
	}

	public static ILayout[] getAllLayouts() {
		if (LAYOUTNAMES == null)
			LAYOUTNAMES = new ILayout[] { new FreeLayout(), new HorizontalRowLayout(), new VerticalRowLayout() };
		return LAYOUTNAMES;
	}

	public static Map<JRElement, Rectangle> layout(Map<JRElement, Rectangle> map, JRElement el) {
		if (el instanceof JRElementGroup && el instanceof JRPropertiesHolder) {
			Dimension d = null;
			if (el instanceof JRCommonElement) {
				JRCommonElement jce = (JRCommonElement) el;
				d = new Dimension(jce.getWidth(), jce.getHeight());
			}
			ILayout layout = LayoutManager.getLayout(new JRPropertiesHolder[] { el }, null, null);
			layout.layout(((JRElementGroup) el).getElements(), d);
		}
		return map;
	}
}
