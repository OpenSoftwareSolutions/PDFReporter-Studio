/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.prm.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.w3c.tools.codec.Base64Decoder;
import org.w3c.tools.codec.Base64FormatException;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.prm.ParameterSet;
import com.jaspersoft.studio.prm.ParameterSetProvider;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ParameterSetWizardPage extends WizardPage {
	private JasperReportsConfiguration jConfig;
	private boolean override = false;
	private ParameterSet prmSet;
	private List lst;
	private String selected;

	public ParameterSetWizardPage(JasperReportsConfiguration jConfig) {
		super("parametersetpage"); //$NON-NLS-1$
		setTitle(Messages.ParameterSetWizardPage_1);
		setDescription(Messages.ParameterSetWizardPage_2);
		this.jConfig = jConfig;
	}

	@Override
	public void createControl(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout());
		setControl(cmp);

		lst = new List(cmp, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		lst.setLayoutData(new GridData(GridData.FILL_BOTH));
		lst.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selected = lst.getItem(lst.getSelectionIndex());
			}
		});

		final Button btn = new Button(cmp, SWT.CHECK);
		btn.setText(Messages.ParameterSetWizardPage_3);
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				override = btn.getSelection();
			}
		});

		String str = jConfig.getProperty(ParameterSet.PARAMETER_SETS);
		if (str != null) {
			try {
				str = new Base64Decoder(str).processString();
			} catch (Base64FormatException e) {
				e.printStackTrace();
				return;
			}
			String[] sets = str.split("\n"); //$NON-NLS-1$

			for (String key : sets) {
				lst.add(key);
				if (lst.getSelectionIndex() != 0) {
					lst.setSelection(0);
					selected = key;
				}
			}
		}
	}

	public boolean isOverride() {
		return override;
	}

	public ParameterSet getValue() {
		String str = selected;
		if (!Misc.isNullOrEmpty(str))
			return ParameterSetProvider.getParameterSet(str, jConfig.getPrefStore());
		return null;
	}

}
