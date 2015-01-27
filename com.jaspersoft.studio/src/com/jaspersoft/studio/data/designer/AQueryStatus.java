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
package com.jaspersoft.studio.data.designer;


public abstract class AQueryStatus {
	protected String msg;
	protected Throwable t;

	public abstract void showError(final Throwable t);

	public abstract void showError(final String message, final Throwable t);

	public abstract void showWarning(final String msg);

	public abstract void showInfo(final String msg);

	protected void setMessage(final Throwable t, String message, boolean enabled) {
		this.t = t;
		msg = message;
	}

}
