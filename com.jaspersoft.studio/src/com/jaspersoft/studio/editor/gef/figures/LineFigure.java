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

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPen;

import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.java2d.StackGraphics2D;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.model.MGraphicElement;

/*
 * The Class LineFigure.
 */
public class LineFigure extends AHandleBoundsFigure {

	protected MGraphicElement model = null;
	
	protected StackGraphics2D cachedGraphics = null;
	
	/**
	 * Instantiates a new line figure.
	 */
	public LineFigure(MGraphicElement lineModel) {
		super();
		this.model = lineModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jaspersoft.studio.editor.gef.figures.GenericFigure#draw(net.sf.jasperreports.engine.export.draw.DrawVisitor,
	 * net.sf.jasperreports.engine.JRElement)
	 */
	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		if (cachedGraphics == null || model.hasChangedProperty()){
			model.setChangedProperty(false);
			Graphics2D oldGraphics = drawVisitor.getGraphics2d();
			cachedGraphics = new StackGraphics2D(oldGraphics);
			drawVisitor.setGraphics2D(cachedGraphics);
			drawVisitor.visitLine((JRLine) jrElement);
			drawVisitor.setGraphics2D(oldGraphics);
		}
		cachedGraphics.setRealDrawer(drawVisitor.getGraphics2d());
		cachedGraphics.paintStack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.editor.gef.figures.GenericFigure#setJRElement(net.sf.jasperreports.engine.JRElement,
	 * net.sf.jasperreports.engine.export.draw.DrawVisitor)
	 */
	@Override
	public void setJRElement(JRElement jrElement, JSSDrawVisitor drawVisitor) {
		super.setJRElement(jrElement, drawVisitor);
		// set bounds + 1/2 border
		int offset = calcBorder(getLinePen());
		Rectangle b = getBounds();
		setBounds(new Rectangle(b.x - offset, b.y - offset, b.width + offset * 2 + 1, b.height + offset * 2 + 1));
	}

	/**
	 * Gets the line pen.
	 * 
	 * @return the line pen
	 */
	protected JRPen getLinePen() {
		JRCommonGraphicElement jrElement = (JRCommonGraphicElement) getJrElement();
		if (jrElement == null)
			return null;
		return jrElement.getLinePen();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.editor.gef.figures.GenericFigure#getHandleBounds()
	 */
	@Override
	public Rectangle getHandleBounds() {
		Rectangle b = getBounds();
		int offset = calcBorder(getLinePen());
		JRElement jrElement = getJrElement();
		if (jrElement == null)
			return new Rectangle(b.x + offset, b.y + offset, 0, 0);
		return new Rectangle(b.x + offset, b.y + offset, jrElement.getWidth() + 1, jrElement.getHeight() + 1);
	}

	/**
	 * Calc border.
	 * 
	 * @param jrPen
	 *          the jr pen
	 * @return the int
	 */
	protected int calcBorder(JRPen jrPen) {
		if (jrPen == null)
			return 0;
		int offset = (int) Math.ceil(((double) jrPen.getLineWidth()) / 2)+1;
		return offset;
	}
}
