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

public class RemoveVSpaceAction extends AbstractFormattingAction {

	/** The Constant ID. */
	public static final String ID = "removevspace"; //$NON-NLS-1$
	
	public RemoveVSpaceAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.RemoveVSpaceAction_actionName);
		setToolTipText(Messages.RemoveVSpaceAction_actionDescription);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/elem_add_vspace_zero.png"));  //$NON-NLS-1$
	}
	
	public static JSSCompoundCommand generateCommand(List<APropertyNode> nodes){
		JSSCompoundCommand command = new JSSCompoundCommand(null);

    if (nodes.isEmpty()) return command;
    List<APropertyNode> sortedElements = sortYX( nodes );

    JRDesignElement jrElement = (JRDesignElement)sortedElements.get(0).getValue();
    int current_y = jrElement.getY() + jrElement.getHeight();

    for (int i=1; i<sortedElements.size(); ++i)
    {
        APropertyNode element = sortedElements.get(i);
        command.setReferenceNodeIfNull(element);
        jrElement = (JRDesignElement)element.getValue();
        SetValueCommand setCommand = new SetValueCommand();
  			setCommand.setTarget(element);
  			setCommand.setPropertyId(JRDesignElement.PROPERTY_Y);
  			setCommand.setPropertyValue(current_y);
	      command.add(setCommand);
        current_y += jrElement.getHeight();
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
