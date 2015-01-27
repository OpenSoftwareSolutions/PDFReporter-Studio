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
package com.jaspersoft.studio.callout.action;

import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.callout.pin.command.CreatePinCommand;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.messages.Messages;

public class CreatePinAction extends ACachedSelectionAction {
	public static String ID = "com.jaspersoft.studio.callout.action.CreatePinAction"; //$NON-NLS-1$

	public CreatePinAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}
	
	@Override
	protected void init() {
		super.init();
		setText(Messages.CreatePinAction_name);
		setToolTipText(Messages.CreatePinAction_tooltip);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/pin-16.png")); //$NON-NLS-1$
		setEnabled(false);
	}
	
	
	public static CreatePinCommand getCreationCommand(MCallout mcallout){
		Rectangle location = new Rectangle();
		location.x = 20 + (Integer) mcallout.getPropertyValue(JRDesignElement.PROPERTY_X);
		location.y = -24 + (Integer) mcallout.getPropertyValue(JRDesignElement.PROPERTY_Y);
		return new CreatePinCommand(mcallout, location);
	}

	@Override
	protected Command createCommand() {
		List<Object> calloutSelection = editor.getSelectionCache().getSelectionModelForType(MCallout.class);
		if (calloutSelection.isEmpty() || calloutSelection.size() != 1){
			return null;
		}
		MCallout mcallout = (MCallout) calloutSelection.get(0);
		return getCreationCommand(mcallout);
	}

}
