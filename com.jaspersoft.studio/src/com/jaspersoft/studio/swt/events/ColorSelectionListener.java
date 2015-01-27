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

import java.util.EventListener;

import com.jaspersoft.studio.swt.widgets.WColorPicker;

/**
 * This listener is meant to be used when dealing with color selection widgets, like for example the {@link WColorPicker}.
 *  
 * @author mrabbi
 * 
 * @see ColorSelectedEvent
 * @see WColorPicker
 *
 */
public interface ColorSelectionListener extends EventListener{
	void changed(ColorSelectedEvent e);
}
