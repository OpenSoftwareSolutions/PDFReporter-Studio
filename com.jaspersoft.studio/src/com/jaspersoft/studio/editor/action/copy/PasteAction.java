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
package com.jaspersoft.studio.editor.action.copy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.IPastable;
import com.jaspersoft.studio.model.band.MBand;

public class PasteAction extends ACachedSelectionAction {

	public PasteAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();

		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Messages.common_paste);
		setId(ActionFactory.PASTE.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(false);
	}

	@Override
	protected boolean calculateEnabled() {
		List<?> selection = getSelectedObjects();
		for (Object obj : selection) {
			if (obj instanceof MBand)
				command = createCommand(selection);
			else if (obj instanceof EditPart && ((EditPart) obj).getModel() instanceof MBand)
				command = createCommand(selection);
		}
		if (!fresh)
			command = createCommand(selection);
		fresh = true;
		return command != null && command.canExecute();
	}

	protected PasteCommand createCommand(List<?> selectedObjects) {
		for (Object selection : selectedObjects) {
			PasteCommand cmd = getPasteComand(selection);
			if (cmd != null)
				return cmd;
			if (selection instanceof StructuredSelection) {
				StructuredSelection s = (StructuredSelection) selection;
				for (Iterator<?> it = s.iterator(); it.hasNext();) {
					Object o = it.next();
					cmd = getPasteComand(o);
					if (cmd != null)
						return cmd;
				}
			}
		}
		return null;
	}

	private PasteCommand getPasteComand(Object selection) {
		if (selection instanceof EditPart) {
			Object modelObj = ((EditPart) selection).getModel();
			// PasteDatasetCommand pasteDataset = null;
			// if (modelObj instanceof ANode) {
			// pasteDataset = new PasteDatasetCommand(((ANode) modelObj).getJasperDesign());
			// if (pasteDataset.canExecute())
			// return pasteDataset;
			// } else
			if (modelObj instanceof ANode) {
				IPastable past = getParent2Paste((ANode) modelObj);
				if (past != null) {
					return new PasteCommand(past);
				}
			}
		} else if (selection instanceof ANode) {
			IPastable past = getParent2Paste((ANode) selection);
			if (past != null) {
				return new PasteCommand(past);
			}
		}
		return null;
	}

	private IPastable getParent2Paste(ANode n) {
		while (n != null) {
			if (n instanceof IPastable) {
				if (n instanceof MBand && n.getValue() == null)
					return null;
				return (IPastable) n;
			}
			n = (ANode) n.getParent();
		}
		return null;
	}

	@Override
	public void run() {
		PasteCommand command = createCommand(getSelectedObjects());
		execute(command);

		// Select the pasted edit part
		GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
		if (viewer != null) {
			viewer.setSelection(new StructuredSelection(getSelectableEditParts(viewer, command.getPasteParent(),
					command.getCreatedNodesNumber())));
		}
	}

	/**
	 * Return a list of the edit part created because the past operation
	 * 
	 * @param viewer
	 * @param pasteParent
	 *          parent of the pasted elements
	 * @param createdElements
	 *          number of pasted elements
	 * @return the editpart created for the paste operation, so they can be selected
	 */
	@SuppressWarnings("rawtypes")
	private List<EditPart> getSelectableEditParts(GraphicalViewer viewer, IPastable pasteParent, int createdElements) {
		List<EditPart> selectableChildren = new ArrayList<EditPart>();
		if (!(pasteParent instanceof ANode))
			return selectableChildren;

		ANode parentModel = (ANode) pasteParent;
		HashSet<INode> pastedModels = new HashSet<INode>();
		int elementsToInsert = createdElements;
		List<INode> childrens = parentModel.getChildren();
		// the list children can be empty in specific use case
		// for example when the elements were pasted into a node
		// that is not expanded in the main editor (i.e: list component)
		if(childrens.size()>=elementsToInsert) {
			while (elementsToInsert > 0) {
				pastedModels.add(childrens.get(childrens.size() - elementsToInsert));
				elementsToInsert--;
			}
		}

		List children = viewer.getContents().getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Object child = iter.next();
			if (child instanceof EditPart) {
				EditPart part = (EditPart) child;
				if (pastedModels.contains(part.getModel()) && part.isSelectable())
					selectableChildren.add(part);
			}
		}
		return selectableChildren;
	}

}
