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
package com.jaspersoft.studio.editor.gef.parts.editPolicy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;

/**
 * Rectangle figure with colored border, used show a color feedback on some 
 * elements
 * @author Orlandin Marco
 *
 */
public class ColoredRectangle extends RectangleFigure{
	
	private Color borderColor;
	
	private float borderWidth;
	
	public ColoredRectangle(Color borderColor, float borderWidth){
		this.borderColor = borderColor;
		this.borderWidth = borderWidth;
	}
	
	protected void outlineShape(Graphics graphics) {
		Graphics2D g = ComponentFigure.getG2D(graphics);
		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);

		Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
		r.x += inset1;
		r.y += inset1;
		r.width -= inset1 + inset2;
		r.height -= inset1 + inset2;
		g.setStroke(new BasicStroke(borderWidth));
		g.setColor(borderColor);
		g.drawRect(r.x,r.y,r.width,r.height);
	}

}
