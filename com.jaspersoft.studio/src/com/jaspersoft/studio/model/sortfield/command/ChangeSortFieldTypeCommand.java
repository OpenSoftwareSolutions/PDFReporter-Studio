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

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;

import com.jaspersoft.studio.model.sortfield.MSortField;
import com.jaspersoft.studio.model.sortfield.MSortFields;
import com.jaspersoft.studio.model.sortfield.command.wizard.SortFieldWizard;
import com.jaspersoft.studio.model.sortfield.command.wizard.WizardSortFieldPage.SHOW_TYPE;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class ChangeSortFieldTypeCommand extends Command {

	/** The jr data set. */
	private JRDesignDataset jrDataSet;

	private JRDesignSortField jrField;
	
	private String oldName;
	
	private SortFieldTypeEnum oldType;

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
	public ChangeSortFieldTypeCommand(MSortFields destNode, MSortField srcNode) {
		super();
		this.jrDataSet = (JRDesignDataset) destNode.getValue();
		this.jrField = (JRDesignSortField)srcNode.getValue();
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
		if (jrField != null) {
			oldName = jrField.getName();
			oldType = jrField.getType();
			jrDataSet.getSortFieldsMap().remove(getSortFieldKey(jrField));
			
			SortFieldWizard wizard = new SortFieldWizard();
			JRDesignSortField dummyField = new JRDesignSortField();
			wizard.init(jrDataSet, dummyField);
			if (jrField.getType() == SortFieldTypeEnum.FIELD) {
				dummyField.setType(SortFieldTypeEnum.VARIABLE);
				wizard.setShownElementsType(SHOW_TYPE.VARIABLES);
			} else {
				dummyField.setType(SortFieldTypeEnum.FIELD);
				wizard.setShownElementsType(SHOW_TYPE.FIELDS);
			}
			WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
			dialog.create();
			if (dialog.open() != Dialog.OK) {
				oldName = null;
				oldType = null;
			} else {
				jrField.setType(dummyField.getType());
				jrField.setName(dummyField.getName());
			}
			jrDataSet.getSortFieldsMap().put(getSortFieldKey(jrField), jrField);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (oldName != null && oldType != null){
			jrDataSet.getSortFieldsMap().remove(getSortFieldKey(jrField));
			jrField.setName(oldName);
			jrField.setType(oldType);
			jrDataSet.getSortFieldsMap().put(getSortFieldKey(jrField), jrField);
			oldName = null;
			oldType = null;
		}
	}
}
