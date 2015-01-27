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
package com.jaspersoft.studio.data.reader;

import net.sf.jasperreports.engine.JRScriptletException;

/**
 * Custom exception that should be raised when a data preview task is interrupted.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class DataPreviewInterruptedException extends JRScriptletException {
	
	private static final long serialVersionUID = 1L;
	
	public DataPreviewInterruptedException(String message) {
		super(message);
	}

}
