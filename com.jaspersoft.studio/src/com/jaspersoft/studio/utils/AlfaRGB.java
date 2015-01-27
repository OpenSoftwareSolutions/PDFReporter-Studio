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
package com.jaspersoft.studio.utils;

import java.io.Serializable;

import org.eclipse.swt.graphics.RGB;

import com.jaspersoft.translation.resources.AbstractResourceDefinition;

public class AlfaRGB implements Serializable{

	private static final long serialVersionUID = 1283053372093648788L;
	
	private RGB rgb;
	
	private int alfa;

	public AlfaRGB(RGB rgb, int alfa) {
		this.rgb = rgb;
		this.alfa = alfa;
	}

	public RGB getRgb() {
		return rgb;
	}

	public int getAlfa() {
		return alfa;
	}
	
	public static AlfaRGB getFullyOpaque(RGB rgb) {
		return (rgb!=null) ? new AlfaRGB(rgb, 255) : null;
	}
	
	public static AlfaRGB getFullyTransparent(RGB rgb) {
		return (rgb!=null) ? new AlfaRGB(rgb, 0) : null;
	}
	
	public static RGB safeGetRGB(AlfaRGB argb) {
		return (argb!=null) ? argb.getRgb() : null;
	}
	
	public void setAlfa(int alfa){
		if (alfa>=0 && alfa<=255){
			this.alfa = alfa;
		}
	}
	
	public void setAlfa(double alfa){
		int value =  new Long(Math.round(255*alfa)).intValue();
		if (value < 0) value = 0;
		if (value > 255) value = 255;
		this.alfa = value;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 != null && arg0 instanceof AlfaRGB){
			AlfaRGB color = (AlfaRGB)arg0;
			return (color.getAlfa() == alfa && AbstractResourceDefinition.safeEquals(getRgb(),color.getRgb()));
		}
		return false;
	}
	
	public AlfaRGB clone() {
		RGB newRGB = null;
		if (rgb != null) newRGB = new RGB(rgb.red, rgb.green, rgb.blue);
		return new AlfaRGB(newRGB, alfa);
	}
}
