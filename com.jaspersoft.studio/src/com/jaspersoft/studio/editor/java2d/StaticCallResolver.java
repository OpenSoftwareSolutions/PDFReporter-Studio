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
package com.jaspersoft.studio.editor.java2d;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Class that provide a static method binding for the StackGraphics2D
 * 
 * @author Orlandin Marco
 *
 */
public class StaticCallResolver {

	/**
	 * This method provide a static binding to the method of a graphics 2d. The method called
	 * is identified by a numeric id. Also the parameter are casted statically. This method
	 * can be used instead of the reflection to improve the performances
	 * 
	 * @param methodId the id of the method
	 * @param parameters the parameters that will be passed to the called method
	 * @param graphics the graphics where the method will be called
	 */
	public static void resolveCall(int methodId, Object[] parameters, Graphics2D graphics){
		
		switch(methodId){
			case 0: graphics.addRenderingHints((Map<?,?>)parameters[0]);
							break;
			case 1: graphics.setRenderingHints((Map<?,?>)parameters[0]);
							break;
			case 2: graphics.clip((Shape)parameters[0]);
							break;
			case 3: graphics.draw((Shape)parameters[0]);
							break;
			case 4: graphics.drawGlyphVector((GlyphVector)parameters[0], (Float)parameters[1], (Float)parameters[2]);
							break;
			case 5: graphics.drawImage((Image)parameters[0], (AffineTransform)parameters[1], (ImageObserver)parameters[2]);
							break;
			case 6: graphics.drawImage((BufferedImage)parameters[0], (BufferedImageOp)parameters[1], (Integer)parameters[2], (Integer)parameters[3]);
							break;
			case 7: graphics.drawRenderableImage((RenderableImage)parameters[0], (AffineTransform)parameters[1]);
							break;
			case 8: graphics.drawRenderedImage((RenderedImage)parameters[0], (AffineTransform)parameters[1]);
							break;
			case 9: graphics.drawString((String)parameters[0], (Integer)parameters[1], (Integer)parameters[2]);
							break;
			case 10: graphics.drawString((String)parameters[0], (Float)parameters[1], (Float)parameters[2]);
							 break;
			case 11: graphics.drawString((AttributedCharacterIterator)parameters[0], (Integer)parameters[1], (Integer)parameters[2]);
							 break;
			case 12: graphics.drawString((AttributedCharacterIterator)parameters[0], (Float)parameters[1], (Float)parameters[2]);	 
							 break;
			case 13: graphics.fill((Shape)parameters[0]);
							 break;
			case 14: graphics.hit((Rectangle)parameters[0], (Shape)parameters[1], (Boolean)parameters[2]);
							 break;
			case 15: graphics.rotate((Double)parameters[0]);
							 break;
			case 16: graphics.rotate((Double)parameters[0], (Double)parameters[1], (Double)parameters[2]);
							 break;
			case 17: graphics.scale((Double)parameters[0], (Double)parameters[1]);
							 break;
			case 18: graphics.setBackground((Color)parameters[0]);
							 break;
			case 19: graphics.setComposite((Composite)parameters[0]);
							 break;
			case 20: graphics.setPaint((Paint)parameters[0]);
							 break;
			case 21: graphics.setRenderingHint((Key)parameters[0], parameters[1]);
			 				 break;
			case 22: graphics.setStroke((Stroke)parameters[0]);
							 break;
			case 23: graphics.setTransform((AffineTransform)parameters[0]);
							 break;
			case 24: graphics.shear((Double)parameters[0], (Double)parameters[1]);
			 				 break;
			case 25: graphics.transform((AffineTransform)parameters[0]);
							 break;
			case 26: graphics.translate((Integer)parameters[0], (Integer)parameters[1]);
			 				 break;
			case 27: graphics.translate((Double)parameters[0], (Double)parameters[1]);
						   break;
			case 28: graphics.clearRect((Integer)parameters[0],(Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3]);
							 break;
			case 29: graphics.clipRect((Integer)parameters[0],(Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3]);
			 				 break;
			case 30: graphics.copyArea((Integer)parameters[0],(Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (Integer)parameters[5]);
			 				 break;
			case 31: graphics.drawArc((Integer)parameters[0],(Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (Integer)parameters[5]);
			 				 break;
			case 32: graphics.drawImage((Image)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (ImageObserver)parameters[3]);
							 break;
			case 33: graphics.drawImage((Image)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Color)parameters[3], (ImageObserver)parameters[4]);
			 				 break;
			case 34: graphics.drawImage((Image)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (ImageObserver)parameters[5]);
			 				 break;
			case 35: graphics.drawImage((Image)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (Color)parameters[5], (ImageObserver)parameters[6]);
			 			   break;
			case 36: graphics.drawImage((Image)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (Integer)parameters[5], (Integer)parameters[6], (Integer)parameters[7], (Integer)parameters[8], (ImageObserver)parameters[9]);
			 				 break;
			case 37: graphics.drawImage((Image)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (Integer)parameters[5], (Integer)parameters[6], (Integer)parameters[7], (Integer)parameters[8], (Color)parameters[9], (ImageObserver)parameters[10]);
			 				 break;
			case 38: graphics.drawLine((Integer)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3]);
			         break;
			case 39: graphics.drawOval((Integer)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3]);
						   break;
			case 40: graphics.drawPolygon((int[])parameters[0], (int[])parameters[1], (Integer)parameters[2]);
			 				 break;
			case 41: graphics.drawPolyline((int[])parameters[0], (int[])parameters[1], (Integer)parameters[2]);
			 				 break;
			case 42: graphics.drawRoundRect((Integer)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (Integer)parameters[5]);
			 				 break;
			case 43: graphics.fillArc((Integer)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (Integer)parameters[5]);
			 				 break;
			case 44: graphics.fillOval((Integer)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3]);
							 break;
			case 45: graphics.fillPolygon((int[])parameters[0], (int[])parameters[1], (Integer)parameters[2]);
			 				 break;
			case 46: graphics.fillRect((Integer)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3]);
			 				 break;
			case 47: graphics.fillRoundRect((Integer)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3], (Integer)parameters[4], (Integer)parameters[5]);
			 				 break;
			case 48: graphics.setClip((Shape)parameters[0]);
			 				 break;
			case 49: graphics.setClip((Integer)parameters[0], (Integer)parameters[1], (Integer)parameters[2], (Integer)parameters[3]);
			 				 break;
			case 50: graphics.setColor((Color)parameters[0]);
							 break;
			case 51: graphics.setFont((Font)parameters[0]);
			 				 break;
			case 52: graphics.setPaintMode();
			 				 break;
			case 53: graphics.setXORMode((Color)parameters[0]);
			 				 break;
      default: throw new RuntimeException("Invalid Action Code");
		}
		
	}
	
}
