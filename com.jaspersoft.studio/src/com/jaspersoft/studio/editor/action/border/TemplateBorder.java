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
package com.jaspersoft.studio.editor.action.border;

import net.sf.jasperreports.engine.type.LineStyleEnum;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * an instance of this class represent a border preset. It also provide the method to return a sample line of the border
 * 
 * @author Orlandin Marco
 * 
 */
public class TemplateBorder {

	/**
	 * The width of the border
	 */
	private Float lineWidth;

	/**
	 * The linestyle of the border
	 */
	private LineStyleEnum lineStyle;

	/**
	 * The color of the border
	 */
	private Color color;

	/**
	 * Width of the line in the sample
	 */
	private static int width;

	/**
	 * 
	 * @param lineWidth
	 *          width of the border
	 * @param lineStyle
	 *          style of the border
	 * @param lineColor
	 *          color of the border (if null is set to black)
	 */
	public TemplateBorder(Float lineWidth, LineStyleEnum lineStyle, RGB lineColor) {
		this.lineStyle = lineStyle;
		this.lineWidth = lineWidth;
		if (lineColor != null)
			this.color = SWTResourceManager.getColor(lineColor);
		else
			this.color = ColorConstants.black;
	}

	/**
	 * With this constructor the border is set to black
	 * 
	 * @param lineWidth
	 *          width of the border
	 * @param lineStyle
	 *          style of the border
	 */
	public TemplateBorder(Float lineWidth, LineStyleEnum lineStyle) {
		this(lineWidth, lineStyle, null); // null means black
	}

	/**
	 * Return the RGB of the color
	 * 
	 * @return and RGB of the color, it can't be null
	 */
	public RGB getColor() {
		return color.getRGB();
	}

	/**
	 * return the style of the line (it can be DOTTED, DASHED, SOLID or DUBLE).
	 */
	public LineStyleEnum getStyle() {
		return lineStyle;
	}

	/**
	 * Return the border width
	 * 
	 * @return a float of the border, it can be null
	 */
	public Float getLineWidth() {
		return lineWidth;
	}

	/**
	 * Ovverride of the equals method. Two border presets are equal if they have a lineWidth that make them not visible
	 * (null or with width<=0), or if the line is visible they are equals if all the fields (color,width,style) are equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TemplateBorder) {
			TemplateBorder other = (TemplateBorder) obj;
			if ((getLineWidth() == null && other.getLineWidth() == null)
					|| (getLineWidth() != null && other.getLineWidth() != null && getLineWidth() <= 0 && other.getLineWidth() <= 0))
				return true;
			boolean colorEquals = (color == null && other.getColor() == null)
					|| (color != null && getColor().equals(other.getColor()));
			boolean widthEquals = (getLineWidth() == null && other.getLineWidth() == null)
					|| (getLineWidth() != null && getLineWidth().equals(other.getLineWidth()));
			return (widthEquals && getStyle() == other.getStyle() && colorEquals);
		}
		return false;
	}

	/**
	 * Return a border preview for a not visible border. The image is created only the first time is requested, then it is
	 * cached and disposed when the application is closed.
	 * 
	 * @return an image with the words "No Borders" rendereized into it
	 */
	private Image getNoBordersImage() {
		String key = "linePreset_noBorders";
		Image image = ResourceManager.getImage(key);
		if (image == null) {
			ImageData data = new ImageData(getWidth(), 15, 1, new PaletteData(new RGB[] { ColorConstants.white.getRGB() }));
			// data.transparentPixel = data.getPixel(0, 0);
			image = new Image(null, data);
			GC graphics = new GC(image);
			try {
				// graphics.setTextAntialias(SWT.ON);
				graphics.setFont(ResourceManager.getFont("Time New Roman", 10, SWT.NORMAL));
				graphics.drawString("No Borders", 5, 0);
			} finally {
				graphics.dispose();
			}
			ResourceManager.addImage(key, image);
		}
		return image;
	}

	/**
	 * Return an image that represent a preview of this border preset. The image is created only the first time is
	 * requested, then it is cached and disposed when the application is closed.
	 */
	public Image getImage() {
		// If the border is not visible return the NoBorder image
		if (lineWidth == null || lineWidth <= 0)
			return getNoBordersImage();
		// The images are cached and disposed at the end
		String key = "linePreset_" + lineStyle.toString() + lineWidth.toString() + getColor().toString();
		Image image = ResourceManager.getImage(key);
		if (image == null) {
			ImageData data = new ImageData(getWidth(), 20, 1, new PaletteData(new RGB[] { ColorConstants.white.getRGB() }));
			// data.transparentPixel = data.getPixel(0, 0);
			image = new Image(null, data);
			GC graphics = new GC(image);
			try {
				int width = image.getBounds().width - 5;
				switch (lineStyle) {
				case DASHED:
					graphics.setLineStyle(SWT.LINE_DASH);
					break;
				case DOTTED:
					graphics.setLineStyle(SWT.LINE_DOT);
					break;
				case SOLID:
					graphics.setLineStyle(SWT.LINE_SOLID);
					break;
				default:
					graphics.setLineStyle(SWT.LINE_SOLID);

				}
				graphics.setForeground(color);
				graphics.setLineWidth(Math.round(lineWidth));
				if (lineStyle == LineStyleEnum.DOUBLE) {
					int startX = 5;
					Float imageHeight = new Float(image.getBounds().height);
					int startY1 = Math.round((imageHeight / 3) - (lineWidth / 2) + lineWidth / 4);
					int startY2 = Math.round((imageHeight / 3) * 2 - (lineWidth / 2) + lineWidth / 4);
					graphics.drawLine(startX, startY1, width, startY1);
					graphics.drawLine(startX, startY2, width, startY2);
				} else {
					int startX = 5;
					Float imageHeight = new Float(image.getBounds().height);
					int startY = Math.round((imageHeight / 2) - (lineWidth / 2));
					graphics.drawLine(startX, startY, width, startY);
				}
			} finally {
				graphics.dispose();
			}
			ResourceManager.addImage(key, image);
		}
		return image;
	}

	/**
	 * Return a border preview for a custom border (not equals to any preset). The image is created only the first time is
	 * requested, then it is cached and disposed when the application is closed.
	 * 
	 * @return an image with the words "Custom" rendereized into it
	 */
	public static Image getCustomImage() {
		String key = "linePreset_custom";
		Image image = ResourceManager.getImage(key);
		if (image == null) {
			ImageData data = new ImageData(getWidth(), 15, 1, new PaletteData(new RGB[] { ColorConstants.white.getRGB() }));
			// data.transparentPixel = data.getPixel(0, 0);
			image = new Image(null, data);
			GC graphics = new GC(image);
			try {
				graphics.setFont(ResourceManager.getFont("Time New Roman", 10, SWT.NORMAL));
				// graphics.setTextAntialias(SWT.ON);
				graphics.drawString("Custom", 5, 0);
			} finally {
				graphics.dispose();
			}
			ResourceManager.addImage(key, image);
		}
		return image;
	}

	/**
	 * set the width of the preview, it influence the preview of all presets (since it is static)
	 * 
	 * @param newWidth
	 *          width of the preview, it must be greater than zero
	 */
	public static void setWidth(int newWidth) {
		if (newWidth > 0)
			width = newWidth;
	}

	/**
	 * Return the width of the preview
	 * 
	 * @return if for some reason the width is <=0 it return a default value of 70.
	 */
	public static int getWidth() {
		if (width <= 0)
			width = 70;
		return width;
	}
}
