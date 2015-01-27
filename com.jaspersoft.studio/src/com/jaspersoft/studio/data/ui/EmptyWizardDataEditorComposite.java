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
package com.jaspersoft.studio.data.ui;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.data.AWizardDataEditorComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.JSSWizard;

/**
 * Empty editor composite that is supposed to be used by those 
 * {@link DataAdapterDescriptor} that directly provide the facility 
 * to retrieve fields (CVS, Excel, etc.).
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class EmptyWizardDataEditorComposite extends AWizardDataEditorComposite {

	private DataAdapterDescriptor dataAdapterDescriptor;
	private WizardPage page;
	private JRDesignDataset dataset;
	
	public EmptyWizardDataEditorComposite(Composite parent, WizardPage page, DataAdapterDescriptor dataAdapterDescriptor) {
		super(parent, page);
		this.dataAdapterDescriptor=dataAdapterDescriptor;
		this.page=page;
		this.setLayout(new FillLayout(SWT.HORIZONTAL));
		Label msg=new Label(this,SWT.NONE);
		msg.setText(Messages.EmptyWizardDataEditorComposite_TitleMsg);
	}

	@Override
	public String getQueryString() {
		// A query string does not make sense
		return null;
	}

	@Override
	public String getQueryLanguage() {
		// A query language does not make sense 
		return null;
	}

	@Override
	public List<JRDesignField> readFields() throws Exception {
		if (getDataAdapterDescriptor() != null && getDataAdapterDescriptor() instanceof IFieldsProvider) {
			try {
				return ((IFieldsProvider) getDataAdapterDescriptor()).getFields(
						DataAdapterServiceUtil.getInstance(getJasperReportsConfiguration()).getService(
								getDataAdapterDescriptor().getDataAdapter()), getJasperReportsConfiguration(), getDataset());

			} catch (JRException ex) {
				// Cleanup of the error. JRException are a very low meaningful exception when working
				// with data, what the user is interested into is the underline error (i.e. an SQL error).
				// That's why we rise the real cause, if any instead of rising the highlevel exception...
				if (ex.getCause() != null && ex.getCause() instanceof Exception) {
					throw (Exception) ex.getCause();
				}
				throw ex;
			}
		}
		return new ArrayList<JRDesignField>();
	}

	public JasperReportsConfiguration getJasperReportsConfiguration() {
		if (getPage() != null && getPage().getWizard() != null && getPage().getWizard() instanceof JSSWizard) {
			return ((JSSWizard) getPage().getWizard()).getConfig();
		}
		return JasperReportsConfiguration.getDefaultJRConfig();
	}
	
	/**
	 * Convenient way to crate a dataset object to be passed to the IFieldsProvider.getFields method
	 * 
	 * @return JRDesignDataset return a dataset with the proper query and language set...
	 */
	public JRDesignDataset getDataset() {
		if (dataset == null) {
			dataset = new JRDesignDataset(getJasperReportsConfiguration(), true);
		}
		return dataset;
	}
	
	/**
	 * @return the page
	 */
	public WizardPage getPage() {
		return page;
	}
	
	/**
	 * @return the dataAdapterDescriptor
	 */
	public DataAdapterDescriptor getDataAdapterDescriptor() {
		return dataAdapterDescriptor;
	}

	@Override
	public List<JRDesignParameter> readParameters() throws Exception { 
		return null;
	}

}
