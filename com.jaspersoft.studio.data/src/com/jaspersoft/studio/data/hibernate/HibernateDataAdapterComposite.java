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
package com.jaspersoft.studio.data.hibernate;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.hibernate.HibernateDataAdapter;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.ADataAdapterComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.messages.Messages;

public class HibernateDataAdapterComposite extends ADataAdapterComposite {

	private Text xmlFileName;
	private Text propFileName;
	private Button btnUseAnnotation;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public HibernateDataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {

		super(parent, style, jrContext);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText(Messages.HibernateDataAdapterComposite_0);

		xmlFileName = new Text(composite, SWT.BORDER);
		xmlFileName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button btnBrowse = new Button(composite, SWT.NONE);
		btnBrowse.setText(Messages.HibernateDataAdapterComposite_1);

		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
				fd.setFilterPath(root.getLocation().toOSString());
				fd.setFileName(xmlFileName.getText());
				fd.setFilterExtensions(new String[] { "*.cfg.xml", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
				String selection = fd.open();
				if (selection != null)
					xmlFileName.setText(selection);
			}
		});

		lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText(Messages.HibernateDataAdapterComposite_4);

		propFileName = new Text(composite, SWT.BORDER);
		propFileName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		btnBrowse = new Button(composite, SWT.PUSH);
		btnBrowse.setText(Messages.HibernateDataAdapterComposite_5);

		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
				fd.setFilterPath(root.getLocation().toOSString());
				fd.setFileName(xmlFileName.getText());
				fd.setFilterExtensions(new String[] { "*.properties", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
				String selection = fd.open();
				if (selection != null)
					propFileName.setText(selection);
			}
		});

		btnUseAnnotation = new Button(composite, SWT.CHECK);
		btnUseAnnotation.setText(Messages.HibernateDataAdapterComposite_8);

	}

	@Override
	protected void bindWidgets(DataAdapter dataAdapter) {
		bindingContext.bindValue(SWTObservables.observeText(xmlFileName, SWT.Modify), PojoObservables.observeValue(dataAdapter, "XMLFileName")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeText(propFileName, SWT.Modify), PojoObservables.observeValue(dataAdapter, "propertiesFileName")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(btnUseAnnotation), PojoObservables.observeValue(dataAdapter, "useAnnotation")); //$NON-NLS-1$
	}

	public DataAdapterDescriptor getDataAdapter() {
		if (dataAdapterDesc == null)
			dataAdapterDesc = new HibernateDataAdapterDescriptor();

		HibernateDataAdapter dataAdapter = (HibernateDataAdapter) dataAdapterDesc.getDataAdapter();

		dataAdapter.setXMLFileName(xmlFileName.getText());
		dataAdapter.setPropertiesFileName(propFileName.getText());
		dataAdapter.setUseAnnotation(btnUseAnnotation.getSelection());

		return dataAdapterDesc;
	}

	@Override
	public String getHelpContextId() {
		return PREFIX.concat("adapter_hibernate");
	}
}
