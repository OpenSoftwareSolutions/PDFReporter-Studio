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
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRPropertiesMap;

/**
 * Interface that define a text contributor for the text decorator
 * @author Orlandin Marco
 *
 */
public interface TextDecoratorInterface {
	
	/**
	 * Used to get the elements that will be painted on the figure
	 * @param mapProperties the property of the element related to the figure
	 * @return  an array of textual element that need to be painted on the figure
	 */
	public ArrayList<TextLocation> getText(JRPropertiesMap mapProperties);
	
	/**
	 * Color used to paint the textual elements
	 * @return an awt color
	 */
	public Color getColor();
	
	/**
	 * Font used to render the text
	 * @return an awt font
	 */
	public Font getFont();
	
}
