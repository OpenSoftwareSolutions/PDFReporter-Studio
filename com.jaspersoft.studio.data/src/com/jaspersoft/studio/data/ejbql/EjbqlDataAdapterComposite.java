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
package com.jaspersoft.studio.data.ejbql;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.ejbql.EjbqlDataAdapter;
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
import com.jaspersoft.studio.data.messages.Messages;

public class EjbqlDataAdapterComposite extends ADataAdapterComposite {

	private Text punitName;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EjbqlDataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {
		super(parent, style, jrContext);
		setLayout(new GridLayout(1, false));

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setText(Messages.EjbqlDataAdapterComposite_0);

		punitName = new Text(this, SWT.BORDER);
		punitName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	@Override
	protected void bindWidgets(DataAdapter dataAdapter) {
		bindingContext.bindValue(SWTObservables.observeText(punitName,
				SWT.Modify), PojoObservables.observeValue(dataAdapter,
				"persistanceUnitName")); //$NON-NLS-1$
	}

	public DataAdapterDescriptor getDataAdapter() {
		if (dataAdapterDesc == null)
			dataAdapterDesc = new EjbqlDataAdapterDescriptor();

		EjbqlDataAdapter dataAdapter = (EjbqlDataAdapter) dataAdapterDesc
				.getDataAdapter();

		dataAdapter.setPersistanceUnitName(punitName.getText());

		return dataAdapterDesc;
	}

	
	@Override
	public String getHelpContextId() {
		return PREFIX.concat("adapter_EJBQL");
	}
}
