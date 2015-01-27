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

/**
 * Interface to declare that a class has the capability to graphically highlight
 * in some way a control
 * 
 * @author Orlandin Marco
 *
 */
public interface IHighlightControl {
	
	/**
	 * highlight the control
	 */
	public void highLightControl();
	
	/**
	 * Restore the highlighted control to its original status
	 */
	public void deHighLightControl();
}
