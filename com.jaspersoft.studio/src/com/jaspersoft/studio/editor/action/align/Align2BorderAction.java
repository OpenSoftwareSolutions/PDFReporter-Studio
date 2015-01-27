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
package com.jaspersoft.studio.editor.action.align;

import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.compatibility.ToolUtilitiesCompatibility;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.editor.action.IGlobalAction;
import com.jaspersoft.studio.editor.gef.commands.AlignCommand;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MGraphicElement;

public class Align2BorderAction extends ACachedSelectionAction implements IGlobalAction {

	/**
	 * Indicates that the bottom edges should be aligned.
	 */
	public static final String ID_ALIGN_BOTTOM = "band_" + GEFActionConstants.ALIGN_BOTTOM; //$NON-NLS-1$

	/**
	 * Indicates that the horizontal centers should be aligned.
	 */
	public static final String ID_ALIGN_CENTER = "band_" + GEFActionConstants.ALIGN_CENTER; //$NON-NLS-1$

	/**
	 * Indicates that the left edges should be aligned.
	 */
	public static final String ID_ALIGN_LEFT = "band_" + GEFActionConstants.ALIGN_LEFT; //$NON-NLS-1$

	/**
	 * Indicates that the vertical midpoints should be aligned.
	 */
	public static final String ID_ALIGN_MIDDLE = "band_" + GEFActionConstants.ALIGN_MIDDLE; //$NON-NLS-1$

	/**
	 * Indicates that the right edges should be aligned.
	 */
	public static final String ID_ALIGN_RIGHT = "band_" + GEFActionConstants.ALIGN_RIGHT; //$NON-NLS-1$

	/**
	 * Indicates that the top edges should be aligned.
	 */
	public static final String ID_ALIGN_TOP = "band_" + GEFActionConstants.ALIGN_TOP; //$NON-NLS-1$
	
	private int alignment;


	/**
	 * Constructs an AlignmentAction with the given part and alignment ID. The alignment ID must by one of:
	 * <UL>
	 * <LI>GEFActionConstants.ALIGN_LEFT
	 * <LI>GEFActionConstants.ALIGN_RIGHT
	 * <LI>GEFActionConstants.ALIGN_CENTER
	 * <LI>GEFActionConstants.ALIGN_TOP
	 * <LI>GEFActionConstants.ALIGN_BOTTOM
	 * <LI>GEFActionConstants.ALIGN_MIDDLE
	 * </UL>
	 * 
	 * @param part
	 *          the workbench part used to obtain context
	 * @param align
	 *          the aligment ID.
	 */
	public Align2BorderAction(IWorkbenchPart part, int align) {
		super(part);
		alignment = align;
		initUI();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Command createCommand() {
		List<EditPart> editparts = editor.getSelectionCache().getSelectionModelPartForType(MGraphicElement.class);

		if (editparts.isEmpty())
			return null;

		JSSCompoundCommand command = new JSSCompoundCommand(null);
		
		editparts = (List<EditPart>)ToolUtilitiesCompatibility.getSelectionWithoutDependants(editparts);
		
		command.setDebugLabel(getText());
		for (int i = 0; i < editparts.size(); i++) {
			EditPart editpart = (EditPart) editparts.get(i);
			if (editpart.getModel() instanceof MGraphicElement){
				command.add(new AlignCommand(alignment, editpart));
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
		case PositionConstants.LEFT:
			setId(ID_ALIGN_LEFT);
			setText(Messages.Align2BorderAction_align_to_left);
			setToolTipText(Messages.Align2BorderAction_align_to_left_tool_tip);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/align-band-left.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/disabled/align-band-left.gif")); //$NON-NLS-1$ 
			break;
		case PositionConstants.RIGHT:
			setId(ID_ALIGN_RIGHT);
			setText(Messages.Align2BorderAction_align_to_right);
			setToolTipText(Messages.Align2BorderAction_align_to_right_tool_tip);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/align-band-right.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/disabled/align-band-right.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.TOP:
			setId(ID_ALIGN_TOP);
			setText(Messages.Align2BorderAction_align_to_top);
			setToolTipText(Messages.Align2BorderAction_align_to_top_tool_tip);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/align-band-top.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/disabled/align-band-top.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.BOTTOM:
			setId(ID_ALIGN_BOTTOM);
			setText(Messages.Align2BorderAction_align_to_bottom);
			setToolTipText(Messages.Align2BorderAction_align_to_bottom_tool_tip);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/align-band-bottom.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/disabled/align-band-bottom.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.CENTER:
			setId(ID_ALIGN_CENTER);
			setText(Messages.Align2BorderAction_align_to_center);
			setToolTipText(Messages.Align2BorderAction_align_to_center_tool_tip);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/align-band-center.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/disabled/align-band-center.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.MIDDLE:
			setId(ID_ALIGN_MIDDLE);
			setText(Messages.Align2BorderAction_align_to_middle);
			setToolTipText(Messages.Align2BorderAction_align_to_middle_tool_tip);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/align-band-middle.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(
					"icons/resources/eclipse/disabled/align-band-middle.gif")); //$NON-NLS-1$ 
			break;
		}
	}


}
