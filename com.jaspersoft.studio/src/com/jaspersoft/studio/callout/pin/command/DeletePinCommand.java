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
package com.jaspersoft.studio.callout.pin.command;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.callout.pin.MPin;

public class DeletePinCommand extends Command {
	private MCallout parent;
	private MPin mpin;

	public DeletePinCommand(MCallout parent, MPin child) {
		super("Delete Pin");
		this.parent = parent;
		this.mpin = child;
	}

	@Override
	public void execute() {
		parent.removePinConnection(mpin.getSourceConnections());
		parent.removeChild(mpin);
		parent.removeChild(mpin.getSourceConnections());
		parent.setPropertyValue("", "");
	}

	@Override
	public void undo() {
		parent.addPinConnection(mpin.getSourceConnections());
		mpin.setParent(parent, -1);
		parent.addChild(mpin.getSourceConnections());
		parent.setPropertyValue("", "");
	}

	@Override
	public boolean canExecute() {
		return true;
	}
}
