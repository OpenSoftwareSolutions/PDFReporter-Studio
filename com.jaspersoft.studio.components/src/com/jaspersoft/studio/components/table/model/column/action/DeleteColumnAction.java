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
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.column.command.RefreshColumnNamesCommand;

/*
 * The Class CreateGroupAction.
 */
public class DeleteColumnAction extends DeleteAction {

	/** The Constant ID. */
	public static final String ID = "delete_table_column"; //$NON-NLS-1$

	/**
	 * Constructs a <code>DeleteColumnAction</code> using the specified part.
	 * 
	 * @param part
	 *            The part for this action
	 */
	public DeleteColumnAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.DeleteColumnAction_text);
		setToolTipText(Messages.DeleteColumnAction_tooltip);
		setId(DeleteColumnAction.ID);
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
		MColumn col = null;
		for (int i = 0; i < objects.size(); i++) {
			EditPart object = (EditPart) objects.get(i);
			if (object.getModel() instanceof MColumn) {
				MColumn model = (MColumn) object.getModel();
				col = model;
				compoundCmd.setReferenceNodeIfNull(model);
				Command cmd = TableComponentFactory.getDeleteColumnCommand(
						model.getParent(), model);
				if (cmd != null)
					compoundCmd.add(cmd);
			}
		}
		//There are no column selected, must return null since the action is not valid
		if (compoundCmd.isEmpty()) return null;
		//Commands to refresh the columns names on undo or execute
		compoundCmd.addFirst(new RefreshColumnNamesCommand(col, false, true));
		compoundCmd.add(new RefreshColumnNamesCommand(col, true, false));
		return compoundCmd;
	}
}
