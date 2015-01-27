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
package com.jaspersoft.studio.wizards.functions;

import java.util.Arrays;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.jaspersoft.studio.wizards.ContextHelpIDs;

/**
 * Wizard page that allows to add additional categories for the new library
 * of functions being created.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class AdditionalFunctionsCategoriesPage extends WizardPage {

	private List categoriesList;

	protected AdditionalFunctionsCategoriesPage() {
		super("additionalFunctionsCategoriesPage");
		setTitle("Additional Categories for the Functions");
		setDescription("Add more categories that will be associated to the generated functions class.");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite mainCmp = new Composite(parent, SWT.NONE);
		mainCmp.setLayout(new GridLayout(3,false));
		Label additionalCategoriesLbl = new Label(mainCmp,SWT.NONE);
		additionalCategoriesLbl.setText("More Categories:");
		additionalCategoriesLbl.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false,1,2));
		
		categoriesList = new List(mainCmp, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		GridData listGD = new GridData(SWT.FILL,SWT.FILL, true, false,1,2);
		listGD.heightHint = 200;
		categoriesList.setLayoutData(listGD);
		
		Button addBtn = new Button(mainCmp, SWT.NONE);
		addBtn.setText("Add...");
		addBtn.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false));
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				java.util.List<String> availableCategories = ((NewFunctionsLibraryWizard)getWizard()).getAvailableCategories();
				for(String c : categoriesList.getItems()){
					availableCategories.remove(c);
				}
				ElementListSelectionDialog dialog = 
				     new ElementListSelectionDialog(UIUtils.getShell(), new LabelProvider());
				dialog.setTitle("Choose an additional category");
				dialog.setMessage("Select a category (* = any string, ? = any char):");
				dialog.setElements(availableCategories.toArray());
				if(dialog.open() == Window.OK) {
					Object[] result = dialog.getResult();
					for(Object cat : result) {
						categoriesList.add((String) cat);
					}
				}
			}
		});
		
		Button removeBtn = new Button(mainCmp, SWT.NONE);
		removeBtn.setText("Remove");
		removeBtn.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false, false));
		removeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selIndexes = categoriesList.getSelectionIndices();
				categoriesList.remove(selIndexes);
			}
		});
			
		setControl(mainCmp);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,ContextHelpIDs.WIZARD_NEW_FUNCTIONS_LIBRARY);
	}
	
	public java.util.List<String> getAdditionalCategories() {
		return Arrays.asList(categoriesList.getItems());
	}

	@Override
	public boolean isPageComplete() {
		// additional categories are not mandatory
		return true;
	}
	
}
