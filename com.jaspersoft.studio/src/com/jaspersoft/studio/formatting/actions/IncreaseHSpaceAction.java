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
package com.jaspersoft.studio.formatting.actions;

import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.SetValueCommand;

public class IncreaseHSpaceAction extends AbstractFormattingAction {

	/** The Constant ID. */
	public static final String ID = "increasehspace"; //$NON-NLS-1$
	
	public IncreaseHSpaceAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.IncreaseHSpaceAction_actionName);
		setToolTipText(Messages.IncreaseHSpaceAction_actionDescription);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/elem_add_hspace_plus.png"));  //$NON-NLS-1$
	}
	
	public static JSSCompoundCommand generateCommand(List<APropertyNode> nodes){
		JSSCompoundCommand command = new JSSCompoundCommand(null);
		
		if (nodes.isEmpty()) return command;
		List<APropertyNode> sortedElements = sortXY(nodes);
    
    for (int i=1; i<sortedElements.size(); ++i)
    {
    		APropertyNode actualNode = sortedElements.get(i);
    		command.setReferenceNodeIfNull(actualNode);
        JRDesignElement element = (JRDesignElement)actualNode.getValue();
	      SetValueCommand setCommand = new SetValueCommand();
  			setCommand.setTarget(actualNode);
  			setCommand.setPropertyId(JRDesignElement.PROPERTY_X);
  			setCommand.setPropertyValue(element.getX() + 5*i);
	      command.add(setCommand);
    }
		
		return command;
	}

	@Override
	protected Command createCommand() {
		List<APropertyNode> nodes = getOperationSet();
		JSSCompoundCommand command = null;
		if (!nodes.isEmpty()){
			command = generateCommand(nodes);
			command.setDebugLabel(getText());
		}
		return command;
	}

	@Override
	protected boolean calculateEnabled() {
		return getOperationSet().size()>1;
	}

}
