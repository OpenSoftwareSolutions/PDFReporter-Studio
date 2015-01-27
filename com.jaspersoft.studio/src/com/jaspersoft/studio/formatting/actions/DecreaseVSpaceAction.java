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

public class DecreaseVSpaceAction extends AbstractFormattingAction{

	/** The Constant ID. */
	public static final String ID = "decreasevspace"; //$NON-NLS-1$
	
	public DecreaseVSpaceAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.DecreaseVSpaceAction_actionName);
		setToolTipText(Messages.DecreaseVSpaceAction_actionDescription);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/elem_add_vspace_min.png"));  //$NON-NLS-1$
	}

	@Override
	protected boolean calculateEnabled() {
		return getOperationSet().size()>1;
	}
	
	public static JSSCompoundCommand generateCommand(List<APropertyNode> nodes){
		JSSCompoundCommand command = new JSSCompoundCommand(null);
		   
		if (nodes.isEmpty()) return command;
		List<APropertyNode> sortedElements = sortYX( nodes );
    
    for (int i=1; i<sortedElements.size(); ++i)
    {
    		APropertyNode element = sortedElements.get(i);
    		command.setReferenceNodeIfNull(element);
    		JRDesignElement jrElement = (JRDesignElement)element.getValue();
        if (jrElement.getY() - 5*i > 0)
        {
	        	SetValueCommand setCommand = new SetValueCommand();
	    			setCommand.setTarget(element);
	    			setCommand.setPropertyId(JRDesignElement.PROPERTY_Y);
	    			setCommand.setPropertyValue(jrElement.getY() - 5*i);
	  	      command.add(setCommand);
        }
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
