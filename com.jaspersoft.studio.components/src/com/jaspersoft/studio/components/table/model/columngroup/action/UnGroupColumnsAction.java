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
package com.jaspersoft.studio.components.table.model.columngroup.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.column.command.CheckColumnsOrder;
import com.jaspersoft.studio.components.table.model.column.command.DeleteColumnCommand;
import com.jaspersoft.studio.components.table.model.column.command.DeleteColumnFromGroupCommand;
import com.jaspersoft.studio.components.table.model.column.command.FixCellHeightsCommand;
import com.jaspersoft.studio.components.table.model.column.command.MoveColumnCommand;
import com.jaspersoft.studio.components.table.model.column.command.RefreshColumnNamesCommand;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroupCell;
import com.jaspersoft.studio.components.table.part.TableCellEditPart;
import com.jaspersoft.studio.editor.outline.part.TreeEditPart;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class UnGroupColumnsAction extends SelectionAction {

	/** The Constant ID. */
	public static final String ID = "ungroup_table_columns"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *            The part for this action
	 */
	public UnGroupColumnsAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.UnGroupColumnsAction_title);
		setToolTipText(Messages.UnGroupColumnsAction_tooltip);
		setId(UnGroupColumnsAction.ID);
		setImageDescriptor(
				Activator.getDefault().getImageDescriptor("icons/table-split-row.png")); //$NON-NLS-1$
		setDisabledImageDescriptor(
				Activator.getDefault().getImageDescriptor("icons/table-split-row.png")); //$NON-NLS-1$
		setEnabled(false);
	}

	public Command createCommand(List<?> objects) {
		if (objects.isEmpty())
			return null;
		if (objects.size() == 1) {
			Object sel = objects.get(0);
			if (sel instanceof TableCellEditPart || sel instanceof TreeEditPart)
				sel = ((AbstractEditPart) sel).getModel();
			if (sel instanceof MColumn) {
				JSSCompoundCommand c = new JSSCompoundCommand(Messages.UnGroupColumnsAction_title, (ANode)sel); 

				MColumn fmc = (MColumn) sel;
				ANode mparent = fmc.getParent();
				c.add(new RefreshColumnNamesCommand(mparent, false, true));
				
				//Create the commands to fix the order on the undo
				List<CheckColumnsOrder> fixOrderCommandList = new ArrayList<CheckColumnsOrder>();
				for (INode src : fmc.getChildren()){
					fixOrderCommandList.add(new CheckColumnsOrder((MColumn)src));
				}
				Collections.sort(fixOrderCommandList);
				//This commands are executed on the undo, so the list must be reversed
				Collections.reverse(fixOrderCommandList);
				c.addAll(fixOrderCommandList);
				
				//Create the commands to move the columns
				int baseIndex = mparent.getChildren().indexOf(fmc);
				for (INode src : fmc.getChildren()) {
					if (mparent instanceof MColumnGroup || mparent instanceof MColumnGroupCell){
						MoveColumnCommand moveCommand = new MoveColumnCommand((MColumn) src, (MColumn) mparent, false);
						moveCommand.setNewIndex(baseIndex);
						baseIndex++;
						c.add(moveCommand);
					}
					else {
						MoveColumnCommand moveCommand = new MoveColumnCommand((MColumn) src, null, false);
						moveCommand.setNewIndex(baseIndex);
						baseIndex++;
						c.add(moveCommand);
					}
				}
				if (mparent instanceof MColumnGroup)
					c.add(new DeleteColumnFromGroupCommand((MColumnGroup) mparent, fmc));
				else if (mparent instanceof MColumnGroupCell)
					c.add(new DeleteColumnFromGroupCommand((MColumnGroupCell) mparent, fmc));
				else if (mparent instanceof AMCollection)
					c.add(new DeleteColumnCommand((AMCollection) mparent, fmc));

				c.add(new FixCellHeightsCommand(fmc));
				c.add(new RefreshColumnNamesCommand(mparent, true, false));
				return c;
			}
		}
		return null;
	}

	@Override
	public void run() {
		execute(createCommand(getSelectedObjects()));
	}

	@Override
	protected boolean calculateEnabled() {
		List<?> objects = getSelectedObjects();
		if (objects != null && objects.size() == 1) {
			Object sel = objects.get(0);
			if (sel instanceof EditPart)
				sel = ((EditPart) sel).getModel();
			return sel instanceof MColumnGroup
					|| sel instanceof MColumnGroupCell;
		}
		return false;
	}
}
