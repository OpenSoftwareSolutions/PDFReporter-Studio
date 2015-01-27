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
import net.sf.jasperreports.engine.design.JRDesignElementGroup;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.model.ANode;

public class DeleteCalloutCommand extends Command {
	private ANode parent;
	private MCallout mcallout;

	public DeleteCalloutCommand(ANode parent, MCallout child) {
		super("Delete Callout");
		this.parent = CreateCalloutCommand.getPropertyHolder((ANode) parent.getRoot());
		this.mcallout = child;
	}

	@Override
	public void execute() {
		mcallout.deleteCallout();
	}

	@Override
	public void undo() {
		mcallout.setParent(parent, -1);
		mcallout.setPropertyValue(JRDesignElement.PROPERTY_X, mcallout.getPropertyValue(JRDesignElement.PROPERTY_X));
		parent.getPropertyChangeSupport()
				.fireIndexedPropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, -1, true, false);
	}

	@Override
	public boolean canExecute() {
		return true;
	}
}
