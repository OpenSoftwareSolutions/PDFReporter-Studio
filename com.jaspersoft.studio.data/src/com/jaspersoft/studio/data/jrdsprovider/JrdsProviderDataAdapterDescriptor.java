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
package com.jaspersoft.studio.data.jrdsprovider;

import java.util.List;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.provider.DataSourceProviderDataAdapter;
import net.sf.jasperreports.data.provider.DataSourceProviderDataAdapterImpl;
import net.sf.jasperreports.data.provider.DataSourceProviderDataAdapterService;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.AWizardDataEditorComposite;
import com.jaspersoft.studio.data.Activator;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterEditor;
import com.jaspersoft.studio.data.IWizardDataEditorProvider;
import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.data.ui.EmptyWizardDataEditorComposite;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JrdsProviderDataAdapterDescriptor extends DataAdapterDescriptor implements IFieldsProvider, IWizardDataEditorProvider {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	@Override
	public DataSourceProviderDataAdapter getDataAdapter() {
		if (dataAdapter == null) {
			dataAdapter = new DataSourceProviderDataAdapterImpl();
			((DataSourceProviderDataAdapter) dataAdapter).setProviderClass("com.jaspersoft.studio.data.sample.PersonBeansDataSource");
		}
		return (DataSourceProviderDataAdapter) dataAdapter;
	}

	@Override
	public DataAdapterEditor getEditor() {
		return new JrdsProviderDataAdapterEditor();
	}

	private IFieldsProvider fprovider;

	public List<JRDesignField> getFields(DataAdapterService con, JasperReportsConfiguration jConfig, JRDataset jDataset) throws JRException, UnsupportedOperationException {
		getFieldProvider(jConfig);
		return fprovider.getFields(con, jConfig, jDataset);
	}

	public boolean supportsGetFieldsOperation(JasperReportsConfiguration jConfig) {
		getFieldProvider(jConfig);
		return fprovider.supportsGetFieldsOperation(jConfig);
	}

	private void getFieldProvider(JasperReportsConfiguration jConfig) {
		if (fprovider == null) {
			fprovider = new JRDSProviderFieldsProvider();
			DataSourceProviderDataAdapterService ds = new DataSourceProviderDataAdapterService(jConfig, getDataAdapter());
			try {
				((JRDSProviderFieldsProvider) fprovider).setProvider(ds.getProvider());
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 */
	@Override
	public Image getIcon(int size) {
		if (size == 16) {
			return Activator.getDefault().getImage("icons/bean-green.png");
		}
		return null;
	}

	@Override
	public AWizardDataEditorComposite createDataEditorComposite(Composite parent, WizardPage page) {
		return new EmptyWizardDataEditorComposite(parent, page, this);
	}
}
