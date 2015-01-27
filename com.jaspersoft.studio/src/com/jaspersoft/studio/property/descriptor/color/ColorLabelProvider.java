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
package com.jaspersoft.studio.property.descriptor.color;

import java.awt.Color;

import net.sf.jasperreports.chartthemes.simple.ColorProvider;
import net.sf.jasperreports.chartthemes.simple.GradientPaintProvider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.ResourceCache;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

/*
 * @author Chicu Veaceslav
 */
public class ColorLabelProvider extends LabelProvider {
	private NullEnum canBeNull;

	private ResourceCache imagesCache = new ResourceCache();

	public ColorLabelProvider(NullEnum canBeNull) {
		super();
		this.canBeNull = canBeNull;
	}

	@Override
	public Image getImage(Object element) {
		return getImage(element, 16, 16);
	}
	
	/**
	 * Generate a trasparency on the color assuming a white background
	 */
	private RGB getBlendedColor(RGB color, int alphaInt){
		float alpha = (float)alphaInt/255;
		int outRed = Math.round(alpha * color.red + (1 - alpha) * 255);
		int outGreen = Math.round(alpha * color.green + (1 - alpha) * 255);
		int outBlue = Math.round(alpha * color.blue + (1 - alpha) * 255);
		return new RGB(outRed, outGreen, outBlue);
	}
	
	public Image getImage(Object element, int width, int height) {
		int alfa = 255;
		if (element == null)
			return JaspersoftStudioPlugin.getInstance().getImage("icons/resources/nocolor.png");
		Display display = Display.getCurrent();
		if (element instanceof ColorProvider)
			element = Colors.getRGB4AWTColor(((ColorProvider) element).getColor());
		if (element instanceof AlfaRGB){
			AlfaRGB alfaRGB = (AlfaRGB) element;
			element = alfaRGB.getRgb();
			alfa = alfaRGB.getAlfa();
		}
		if (element instanceof RGB) {
			RGB rgb = (RGB) element;
			String key = rgb.toString() + Integer.toString(width) + "_" + Integer.toString(height)+ "_" + Integer.toString(alfa);
			Image result = imagesCache.getImage(key);
			if (result == null) {
				RGB black = new RGB(0, 0, 0);
				if (alfa < 255) rgb = getBlendedColor(rgb, alfa);
				PaletteData dataPalette = new PaletteData(new RGB[] { black, black, rgb });
				ImageData data = new ImageData(width, height, 4, dataPalette);
				data.transparentPixel = 0;
				for (int y = 0; y < data.height; y++) {
					for (int x = 0; x < data.width; x++) {
						if (x == 0 || y == 0 || x == data.width - 1 || y == data.height - 1)
							data.setPixel(x, y, 1);
						else
							data.setPixel(x, y, 2);
					}
				}
				result = new Image(display, data);
				imagesCache.storeImage(key, result);
			}
			return result;
		} else if (element instanceof GradientPaintProvider) {
			Image newImage = new Image(display, width, height);
			GC gc = new GC(newImage);
			try {
				gc.setForeground(Colors.getSWT4AWTColor(((GradientPaintProvider) element).getColor1()));
				gc.setBackground(Colors.getSWT4AWTColor(((GradientPaintProvider) element).getColor2()));
				gc.fillGradientRectangle(0, 0, width, height, false);
				gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.drawRectangle(0, 0, width - 1, height - 1);
			} finally {
				gc.dispose();
			}
			return newImage;
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "";
		if (element instanceof Color)
			element = Colors.getRGB4AWTColor((Color) element);
		if (element instanceof ColorProvider)
			element = Colors.getRGB4AWTColor(((ColorProvider) element).getColor());
		if (element instanceof AlfaRGB)
			element = ((AlfaRGB) element).getRgb();
		if (element instanceof RGB) {
			RGB rgb = (RGB) element;
			return rgb.red + "," + rgb.green + "," + rgb.blue; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} else if (element instanceof GradientPaintProvider)
			return getText(((GradientPaintProvider) element).getColor1()) + " - " //$NON-NLS-1$
					+ getText(((GradientPaintProvider) element).getColor2());
		return "";
	}

	@Override
	public void dispose() {
		super.dispose();
		imagesCache.dispose();
	}

}
