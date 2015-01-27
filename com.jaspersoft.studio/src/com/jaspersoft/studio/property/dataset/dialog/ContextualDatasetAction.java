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
package com.jaspersoft.studio.property.dataset.dialog;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.dataset.MDataset;

/**
 * Contextual action to open a dataset and query dialog, available
 * only when a dataset or a report is selected
 * 
 * @author Orlandin Marco
 *
 */
public class ContextualDatasetAction extends DatasetAction {
	
	public static final String ID = "ContextualDatasetAction"; 

	
	private MDataset datasetToOpen = null;

	public ContextualDatasetAction(IWorkbenchPart part) {
		super(part);
	}
	
	/**
	 * Initializes this action's text and images.
	 */
	protected void init() {
		super.init();
		setId(ID);
	}
	
	@Override
	protected MDataset getMDatasetToShow() {
		return datasetToOpen;
	}
	
	
	@Override
	protected boolean calculateEnabled() {
		ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			// Selection of an EditPart that wraps the MDataset element, or one of its children.
			// Example: selecting an Dataset from the Outline view, or its fields
			if (firstElement instanceof EditPart && ((EditPart) firstElement).getModel() instanceof ANode) {
				ANode currentNode = (ANode) ((EditPart) firstElement).getModel();
				if (currentNode instanceof MDataset) {
					datasetToOpen = (MDataset) currentNode; 
					return true;
				} else if (currentNode instanceof MReport || currentNode instanceof MPage) {
					datasetToOpen = (MDataset) ((APropertyNode)currentNode).getPropertyValue(JasperDesign.PROPERTY_MAIN_DATASET);
					return true;
				}
			}
		}
		datasetToOpen = null;
		return false;
	}
}
