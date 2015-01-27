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
package com.jaspersoft.studio.wizards.group;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.messages.Messages;

public class WizardBandGroupLayoutPage extends WizardPage {
	private boolean addHeader = true;
	private boolean addFooter = true;

	public boolean isAddHeader() {
		return addHeader;
	}

	public boolean isAddFooter() {
		return addFooter;
	}

	public WizardBandGroupLayoutPage() {
		super("grouplayout"); //$NON-NLS-1$
		setTitle(Messages.WizardBandGroupLayoutPage_group_layout);
		setDescription(Messages.WizardBandGroupLayoutPage_description);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		setControl(composite);

		final Button addHeaderButon = new Button(composite, SWT.CHECK);
		addHeaderButon.setText(Messages.WizardBandGroupLayoutPage_add_group_header);
		addHeaderButon.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				addHeader = addHeaderButon.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		addHeaderButon.setSelection(true);

		final Button addFooterButon = new Button(composite, SWT.CHECK);
		addFooterButon.setText(Messages.WizardBandGroupLayoutPage_add_group_footer);
		addFooterButon.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				addFooter = addFooterButon.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		addFooterButon.setSelection(true);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "Jaspersoft.wizard");
	}

}
