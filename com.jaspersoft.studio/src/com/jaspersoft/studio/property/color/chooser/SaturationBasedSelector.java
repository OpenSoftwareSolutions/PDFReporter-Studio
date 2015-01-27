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
package com.jaspersoft.studio.property.color.chooser;

import org.eclipse.swt.graphics.RGB;

/**
 * Governor for the ColorSelectorWidget where the user clicking the rectangle
 * slider area control the saturation of the color, and on the square area are shown
 * the color variations of hue and brightness with the selected saturation
 * 
 * @author Orlandin Marco
 *
 */
public class SaturationBasedSelector implements IWidgetGovernor{

	@Override
	public int getSliderMax() {
		return 100;
	}

	@Override
	public int getSliderMin() {
		return 0;
	}

	@Override
	public int getPadMinX() {
		return 0;
	}

	@Override
	public int getPadMinY() {
		return 0;
	}

	@Override
	public int getPadMaxX() {
		return 360;
	}

	@Override
	public int getPadMaxY() {
		return 100;
	}
	

	@Override
	public RGB getPadColor(int x, int y, int sliderPosition) {
		float fSat =  (float)Math.abs(100-sliderPosition)/100;
		float fBright = (float) Math.abs(100-y) / 100;
		return new RGB((float)Math.abs(360-x), fSat, fBright);
	}
	
	@Override
	public RGB getSliderColor(int x, int y, int sliderPosition) {
		return getPadColor(x, y, sliderPosition);
	}
	
	@Override
	public int[] getXYSlider(Object color) {
		float[] hsb = null;
		if (color instanceof RGB){
			hsb = ((RGB)color).getHSB();
		} else if (color instanceof float[]){
			hsb = (float[])color;
		}
		if (hsb == null) return new int[]{0,0,0};
		return new int[]{Math.round(Math.abs(360-hsb[0])), Math.abs(Math.round(100-hsb[2]*100)), Math.abs(Math.round(100-hsb[1]*100))};
	}
	

}
