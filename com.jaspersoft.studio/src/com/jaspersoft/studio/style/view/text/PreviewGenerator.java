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
package com.jaspersoft.studio.style.view.text;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * Class that offer the static methods to generate a JR static text image starting
 * from a TextStyle
 * 
 * @author Orlandin Marco
 *
 */
public class PreviewGenerator {

	/**
	 * Buffered image where the  preview is painted
	 */
	private static BufferedImage bi = null;
	
	/**
	 * Visitor used to print the jr element
	 */
	private static DrawVisitor visitor = null;
	
	/**
	 * Design where the printed static text element is placed
	 */
	private static JasperDesign jasperDesign = null;
	
	/**
	 * The static text element
	 */
	private static JRDesignStaticText textElement = null;
	
	/**
	 * Generate an swt image data as preview of a text style.
	 * It show how a text element should appear when the style 
	 * is applied 
	 * 
	 * @param style The style used to generate the preview
	 * @param width the width of the preview area
	 * @param height the eight of the preview area
	 * @param containerBackground the background used then the style has a transparent background
	 * @return and swt image data
	 */
	public static ImageData generatePreview(TextStyle style, int width, int height, RGB containerBackground) {
		if (jasperDesign == null)
			createDesign();
		((JRDesignBand) jasperDesign.getTitle()).setHeight(height);
		jasperDesign.setPageWidth(width);
		setDesignElement(style, width, height, containerBackground);
		// If we have not a buffered image or the old one has a different size from what we need, the we create a new
		// buffered image and the cache it
		createBufferedImage();
		visitor.visitStaticText(textElement);
		return convertToSWT(bi);
	}
	
	/**
	 * Create a very minimal jasperdesign where the static text is placed. It is cached
	 * since we don't need to create it everytime
	 */
	private static void createDesign()
  {
       jasperDesign = new JasperDesign();
       JRDesignBand jrBand = new JRDesignBand();
   		 jasperDesign.setTitle(jrBand);
       textElement = new JRDesignStaticText();
       jasperDesign.setLeftMargin(0);
       jasperDesign.setRightMargin(0);
       jasperDesign.setTopMargin(0);
       jasperDesign.setBottomMargin(0);
       jrBand.addElement(textElement);
       jrBand.setSplitType(SplitTypeEnum.STRETCH);
       textElement.setStretchType(StretchTypeEnum.NO_STRETCH);
       textElement.setPrintRepeatedValues(false);
       textElement.setPrintWhenDetailOverflows(true);
   }
	 
	 /**
	  * Set the static text element to have the appearance described in the text style
	  * 
	  * @param style text style
	  * @param width width of the static text element
	  * @param height height of the static text element 
	  * @param containerBackground the background used then the style has a transparent background
	  */
	 private static void setDesignElement(TextStyle style, int width, int height, RGB containerBackground) {
      textElement.setWidth(width-1);
      textElement.setHeight(height-1);
      textElement.setText(style.getDescription());
      UpdateStyleCommand.applayStyleToTextElement(style,textElement);
      if (style.isTransparent()) {
      	textElement.setMode(ModeEnum.OPAQUE);
      	textElement.setBackcolor(new Color(containerBackground.red, containerBackground.green, containerBackground.blue));
      }
  }
	 
	 /**
	  * Create a buffered image of the same size of the text element and cache it
	  */
	 private static void createBufferedImage(){
     bi = new BufferedImage(textElement.getWidth(), textElement.getHeight(), BufferedImage.TYPE_INT_ARGB);
     Graphics2D g2d = bi.createGraphics();
     g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
     visitor = new DrawVisitor(new ReportConverter(DefaultJasperReportsContext.getInstance(), jasperDesign, true), g2d);
	 }

	 /**
	  * Convert an AWT BufferedImage to an swt imagedata
	  * 
	  * @param bufferedImage the awt buffered image
	  * @return the converted image data
	  */
	private static ImageData convertToSWT(BufferedImage bufferedImage) {
	  	if (bufferedImage.getColorModel() instanceof DirectColorModel) {
	  		DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
	  		PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
	  		ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
	  		for (int y = 0; y < data.height; y++) {
	  			for (int x = 0; x < data.width; x++) {
	  				int rgb = bufferedImage.getRGB(x, y);
	  				int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF)); 
	  				data.setPixel(x, y, pixel);
	  				if (colorModel.hasAlpha()) {
	  					data.setAlpha(x, y, (rgb >> 24) & 0xFF);
	  				}
	  			}
	  		}
	  		return data;		
	  	} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
	  		IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
	  		int size = colorModel.getMapSize();
	  		byte[] reds = new byte[size];
	  		byte[] greens = new byte[size];
	  		byte[] blues = new byte[size];
	  		colorModel.getReds(reds);
	  		colorModel.getGreens(greens);
	  		colorModel.getBlues(blues);
	  		RGB[] rgbs = new RGB[size];
	  		for (int i = 0; i < rgbs.length; i++) {
	  			rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
	  		}
	  		PaletteData palette = new PaletteData(rgbs);
	  		ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
	  		data.transparentPixel = colorModel.getTransparentPixel();
	  		WritableRaster raster = bufferedImage.getRaster();
	  		int[] pixelArray = new int[1];
	  		for (int y = 0; y < data.height; y++) {
	  			for (int x = 0; x < data.width; x++) {
	  				raster.getPixel(x, y, pixelArray);
	  				data.setPixel(x, y, pixelArray[0]);
	  			}
	  		}
	  		return data;
	  	}
	  	return null;
	  }

	
}
