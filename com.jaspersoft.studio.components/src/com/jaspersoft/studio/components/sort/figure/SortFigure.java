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
package com.jaspersoft.studio.components.sort.figure;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sf.jasperreports.components.sort.SortComponent;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.figures.JRComponentFigure;

/**
 * 
 * @author veaceslav chicu
 * 
 */
public class SortFigure extends JRComponentFigure {

	/**
	 * Instantiates a new map figure.
	 */
	public SortFigure() {
		super();
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D gr = ComponentFigure.getG2D(graphics);
		if (gr != null) {
			Color oldColor = gr.getColor();
			Font oldFont = gr.getFont();
			Shape oldClip = gr.getClip();

			AffineTransform af = gr.getTransform();

			try {
				Rectangle r = (this instanceof HandleBounds) ? ((HandleBounds) this)
						.getHandleBounds() : this.getBounds();
				// Graphics2D newGraphics = (Graphics2D) graphics2d.create(b.x,
				// b.y,
				// b.width, b.height);

				gr.translate(r.x, r.y);

				AffineTransform new_af = (AffineTransform) af.clone();
				AffineTransform translate = AffineTransform
						.getTranslateInstance(getInsets().left + r.x,
								getInsets().top + r.y);
				new_af.concatenate(translate);
				gr.setTransform(new_af);

				JRElement e = getJrElement();

				// Composite oldComposite = gr.getComposite();

				Shape rect = new Rectangle2D.Float(0, 0, e.getWidth(),
						e.getHeight());
				gr.clip(rect);

				if (gr.getBackground() != null
						&& e.getModeValue() == ModeEnum.OPAQUE) {
					gr.setColor(e.getBackcolor());
					gr.fillRect(0, 0, e.getWidth(), e.getHeight());
				}

				// Draw the small arrow in the center...

				SortComponent c = (SortComponent) ((JRDesignComponentElement) e)
						.getComponent();

				int size = 10;

				Font f = new java.awt.Font("Dialog", Font.PLAIN, size);

				gr.setFont(f);
				Rectangle2D stringBounds = gr.getFontMetrics().getStringBounds(
						"\u25B2", gr);

				Color col = Color.white;
				if (c.getHandlerColor() != null)
					col = c.getHandlerColor();

				gr.setColor(col);

				int x = 0;
				if (c.getHandlerHorizontalAlign() == null)
					x = 0;
				else if (c.getHandlerHorizontalAlign() == HorizontalAlignEnum.CENTER)
					x = (int) ((e.getWidth() - stringBounds.getWidth()) / 2);
				else if (c.getHandlerHorizontalAlign() == HorizontalAlignEnum.RIGHT)
					x = (int) (e.getWidth() - stringBounds.getWidth());

				int y = 0;
				if (c.getHandlerVerticalAlign() == null)
					y = (int) (stringBounds.getHeight());
				else if (c.getHandlerVerticalAlign() == VerticalAlignEnum.TOP)
					y = (int) (stringBounds.getHeight());
				else if (c.getHandlerVerticalAlign() == VerticalAlignEnum.MIDDLE)
					y = (int) ((e.getHeight() + stringBounds.getHeight()) / 2);
				else if (c.getHandlerVerticalAlign() == VerticalAlignEnum.BOTTOM)
					y = (int) (e.getHeight());

				gr.drawString("\u25B2", x, y);

			} catch (Exception ex) {
			} finally {
				gr.setTransform(af);
				gr.setColor(oldColor);
				gr.setFont(oldFont);
				gr.setClip(oldClip);
			}
			// super.paintWidgetImplementation();
		}
	}

}
