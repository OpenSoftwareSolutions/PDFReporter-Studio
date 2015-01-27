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
package com.jaspersoft.studio.editor.gef.parts.editPolicy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.ResizeTracker;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.model.ANode;

/**
 * Extends the default resize tracker allowing converting
 * the compound commands into JSSCompoundCommands, that 
 * disable the editor refresh during the execution of the 
 * single commands 
 * 
 * @author Orlandin Marco
 *
 */
public class JSSCompoundResizeTracker extends ResizeTracker {

	public JSSCompoundResizeTracker(GraphicalEditPart owner, int direction) {
		super(owner, direction);
	}

	/**
	 * Search a valid node that has as ancestor a node to disable the 
	 * editor refresh. The node is searched between the selection set
	 * 
	 * @return the node if it is found, null otherwise
	 */
	private ANode getLockableNode(){
		for(Object part : getOperationSet()){
			if (part instanceof EditPart){
				EditPart ePart = (EditPart)part;
				if (ePart.getModel() instanceof ANode){
					ANode mainNode = JSSCompoundCommand.getMainNode((ANode)ePart.getModel());
					if (mainNode != null) return mainNode;
				}
			}
		}
		return null;
	}
	
	/**
	 * If the returned command is a compound command and not a JSS compound command
	 * it is converted into a JSS compound command, otherwise it is returned as it is
	 */
	@Override
	protected Command getCurrentCommand() {
		Command command = super.getCurrentCommand();
		if (!(command instanceof JSSCompoundCommand)){
			if (command instanceof CompoundCommand){
				CompoundCommand cc = (CompoundCommand)command;
				JSSCompoundCommand jsscc = new JSSCompoundCommand(cc,getLockableNode()) ;
				command = jsscc;
			} else {
				JSSCompoundCommand jsscc = new JSSCompoundCommand(getLockableNode());
				jsscc.add(command);
				command = jsscc;
			}
		}
		return command;
		
		
	}
}
