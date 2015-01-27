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
package com.jaspersoft.studio.components.table.model.column.action;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.MTableDetail;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.column.command.DeleteColumnCellCommand;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

/**
 * Action to delete an full row of the table
 * 
 * @author Orlandin Marco
 *
 */
public class DeleteRowAction extends DeleteAction {

	/** The Constant ID. */
	public static final String ID = "delete_table_row"; //$NON-NLS-1$
	
	public DeleteRowAction(IWorkbenchPart part) {
		super(part);
	}
	

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.DeleteRowAction_name);
		setToolTipText(Messages.DeleteRowAction_tooltip);
		setId(DeleteRowAction.ID);
	}
	
	/**
	 * Recursively generate the commands to delete every element of the row
	 * 
	 * @param children the actual list of children
	 * @param container the container where the commands are placed 
	 */
	private void createDeleteCommands(List<INode> children, JSSCompoundCommand container){
		for(INode child : children){
			createDeleteCommands(child.getChildren(), container);
			if (child instanceof MColumn){
				Command cmd = new DeleteColumnCellCommand((ANode)child.getParent(), (MColumn)child);
				if (cmd != null)
					container.add(cmd);
			}
		}
	}
	
	/**
	 * Search for every AMcollection (superclass of the row of the table)
	 * and create a delete cell command for everyone of its children
	 */
	@Override
	public Command createDeleteCommand(List objects) {
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof EditPart))
			return null;

		GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_DELETE);
		deleteReq.setEditParts(objects);

		JSSCompoundCommand compoundCmd = new JSSCompoundCommand(getText(), null);
		for (int i = 0; i < objects.size(); i++) {
			EditPart object = (EditPart) objects.get(i);
			if (object.getModel() instanceof AMCollection && !(object.getModel() instanceof MTableDetail)) {
				AMCollection model = (AMCollection) object.getModel();
				compoundCmd.setReferenceNodeIfNull(model);
				createDeleteCommands(model.getChildren(), compoundCmd);
			}
		}
		return compoundCmd;
	}
}
