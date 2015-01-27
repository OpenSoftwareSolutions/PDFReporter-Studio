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
package com.jaspersoft.studio.callout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.swt.graphics.Cursor;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.utils.Colors;

/**
 * Handle to color an design the figures on the border of a selection
 * 
 * @author Marco Orlandin
 * 
 */
public class SothEastRectangleHandles extends ResizeHandle {
	/**
	 * The default size for square handles.
	 */
	protected static int JSS_HANDLE_SIZE = 24;
	private Color bcolor;

	/**
	 * Creates a new ResizeHandle for the given GraphicalEditPart.
	 * 
	 * @see SquareHandle#SquareHandle(GraphicalEditPart, Locator, Cursor)
	 */
	public SothEastRectangleHandles(GraphicalEditPart owner, Locator loc, Cursor c) {
		super(owner, loc, c);
	}

	/**
	 * Creates a new ResizeHandle for the given GraphicalEditPart. <code>direction</code> is the relative direction from
	 * the center of the owner figure. For example, <code>SOUTH_EAST</code> would place the handle in the lower-right
	 * corner of its owner figure. These direction constants can be found in {@link org.eclipse.draw2d.PositionConstants}.
	 * 
	 * @param owner
	 *          owner of the ResizeHandle
	 * @param direction
	 *          relative direction from the center of the owner figure
	 */
	public SothEastRectangleHandles(GraphicalEditPart owner, int direction) {
		super(owner, direction);
	}

	/**
	 * Initializes the handle.
	 */
	@Override
	protected void init() {
		super.init();
		setPreferredSize(new Dimension(JSS_HANDLE_SIZE, JSS_HANDLE_SIZE));
	}

	/**
	 * Paint the element with the selected color
	 */
	@Override
	public void paintFigure(Graphics g) {
		Rectangle r = getBounds();
		r.shrink(1, 1);
		try {
			Graphics2D gr = ComponentFigure.getG2D(g);
			if (g != null) {
				if (bcolor == null)
					bcolor = Colors.getAWT4SWTColor(((LineBorder) getOwnerFigure().getBorder()).getColor());
				gr.setColor(bcolor);
				gr.setStroke(new BasicStroke(1.0f));

				// GradientPaint gp = new GradientPaint(r.x + r.width, r.y + r.height, newColor[1], r.x, r.y, newColor[2]);
				// gr.setPaint(gp);

				Polygon p = new Polygon();
				p.addPoint(r.x, r.y + r.height / 2);
				p.addPoint(r.x + r.width / 2, r.y);
				p.addPoint(r.x + r.width / 2, r.y + r.height / 2);
				gr.fillPolygon(p);

				gr.setColor(Colors.getAWT4SWTColor(getForegroundColor()));
				gr.drawLine(r.x, r.y + r.height / 2, r.x + r.width / 2, r.y);

				// gr.drawRect(r.x - 1, r.y - 1, r.width + 1, r.height + 1);
				// GradientPaint gp = new GradientPaint(r.x + r.width, r.y + r.height, newColor[1], r.x, r.y, newColor[2]);
				// gr.setPaint(gp);
				// gr.fillRect(r.x, r.y, r.width, r.height);
			}
		} finally {
			r.expand(1, 1);
		}
	}

	/**
	 * Size of the designed resizing images
	 * 
	 * @param newSize
	 *          the new size in pixel
	 */
	public void setSize(int newSize) {
		JSS_HANDLE_SIZE = newSize;
	}

}
