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

public class EqualsHSpaceAction extends AbstractFormattingAction{

	/** The Constant ID. */
	public static final String ID = "samehspace"; //$NON-NLS-1$
	
	public EqualsHSpaceAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.EqualsHSpaceAction_actionName);
		setToolTipText(Messages.EqualsHSpaceAction_actionDescription);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/elem_add_hspace.png"));  //$NON-NLS-1$
	}

	@Override
	protected boolean calculateEnabled() {
		return getOperationSet().size()>1;
	}
	
	public static JSSCompoundCommand generateCommand(List<APropertyNode> nodes){
		JSSCompoundCommand command = new JSSCompoundCommand(null);    

    if (nodes.isEmpty()) return command;
    List<APropertyNode> sortedElements = sortXY( nodes );
    
    int gap = 0;
    int usedSpace = 0;
    JRDesignElement jrElement = (JRDesignElement)sortedElements.get(0).getValue();
    int minX = jrElement.getX();
    int maxX = minX + jrElement.getWidth();
    for (APropertyNode element : sortedElements)
    {
    		command.setReferenceNodeIfNull(element);
    		jrElement = (JRDesignElement)element.getValue();
        if (minX > jrElement.getX()) minX = jrElement.getX();
        if (maxX < jrElement.getX()+jrElement.getWidth()) maxX = jrElement.getX()+jrElement.getWidth();
        usedSpace += jrElement.getWidth();
    }
    
    gap = (maxX - minX - usedSpace)/(nodes.size()-1);
    
    int actualX = minX;
    
    for (int i=0; i<sortedElements.size(); ++i)
    {
    		APropertyNode element = sortedElements.get(i);
        jrElement = (JRDesignElement)element.getValue();
        if (i == 0) {
            actualX = jrElement.getX() + jrElement.getWidth() + gap;
            continue;
        }
        int setX;
        if (i == sortedElements.size() - 1)
        {
            // Trick to avoid calculations errors.
        		setX = maxX - jrElement.getWidth();  
        }
        else
        {
        		setX = actualX;
        }
        
        SetValueCommand setCommand = new SetValueCommand();
  			setCommand.setTarget(element);
  			setCommand.setPropertyId(JRDesignElement.PROPERTY_X);
  			setCommand.setPropertyValue(setX);
  			command.add(setCommand);
        
        actualX = setX + jrElement.getWidth() + gap;
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
