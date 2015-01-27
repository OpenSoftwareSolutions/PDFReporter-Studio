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
package com.jaspersoft.studio.editor.palette;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.jface.dialogs.Dialog;

import com.jaspersoft.studio.model.DialogEnabledCommand;

/**
 * Custom creation tool that add support for dialog prompted during the creation of an element dragged from the palette.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 * @see DialogEnabledCommand
 * 
 */
public final class JDCreationTool extends CreationTool {

	public JDCreationTool() {
		super();
	}

	public JDCreationTool(CreationFactory aFactory) {
		super(aFactory);
	}

	@Override
	protected void performCreation(int button) {
		Command currCommand = getCurrentCommand();
		if (currCommand instanceof DialogEnabledCommand) {
			// If we have a special command that supports dialog (i.e: image creation)
			// we'll show the popup dialog and continue with creation only if
			// the user has confirmed.
			if (((DialogEnabledCommand) currCommand).openDialog() == Dialog.CANCEL) {
				return;
			}

			// we have to execute on the same command, because between getCurrentCommand , mouse events call setCurrentCommand
			if (currCommand != null && currCommand.canExecute())
				executeCommand(currCommand);
			setCurrentCommand(null);
		}
		super.performCreation(button);
	}

}
