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
package org.eclipse.wb.swt;

import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;

public class Keyboard {
	/**
	 * Return the hotkey used with the mousewheel to request a zoom operation
	 * 
	 * @return SWT.command is the os is mac, SWT.ctrl otherwise
	 */
	public static int getCtrlKey() {
		return Util.isMac() ? SWT.COMMAND : SWT.CTRL;
	}
}
