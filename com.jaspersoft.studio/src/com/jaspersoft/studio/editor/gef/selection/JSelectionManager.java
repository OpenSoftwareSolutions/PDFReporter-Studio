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
package com.jaspersoft.studio.editor.gef.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.SelectionManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.band.BandEditPart;
import com.jaspersoft.studio.model.ANode;

/**
 * Class that extend the default SelectionManager to change the behavior when a selection of more elements 
 * is done
 * @author Orlandin Marco
 *
 */
public class JSelectionManager extends SelectionManager {
	
	private static EditPart lastSelected = null;
	
	/**
	 * Take a node from the selection check if for its model the refresh is ignored
	 * or not
	 * 
	 * @param selection actual selection
	 * @return true if the refresh is ignored, false otherwise
	 */
	private boolean isRefreshIgnored(List<?> selection){
		for (Object item : selection){
			if (item instanceof EditPart) {
				EditPart part = (EditPart)item;
				if (part.getModel() instanceof ANode){
					ANode mainNode = JSSCompoundCommand.getMainNode((ANode)part.getModel());
					if (mainNode != null) return JSSCompoundCommand.isRefreshEventsIgnored(mainNode);
				}
			}
		}
		return false;
	}
	
	/**
	 * Before to call the deselect check if the refresh is ignored or not, since it case a lot
	 * of updates
	 */
	@Override
	public void deselect(EditPart editpart) {
		if (!JSSCompoundCommand.isRefreshEventsIgnored((ANode)editpart.getModel())){
			super.deselect(editpart);
		} 
	}
	
	/**
	 * Remove from the current selection the editpart that are not selectable
	 * 
	 * @param orderedSelection the current selection
	 * @return a subset of the selection with only the selectable edit parts
	 */
	private IStructuredSelection removeUnselectableParts(List<?> orderedSelection){
		List<EditPart> result = new ArrayList<EditPart>();
		
		for (Object obj : orderedSelection) {
			EditPart part = (EditPart) obj;
			if (part.isSelectable()) result.add(part);
		}
		
		return new StructuredSelection(result);
	}
	
	/**
	 * Sets the selection, override the original method to store and give the status of selected primary
	 * to the item that was primary before the setSelection. If the element isn't in the new selected items
	 * the default behavior will be used (the primary element will be the last on the list of the new selected 
	 * items).
	 * 
	 * @param newSelection
	 *            the new selection
	 * @since 3.2
	 */
	@Override
	public void setSelection(ISelection newSelection) {
		if (!(newSelection instanceof IStructuredSelection))
			return;
		List<?> orderedSelection = ((IStructuredSelection) newSelection).toList();
		//Beofre to set a selection that causes a lot of events check if the refresh is ignored
		if (isRefreshIgnored(orderedSelection)) 
			return;
		EditPart focusedEditPart = null;
		if (!orderedSelection.isEmpty()) {
			Iterator<?> itr = orderedSelection.iterator();
			EditPart part = (EditPart) itr.next();
			if (part.getViewer() != null){
				focusedEditPart = part.getViewer().getFocusEditPart();
				//Search a focused element into the selection
				if (!(focusedEditPart instanceof BandEditPart) && (focusedEditPart instanceof FigureEditPart)){
					lastSelected = focusedEditPart;
				} 
				//If not found the last selected element will be chosen if it is in the selection
				if (lastSelected != null && !orderedSelection.contains(lastSelected)){
					lastSelected = null;
				} 
				super.setSelection(removeUnselectableParts(orderedSelection));
				//If even the last selected element wasn't in the selection the first element of the selection will be chosen
				if (lastSelected == null){
					lastSelected = (EditPart)orderedSelection.get(0);
				}
				
				itr = orderedSelection.iterator();
				while (itr.hasNext()) {
						part = (EditPart) itr.next();
						if (part.isSelectable()) part.setSelected(EditPart.SELECTED);
				}
				if (lastSelected.isSelectable()) lastSelected.setSelected(EditPart.SELECTED_PRIMARY);
			}
		}else {
			super.deselectAll();
		}
	}
}
