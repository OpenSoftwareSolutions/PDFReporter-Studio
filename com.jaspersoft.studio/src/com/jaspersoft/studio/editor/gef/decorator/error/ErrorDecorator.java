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
package com.jaspersoft.studio.editor.gef.decorator.error;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.gef.decorator.IDecorator;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.messages.Messages;

/**
 * Design a border and a warning icon on an element that is out of bounds
 * 
 * @author Orlandin Marco
 * 
 */
public class ErrorDecorator implements IDecorator {

	/**
	 * The color of the warning border
	 */
	private static Color JSS_WARNING_BORDER_COLOR = new Color(255, 0, 0, 128);

	/**
	 * The size of the warning border
	 */
	private static float JSS_WARNING_BORDER_SIZE = 1.0f;

	/**
	 * Standard constructor, load the warningIcon
	 */
	public ErrorDecorator() {

	}

	/**
	 * Print the warning icon when an element is out of bound and set the tooltip text.
	 */
	@Override
	public void paint(Graphics graphics, ComponentFigure fig) {
		if (fig.getJrElement() instanceof JRDesignElement) {
			Rectangle r = fig.getBounds();
			Graphics2D g = ComponentFigure.getG2D(graphics);
			if (g != null) {
				Stroke oldStroke = g.getStroke();
				Color oldColor = g.getColor();
				g.setColor(JSS_WARNING_BORDER_COLOR);
				g.setStroke(new BasicStroke(JSS_WARNING_BORDER_SIZE));

				PrecisionRectangle tempRect = new PrecisionRectangle();
				tempRect.setBounds(fig.getBounds());
				if (JSS_WARNING_BORDER_SIZE % 2 == 1) {
					tempRect.width--;
					tempRect.height--;
				}
				tempRect.width = tempRect.width - (int) Math.ceil(JSS_WARNING_BORDER_SIZE);
				tempRect.height = tempRect.height - (int) Math.ceil(JSS_WARNING_BORDER_SIZE);
				tempRect.shrink(JSS_WARNING_BORDER_SIZE, JSS_WARNING_BORDER_SIZE);
				g.setStroke(new BasicStroke(JSS_WARNING_BORDER_SIZE));
				g.drawRect(tempRect.x, tempRect.y, tempRect.width, tempRect.height);

				RoundedPolygon.paintComplexWarning(r.x + r.width - 5, r.y - 2, 6, 12, JSS_WARNING_BORDER_SIZE, g);
				fig.setToolTip(new org.eclipse.draw2d.Label(Messages.ErrorDecorator_PositionErrorToolTip, ResourceManager
						.getPluginImage(JaspersoftStudioPlugin.PLUGIN_ID, "icons/resources/warning2.png"))); //$NON-NLS-2$
				g.setStroke(oldStroke);
				g.setColor(oldColor);
			}
		}
	}

	/**
	 * Set the color of the border of an element when it is out of bounds
	 * 
	 * @param newColor
	 *          the new color
	 */
	public void setBorderColor(Color newColor) {
		JSS_WARNING_BORDER_COLOR = newColor;
	}

	/**
	 * Set the size of the border of an element when it is out of bounds
	 * 
	 * @param newSize
	 *          the new size
	 */
	public void setBorderSize(float newSize) {
		JSS_WARNING_BORDER_SIZE = newSize;
	}

	@Override
	public boolean equals(Object obj) {
		return this.getClass().equals(obj.getClass());
	};

}
