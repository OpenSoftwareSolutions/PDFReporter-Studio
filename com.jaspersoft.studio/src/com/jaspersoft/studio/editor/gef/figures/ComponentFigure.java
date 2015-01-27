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
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElement;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

import com.jaspersoft.studio.editor.gef.decorator.IDecorator;
import com.jaspersoft.studio.editor.java2d.J2DGraphics;
import com.jaspersoft.studio.editor.java2d.J2DScaledGraphics;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;

/*
 * The Class GenericFigure.
 */
public class ComponentFigure extends RectangleFigure {

	/** The jr element. */
	protected JRElement jrElement;

	/** The draw visitor. */
	protected JSSDrawVisitor drawVisitor;

	/**
	 * Instantiates a new generic figure.
	 */
	public ComponentFigure() {
		super();
		setLayoutManager(new FreeformLayout());
	}

	/**
	 * Sets the jr element.
	 * 
	 * @param jrElement
	 *          the jr element
	 * @param drawVisitor
	 *          the draw visitor
	 */
	public void setJRElement(JRElement jrElement, JSSDrawVisitor drawVisitor) {
		this.drawVisitor = drawVisitor;
		this.jrElement = jrElement;
		if (jrElement != null)
			setSize(jrElement.getWidth(), jrElement.getHeight());
	}

	public static Graphics2D getG2D(Graphics graphics) {
		Graphics2D graphics2d = null;
		if (graphics instanceof J2DGraphics)
			graphics2d = ((J2DGraphics) graphics).getGraphics2D();
		else if (graphics instanceof J2DScaledGraphics)
			graphics2d = ((J2DScaledGraphics) graphics).getGraphics2D();
		return graphics2d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paint(Graphics graphics) {
		Rectangle b = (this instanceof HandleBounds) ? ((HandleBounds) this).getHandleBounds() : this.getBounds();
		try {
			graphics.translate(b.x, b.y);
			Graphics2D graphics2d = getG2D(graphics);
			if (graphics2d != null) {
				if (drawVisitor != null) {
					drawVisitor.setGraphics2D(graphics2d);

					draw(drawVisitor, jrElement);
				} else
					graphics2d.drawRect(b.x, b.y, b.width, b.height);
			} else {
				System.out.println("not a 2d");
			}
		} catch (Exception e) {
			// when a font is missing exception is thrown by DrawVisitor
			// FIXME: maybe draw something, else?
			e.printStackTrace();
		} finally {
			graphics.translate(-b.x, -b.y);
		}
		paintBorder(graphics);
		paintDecorators(graphics);
	}

	/**
	 * Draw.
	 * 
	 * @param drawVisitor
	 *          the draw visitor
	 * @param jrElement
	 *          the jr element
	 */
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		if (jrElement instanceof JRComponentElement)
			drawVisitor.visitComponentElement((JRComponentElement) jrElement);
	}

	/**
	 * Gets the jr element.
	 * 
	 * @return the jr element
	 */
	public JRElement getJrElement() {
		return jrElement;
	}

	private void paintDecorators(Graphics graphics) {
		if (decorators == null)
			return;
		for (IDecorator d : decorators)
			d.paint(graphics, this);
	}

	private List<IDecorator> decorators;

	public void addDecorator(IDecorator decorator) {
		if (decorators == null)
			decorators = new ArrayList<IDecorator>();
		decorators.add(decorator);
	}

	/**
	 * Add a decorator to the decorators list, but only if it isn't already present
	 * 
	 * @param decorator
	 *          the decorator to add
	 */
	public void addDecoratorOnce(IDecorator decorator) {
		if (decorators == null)
			decorators = new ArrayList<IDecorator>();
		if (!decorators.contains(decorator))
			decorators.add(decorator);
	}

	public void removeDecorator(IDecorator decorator) {
		if (decorators != null)
			decorators.remove(decorator);
	}
}
