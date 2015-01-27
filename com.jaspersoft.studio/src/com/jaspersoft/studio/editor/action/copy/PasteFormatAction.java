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
package com.jaspersoft.studio.editor.action.copy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;

/**
 * Action to paste the appearance of an element on a set of 
 * other elements 
 * 
 * @author Orlandin Marco
 *
 */
public class PasteFormatAction extends ACachedSelectionAction {

	public static final String ID = "PasteFormatAction"; //$NON-NLS-1$
	
	public PasteFormatAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		setText(Messages.PasteFormatAction_title);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/paste_format.png")); //$NON-NLS-1$
		setEnabled(false);
	}


	@Override
	public void run() {
		execute(createCommand());
	}
	
	@Override
	protected Command createCommand() {
		if (!CopyFormatAction.hasCopiedValues()) return null;
		List<APropertyNode> selectedNodes = getNodes();
		if (selectedNodes.isEmpty()) return null;
		PasteFormatCommand command = new PasteFormatCommand(selectedNodes);
		return command;
	};

	/**
	 * Return the list of APropertyNode inside the selection
	 * 
	 * @param selectedObjects the actual selection
	 * @return a not null list of APropertyNode
	 */
	protected List<APropertyNode> getNodes() {
		List<APropertyNode> result = new ArrayList<APropertyNode>();
		List<Object> nodes = editor.getSelectionCache().getSelectionModelForType(APropertyNode.class);
		for (Object it : nodes) {
			// Before to add an element it is checked if its nested, this is done to avoid to copy twice an element because
			// it is also directly selected with also its container (ie a frame) selected
			result.add((APropertyNode)it);
		}
		return result;
	}
}
