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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class LayoutCommand extends Command {
	private Map<JRElement, Rectangle> map = new HashMap<JRElement, Rectangle>();
	private ILayout layout;
	private JRElementGroup container;
	private Dimension size;

	public LayoutCommand(JRElementGroup container, ILayout layout, Dimension size) {
		super();
		this.size = size;
		this.layout = layout;
		this.container = container;
	}

	@Override
	public void execute() {
		if (layout != null && container != null)
			map = layout.layout(container.getElements(), size);
	}

	@Override
	public void undo() {
		for (JRElement el : map.keySet()) {
			Rectangle r = map.get(el);
			el.setX(r.x);
			((JRDesignElement) el).setY(r.y);
			el.setWidth(r.width);
			((JRDesignElement) el).setHeight(r.height);
		}
	}
}
