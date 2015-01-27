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
package com.jaspersoft.studio.model.variable.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.contentoutline.ContentOutline;

import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.variable.MVariable;
import com.jaspersoft.studio.model.variable.MVariables;
import com.jaspersoft.studio.utils.SelectionHelper;

public class CreateVariableHandler extends Action {

	/**
	 * Search for a MVariables inside a list o elements
	 * @param reportElements list of elements
	 * @return summary band if found, null otherwise
	 */
	private MVariables searchVariables(List<INode> reportElements){
		for(INode node : reportElements)
			if (node instanceof MVariables) {
				MVariables variables = (MVariables) node;
				return variables;
			}
		return null;
	}
	
	/**
	 * Search the MVariables element from the root of the document
	 * @param root root node of the document
	 * @return summary band if found, null otherwise
	 */
	private MVariables getVariables(INode root){
		if (root != null){
			List<INode> children = root.getChildren();
			for(INode node : children){
				if (node instanceof MReport)
					return searchVariables(node.getChildren());
			}
		}
		return null;
	}
	
	private Iterator getFirstChildrendIterator(List children){
		if (children != null) return children.iterator();
		return null;
	}
	
	private StructuredSelection getLastVariable(EditPart root){
		List children = root.getChildren();
	 	Iterator it = getFirstChildrendIterator(children);
	 	while(it != null && it.hasNext()){
	 		EditPart actualPart = (EditPart)it.next();
	 		if (actualPart.getModel() instanceof MReport || actualPart.getModel() instanceof MRoot) it = getFirstChildrendIterator(actualPart.getChildren());
	 		if (actualPart.getModel() instanceof MVariables) {
	 			List variables = actualPart.getChildren();
	 			if (variables != null){
		 			int last = variables.size() - 1;
		 			return new StructuredSelection(variables.get(last));
	 			}
	 		}
	 	}
	 	return null;
	}
	
	private StructuredSelection getVariables(EditPart root){
		List children = root.getChildren();
	 	Iterator it = getFirstChildrendIterator(children);
	 	while(it != null && it.hasNext()){
	 		EditPart actualPart = (EditPart)it.next();
	 		if (actualPart.getModel() instanceof MReport || actualPart.getModel() instanceof MRoot) it = getFirstChildrendIterator(actualPart.getChildren());
	 		if (actualPart.getModel() instanceof MVariables) return new StructuredSelection(actualPart);
	 	}
	 	return null;
	}
	
	@Override
	public void run() {
		IEditorPart activeJRXMLEditor = SelectionHelper.getActiveJRXMLEditor();
		if (activeJRXMLEditor != null && activeJRXMLEditor instanceof JrxmlEditor) {
			INode root = ((JrxmlEditor) activeJRXMLEditor).getModel();
			MVariables reportVariables = getVariables(root);
			if (reportVariables != null){
				MVariable var =  new MVariable();
				CreateVariableCommand command = new CreateVariableCommand(reportVariables, var, -1);		
				command.execute();//I create the variable with a command
				ContentOutline outline = (ContentOutline)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("org.eclipse.ui.views.ContentOutline").getSite().getPart(); //Retrive the outline pane
				ISelection s = outline.getSelection();
				if (s instanceof StructuredSelection) {
					Object obj = ((StructuredSelection) s).getFirstElement();
					if (obj instanceof EditPart) {
						EditPart editPart = ((EditPart) obj).getRoot();
						StructuredSelection lastVar = getLastVariable(editPart); // Search for the edit part of the last variable selected
						if (lastVar != null) {
							outline.setSelection(lastVar); //Set the selection into the outline
							//Here i need something similar to getWorkbenchPart().getSite().getSelectionProvider().setSelection(newselection) where getWorkBenchPart return something of the  type ReportEditor
							/*I thought also to use directly the action instead of the command but to work correctly it must be instantiated with a ReportEditor parameter
							CreateVariableAction createAction = new CreateVariableAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart());
							createAction.forcerSelection(lastVar);
							createAction.run();		
							*/					
						}
					}
				}
			}
		}
	}
}
