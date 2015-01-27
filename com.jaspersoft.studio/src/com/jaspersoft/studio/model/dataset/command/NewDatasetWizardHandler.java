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
package com.jaspersoft.studio.model.dataset.command;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;

import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.utils.SelectionHelper;

public class NewDatasetWizardHandler extends Action{

	/**
	 * Search the MReport element from the root of the document
	 * @param root root node of the document
	 * @return summary band if found, null otherwise
	 */
	private MReport getReport(INode root){
		if (root != null){
			List<INode> children = root.getChildren();
			for(INode node : children){
				if (node instanceof MReport)
					return (MReport)node;
			}
		}
		return null;
	}
	
	@Override
	public void run() {
		IEditorPart activeJRXMLEditor = SelectionHelper.getActiveJRXMLEditor();
		if (activeJRXMLEditor != null && activeJRXMLEditor instanceof JrxmlEditor) {
			INode root = ((JrxmlEditor) activeJRXMLEditor).getModel();
			MReport report = getReport(root);
			if (report != null){
				MDataset tempDataset = new MDataset();
				CreateDatasetCommand command = new CreateDatasetCommand(report, tempDataset, -1);		
				command.execute();
			}
		}
	};

}
