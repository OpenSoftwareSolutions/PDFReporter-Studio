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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.compatibility.ToolUtilitiesCompatibility;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.editor.action.IGlobalAction;
import com.jaspersoft.studio.editor.gef.commands.ResizeCommand;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MGraphicElement;

public class Size2BorderAction extends ACachedSelectionAction implements IGlobalAction {

	/**
	 * Indicates that the bottom edges should be aligned.
	 */
	public static final String ID_SIZE_WIDTH = "band_WIDTH"; //$NON-NLS-1$

	/**
	 * Indicates that the horizontal centers should be aligned.
	 */
	public static final String ID_SIZE_HEIGHT = "band_HEIGHT"; //$NON-NLS-1$

	/**
	 * Indicates that the left edges should be aligned.
	 */
	public static final String ID_SIZE_BOTH = "band_BOTH"; //$NON-NLS-1$

	public final static int WIDTH = 0;
	public final static int HEIGHT = 1;
	public final static int BOTH = 2;

	private int alignment;

	public Size2BorderAction(IWorkbenchPart part, int alignment) {
		super(part);
		this.alignment = alignment;
		initUI();
	}


	@SuppressWarnings("unchecked")
	@Override
	protected Command createCommand() {
		List<EditPart> editparts = editor.getSelectionCache().getSelectionModelPartForType(MGraphicElement.class);
		editparts = (List<EditPart>)ToolUtilitiesCompatibility.getSelectionWithoutDependants(editparts);
		
		if (editparts.isEmpty())
			return null;

		JSSCompoundCommand command = new JSSCompoundCommand(null);

		command.setDebugLabel(getText());
		for (int i = 0; i < editparts.size(); i++) {
			EditPart editpart = (EditPart) editparts.get(i);
			if (editpart.getModel() instanceof MGraphicElement){
				command.add(new ResizeCommand(alignment, editpart));
				command.setReferenceNodeIfNull(editpart.getModel());
			}
		}
		return command;
	}

	/**
	 * Initializes the actions UI presentation.
	 */
	protected void initUI() {
		switch (alignment) {
		case WIDTH:
			setId(ID_SIZE_WIDTH);
			setText(Messages.Size2BorderAction_fit_width);
			setToolTipText(Messages.Size2BorderAction_fit_width_tool_tip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/eclipseapps/size_to_control_width.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/eclipseapps/size_to_control_width.gif")); //$NON-NLS-1$ 
			break;

		case HEIGHT:
			setId(ID_SIZE_HEIGHT);
			setText(Messages.Size2BorderAction_fit_height);
			setToolTipText(Messages.Size2BorderAction_fit_height_tool_tip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/eclipseapps/size_to_control_height.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/eclipseapps/size_to_control_height.gif")); //$NON-NLS-1$ 
			break;

		case BOTH:
			setId(ID_SIZE_BOTH);
			setText(Messages.Size2BorderAction_fit_both);
			setToolTipText(Messages.Size2BorderAction_fit_both_tool_tip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/eclipseapps/size_to_control.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/eclipseapps/size_to_control.gif")); //$NON-NLS-1$ 
			break;
		}
	}

}
