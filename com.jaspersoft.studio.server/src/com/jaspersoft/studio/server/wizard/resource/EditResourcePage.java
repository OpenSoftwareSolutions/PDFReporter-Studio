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
package com.jaspersoft.studio.server.wizard.resource;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.wizards.AWizardPage;

public class EditResourcePage extends AWizardPage {
	private APageContent[] rcontent;
	private TabFolder tabFolder;
	private List<String> helpContexts;

	public EditResourcePage(APageContent... rcontent) {
		super("editresourcepage"); //$NON-NLS-1$
		this.rcontent = rcontent;
		setTitle(Messages.EditResourcePage_1);
		setDescription(Messages.EditResourcePage_2);
		for (APageContent p : rcontent)
			p.setPage(this);
	}

	public void createControl(Composite parent) {
		tabFolder = new TabFolder(parent, SWT.TOP);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		DataBindingContext bindingContext = new DataBindingContext();
		WizardPageSupport.create(this, bindingContext);
		helpContexts = new ArrayList<String>();
		for (APageContent pc : rcontent) {
			Control cmp = pc.createContent(tabFolder);
			if (cmp == null)
				continue;
			pc.setBindingContext(bindingContext);

			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText(pc.getName());
			helpContexts.add(pc.getHelpContext());
			item.setControl(cmp);
		}
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectHelpByTab(tabFolder.getSelectionIndex());
			}
		});
		setControl(tabFolder);
		selectHelpByTab(1);
	}

	private void selectHelpByTab(int selectedTab) {
		if (selectedTab < 0 || helpContexts.size() <= selectedTab)
			setContextName(helpContexts.get(0));
		else
			setContextName(helpContexts.get(selectedTab));
	}

	public void setFirstPage(int indx) {
		if (tabFolder.getItemCount() > indx)
			tabFolder.setSelection(indx);
	}

	@Override
	public void dispose() {
		for (APageContent p : rcontent)
			p.dispose();
		super.dispose();
	}

	@Override
	protected String getContextName() {
		return null;
	}
}
