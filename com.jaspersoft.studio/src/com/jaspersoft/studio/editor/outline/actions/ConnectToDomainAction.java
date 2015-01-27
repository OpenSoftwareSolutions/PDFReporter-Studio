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
package com.jaspersoft.studio.editor.outline.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.property.dataset.wizard.ConnectToDomainWizard;

/**
 * Action to open the wizard to export create the domain parameters for 
 * a dataset and its dataset runs
 * 
 * @author Orlandin Marco
 *
 */
public class ConnectToDomainAction extends SelectionAction {

	/** The Constant ID. */
	public static final String ID = "connect_to_domain"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public ConnectToDomainAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.ConnectToDomainWizardPage_dialogTitle);
		setToolTipText(Messages.ConnectToDomainAction_actionTooltip);
		setId(ConnectToDomainAction.ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/connectdomain.png")); //$NON-NLS-1$
		setEnabled(false);
	}


	/**
	 * Enable only if there is exactly one dataset selected
	 */
	@Override
	protected boolean calculateEnabled() {
		List<MDataset> selectedDatasets = getSelectedDatasets();
		return !selectedDatasets.isEmpty() && selectedDatasets.size()==1;
	}

	@Override
	public void run() {
		ConnectToDomainWizard importWizard = new ConnectToDomainWizard(getSelectedDatasets().get(0));
		WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), importWizard);
		dialog.open();
	}
	
	/**
	 * Return the list of all the selected MDataset. 
	 * 
	 * @return a not null list of MDataset
	 */
	private List<MDataset> getSelectedDatasets(){
		List<?> objects = getSelectedObjects();
		if (objects == null || objects.isEmpty())
			return new ArrayList<MDataset>();
		List<MDataset> result = new ArrayList<MDataset>();
		for (Object obj : objects){
			if (obj instanceof EditPart) {
				ANode n = (ANode) ((EditPart) obj).getModel();
				if (n instanceof MDataset) {
					result.add((MDataset)n);
				}
			}
		}
		return result;
	}
}
