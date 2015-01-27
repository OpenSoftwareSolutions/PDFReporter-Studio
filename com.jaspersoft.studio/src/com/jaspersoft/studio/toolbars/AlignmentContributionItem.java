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
package com.jaspersoft.studio.toolbars;

import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.gef.commands.AlignCommand;
import com.jaspersoft.studio.formatting.actions.JoinLeftAction;
import com.jaspersoft.studio.formatting.actions.JoinRightAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElement;

/**
 * Toolbar controls to align the selected elements
 * 
 * @author Orlandin Marco
 *
 */
public class AlignmentContributionItem extends CommonToolbarHandler{
	
	/**
	 * Enumaration for the join buttons pressed, to know if the pressed button is
	 * join left or join right
	 */
	private enum JOIN_DIRECTION{LEFT, RIGHT};
	
	/**
	 * Selection listener that create the right command when a button is pushed
	 */
	private SelectionAdapter alignButtonPressed = new SelectionAdapter() {
		
		public void widgetSelected(SelectionEvent e) {
			List<Object> selection = getSelectionForType(MGraphicElement.class);
			if (selection.isEmpty())
				return;
			
			Integer alignment = (Integer)e.widget.getData();
			JSSCompoundCommand command = new JSSCompoundCommand("Align Command", null);
			for (Object model : selection) {
				command.add(new AlignCommand(alignment, (MGraphicElement)model));
				command.setReferenceNodeIfNull(model);
			}
			getCommandStack().execute(command);
		}
	};
	
	/**
	 * Selection listener that create the right command when a button is pushed
	 */
	private SelectionAdapter joinButtonPressed = new SelectionAdapter() {
	
		public void widgetSelected(SelectionEvent e) {
			List<Object> selection = getSelectionForType(MGraphicElement.class);
			if (selection.isEmpty())
				return;
			JSSCompoundCommand command = null;
			@SuppressWarnings("unchecked")
			List<APropertyNode> typedSelection = (List<APropertyNode>)(List<?>)selection;
			if (JOIN_DIRECTION.LEFT.equals(e.widget.getData())){
				command = JoinLeftAction.generateCommand(typedSelection);
			} else if (JOIN_DIRECTION.RIGHT.equals(e.widget.getData())){
				command = JoinRightAction.generateCommand(typedSelection);
			}
			if (command != null) getCommandStack().execute(command);
		}
	};
	

	@Override
	protected Control createControl(Composite parent) {
		super.createControl(parent);
		ToolBar buttons = new ToolBar(parent, SWT.FLAT | SWT.WRAP);
		
		ToolItem button = new ToolItem(buttons, SWT.PUSH);
		button.setToolTipText(Messages.Align2BorderAction_align_to_left_tool_tip);
		button.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/align-band-left.gif"));
		button.setData(new Integer(PositionConstants.LEFT));
		button.addSelectionListener(alignButtonPressed);
		
		button = new ToolItem(buttons, SWT.PUSH);
		button.setToolTipText(Messages.Align2BorderAction_align_to_right_tool_tip);
		button.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/align-band-right.gif"));
		button.setData(new Integer(PositionConstants.RIGHT));
		button.addSelectionListener(alignButtonPressed);

		button = new ToolItem(buttons, SWT.PUSH);
		button.setToolTipText(Messages.Align2BorderAction_align_to_top_tool_tip);
		button.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/align-band-top.gif"));
		button.setData(new Integer(PositionConstants.TOP));
		button.addSelectionListener(alignButtonPressed);

		button = new ToolItem(buttons, SWT.PUSH);
		button.setToolTipText(Messages.Align2BorderAction_align_to_bottom_tool_tip);
		button.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/align-band-bottom.gif"));
		button.setData(new Integer(PositionConstants.BOTTOM));
		button.addSelectionListener(alignButtonPressed);
		
		button = new ToolItem(buttons, SWT.PUSH);
		button.setToolTipText(Messages.Align2BorderAction_align_to_center_tool_tip);
		button.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/align-band-center.gif"));
		button.setData(new Integer(PositionConstants.CENTER));
		button.addSelectionListener(alignButtonPressed);

		button = new ToolItem(buttons, SWT.PUSH);
		button.setToolTipText(Messages.Align2BorderAction_align_to_middle_tool_tip);
		button.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/align-band-middle.gif"));
		button.setData(new Integer(PositionConstants.MIDDLE));
		button.addSelectionListener(alignButtonPressed);
		
		button = new ToolItem(buttons, SWT.PUSH);
		button.setToolTipText(Messages.JoinLeftAction_actionDescription);
		button.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/joinleft.png"));
		button.setData(JOIN_DIRECTION.LEFT);
		button.addSelectionListener(joinButtonPressed);
		
		button = new ToolItem(buttons, SWT.PUSH);
		button.setToolTipText(Messages.JoinRightAction_actionDescription);
		button.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/joinright.png"));
		button.setData(JOIN_DIRECTION.RIGHT);
		button.addSelectionListener(joinButtonPressed);
		
		return buttons;
	}
	
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) return false;
		List<Object> selection = getSelectionForType(MGraphicElement.class);
		return selection.size() > 0;
	}
}
