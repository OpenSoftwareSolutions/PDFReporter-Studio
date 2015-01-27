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
package com.jaspersoft.studio.editor.action.size;

import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.compatibility.ToolUtilitiesCompatibility;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.editor.action.IGlobalAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.property.SetValueCommand;

/**
 * Action to match the resize the selected elements as the primary element
 * inside the selection
 * 
 * @author Orlandin Marco
 *
 */
public class MatchSizeAction extends ACachedSelectionAction implements IGlobalAction {
	
	/**
	 * The type of the resize
	 */
	private TYPE resizeType;
	
	public enum TYPE{WIDTH, HEIGHT, BOTH};
	
	/**
	 * Indicates that the bottom edges should be aligned.
	 */
	public static final String ID_SIZE_WIDTH = "size_WIDTH"; //$NON-NLS-1$

	/**
	 * Indicates that the horizontal centers should be aligned.
	 */
	public static final String ID_SIZE_HEIGHT = "size_HEIGHT"; //$NON-NLS-1$

	/**
	 * Indicates that the left edges should be aligned.
	 */
	public static final String ID_SIZE_BOTH = "size_BOTH"; //$NON-NLS-1$

	public MatchSizeAction(IWorkbenchPart part, TYPE type) {
		super(part);
		this.resizeType = type;
		initUI();
	}

	/**
	 * Create the alignment command for the selected elements
	 * 
	 * @return the alignment command
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Command createCommand() {
		List<EditPart> editparts = editor.getSelectionCache().getSelectionModelPartForType(MGraphicElement.class);
		editparts = (List<EditPart>)ToolUtilitiesCompatibility.getSelectionWithoutDependants(editparts);
		EditPart primary = getPrimary(editparts);

		if (editparts.size() < 2 || primary == null)
			return null;

		MGraphicElement primaryModel = (MGraphicElement)primary.getModel();
		JRDesignElement primaryElement = primaryModel.getValue();
		JSSCompoundCommand command = new JSSCompoundCommand(primaryModel);
		command.setDebugLabel(getText());
		
		switch (resizeType) {
		case WIDTH:
			for (EditPart part : editparts) {
				APropertyNode model = (APropertyNode)part.getModel();
				command.add(createCommand(JRDesignElement.PROPERTY_WIDTH, model, primaryElement.getWidth()));
			}
			break;
		case HEIGHT:
			for (EditPart part : editparts) {
				APropertyNode model = (APropertyNode)part.getModel();
				command.add(createCommand(JRDesignElement.PROPERTY_HEIGHT, model, primaryElement.getHeight()));
			}
			break;
		case BOTH:
			for (EditPart part : editparts) {
				APropertyNode model = (APropertyNode)part.getModel();
				command.add(createCommand(JRDesignElement.PROPERTY_WIDTH, model, primaryElement.getWidth()));
				command.add(createCommand(JRDesignElement.PROPERTY_HEIGHT, model, primaryElement.getHeight()));
			}
			break;
		}
		return command;
	}
	

	/**
	 * Return the primary object of the selections, or the last object if none of them is the primary
	 * 
	 * @param editparts
	 *          List of selected objects
	 * @return The primary object or a substitute if itsn't present
	 */
	protected EditPart getPrimary(List<EditPart> editparts) {
		EditPart partialResult = null;
		for (EditPart part : editparts){
			if (part.getModel() instanceof MGraphicElement){
				partialResult = part;
				if (partialResult.getSelected() == EditPart.SELECTED_PRIMARY) {
					break;
				}
			}
		}
		return partialResult;
	}
	
	
	private SetValueCommand createCommand(String property, APropertyNode destination, Object value){
		SetValueCommand newCommand = new SetValueCommand();
		newCommand.setTarget(destination);
		newCommand.setPropertyValue(value);
		newCommand.setPropertyId(property);
		return newCommand;
	}
	
	/**
	 * Initializes the actions UI presentation.
	 */
	protected void initUI() {
		switch (resizeType) {
		case WIDTH:
			setId(ID_SIZE_WIDTH);
			setText(Messages.MatchSizeAction_widthText);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/matchwidth.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/matchwidth_d.gif")); //$NON-NLS-1$
			setToolTipText(Messages.MatchSizeAction_widthTooltip);
			break;

		case HEIGHT:
			setId(ID_SIZE_HEIGHT);
			setText(Messages.MatchSizeAction_heightText);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/matchheight.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/matchheight_d.gif")); //$NON-NLS-1$
			setToolTipText(Messages.MatchSizeAction_heightTooltip);
			break;

		case BOTH:
			setId(ID_SIZE_BOTH);
			setText(Messages.MatchSizeAction_match_size);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/match-size.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/disabled/match-size.gif")); //$NON-NLS-1$
			setToolTipText(Messages.MatchSizeAction_match_size_tool_tip);
			break;
		}
	}
}
