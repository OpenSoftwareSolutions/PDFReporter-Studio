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
package com.jaspersoft.studio.model.sortfield.command;

import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSortField;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.sortfield.MSortField;
import com.jaspersoft.studio.model.sortfield.MSortFields;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class ChangeSortFieldNameCommand extends Command {

	/** The jr field. */
	private MSortField oldFieldModel;

	/** The jr data set. */
	private JRDesignDataset jrDataSet;

	private String oldName;
	
	private String newName;

	/**
	 * Instantiates a new creates the field command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public ChangeSortFieldNameCommand(MSortFields destNode,  MSortField srcNode, String newName) {
		super();
		this.jrDataSet = (JRDesignDataset) destNode.getValue();
		this.oldFieldModel = srcNode;
		this.newName = newName;
	}

	private String getSortFieldKey(JRSortField sortField)
	{
		return sortField.getName() + "|" + sortField.getType().getName();
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (oldFieldModel.getValue() != null) {
			JRDesignSortField sortField = (JRDesignSortField)oldFieldModel.getValue();
			oldName = sortField.getName();
			//Don't do anything if the name is the same
			if (oldName.equals(newName)) return;
			jrDataSet.getSortFieldsMap().remove(getSortFieldKey(sortField));
			sortField.setName(newName);
			jrDataSet.getSortFieldsMap().put(getSortFieldKey(sortField), sortField);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return oldName != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		JRDesignSortField sortField = (JRDesignSortField)oldFieldModel.getValue();
		if (oldName.equals(newName)) return;
		jrDataSet.getSortFieldsMap().remove(getSortFieldKey(sortField));
		sortField.setName(oldName);
		jrDataSet.getSortFieldsMap().put(getSortFieldKey(sortField), sortField);
		oldName = null;
	}
}
