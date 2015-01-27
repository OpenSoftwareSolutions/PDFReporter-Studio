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
package com.jaspersoft.studio.toolbars;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.callout.action.CreatePinAction;

/**
 * Create the toolbar button to create a ping
 * 
 * @author Orlandin Marco
 *
 */
public class CreationContributionItem extends CommonToolbarHandler {

	@Override
	protected Control createControl(Composite parent) {
		super.createControl(parent);
		ToolBar buttons = new ToolBar(parent, SWT.FLAT | SWT.WRAP);
		
		ToolItem createPin = new ToolItem(buttons, SWT.PUSH);
		createPin.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/pin-16.png"));
		createPin.addSelectionListener(new SelectionAdapter() {
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<Object> selection = getSelectionForType(MCallout.class);
				if (selection.size() == 1){
					Command cmd = CreatePinAction.getCreationCommand((MCallout)selection.get(0));
					if (cmd != null){
						getCommandStack().execute(cmd);;
					}
				}
			}
		});
		
		return buttons;
	}
	
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) return false;
		List<Object> selection = getSelectionForType(MCallout.class);
		return selection.size() == 1;
	}
}
