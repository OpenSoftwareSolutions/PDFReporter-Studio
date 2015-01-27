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
package com.jaspersoft.studio.editor.gef.decorator.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.gef.decorator.IDecorator;
import com.jaspersoft.studio.editor.gef.decorator.pdf.PDFDecorator;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.java2d.J2DUtils;

/**
 * This decorator is used to print one or more lines of text into an element.
 * The text is given by contributor modules, so to print new text it is sufficient to 
 * add a new module. A contributor must implement the TextDecoratorInterface interface.
 * @author Orlandin Marco
 *
 */
public class TextDecorator implements IDecorator {

	/**
	 * Left upper corner image
	 */
	private static ImageIcon startImageAwt = null;

	/**
	 * right lower corner image
	 */
	private static ImageIcon endImageAwt = null;

	/**
	 * List of text contributor
	 */
	private ArrayList<TextDecoratorInterface> textDecorators;
	
	public TextDecorator(){
		textDecorators = new ArrayList<TextDecoratorInterface>();
		if (startImageAwt == null || endImageAwt == null) {
			startImageAwt = new javax.swing.ImageIcon(PDFDecorator.class.getResource("/icons/resources/corner1.png"));
			endImageAwt = new javax.swing.ImageIcon(PDFDecorator.class.getResource("/icons/resources/corner2.png"));
		}
	}
	
	/**
	 * Add a new text contributor to the decorator
	 * @param newDecorator the new contributor
	 */
	public void addDecorator(TextDecoratorInterface newDecorator){
		textDecorators.add(newDecorator);
	}
	
	/**
	 * Remove a previous added contributor to the decorator
	 * @param toRemove element to remove
	 */
	public void removeDecorator(TextDecoratorInterface toRemove){
		textDecorators.remove(toRemove);
	}
	
	/**
	 * Check if a contributor is present
	 * @param element element to search
	 * @return true if the contributor is already present, false otherwise
	 */
	public boolean contains(TextDecoratorInterface element){
		return textDecorators.contains(element);
	}

	@Override
	public void paint(Graphics graphics, ComponentFigure fig) {
		if (fig.getJrElement() instanceof JRDesignElement) {
			Rectangle r = fig.getBounds();
			Graphics2D g = ComponentFigure.getG2D(graphics);
			if (g != null) {
				HashMap<TextLocation.Location, Integer> textMap = new HashMap<TextLocation.Location, Integer>();
				Stroke oldStroke = g.getStroke();
				Color oldColor = g.getColor();
				Font oldFont = g.getFont();
				g.setStroke(J2DUtils.getInvertedZoomedStroke(oldStroke, graphics.getAbsoluteScale()));
				boolean leftUpperCorner = false;
				boolean rightLowerCorner = false;
				for (TextDecoratorInterface decorator : textDecorators) {
					ArrayList<TextLocation> texts = decorator.getText(fig.getJrElement().getPropertiesMap());
					g.setFont(decorator.getFont());
					g.setColor(decorator.getColor());
					for (TextLocation text : texts) {
						if (text.hasValue()) {
							Integer strWidth;
							if (!textMap.containsKey(text.getLocation())) {
								textMap.put(text.getLocation(), 0);
								strWidth = 0;
							} else
								strWidth = textMap.get(text.getLocation());
							switch (text.getLocation()) {
							case TopLeft:
								g.drawString(text.getValue().getIterator(), r.x + strWidth + 4, r.y + 11);
							
								strWidth += g.getFontMetrics().stringWidth(text.getText())+(text.getLenght());
								leftUpperCorner = true;
								break;
							case TopRight:
								strWidth += g.getFontMetrics().stringWidth(text.getText())+text.getLenght();
								g.drawString(text.getValue().getIterator(), r.x + r.width - strWidth - 6, r.y + 11);
								break;
							case BottomLeft:
								g.drawString(text.getValue().getIterator(), r.x + strWidth + 4, r.y + r.height - 6);
								strWidth += g.getFontMetrics().stringWidth(text.getText())+text.getLenght();
								break;
							case BottomRight:
								strWidth += g.getFontMetrics().stringWidth(text.getText())+text.getLenght();
								g.drawString(text.getValue().getIterator(), r.x + r.width - strWidth - 6, r.y + r.height - 6);
								rightLowerCorner = true;
								break;
							}
							//Put a space at the end of the string
							strWidth += g.getFontMetrics().stringWidth(" ")+1;
							textMap.put(text.getLocation(), strWidth);
						}
					}
				}
				if (leftUpperCorner)
					drawStart(g, r);
				if (rightLowerCorner)
					drawEnd(g, r);
				g.setStroke(oldStroke);
				g.setColor(oldColor);
				g.setFont(oldFont);
			}
		}
	}
	
	/**
	 * Draw the image on the right lower corner
	 * @param gr object used to draw the image
	 * @param r item where the image will be drawn
	 */
	private void drawEnd(Graphics2D gr, Rectangle r) {
		gr.drawImage(endImageAwt.getImage(), r.x + r.width - endImageAwt.getIconWidth() - 2,
				r.y + r.height - endImageAwt.getIconHeight() - 2, null);
	}

	/**
	 * Draw the image on the left upper corner
	 * @param gr object used to draw the image
	 * @param r item where the image will be drawn
	 */
	private void drawStart(Graphics2D gr, Rectangle r) {
		gr.drawImage(startImageAwt.getImage(), r.x, r.y, null);
	}

}
