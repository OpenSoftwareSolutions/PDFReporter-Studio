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
package com.jaspersoft.studio.property.section.widgets;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;

/**
 * highlight a control by changing its background color
 * 
 * @author Orlandin Marco
 *
 */
public class BackgroundHighlight implements IHighlightControl{

	/**
	 * Control to highlight
	 */
	private Control controlToHighlight;
	
	/**
	 * Color of the background prior the change
	 */
	private RGB oldBackground;
	
	/**
	 * Create an instance of the class
	 * 
	 * @param control control to highlight
	 */
	public BackgroundHighlight(Control control){
		this.controlToHighlight = control;
	}
	
	/**
	 * Highlight the control by changing its background color
	 */
	public void highLightControl(){
		if (controlToHighlight != null && !controlToHighlight.isDisposed()){
			oldBackground = controlToHighlight.getBackground().getRGB();
			controlToHighlight.setBackground(ColorConstants.orange);
		}
	}
	
	/**
	 * Restore the control background to the default one
	 */
	public void deHighLightControl(){
		if (oldBackground != null && !controlToHighlight.isDisposed()){
			controlToHighlight.setBackground(null);
			controlToHighlight.redraw();
		}
	}
	
}
