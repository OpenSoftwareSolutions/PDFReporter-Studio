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
package com.jaspersoft.studio.components.chart.wizard.fragments.data.dialog;

import java.util.ArrayList;
import java.util.Map;

import net.sf.jasperreports.engine.design.JRDesignElementDataset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

public class ChartDatasetDialog extends FormDialog {
	private Map<Class<? extends JRDesignElementDataset>, String> map;
	private Class<? extends JRDesignElementDataset> selection;
	private java.util.List<Class<? extends JRDesignElementDataset>> list;

	public ChartDatasetDialog(Shell parentShellProvider,
			Map<Class<? extends JRDesignElementDataset>, String> map,
			Class<? extends JRDesignElementDataset> selection) {
		super(parentShellProvider);
		this.map = map;
		this.selection = selection;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Chart Dataset");
	}

	@Override
	protected void createFormContent(IManagedForm mform) {
		mform.getForm().setText("Select a Chart Dataset ");

		mform.getForm().getBody().setLayout(new GridLayout());

		final List dsList = new List(mform.getForm().getBody(), SWT.READ_ONLY
				| SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		gd.widthHint = 200;
		dsList.setLayoutData(gd);
		dsList.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				selection = list.get(dsList.getSelectionIndex());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		list = new ArrayList<Class<? extends JRDesignElementDataset>>();
		java.util.List<String> items = new ArrayList<String>();
		for (Class<? extends JRDesignElementDataset> key : map.keySet()) {
			list.add(key);
			items.add(map.get(key));
		}
		dsList.setItems(items.toArray(new String[items.size()]));
		dsList.select(list.indexOf(selection));
	}

	public Class<? extends JRDesignElementDataset> getSelection() {
		return selection;
	}
}
