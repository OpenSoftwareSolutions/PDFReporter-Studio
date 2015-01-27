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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.IQueryDesigner;
import com.jaspersoft.studio.data.designer.AQueryDesignerContainer;
import com.jaspersoft.studio.data.designer.AQueryStatus;
import com.jaspersoft.studio.property.dataset.dialog.QDesignerFactory;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class WizardQueryEditorComposite extends SimpleQueryWizardDataEditorComposite {

	public WizardQueryEditorComposite(Composite parent, WizardPage page, DataAdapterDescriptor dataAdapterDescriptor,
			String lang) {
		super(parent, page, lang);
		this.setDataAdapterDescriptor(dataAdapterDescriptor);
	}

	protected IQueryDesigner designer;
	private QDesignerFactory qdfactory;

	@Override
	protected void createCompositeContent() {
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);

		AQueryDesignerContainer qdc = new AQueryDesignerContainer() {
			protected void createStatusBar(Composite comp) {
				qStatus = new WizardQueryStatus(getPage());
			}

			@Override
			public AQueryStatus getQueryStatus() {
				if (qStatus == null)
					createStatusBar(null);
				return super.getQueryStatus();
			}

			@Override
			public void setParameters(List<JRDesignParameter> params) {

			}

			@Override
			public void setFields(List<JRDesignField> fields) {

			}

			@Override
			public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable)
					throws InvocationTargetException, InterruptedException {
				getPage().getWizard().getContainer().run(fork, cancelable, runnable);
			}

			@Override
			public DataAdapterDescriptor getDataAdapter() {
				return getDataAdapterDescriptor();
			}

			@Override
			public List<JRDesignField> getCurrentFields() {
				return new ArrayList<JRDesignField>();
			}

			@Override
			public int getContainerType() {
				return CONTAINER_WITH_NO_TABLES;
			}

			@Override
			public void doGetFields(IProgressMonitor monitor) {
				monitor.beginTask(com.jaspersoft.studio.messages.Messages.DataQueryAdapters_jobname, -1);

				ClassLoader oldClassloader = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(jConfig.getClassLoader());

				try {
					setFields(readFields());
				} catch (Exception e) {
					if (e.getCause() != null)
						qStatus.showError(e.getCause().getMessage(), e);
					else
						qStatus.showError(e);
				} finally {
					Thread.currentThread().setContextClassLoader(oldClassloader);
					monitor.done();
				}
			}
		};
		qdfactory = new QDesignerFactory(this, null, qdc);
		designer = qdfactory.getDesigner(getQueryLanguage());

		designer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

	}

	@Override
	public void setDataAdapterDescriptor(DataAdapterDescriptor dataAdapterDescriptor) {
		super.setDataAdapterDescriptor(dataAdapterDescriptor);
		JasperReportsConfiguration jConfig = getJasperReportsConfiguration();
		designer.setQuery(jConfig.getJasperDesign(), getDataset(), jConfig);
		designer.setDataAdapter(dataAdapterDescriptor);
	}

	@Override
	public void dispose() {
		qdfactory.dispose();
		super.dispose();
	}

	public String getQueryString() {
		return designer.getQuery();
	}
}
