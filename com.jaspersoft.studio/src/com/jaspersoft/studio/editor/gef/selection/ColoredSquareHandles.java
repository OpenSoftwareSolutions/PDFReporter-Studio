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
package com.jaspersoft.studio.editor.gef.selection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.ResizeHandle;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MGraphicElement;

/**
 * Handle to color an design the figures on the border of a selection
 * 
 * @author Marco Orlandin
 * 
 */
public class ColoredSquareHandles extends ResizeHandle {
	/**
	 * The default size for square handles.
	 */
	protected static int JSS_HANDLE_SIZE = 8;

	/**
	 * The color of an element that cover entirely another element
	 */
	protected static Color[] JSS_OVERLAP_COLOR = null;

	/**
	 * The color of an element that overlap partially another element
	 */
	protected static Color[] JSS_COVER_COLOR = null;

	/**
	 * The color of a focused element
	 */
	protected static Color[] JSS_FOCUSED_COLOR = null;

	/**
	 * The color of a not focused element
	 */
	protected static Color[] JSS_NOT_FOCUSED_COLOR = null;

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
	public ColoredSquareHandles(GraphicalEditPart owner, int direction) {
		super(owner, direction);
	}

	/**
	 * Create a color array for border and gradient
	 * 
	 * @param baseColor
	 *          value from 0 to 1 that represent the base color H in HSB system
	 * @return an array of colors, different version of the base color
	 */
	private Color[] CreateColor(float baseColor) {
		Color[] result = { Color.getHSBColor(baseColor, 0.9f, 0.4f), Color.getHSBColor(baseColor, 0.8f, 0.2f),
				Color.getHSBColor(baseColor, 0.5f, 0.9f) };
		return result;
	}

	/**
	 * Initializes the handle.
	 */
	@Override
	protected void init() {
		super.init();
		setPreferredSize(new Dimension(JSS_HANDLE_SIZE, JSS_HANDLE_SIZE));
		// Initialize the color only once
		if (JSS_OVERLAP_COLOR == null) {
			float baseColor = new Float(Math.tan(Math.toRadians(120.0)));
			JSS_OVERLAP_COLOR = CreateColor(baseColor);
			baseColor = new Float(Math.tan(Math.toRadians(0.0)));
			JSS_COVER_COLOR = CreateColor(baseColor);
			baseColor = new Float(Math.tan(Math.toRadians(30.0)));
			JSS_FOCUSED_COLOR = CreateColor(baseColor);
			baseColor = new Float(Math.tan(Math.toRadians(0.0)));
			Color[] result = { Color.getHSBColor(baseColor, 0.2f, 0.3f), Color.getHSBColor(baseColor, 0.05f, 0.3f),
					Color.getHSBColor(baseColor, 0.05f, 0.6f) };
			JSS_NOT_FOCUSED_COLOR = result;
		}
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
				Color[] newColor = getFillColorAwt();
				gr.setColor(newColor[0]);
				gr.setStroke(new BasicStroke(1.0f));
				gr.drawRect(r.x - 1, r.y - 1, r.width + 1, r.height + 1);
				GradientPaint gp = new GradientPaint(r.x + r.width, r.y + r.height, newColor[1], r.x, r.y, newColor[2]);
				gr.setPaint(gp);
				gr.fillRect(r.x, r.y, r.width, r.height);
			}
		} finally {
			r.expand(1, 1);
		}
	}

	/**
	 * Returns the color for the inside of the handle.
	 * 
	 * @return the color of the handle
	 */
	protected Color[] getFillColorAwt() {
		ANode element = (ANode) getOwner().getModel();
		Rectangle bound1 = ((IGraphicElement) element).getBounds();
		int index1 = element.getParent().getChildren().indexOf(element);
		List<INode> brothers = element.getParent().getChildren();
		Iterator<INode> it = brothers.iterator();
		boolean overlap = false;
		boolean cover = false;
		while (it.hasNext() && !cover) {
			INode actualElement = it.next();
			if (!actualElement.equals(element) && actualElement instanceof MGraphicElement) {
				MGraphicElement element2 = (MGraphicElement) actualElement;
				int index2 = element.getParent().getChildren().indexOf(element2);
				Rectangle bound2 = element2.getBounds();
				if (bound1.intersects(bound2)) {
					overlap = true;
				}
				// Check if contains and the z-order
				if (bound1.contains(bound2) && index1 > index2) {
					cover = true;
				}
			}
		}
		if (cover)
			return JSS_COVER_COLOR;
		if (overlap)
			return JSS_OVERLAP_COLOR;
		return (isPrimary()) ? JSS_FOCUSED_COLOR : JSS_NOT_FOCUSED_COLOR;
	}

	/**
	 * Set the color of the selection resizing images when 2 or more elements are overlapped
	 * 
	 * @param baseColor
	 *          value in degree that represent the base color H in HSB system
	 */
	public void setOverlapColor(float newColor) {
		float baseColor = new Float(Math.tan(Math.toRadians(newColor)));
		JSS_OVERLAP_COLOR = CreateColor(baseColor);
	}

	/**
	 * Set the color of the selection resizing images when one element cover one or more elements
	 * 
	 * @param baseColor
	 *          value in degree that represent the base color H in HSB system
	 */
	public void setCoverColor(float newColor) {
		float baseColor = new Float(Math.tan(Math.toRadians(newColor)));
		JSS_COVER_COLOR = CreateColor(baseColor);
	}

	/**
	 * Set the color of the selection resizing images of a focused element
	 * 
	 * @param baseColor
	 *          value in degree that represent the base color H in HSB system
	 */
	public void setFocusedColor(float newColor) {
		float baseColor = new Float(Math.tan(Math.toRadians(newColor)));
		JSS_FOCUSED_COLOR = CreateColor(baseColor);
	}

	/**
	 * Set the color of the selection resizing images of a not focused element
	 * 
	 * @param baseColor
	 *          value in degree that represent the base color H in HSB system
	 */
	public void setNotFocusedColor(float newColor) {
		float baseColor = new Float(Math.tan(Math.toRadians(newColor)));
		Color[] result = { Color.getHSBColor(baseColor, 0.2f, 0.3f), Color.getHSBColor(baseColor, 0.05f, 0.3f),
				Color.getHSBColor(baseColor, 0.05f, 0.6f) };
		JSS_NOT_FOCUSED_COLOR = result;
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
