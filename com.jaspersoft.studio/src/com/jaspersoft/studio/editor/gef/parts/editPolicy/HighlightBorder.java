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
package com.jaspersoft.studio.editor.gef.parts.editPolicy;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;

/**
 * Print an internal border for the figure, half-transparent
 * 
 * @author Orlandin Marco
 * 
 */
public class HighlightBorder extends LineBorder {
	public HighlightBorder(Color color, int width) {
		super(color, width);
	}

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Graphics2D g = ComponentFigure.getG2D(graphics);
		if (g != null) {
			tempRect.setBounds(getPaintRectangle(figure, insets));
			if (getWidth() % 2 == 1) {
				tempRect.width--;
				tempRect.height--;
			}
			tempRect.width = tempRect.width - getWidth();
			tempRect.height = tempRect.height - getWidth();
			tempRect.shrink(getWidth() / 2, getWidth() / 2);
			g.setStroke(new BasicStroke(getWidth()));
			if (getColor() != null) {
				RGB colorRGB = getColor().getRGB();
				g.setColor(new java.awt.Color(colorRGB.red, colorRGB.green, colorRGB.blue, 128));
			}
			g.drawRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);
		}
	}
}
