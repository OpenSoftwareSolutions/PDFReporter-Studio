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
package com.jaspersoft.studio.components.list.figure;

import java.awt.Color;
import java.awt.Graphics2D;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.components.list.model.MList;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.figures.JRComponentFigure;

/**
 * @author Chicu Veaceslav & Orlandin Marco
 * @version $Id$
 * 
 * This class print a list element into the designer, it's essentially a rectangle, but there is the 
 * possibility to print the line of the cell width and height (that can be different from the one of the element.
 * The cell size is printed only if it is differemnt form the size of the element
 */
public class ListFigure extends JRComponentFigure {

	/**
	 * Height of the cell
	 */
	private int cellHeight;
	
	/**
	 * Width of the cel
	 */
	private int cellWidth;
	
	/**
	 * Set the height of the cell
	 * @param cellHeight the new height
	 */
	public void setCellHeight(int cellHeight){
		this.cellHeight = cellHeight;
	}
	
	/**
	 * Set the width of the cell
	 * @param cellWidth the new width
	 */
	public void setCellWidth(int cellWidth){
		this.cellWidth = cellWidth;
	}
	
	/**
	 * Instantiates a new text field figure.
	 */
	public ListFigure(MList model) {
		super(model);
	}
	
	/**
	 * Print the element and the cell lines, but only if they are different from the size of the element
	 */
	public void paint(Graphics graphics) {
		super.paint(graphics);
		Rectangle b = getBounds();
		Graphics2D g = ComponentFigure.getG2D(graphics);
		if (g != null) {
			Color oldColor = g.getColor();
			g.setColor(Color.blue);
			if (cellHeight != b.height-1) g.drawLine(b.x, b.y + cellHeight, b.x + b.width, b.y + cellHeight);
			if (cellWidth != b.width-1) g.drawLine(b.x + cellWidth, b.y, b.x + cellWidth, b.y+b.height);
			g.setColor(oldColor);
		}
	}
}
