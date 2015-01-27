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

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRLineBox;

import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.java2d.StackGraphics2D;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.model.MGraphicElement;

/*
 * The Class FrameFigure.
 * 
 * @author Chicu Veaceslav
 */
public class FrameFigure extends AHandleBoundsFigure {
	
	/**
	 * The model associated with the figure
	 */
	protected MGraphicElement model = null;
	
	protected StackGraphics2D cachedGraphics = null;
	
	/**
	 * Instantiates a new text field figure.
	 */
	public FrameFigure(MGraphicElement frameModel) {
		super();
		this.model = frameModel;
		setLayoutManager(new XYLayout());
	}

	
	public FrameFigure(){
		super();
		this.model = null;
		setLayoutManager(new XYLayout());
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
		if (model == null){
			drawVisitor.visitFrame((JRFrame) jrElement);
			return;
		} else if (cachedGraphics == null || model.hasChangedProperty()){
			model.setChangedProperty(false);
			Graphics2D oldGraphics = drawVisitor.getGraphics2d();
			cachedGraphics = new StackGraphics2D(oldGraphics);
			drawVisitor.setGraphics2D(cachedGraphics);
			drawVisitor.visitFrame((JRFrame) jrElement);
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
		Rectangle o = calcBorder(getLineBox());
		Rectangle b = getBounds();
		setBounds(new Rectangle(b.x - o.x, b.y - o.y, b.width + o.x + o.width + 1, b.height + o.y + o.height + 1));
	}

	/**
	 * Gets the line box.
	 * 
	 * @return the line box
	 */
	protected JRLineBox getLineBox() {
		JRElement jrElement = getJrElement();
		JRLineBox box = null;
		if (jrElement != null) {
			if (jrElement instanceof JRBoxContainer)
				box = ((JRBoxContainer) jrElement).getLineBox();
			if (box == null && jrElement.getStyle() != null)
				box = jrElement.getStyle().getLineBox();
		} else {
			box = null;
		}
		return box;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.editor.gef.figures.GenericFigure#getHandleBounds()
	 */
	@Override
	public Rectangle getHandleBounds() {
		Rectangle b = getBounds();
		Rectangle o = calcBorder(getLineBox());
		return new Rectangle(b.x + o.x, b.y + o.y, getElementWidth() + 1, getElementHeight() + 1);
	}

	protected int getElementWidth() {
		return getJrElement() != null ? getJrElement().getWidth() : 0;
	}

	protected int getElementHeight() {
		return getJrElement() != null ? getJrElement().getHeight() : 0;
	}

	/**
	 * Calc border.
	 * 
	 * @param jrLineBox
	 *          the jr line box
	 * @return the rectangle
	 */
	protected Rectangle calcBorder(JRLineBox jrLineBox) {
		if (jrLineBox == null)
			return new Rectangle(0, 0, 0, 0);
		int x = (int) Math.ceil(jrLineBox.getLeftPen().getLineWidth() / 2);
		int y = (int) Math.ceil(jrLineBox.getTopPen().getLineWidth() / 2);
		int w = (int) Math.ceil(jrLineBox.getRightPen().getLineWidth() / 2) + 1;
		int h = (int) Math.ceil(jrLineBox.getBottomPen().getLineWidth() / 2) + 1;
		return new Rectangle(x, y, w, h);
	}
	
	/**
	 * Return the model associated to this figure, can be null
	 */
	public MGraphicElement getModel(){
		return model;
	}
}
