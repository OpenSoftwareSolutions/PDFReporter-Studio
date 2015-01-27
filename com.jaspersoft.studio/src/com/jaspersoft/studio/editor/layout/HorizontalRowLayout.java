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
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.messages.Messages;

public class HorizontalRowLayout implements ILayout {
	public Map<JRElement, Rectangle> layout(JRElement[] elements, Dimension c) {
		Map<JRElement, Rectangle> map = new HashMap<JRElement, Rectangle>();
		int x = 0;
		int y = 0;
		int w = (int) Math.floor((float) c.width / elements.length);
		int rest = c.width - w * elements.length;
		int h = c.height;
		for (JRElement el : elements) {
			JRDesignElement del = (JRDesignElement) el;
			map.put(el, new Rectangle(el.getX(), el.getY(), el.getWidth(), el.getHeight()));
			del.setX(x);
			del.setY(y);
			del.setWidth(w + rest);
			del.setHeight(h);
			// if last grab free pixels
			x += w + rest;
			if (rest > 0)
				rest = 0;
			LayoutManager.layout(map, el);
		}
		return map;
	}

	@Override
	public String getName() {
		return Messages.HorizontalRowLayout_name;
	}

	@Override
	public String getToolTip() {
		return Messages.HorizontalRowLayout_toolTip;
	}

	@Override
	public String getIcon() {
		return "icons/layout-3.png"; //$NON-NLS-1$
	}
}
