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
package com.jaspersoft.studio.editor.gef.figures;

import net.sf.jasperreports.engine.JRElement;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public abstract class AElementBoundsFigure extends AHandleBoundsFigure {
	@Override
	protected void paintBorder(Graphics graphics) {
		if (getBorder() != null) {
			Figure f = new Figure();
			f.setBounds(getHandleBounds());
			getBorder().paint(f, graphics, NO_INSETS);
		}
	}

	public Rectangle getHandleBounds() {
		Rectangle b = getBounds();
		JRElement jrElement = getJrElement();
		return new Rectangle(b.x, b.y, jrElement.getWidth() + 1, jrElement.getHeight() + 1);
	}
}
