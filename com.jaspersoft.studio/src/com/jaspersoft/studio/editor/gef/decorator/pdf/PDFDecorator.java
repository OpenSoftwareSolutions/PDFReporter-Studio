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
package com.jaspersoft.studio.editor.gef.decorator.pdf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.gef.decorator.IDecorator;
import com.jaspersoft.studio.editor.gef.decorator.text.TextDecoratorInterface;
import com.jaspersoft.studio.editor.gef.decorator.text.TextLocation;
import com.jaspersoft.studio.editor.gef.decorator.text.TextLocation.Location;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.java2d.J2DUtils;

/**
 * Draw the selected PDF 508 attributes on an element. it also provide an interface to became a text 
 * contributor
 * 
 * @author Orlandin Marco
 * 
 */
public class PDFDecorator implements IDecorator, TextDecoratorInterface {

	/**
	 * Left upper corner image
	 */
	private static ImageIcon startImageAwt = null;

	/**
	 * right lower corner image
	 */
	private static ImageIcon endImageAwt = null;

	/**
	 * Font of the text
	 */
	private static Font JSS_TEXT_FONT = new Font("SansSerif", 0, 10);

	/**
	 * Color of the text
	 */
	private static Color JSS_TEXT_COLOR = new Color(195, 47, 193);

	/**
	 * Constructor, load the images if the weren't loaded before
	 */
	public PDFDecorator() {
		if (startImageAwt == null || endImageAwt == null) {
			startImageAwt = new javax.swing.ImageIcon(PDFDecorator.class.getResource("/icons/resources/corner1.png"));
			endImageAwt = new javax.swing.ImageIcon(PDFDecorator.class.getResource("/icons/resources/corner2.png"));
		}
	}

	/**
	 * Print on the element it's selected pdf tags
	 */
	@Override
	public void paint(Graphics graphics, ComponentFigure fig) {
		if (fig.getJrElement() instanceof JRDesignElement) {

			Rectangle r = fig.getBounds();
			Graphics2D g = ComponentFigure.getG2D(graphics);
			if (g != null) {
				Stroke oldStroke = g.getStroke();
				g.setStroke(J2DUtils.getInvertedZoomedStroke(oldStroke, graphics.getAbsoluteScale()));

				String tagValue = "";
				String startString = "";
				String fullString = "";
				String endString = "";

				boolean drawstart = false;
				boolean drawend = false;

				String[] tags = { "net.sf.jasperreports.export.pdf.tag.h1", "H1", "net.sf.jasperreports.export.pdf.tag.h2",
						"H2", "net.sf.jasperreports.export.pdf.tag.h3", "H3", "net.sf.jasperreports.export.pdf.tag.table", "TBL",
						"net.sf.jasperreports.export.pdf.tag.tr", "TR", "net.sf.jasperreports.export.pdf.tag.th", "TH",
						"net.sf.jasperreports.export.pdf.tag.td", "TD" };

				JRPropertiesMap v = fig.getJrElement().getPropertiesMap();
				for (int i = 0; i < tags.length; i += 2) {
					String prop = tags[i];
					String label = tags[i + 1];
					tagValue = v.getProperty(prop);
					if (tagValue != null) {
						if (tagValue.equals("full")) {
							drawstart = true;
							drawend = true;
							fullString += label + " ";
						} else if (tagValue.equals("start")) {
							drawstart = true;
							startString += label + " ";
						} else if (tagValue.equals("end")) {
							drawend = true;
							endString = label + " " + endString;
						}
					}

				}
				if (drawstart)
					drawStart(g, r);
				if (drawend)
					drawEnd(g, r);

				Font f = g.getFont();

				Color color = g.getColor();

				startString = startString.trim();
				endString = endString.trim();
				fullString = fullString.trim();

				g.setFont(JSS_TEXT_FONT);
				g.setColor(JSS_TEXT_COLOR);

				if (startString.length() > 0) {
					g.drawString(startString, r.x + 4, r.y + 11);
				}

				if (endString.length() > 0) {
					int strWidth = g.getFontMetrics().stringWidth(endString);
					g.drawString(endString, r.x + r.width - strWidth - 6, r.y + r.height - 6);
				}

				if (fullString.length() > 0) {
					int strWidth = 0;
					if (startString.length() > 0)
						strWidth = g.getFontMetrics().stringWidth(startString + " ");
					AttributedString as = new AttributedString(fullString);
					as.addAttribute(TextAttribute.FONT, g.getFont());
					as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					g.drawString(as.getIterator(), r.x + 4 + strWidth, r.y + 11);
				}

				g.setFont(f);
				g.setColor(color);
			}
		}
	}

	/**
	 * Set the font of the displayed text
	 * 
	 * @param newFont
	 *          the new font
	 */
	public void setTextFont(Font newFont) {
		JSS_TEXT_FONT = newFont;
	}

	/**
	 * Set the color of the displayed text
	 * 
	 * @param newColor
	 *          the new color
	 */
	public void setTextColor(Color newColor) {
		JSS_TEXT_COLOR = newColor;
	}

	/**
	 * Draw the image on the right lower corner
	 * 
	 * @param gr
	 *          object used to draw the image
	 * @param r
	 *          item where the image will be drawn
	 */
	private void drawEnd(Graphics2D gr, Rectangle r) {
		gr.drawImage(endImageAwt.getImage(), r.x + r.width - endImageAwt.getIconWidth() - 2,
				r.y + r.height - endImageAwt.getIconHeight() - 2, null);
	}

	/**
	 * Draw the image on the left upper corner
	 * 
	 * @param gr
	 *          object used to draw the image
	 * @param r
	 *          item where the image will be drawn
	 */
	private void drawStart(Graphics2D gr, Rectangle r) {
		gr.drawImage(startImageAwt.getImage(), r.x, r.y, null);
	}

	@Override
	public ArrayList<TextLocation> getText(JRPropertiesMap mapProperties) {
		ArrayList<TextLocation> result = new ArrayList<TextLocation>();
		String tagValue = "";
		String startString = "";
		String fullString = "";
		String endString = "";

		String[] tags = { "net.sf.jasperreports.export.pdf.tag.h1", "H1", "net.sf.jasperreports.export.pdf.tag.h2",
				"H2", "net.sf.jasperreports.export.pdf.tag.h3", "H3", "net.sf.jasperreports.export.pdf.tag.table", "TBL",
				"net.sf.jasperreports.export.pdf.tag.tr", "TR", "net.sf.jasperreports.export.pdf.tag.th", "TH",
				"net.sf.jasperreports.export.pdf.tag.td", "TD" };

		for (int i = 0; i < tags.length; i += 2) {
			String prop = tags[i];
			String label = tags[i + 1];
			tagValue = mapProperties.getProperty(prop);
			if (tagValue != null) {
				if (tagValue.equals("full")) {
					fullString += label + " ";
				} else if (tagValue.equals("start")) {
					startString += label + " ";
				} else if (tagValue.equals("end")) {
					endString = label + " " + endString;
				}
			}

		}
	
		startString = startString.trim();
		endString = endString.trim();
		fullString = fullString.trim();

		if (startString.length() > 0) {
			result.add(new TextLocation(Location.TopLeft, startString));
		}

		if (endString.length() > 0) {
			result.add(new TextLocation(Location.BottomRight, endString));
		}

		if (fullString.length() > 0) {
			TextLocation as = new TextLocation(Location.TopLeft, fullString);
			as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			result.add(as);
		}
		
		return result;
	}

	@Override
	public Color getColor() {
		return JSS_TEXT_COLOR;
	}

	@Override
	public Font getFont() {
		return JSS_TEXT_FONT;
	}

}
