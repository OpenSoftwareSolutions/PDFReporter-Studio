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
package com.jaspersoft.studio.editor.gef.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.utils.Misc;

public class FigureTextWriter {
	private String text;
	private boolean showName;
	private Point namePosition;
	private Dimension nameSize;

	public void setShowName(boolean showName) {
		this.showName = showName;
	}

	public void setText(String bandText) {
		this.text = bandText;
		// reset the band name position and size
		this.namePosition = null;
		this.nameSize = null;
	}

	/*
	 * Computes the position for the band name text and position.
	 */
	private void computeNamePositionAndSize(Graphics2D g, Rectangle clientArea) {
		if (namePosition == null || !oldca.equals(clientArea)) {
			FontRenderContext frc = g.getFontRenderContext();
			Font currfont = g.getFont();
			float sw = (float) currfont.getStringBounds(text, frc).getWidth();
			LineMetrics lm = currfont.getLineMetrics(text, frc);
			float sh = lm.getHeight();
			Rectangle tmpRect = clientArea;
			float sx = tmpRect.x + (tmpRect.width - sw) / 2;
			float sy = tmpRect.y + (tmpRect.height + sh) / 2 - lm.getDescent();
			namePosition = new Point((int) sx, (int) sy);
			nameSize = new Dimension((int) sw, (int) sh);
		}
		oldca = clientArea;
	}

	private Rectangle oldca;

	public void painText(Graphics2D g, IFigure figure) {
		if (!Misc.nvl(text).isEmpty() && showName) {
			Font currfont = g.getFont();
			if (currfont.getSize() != 16f)
				g.setFont(currfont.deriveFont(16f));

			computeNamePositionAndSize(g, figure.getClientArea());
			if (nameSize.height < figure.getBounds().height) {
				java.awt.Color currColor = g.getColor();
				g.setColor(java.awt.Color.GRAY);
				g.drawString(text, namePosition.x, namePosition.y);
				g.setColor(currColor);
			}
			g.setFont(currfont);
		}
	}
}
