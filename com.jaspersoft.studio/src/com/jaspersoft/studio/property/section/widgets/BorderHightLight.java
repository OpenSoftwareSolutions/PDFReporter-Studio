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

import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Control;

/**
 * highlight a control by drawing a colored border around it
 * 
 * @author Orlandin Marco
 *
 */
public class BorderHightLight implements IHighlightControl {

	/**
	 * Control to highlight
	 */
	private Control controlToHighlight;
	
	/**
	 * Paint listener that will be used to pain the colored border
	 */
	private PaintListener borderPainter;
	
	/**
	 * Create an instance of the class. The paint listener will be read from 
	 * the class DefaultWidgetsHighlighters in accord with the type of the control
	 * 
	 * @param control control to highlight. 
	 */
	public BorderHightLight(Control controlToHighLight){
		this.controlToHighlight = controlToHighLight;
		borderPainter = null;
	}
	
	/**
	 * Create an instance of the class. This allow to specify manually which PaintListener
	 * use from the class DefaultWidgetsHighlighters
	 * 
	 * @param control control to highlight. 
	 * @param forcePainterClass type of PaintListener that will be required from the class DefaultWidgetsHighlighters
	 */
	public BorderHightLight(Control controlToHighLight, Class<?> forcePainterClass){
		this.controlToHighlight = controlToHighLight;
		borderPainter = DefaultWidgetsHighlighters.getWidgetForType(forcePainterClass);;
	}
	
	/**
	 * Highlight the control by changing adding a PaintListener that will draw an orange border around the control
	 */
	@Override
	public void highLightControl() {
		if (controlToHighlight != null && !controlToHighlight.isDisposed()){
			if (borderPainter == null) borderPainter = DefaultWidgetsHighlighters.getWidgetForType(controlToHighlight.getClass());
			controlToHighlight.addPaintListener(borderPainter);
			controlToHighlight.redraw();
		}
	}

	/**
	 * Bring back the control to its original status by removing the PaintListener 
	 */
	@Override
	public void deHighLightControl() {
		if (controlToHighlight != null && borderPainter != null && !controlToHighlight.isDisposed()){
			controlToHighlight.removePaintListener(borderPainter);
			controlToHighlight.redraw();
		}
	}

}
