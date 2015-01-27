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
package com.jaspersoft.studio.editor.dnd;

import org.eclipse.gef.Request;
/*/*
 * The Class NativeDropRequest.
 */
public class NativeDropRequest extends Request {

	/** The data. */
	private Object data;

	/** The Constant ID. */
	public static final String ID = "$Native Drop Request";//$NON-NLS-1$

	/**
	 * Instantiates a new native drop request.
	 */
	public NativeDropRequest() {
		super(ID);
	}

	/**
	 * Instantiates a new native drop request.
	 * 
	 * @param type
	 *          the type
	 */
	public NativeDropRequest(Object type) {
		super(type);
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *          the new data
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
