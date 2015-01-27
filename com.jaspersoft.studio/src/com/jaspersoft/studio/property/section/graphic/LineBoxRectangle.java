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
package com.jaspersoft.studio.property.section.graphic;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBasePrintText;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.model.ILineBox;
import com.jaspersoft.studio.model.style.MStyle;

/**
 * Widget that contains the method to represent the border of an element and permit to view or edit their properties
 * 
 * @author Marco Orlandin
 * 
 */
public class LineBoxRectangle extends RectangleFigure {

	/**
	 * The drawer of the border
	 */
	private LineBoxDrawer bd;

	/**
	 * The section of the element
	 */
	private BordersSection section;

	public LineBoxRectangle(LineBoxDrawer drawer, BordersSection section) {
		bd = drawer;
		this.section = section;
	}

	/**
	 * Request the painting of the border
	 */
	@Override
	public void paint(Graphics graphics) {
		try {
			Graphics2D g = ComponentFigure.getG2D(graphics);
			if (g != null) {

				Rectangle b = getBounds();

				JRPrintElement pe = new JRBasePrintText(null);
				pe.setX(b.x + 10);
				pe.setY(b.y + 10);
				pe.setWidth(b.width - 20);
				pe.setHeight(b.height - 20);
				if (section.getElement() instanceof ILineBox && section.getElement() != null)
					bd.drawBox(g, ((ILineBox) section.getElement()).getBoxContainer().getLineBox(), pe);
				else if (section.getElement() instanceof MStyle) {
					MStyle styleModel = (MStyle) section.getElement();
					bd.drawBox(g, ((JRStyle) styleModel.getValue()).getLineBox(), pe);
				}
			} else {
				graphics.drawRectangle(0, 0, 100, 100);
			}
		} catch (Exception e) {
			// when a font is missing exception is thrown by DrawVisitor
			// FIXME: maybe draw something, else?
			e.printStackTrace();
		}
	}
}
