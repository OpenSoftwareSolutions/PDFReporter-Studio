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
package com.jaspersoft.studio.properties.internal;

import org.eclipse.swt.widgets.Control;

/**
 * Interface to implement if a widget can be highlighted by the search widget function
 * 
 * @author Orlandin Marco
 *
 */
public interface IHighlightPropertyWidget {
	
	/**
	 * Highlight the widget, for a specific time
	 * 
	 * @param ms time to wait to have the widget return to normal
	 */
	public void highLightWidget(long ms);
	
	/**
	 * Return the control that will be highlighted
	 */
	public Control getControlToBorder();
}
