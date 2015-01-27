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
package com.jaspersoft.studio.swt.events;

import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Widget;

import com.jaspersoft.studio.swt.widgets.WColorPicker;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * The event that should be notified/received when dealing with color selection widgets, like for example the {@link WColorPicker}.
 * 
 * @author mrabbi
 * 
 * @see ColorSelectionListener
 * @see WColorPicker
 *
 */
public class ColorSelectedEvent extends TypedEvent{

	private static final long serialVersionUID = 1849191962501917969L;
	public AlfaRGB selectedColor = null;

	public ColorSelectedEvent(Widget widget){
		super(widget);
	}
	
}
