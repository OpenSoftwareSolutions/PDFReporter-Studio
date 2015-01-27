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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.column.command.RefreshColumnNamesCommand;
import com.jaspersoft.studio.editor.outline.actions.ACreateAction;
import com.jaspersoft.studio.editor.palette.JDPaletteCreationFactory;
import com.jaspersoft.studio.model.ANode;

/**
 * 
 * Generic action to create a column, this action place before and after the commands
 * to create the column the commands to refresh the column number after the execute or the undo
 * 
 * @author Orlandin Marco
 *
 */
public abstract class CreateColumnAction extends ACreateAction {
	
	public CreateColumnAction(IWorkbenchPart part) {
		super(part);
		setCreationFactory(new JDPaletteCreationFactory(MColumn.class));
	}

	/**
	 * Return the first node inside a table found inside the selection
	 * 
	 * @return a ANode of the table contained in the selection or null if it can't be found
	 */
	protected ANode getTableNode(List<?> objects){
		for (int i = 0; i < objects.size(); i++) {
			Object obj = objects.get(i);
			if (obj instanceof EditPart) {
				EditPart object = (EditPart) obj;
				if (object.getModel() instanceof MColumn){
					return (MColumn)object.getModel();
				} else if (object.getModel() instanceof AMCollection){
					return ((AMCollection)object.getModel()).getParent();
				} else if (object.getModel() instanceof MTable){
					return (ANode)object.getModel();
				}
			}
		}
		return null;
	}
	
	public void execute(ISelection selection){
		if (selection instanceof IStructuredSelection)
			execute(((IStructuredSelection) selection).toList());
	}
	
	public void execute(List<?> editparts){
		JSSCompoundCommand executedCommand = (JSSCompoundCommand)createCommand(editparts);
		execute(executedCommand);
	}
	
	public boolean canExecute(List<?> editparts){
		Command cmd = createCommand(editparts);
		return (cmd != null && cmd.canExecute());
	}
	
	public boolean canExecute(ISelection selection){
		if (selection instanceof IStructuredSelection)
			return canExecute(((IStructuredSelection) selection).toList());
		return false;
	}
	
	@Override
	public Command createCommand(){
		return createCommand(getSelectedObjects());
	}
	
	public Command createCommand(List<?> objects) {
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof EditPart))
			return null;

		CreateRequest createReq = new CreateRequest(RequestConstants.REQ_CREATE);
		createReq.setLocation(location);
		createReq.setFactory(creationFactory);
		Map<Object, Object> map = new HashMap<Object, Object>();
		if (!setExtendedData(map, objects))
			return null;
		createReq.setExtendedData(map);

		JSSCompoundCommand jssCcmd = new JSSCompoundCommand(null);		
		for (int i = 0; i < objects.size(); i++) {
			Object obj = objects.get(i);
			if (obj instanceof EditPart) {
				EditPart object = (EditPart) obj;
				//Set the node if necessary to disable the refresh
				jssCcmd.setReferenceNodeIfNull(object.getModel());	
				Command cmd = object.getCommand(createReq);
				if (cmd != null) {
					jssCcmd.add(cmd);
				}
			}
		}
		if(!jssCcmd.isEmpty()) {
			//Append the command to refresh the column names
			ANode tableNode = getTableNode(objects);
			jssCcmd.addFirst(new RefreshColumnNamesCommand(tableNode, false, true));
			jssCcmd.add(new RefreshColumnNamesCommand(tableNode, true, false));
			return jssCcmd;
		}
		else {
			return null;
		}
	}
	
	@Override
	public void run() {
		execute(getSelectedObjects());
	}
}
