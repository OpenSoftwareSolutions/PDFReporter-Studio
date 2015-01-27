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
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.column.command.CheckColumnsOrder;
import com.jaspersoft.studio.components.table.model.column.command.FixCellHeightsCommand;
import com.jaspersoft.studio.components.table.model.column.command.MoveColumnCommand;
import com.jaspersoft.studio.components.table.model.column.command.RefreshColumnNamesCommand;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.table.model.columngroup.command.CreateColumnGroupCommand;
import com.jaspersoft.studio.editor.outline.actions.ACreateAction;
import com.jaspersoft.studio.editor.palette.JDPaletteCreationFactory;
import com.jaspersoft.studio.model.ANode;

/*
 * The Class CreateGroupAction.
 */
public class GroupColumnsAction extends ACreateAction {

	/**
	 *  The Constant ID. 
	 */
	public static final String ID = "create_table_column_group"; //$NON-NLS-1$
	

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *            The part for this action
	 */
	public GroupColumnsAction(IWorkbenchPart part) {
		super(part);
		setCreationFactory(new JDPaletteCreationFactory(MColumnGroup.class));
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.CreateColumnAction_create_column_group);
		setToolTipText(Messages.CreateColumnAction_create_column_group_tool_tip);
		setId(GroupColumnsAction.ID);
		setImageDescriptor(
				Activator.getDefault().getImageDescriptor("icons/table-join-row.png"));
		setDisabledImageDescriptor(
				Activator.getDefault().getImageDescriptor("icons/table-join-row.png"));
		setEnabled(false);
	}

	@Override
	public Command createCommand() {
		List<Object> objects = getSelectedObjects();
		if (objects.isEmpty())
			return null;
		List<MColumn> columns = new ArrayList<MColumn>();
		for (Object obj : objects) {
			if (!(obj instanceof EditPart))
				return super.createCommand();
			EditPart ep = (EditPart) obj;
			if (ep.getModel() instanceof MColumn)
				columns.add((MColumn) ep.getModel());
			else
				return super.createCommand();
		}

		MColumn fmc = columns.get(0);
		ANode mparent = fmc.getParent();
		JSSCompoundCommand c = new JSSCompoundCommand(Messages.CreateColumnAction_create_column_group, mparent);
		MColumnGroup mcolgr = new MColumnGroup();
		int index = mparent.getChildren().indexOf(fmc);
		CreateColumnGroupCommand cmd = createGroup(index, mparent, mcolgr);
		mcolgr.setValue(cmd.createColumn(fmc.getJasperDesign(), fmc.getMTable()
				.getStandardTable()));
		
		c.add(new RefreshColumnNamesCommand(mparent, false, true));

		//Create the commands to fix the order on the undo
		List<CheckColumnsOrder> fixOrderCommandList = new ArrayList<CheckColumnsOrder>();
		for (MColumn src : columns){
			fixOrderCommandList.add(new CheckColumnsOrder(src));
		}
		Collections.sort(fixOrderCommandList);
		//This commands are executed on the undo, so the list must be reversed
		Collections.reverse(fixOrderCommandList);
		c.addAll(fixOrderCommandList);
		
		//Add tge commands to move the columns
		c.add(createGroup(index, mparent, mcolgr));
		for (MColumn src : columns){
			c.add(new MoveColumnCommand(src, mcolgr, false));
		}

		c.add(new FixCellHeightsCommand(fmc));
		c.add(new RefreshColumnNamesCommand(mparent, true, false));
		return c;
	}

	public CreateColumnGroupCommand createGroup(int index, ANode mparent,
			MColumnGroup mcolgr) {
		CreateColumnGroupCommand cmd = null;
		if (mparent instanceof MColumn)
			cmd = new CreateColumnGroupCommand((MColumn) mparent, mcolgr, index);
		if (mparent instanceof AMCollection)
			cmd = new CreateColumnGroupCommand((AMCollection) mparent, mcolgr,
					index);
		cmd.setResize(false);
		return cmd;
	}
}
