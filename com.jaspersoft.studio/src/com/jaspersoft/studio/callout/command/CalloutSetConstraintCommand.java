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
package com.jaspersoft.studio.callout.command;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.callout.MCallout;

public class CalloutSetConstraintCommand extends Command {
	private MCallout mcallout;
	private Rectangle location;
	private Rectangle oldLocation;

	public CalloutSetConstraintCommand(MCallout mcallout, Rectangle location) {
		super("Move or Resize a Callout");
		this.mcallout = mcallout;
		this.location = location;
	}

	@Override
	public void execute() {
		if (oldLocation == null) {
			oldLocation = new Rectangle();
			oldLocation.x = (Integer) mcallout.getPropertyValue(JRDesignElement.PROPERTY_X);
			oldLocation.y = (Integer) mcallout.getPropertyValue(JRDesignElement.PROPERTY_Y);
			oldLocation.width = (Integer) mcallout.getPropertyValue(JRDesignElement.PROPERTY_WIDTH);
			oldLocation.height = (Integer) mcallout.getPropertyValue(JRDesignElement.PROPERTY_HEIGHT);
		}

		mcallout.setPropertyValue(JRDesignElement.PROPERTY_X, location.x);
		mcallout.setPropertyValue(JRDesignElement.PROPERTY_Y, location.y);
		mcallout.setPropertyValue(JRDesignElement.PROPERTY_WIDTH, location.width);
		mcallout.setPropertyValue(JRDesignElement.PROPERTY_HEIGHT, location.height);

		mcallout.getPropertyChangeSupport().firePropertyChange(JRDesignElement.PROPERTY_X, true, false);
	}

	@Override
	public void undo() {
		mcallout.setPropertyValue(JRDesignElement.PROPERTY_X, oldLocation.x);
		mcallout.setPropertyValue(JRDesignElement.PROPERTY_Y, oldLocation.y);
		mcallout.setPropertyValue(JRDesignElement.PROPERTY_WIDTH, oldLocation.width);
		mcallout.setPropertyValue(JRDesignElement.PROPERTY_HEIGHT, oldLocation.height);

		mcallout.getPropertyChangeSupport().firePropertyChange(JRDesignElement.PROPERTY_X, true, false);
	}
}
