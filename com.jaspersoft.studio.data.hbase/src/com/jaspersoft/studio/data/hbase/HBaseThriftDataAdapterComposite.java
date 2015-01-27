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
package com.jaspersoft.studio.data.hbase;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.ADataAdapterComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseThriftDataAdapterComposite extends ADataAdapterComposite {
	private Text host;
	private Text port;

	private HBaseThriftDataAdapterDescriptor dataAdapterDescriptor;

	public HBaseThriftDataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {
		super(parent, style, jrContext);
		initComponents();
	}

	private void initComponents() {
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("Host Name");

		host = new Text(this, SWT.BORDER);
		host.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(this, SWT.NONE).setText("Port");

		port = new Text(this, SWT.BORDER);
		port.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	@Override
	public void setDataAdapter(DataAdapterDescriptor dataAdapterDescriptor) {
		super.setDataAdapter(dataAdapterDescriptor);
		this.dataAdapterDescriptor = (HBaseThriftDataAdapterDescriptor) dataAdapterDescriptor;
		bindWidgets(dataAdapterDescriptor.getDataAdapter());
	}

	public HBaseThriftDataAdapterDescriptor getDataAdapter() {
		if (dataAdapterDescriptor == null)
			dataAdapterDescriptor = new HBaseThriftDataAdapterDescriptor();
		return dataAdapterDescriptor;
	}

	@Override
	protected void bindWidgets(DataAdapter dataAdapter) {
		bindingContext.bindValue(SWTObservables.observeText(host, SWT.Modify), PojoObservables.observeValue(dataAdapter, "host")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeText(port, SWT.Modify), PojoObservables.observeValue(dataAdapter, "port")); //$NON-NLS-1$
	}

	@Override
	public String getHelpContextId() {
		return PREFIX.concat("adapter_hbasethrift");
	}
}
