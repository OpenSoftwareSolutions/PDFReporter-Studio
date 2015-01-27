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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.callout.pin.MPin;
import com.jaspersoft.studio.callout.pin.MPinConnection;

public class CreatePinCommand extends Command {
	private Rectangle location;
	private MCallout parent;
	private MPin mpin;

	public CreatePinCommand(MCallout parent, Rectangle location) {
		super("Create Pin");
		this.location = location;
		this.parent = parent;
	}

	@Override
	public void execute() {
		if (mpin == null)
			createObject();
		else {
			parent.addPinConnection(mpin.getSourceConnections());
			mpin.setParent(parent, -1);
			parent.addChild(mpin.getSourceConnections());
		}
		parent.setPropertyValue("", "");
	}

	private void createObject() {
		mpin = new MPin(parent, new Point(location.x, location.y));
		new MPinConnection(parent, mpin);
	}

	@Override
	public void undo() {
		parent.removePinConnection(mpin.getSourceConnections());
		parent.removeChild(mpin);
		parent.removeChild(mpin.getSourceConnections());
		parent.setPropertyValue("", "");
	}

	@Override
	public boolean canExecute() {
		return true;
	}
}
