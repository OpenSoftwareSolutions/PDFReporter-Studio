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

import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.IGraphicElementContainer;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.property.SetValueCommand;

public class CenterInParentAction extends AbstractFormattingAction {

	/** The Constant ID. */
	public static final String ID = "centerinparent"; //$NON-NLS-1$
	
	public CenterInParentAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.CenterInParentAction_actionName);
		setToolTipText(Messages.CenterInParentAction_actionDescription);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/align-container-center.png"));  //$NON-NLS-1$
	}
	
	public static JSSCompoundCommand generateCommand(List<APropertyNode> nodes){
		JSSCompoundCommand command = new JSSCompoundCommand(null);
		
		if (nodes.isEmpty()) return command;
    for (APropertyNode element : nodes)
    {
    		command.setReferenceNodeIfNull(element);
        ANode parent = element.getParent();
        
        Dimension parentBounds = null;
    		if (parent instanceof IContainer) {
    			if (parent instanceof MBand) {
    				// height of band, width of Report - margins
    				int h = ((JRDesignBand) ((MBand) parent).getValue()).getHeight();
    				JasperDesign jasperDesign = element.getJasperDesign();
    				int w = jasperDesign.getPageWidth() - jasperDesign.getLeftMargin() - jasperDesign.getRightMargin();
    				parentBounds = new Dimension(w, h);
    			} else if (parent instanceof IGraphicElementContainer){
    				parentBounds = ((IGraphicElementContainer) parent).getSize();
    			}
    		}

        if (parentBounds != null){
	        JRDesignElement jrElement = (JRDesignElement)element.getValue();
		      SetValueCommand setCommand = new SetValueCommand();
	  			setCommand.setTarget(element);
	  			setCommand.setPropertyId(JRDesignElement.PROPERTY_X);
	  			setCommand.setPropertyValue(parentBounds.width / 2 - jrElement.getWidth() / 2);
		      command.add(setCommand);
		      setCommand = new SetValueCommand();
	  			setCommand.setTarget(element);
	  			setCommand.setPropertyId(JRDesignElement.PROPERTY_Y);
	  			setCommand.setPropertyValue(parentBounds.height / 2 - jrElement.getHeight() / 2);
		      command.add(setCommand);
        }
    }
		
		return command;
	}
	
	@Override
	protected Command createCommand() {
		List<APropertyNode> nodes = getOperationSet();
		Command command = null;
		if (!nodes.isEmpty()) {
			command = generateCommand(nodes);
			command.setDebugLabel(getText());
		}
		return command;
	}

	@Override
	protected boolean calculateEnabled() {
		return getOperationSet().size()>0;
	}

}
