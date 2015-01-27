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
 * This interface define the methods that a governor for the ColorSelectorWidget must 
 * offer. This methods defines how the square are and the rectangle area (the slider)
 * are filled.
 * 
 * @author Orlandin Marco
 *
 */
public interface IWidgetGovernor {
	
	/**
	 * Minimum relative value for the rectangle area
	 * 
	 * @return an integer value
	 */
	public int getSliderMin();
	
	/**
	 * Maximum relative value for the rectangle area
	 * 
	 * @return an integer value
	 */
	public int getSliderMax();
	
	/**
	 * Minimum relative value for the square area on the X-axis
	 * 
	 * @return an integer value
	 */
	public int getPadMinX();
	
	/**
	 * Maximum relative value for the square area on the X-axis
	 * 
	 * @return an integer value
	 */
	public int getPadMaxX();
	
	/**
	 * Minimum relative value for the square area on the Y-axis
	 * 
	 * @return an integer value
	 */
	public int getPadMinY();
	
	/**
	 * Maximum relative value for the square area on the Y-axis
	 * 
	 * @return an integer value
	 */
	public int getPadMaxY();
	
	/**
	 * Get the RGB of the color to paint into a point of the slider area
	 * 
	 * @param x coordinate X of the selection in the square area. This must be inside the getPadMinX and getPadMaxX values
	 * @param y coordinate Y of the selection in the square area. This must be inside the getPadMinY and getPadMaxY values
	 * @param sliderPosition coordinate Y of the selection in the rectangle slider area. This must be inside the SliderMin and SliderMax values
	 * @return the color to paint into the sliderPosition of the rectangle area
	 */
	public RGB getSliderColor(int x, int y, int sliderPosition);
	
	/**
	 * Get the RGB of the color to paint into a point of the square area
	 * 
	 * @param x coordinate X of the selection in the square area. This must be inside the getPadMinX and getPadMaxX values
	 * @param y coordinate Y of the selection in the square area. This must be inside the getPadMinY and getPadMaxY values
	 * @param sliderPosition coordinate Y of the selection in the rectangle slider area. This must be inside the SliderMin and SliderMax values
	 * @return the color to paint into the x,y coordinate of the square area
	 */
	public RGB getPadColor(int x, int y, int sliderPosition);
	
	public int[] getXYSlider(Object color);
	
}
