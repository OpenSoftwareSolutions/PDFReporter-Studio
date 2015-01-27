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
package com.jaspersoft.studio.editor.report;

/**
 * 
 * Interface for a listener that can be added on the common selection cache to 
 * notify when the selection change
 * 
 * @author Orlandin Marco
 *
 */
public interface SelectionChangedListener {

	/**
	 * Method called when the selection change
	 */
	public void selectionChanged();
	
}
