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
package com.jaspersoft.studio.handlers;

import net.sf.jasperreports.eclipse.ui.util.ExceptionDetailsErrorDialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Class to display an error dialog in Eclipse Style, with a message and a detail
 * button to see more information. The extension was done because in the default class
 * is not possible to personalize properly the detail section
 * 
 * @author Orlandin Marco
 *
 */
public class ConflictDetailsError extends ExceptionDetailsErrorDialog{
	
	/**
	 * Build the class
	 * 
	 * @param parentShell shell of the parent
	 * @param dialogTitle title of the dialog
	 * @param message message of the dialog
	 * @param status status, contains the message to show in the detail section
	 * @param displayMask
	 */
	public ConflictDetailsError(Shell parentShell, String dialogTitle, String message, IStatus status, int displayMask) {
		super(parentShell, dialogTitle, message, status, displayMask);
	}

	/**
	 * Create the detail section with the message extracted from the status
	 */
	@Override
	protected void populateList(Text listToPopulate, IStatus buildingStatus,
			int nesting, boolean includeStatus) {
			String message = buildingStatus.getMessage();
			listToPopulate.append(message);
	}
	
	/**
	 * The main message is a void string if it is null, otherwise it is the 
	 * message passed as parameter
	 */
	public String buildMessage(String message, IStatus status) {
		String msg = message == null ? "" : message; //$NON-NLS-1$
		return msg;
	}
	
	/**
	 * force to show by default the detail section
	 */
	protected boolean shouldShowDetailsButton() {
		return true;
	}
}
