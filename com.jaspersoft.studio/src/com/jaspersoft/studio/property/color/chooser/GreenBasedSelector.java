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
 * slider area control the green component of the color, and on the square area are shown
 * the color variations of red and blue components with the selected green 
 * 
 * @author Orlandin Marco
 *
 */
public class GreenBasedSelector implements IWidgetGovernor{
	@Override
	public int getSliderMax() {
		return 255;
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
		return 255;
	}

	@Override
	public int getPadMaxY() {
		return 255;
	}

	@Override
	public RGB getPadColor(int x, int y, int sliderPosition) {
		return new RGB(Math.abs(255-y),Math.abs(255-sliderPosition),x);
	}
	
	@Override
	public RGB getSliderColor(int x, int y, int sliderPosition) {
		return getPadColor(x, y, sliderPosition);
	}

	public int getX(RGB color) {
		return color.blue;
	}

	public int getY(RGB color) {
		return Math.abs(255-color.red);
	}

	public int getSlider(RGB color) {
		return Math.abs(255-color.green);
	}

	@Override
	public int[] getXYSlider(Object color) {
		if (color instanceof RGB){
			RGB rgbColor = (RGB) color;
			return new int[]{getX(rgbColor), getY(rgbColor), getSlider(rgbColor)};
		} else if (color instanceof float[]){
			float[] hsb = (float[])color;
			RGB rgbColor = new RGB(hsb[0], hsb[1], hsb[2]);
			return new int[]{getX(rgbColor), getY(rgbColor), getSlider(rgbColor)};
		}
		return new int[]{0,0,0};
	}
	
}
