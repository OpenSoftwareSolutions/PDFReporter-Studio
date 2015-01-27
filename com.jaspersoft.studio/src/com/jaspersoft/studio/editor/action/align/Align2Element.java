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

import java.util.ArrayList;
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
import com.jaspersoft.studio.editor.gef.commands.Align2ElementCommand;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MGraphicElement;

/**
 * This class copy an alignment action to add the primary element checking, to take it as reference to move all the
 * other selected elements
 * 
 * @author Orlandin Marco
 * 
 */
public class Align2Element extends ACachedSelectionAction implements IGlobalAction {

	/**
	 * Indicates that the bottom edges should be aligned.
	 */
	public static final String ID_ALIGN_BOTTOM = GEFActionConstants.ALIGN_BOTTOM;

	/**
	 * Indicates that the horizontal centers should be aligned.
	 */
	public static final String ID_ALIGN_CENTER = GEFActionConstants.ALIGN_CENTER;

	/**
	 * Indicates that the left edges should be aligned.
	 */
	public static final String ID_ALIGN_LEFT = GEFActionConstants.ALIGN_LEFT;

	/**
	 * Indicates that the vertical midpoints should be aligned.
	 */
	public static final String ID_ALIGN_MIDDLE = GEFActionConstants.ALIGN_MIDDLE;

	/**
	 * Indicates that the right edges should be aligned.
	 */
	public static final String ID_ALIGN_RIGHT = GEFActionConstants.ALIGN_RIGHT;

	/**
	 * Indicates that the top edges should be aligned.
	 */
	public static final String ID_ALIGN_TOP = GEFActionConstants.ALIGN_TOP;

	/**
	 * The type of alignment
	 */
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
	public Align2Element(IWorkbenchPart part, int align) {
		super(part);
		alignment = align;
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
		JSSCompoundCommand command = new JSSCompoundCommand(primaryModel);
	
		List<MGraphicElement> selection = new ArrayList<MGraphicElement>();
		for(EditPart part : editparts){
			selection.add((MGraphicElement)part.getModel());
		}
		
		command.setDebugLabel(getText());
		for (int i = 0; i < editparts.size(); i++) {
			command.add(new Align2ElementCommand(alignment, primaryModel, selection));
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


	/**
	 * Initializes the actions UI presentation.
	 */
	protected void initUI() {
		switch (alignment) {
		case PositionConstants.LEFT:
			setId(ID_ALIGN_LEFT);
			setText(Messages.Align2Element_Align_To_Left);
			setToolTipText(Messages.Align2Element_Align_To_Left_tool_tip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/align-left.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/disabled/align-left.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.RIGHT:
			setId(ID_ALIGN_RIGHT);
			setText(Messages.Align2Element_Align_To_Right);
			setToolTipText(Messages.Align2Element_Align_To_Right_tool_tip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/align-right.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/disabled/align-right.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.TOP:
			setId(ID_ALIGN_TOP);
			setText(Messages.Align2Element_Align_To_Top);
			setToolTipText(Messages.Align2Element_Align_To_Top_tool_tip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/align-top.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/disabled/align-top.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.BOTTOM:
			setId(ID_ALIGN_BOTTOM);
			setText(Messages.Align2Element_Align_To_Bottom);
			setToolTipText(Messages.Align2Element_Align_To_Bottom_tool_tip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/align-bottom.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/disabled/align-bottom.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.CENTER:
			setId(ID_ALIGN_CENTER);
			setText(Messages.Align2Element_Align_To_Center);
			setToolTipText(Messages.Align2Element_Align_To_Center_tool_tip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/align-center.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/disabled/align-center.gif")); //$NON-NLS-1$ 
			break;

		case PositionConstants.MIDDLE:
			setId(ID_ALIGN_MIDDLE);
			setText(Messages.Align2Element_Align_To_Middle);
			setToolTipText(Messages.Align2Element_Align_To_Middle_tooltip);
			setImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/align-middle.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(
					JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/disabled/align-middle.gif")); //$NON-NLS-1$ 
			break;
		}
	}

}
