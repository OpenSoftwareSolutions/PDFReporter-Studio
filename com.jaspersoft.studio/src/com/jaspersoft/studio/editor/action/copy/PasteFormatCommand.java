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

import java.util.List;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.SetValueCommand;

public class PasteFormatCommand extends Command {

	/**
	 * List of commands executed to copy the appearance of an element into 
	 * one or more elements
	 */
	protected JSSCompoundCommand executedCommands;
	
	/**
	 * Nodes where the appearance is pasted
	 */
	protected List<APropertyNode> editedNodes;

	public PasteFormatCommand(List<APropertyNode> editedNodes) {
		this.editedNodes = editedNodes;
		executedCommands = null;
	}

	@Override
	public boolean canExecute() {
		return !editedNodes.isEmpty() && CopyFormatAction.hasCopiedValues();
	}

	@Override
	public void execute() {
		//The list of nodes is not empty for the canExecuted
		executedCommands = new JSSCompoundCommand(editedNodes.get(0));
		for(APropertyNode node : editedNodes){
			generateCommandsForNode(node, executedCommands);
		}
		executedCommands.execute();
	}
	
	/**
	 * Generate the sets of SetValueCommand, one for every copied property, to 
	 * set the appearance of the target node
	 * 
	 * @param node the target node
	 * @param commands the container for the commands
	 */
	private void generateCommandsForNode(APropertyNode node, JSSCompoundCommand commands){
		for(String propString : CopyFormatAction.propertyNames){
			Object copiedValue = CopyFormatAction.getCopiedValues(propString);
			SetValueCommand setCommand = new SetValueCommand();
			setCommand.setTarget(node);
			setCommand.setPropertyId(propString);
			setCommand.setPropertyValue(copiedValue);
			commands.add(setCommand);
		}
	}

	@Override
	public void redo() {
		executedCommands.redo();
	}

	@Override
	public boolean canUndo() {
		return executedCommands.canUndo();
	}

	@Override
	public void undo() {
		executedCommands.undo();
	}

}
