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
import com.jaspersoft.studio.components.table.TableComponentFactory;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.MTableDetail;
import com.jaspersoft.studio.components.table.model.column.MCell;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.model.ANode;

/*
 * The Class CreateGroupAction.
 */
public class DeleteColumnCellAction extends DeleteAction {

	/** The Constant ID. */
	public static final String ID = "delete_table_cell"; //$NON-NLS-1$

	/**
	 * Constructs a <code>DeleteColumnCellAction</code> using the specified
	 * part.
	 * 
	 * @param part
	 *            The part for this action
	 */
	public DeleteColumnCellAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.DeleteColumnCellAction_name);
		setToolTipText(Messages.DeleteColumnCellAction_tooltip);
		setId(DeleteColumnCellAction.ID);
	}

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
			if (object.getModel() instanceof MCell) {
				MColumn model = (MColumn) object.getModel();
				ANode parent = model.getParent();
				compoundCmd.setReferenceNodeIfNull(model);
				//The cell of the detail can not be deleted
				if (!(parent instanceof MTableDetail)){
					Command cmd = TableComponentFactory.getDeleteCellCommand(model.getParent(), model);
					if (cmd != null)
						compoundCmd.add(cmd);
				}
			}
		}
		return compoundCmd;
	}
}
