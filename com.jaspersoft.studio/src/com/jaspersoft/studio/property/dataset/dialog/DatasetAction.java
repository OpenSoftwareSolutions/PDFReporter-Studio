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

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.editor.report.ReportEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.utils.SelectionHelper;

public class DatasetAction extends SelectionAction {
	public static final String ID = "datasetAction"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param diagramViewer
	 *          the GraphicalViewer whose grid enablement and visibility properties are to be toggled
	 */
	public DatasetAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	/**
	 * Initializes this action's text and images.
	 */
	protected void init() {
		super.init();
		setText(Messages.DatasetAction_Title);
		setToolTipText(Messages.DatasetAction_Tooltip);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(JaspersoftStudioPlugin.PLUGIN_ID,
				"icons/resources/dataset-16.png")); //$NON-NLS-1$
		setId(ID);
		setEnabled(false);
	}

	private static boolean dialogExists = false;

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		if (dialogExists)
			return;
		dialogExists = true;
		try {
			MDataset mdataset = getMDatasetToShow();
			if (mdataset != null)
				new DatasetDialog(UIUtils.getShell(), mdataset, mdataset.getJasperConfiguration(), getCommandStack()).open();
		} catch (Exception e) {
			UIUtils.showError(Messages.DatasetAction_ErrorMsg, e);
		} finally {
			dialogExists = false;
		}
	}

	/*
	 * Gets the MDataset instance for which we should open the "Dataset & Query" dialog.
	 */
	protected MDataset getMDatasetToShow() {
		final AbstractVisualEditor part = (AbstractVisualEditor) getWorkbenchPart();
		//Reinitialize the outline if disposed
		part.getAdapter(IContentOutlinePage.class);
		//Get the selection from the outline view, since they are synchronized it the same of the editor
		ISelection selection = 	part.getOutlineSelection();
		//If it is empty (for example outline closed) fallback
		if (selection.isEmpty()){
			selection = getSelection();
		}

		if (selection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			// Selection of an EditPart that wraps the MDataset element, or one of its children.
			// Example: selecting an Dataset from the Outline view, or its fields
			if (firstElement instanceof EditPart && ((EditPart) firstElement).getModel() instanceof ANode) {
				ANode currentNode = (ANode) ((EditPart) firstElement).getModel();
				while (currentNode != null) {
					if (currentNode instanceof MDataset) {
						return (MDataset) currentNode;
					} else {
						currentNode = currentNode.getParent();
					}
				}
			}
		}

		if (part.getModel() != null && !part.getModel().getChildren().isEmpty() && part instanceof ReportEditor) {
			MReport mreport = (MReport) part.getModel().getChildren().get(0);
			// get report main dataset
			return (MDataset) mreport.getPropertyValue(JasperDesign.PROPERTY_MAIN_DATASET);
		} else {
			// Handle custom editors for elements like table, crosstab and list
			// FIXME - Now this solution works because list/crosstab/table editors will
			// have only one child MDataset element. Once this will be no longer valid,
			// the code below must be changed
			if (part.getModel() != null && !part.getModel().getChildren().isEmpty()) {
				INode firstChild = part.getModel().getChildren().get(0);
				if (firstChild instanceof MPage) {
					for (INode c : firstChild.getChildren()) {
						if (c instanceof MDataset) {
							return (MDataset) c;
						}
					}
				}
			}
		}

		// Try a fallback solution in order to be sure to have a valid dataset
		// Get it from the currently opened active editor
		IEditorPart activeJRXMLEditor = SelectionHelper.getActiveJRXMLEditor();
		if (activeJRXMLEditor != null && activeJRXMLEditor instanceof JrxmlEditor) {
			final ANode mroot = (ANode) ((JrxmlEditor) activeJRXMLEditor).getModel();
			if (mroot != null) {
				final ANode mreport = (ANode) mroot.getChildren().get(0);
				return (MDataset) ((MReport) mreport).getPropertyValue(JasperDesign.PROPERTY_MAIN_DATASET);
			}
		}

		return null;
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

}
