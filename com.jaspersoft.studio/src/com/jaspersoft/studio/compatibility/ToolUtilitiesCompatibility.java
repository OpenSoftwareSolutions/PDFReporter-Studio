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
package com.jaspersoft.studio.compatibility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.IEditorPart;

import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.gef.parts.PageEditPart;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.utils.SelectionHelper;

/**
 * On eclipse 3.6 gef has a bug on the library method getSelectionWithoutDependants
 * since it try to cast always to graphical edit part. This was resolved in the next 
 * version but to assure back compatibility and avoid class cast exception we imported it.
 * 
 * It also cache the last request for a selection without dependents to provide the 
 * result faster if the selection is the same
 * 
 * @author Orlandin Marco
 *
 */
public class ToolUtilitiesCompatibility {
	
	/**
	 * The last input, used for caching
	 */
	private static List<?> lastInput;
	
	/**
	 * The last output, used for caching
	 */
	private static List<?> lastOutput;
	
	/**
	 * Returns a list containing the top level selected edit parts based on the
	 * passed in list of selection. It avoid to provide the main element of a subeditor
	 * and provide caching for the last result requested
	 * 
	 * @param selectedParts
	 *            the complete selection
	 * @return the selection excluding dependents
	 */
	public static List<?> getSelectionWithoutDependants(List<?> selectedParts) {
		if (selectedParts.isEmpty()) return selectedParts;
		
		if (selectedParts.equals(lastInput)) return lastOutput;
		
		lastInput = selectedParts;
		List<Object> result = new ArrayList<Object>();
		HashSet<ANode> models = new HashSet<ANode>();
		ANode pageModel = getPageModel((ANode)((EditPart)selectedParts.get(0)).getModel());
		removeSubeditorMainElement(selectedParts, pageModel);
		
		for(Object part : selectedParts){
			models.add((ANode)((EditPart)part).getModel());
		}
		for (int i = 0; i < selectedParts.size(); i++) {
			EditPart editpart = (EditPart) selectedParts.get(i);
			ANode model = (ANode)editpart.getModel();
			if (!isAncestorContainedIn(models, model))
				result.add(editpart);
		}
		lastOutput = result;
		return result;
	}
	
	/**
	 * Return the page edit part ancestor of the current node
	 * 
	 * @param node the node
	 * @return a page edit part if it can be found, otherwise null
	 */
	public static EditPart getPageEditPart(EditPart node){
		if (node == null) return null;
		EditPart parent = node.getParent();
		if (node instanceof PageEditPart) return node;
		else return getPageEditPart(parent);
	}
	
	/**
	 * Return the page model ancestor of the current node
	 * 
	 * @param node the node
	 * @return a page edit part if it can be found, otherwise null
	 */
	public static ANode getPageModel(ANode node){
		if (node == null) return null;
		ANode parent = node.getParent();
		if (parent instanceof MPage) return parent;
		else return getPageModel(parent);
	}
	
	/**
	 * Remove from the currently selected parts the one edited inside the current
	 * editor if a subeditor. If the current editor is not a subeditor or the 
	 * subeditor main element is not contained into the selection this dosen't 
	 * do nothing
	 * 
	 * @param selectedParts the currently selected parts
	 * @param page an mpage of a subeditor
	 */
	private static void removeSubeditorMainElement(List<?> selectedParts, ANode page){
		if (page == null) return;
		//The edited node is always the last one
		INode editedElement = page.getChildren().get(page.getChildren().size()-1);
		for(Object part : selectedParts){
			EditPart ePart = (EditPart)part;
			if (ePart.getModel() == editedElement){
				selectedParts.remove(part);
				break;
			}
		}
	}


	/**
	 * Checks if collection contains any ancestor of editpart <code>ep</code>
	 * 
	 * @param c collection of editparts
	 * @param ep the editparts to check ancestors for
	 * @return <code>true</code> if collection contains any ancestor(s) of the
	 *         editpart <code>ep</code>
	 */
	public static boolean isAncestorContainedIn(HashSet<ANode> c, ANode ep) {
		ep = ep.getParent();
		while (ep != null) {
			if (c.contains(ep))
				return true;
			ep = ep.getParent();
		}
		return false;
	}
	
	/**
	 * Check if the passed edit part is the main element inside a subeditor.
	 */
	public static boolean isSubeditorMainElement(EditPart part){
		IEditorPart editorPart = SelectionHelper.getActiveJRXMLEditor();
		if (editorPart instanceof JrxmlEditor){
			JrxmlEditor editor = (JrxmlEditor)editorPart;
			if (editor != null && editor.getReportContainer().getActivePage() > 0 && editor.getReportContainer().getActiveEditor() instanceof AbstractVisualEditor){
				AbstractVisualEditor visualEditor = (AbstractVisualEditor)editor.getReportContainer().getActiveEditor();
				INode managedNode = visualEditor.getManagedElement();
				return managedNode == part.getModel();
			}
		}
		return false;
	}
}
