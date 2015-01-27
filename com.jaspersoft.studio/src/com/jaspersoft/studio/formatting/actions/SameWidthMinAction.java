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

import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.SetValueCommand;

public class SameWidthMinAction extends AbstractFormattingAction {

	/** The Constant ID. */
	public static final String ID = "matchwidthmin"; //$NON-NLS-1$

	public SameWidthMinAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.SameWidthMinAction_actionName);
		setToolTipText(Messages.SameWidthMinAction_actionDescription);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/matchwidthmin.png")); //$NON-NLS-1$
	}

	@Override
	protected boolean calculateEnabled() {
		return getOperationSet().size() > 1;
	}

	public static JSSCompoundCommand generateCommand(List<APropertyNode> nodes) {
		JSSCompoundCommand command = new JSSCompoundCommand(null);

		int width = (Integer) nodes.get(0).getPropertyValue(JRDesignElement.PROPERTY_WIDTH);

		// Find the smallest one...
		for (int i = 1; i < nodes.size(); ++i) {
			if (nodes.get(i).getValue() instanceof JRDesignElement) {
				JRDesignElement element = (JRDesignElement) nodes.get(i).getValue();
				if (width > element.getWidth())
					width = element.getWidth();
			} else if (nodes.get(i).getValue() instanceof StandardColumn) {
				StandardColumn element = (StandardColumn) nodes.get(i).getValue();
				if (width > element.getWidth())
					width = element.getWidth();
			}
		}

		for (APropertyNode node : nodes) {
			command.setReferenceNodeIfNull(node);
			SetValueCommand setCommand = new SetValueCommand();
			setCommand.setTarget(node);
			setCommand.setPropertyId(JRDesignElement.PROPERTY_WIDTH);
			setCommand.setPropertyValue(width);
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
