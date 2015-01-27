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
package com.jaspersoft.studio.editor.gef.figures.layers;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.java2d.J2DUtils;

public class GridLayer extends org.eclipse.gef.editparts.GridLayer {
	public GridLayer() {
		super();
		setForegroundColor(ColorConstants.lightGray);
	}

	@Override
	protected void paintGrid(Graphics graphics) {
		Graphics2D g = ComponentFigure.getG2D(graphics);
		if (g != null) {
			Stroke oldStroke = g.getStroke();

			g.setStroke(new BasicStroke(0.1f));
			Rectangle clip = getBounds();// g.getClip(Rectangle.SINGLETON);
			if (gridX > 0) {
				if (origin.x >= clip.x)
					while (origin.x - gridX >= clip.x)
						origin.x -= gridX;
				else
					while (origin.x < clip.x)
						origin.x += gridX;
				int j = 0;
				for (int i = origin.x; i < clip.x + clip.width; i += gridX) {
					if (j == 10) {
						g.setStroke(new BasicStroke(0.5f));
						j = 0;
					} else
						g.setStroke(new BasicStroke(0.1f));

					g.setStroke(J2DUtils.getInvertedZoomedStroke(g.getStroke(), graphics.getAbsoluteScale()));
					graphics.drawLine(i, clip.y, i, clip.y + clip.height);
					j++;
				}
			}
			if (gridY > 0) {
				if (origin.y >= clip.y)
					while (origin.y - gridY >= clip.y)
						origin.y -= gridY;
				else
					while (origin.y < clip.y)
						origin.y += gridY;
				int j = 0;
				for (int i = origin.y; i < clip.y + clip.height; i += gridY) {
					if (j == 10) {
						g.setStroke(new BasicStroke(0.5f));
						j = 0;
					} else
						g.setStroke(new BasicStroke(0.1f));

					g.setStroke(J2DUtils.getInvertedZoomedStroke(g.getStroke(), graphics.getAbsoluteScale()));
					g.drawLine(clip.x, i, clip.x + clip.width, i);
					j++;
				}
			}
			g.setStroke(oldStroke);
		}
	}

}
