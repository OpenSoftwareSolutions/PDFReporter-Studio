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

public class DecreaseHSpaceAction extends AbstractFormattingAction{

	/** The Constant ID. */
	public static final String ID = "decreasehspace"; //$NON-NLS-1$
	
	public DecreaseHSpaceAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.DecreaseHSpaceAction_actionName);
		setToolTipText(Messages.DecreaseHSpaceAction_actionDescription);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/elem_add_hspace_min.png"));  //$NON-NLS-1$
	}

	@Override
	protected boolean calculateEnabled() {
		return getOperationSet().size()>1;
	}
	
	public static JSSCompoundCommand generateCommand(List<APropertyNode> nodes){
		JSSCompoundCommand command = new JSSCompoundCommand(null);
		   
		if (nodes.isEmpty()) return command;
	  nodes = sortXY(nodes);
    
    for (int i=1; i<nodes.size(); ++i)
    {
    		APropertyNode node = nodes.get(i);
    		command.setReferenceNodeIfNull(node);
        JRDesignElement element = (JRDesignElement)node.getValue();
      	SetValueCommand setCommand = new SetValueCommand();
  			setCommand.setTarget(node);
  			setCommand.setPropertyId(JRDesignElement.PROPERTY_X);
  			setCommand.setPropertyValue(element.getX() - 5*i);
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
	
}
