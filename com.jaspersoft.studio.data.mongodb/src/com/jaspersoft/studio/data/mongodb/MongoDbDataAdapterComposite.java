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
package com.jaspersoft.studio.data.mongodb;

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

import com.jaspersoft.mongodb.adapter.MongoDbDataAdapter;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.ADataAdapterComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.mongodb.messages.Messages;
import com.jaspersoft.studio.data.secret.DataAdaptersSecretsProvider;
import com.jaspersoft.studio.swt.widgets.WSecretText;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class MongoDbDataAdapterComposite extends ADataAdapterComposite {
	private Text mongoUriField;
	private Text usernameField;
	private WSecretText passwordField;

	private MongoDbDataAdapterDescriptor dataAdapterDescriptor;

	public MongoDbDataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {
		super(parent, style, jrContext);
		initComponents();
	}

	private void initComponents() {
		setLayout(new GridLayout(2, false));

		createLabel(Messages.MongoDbDataAdapterComposite_labelURI);
		mongoUriField = new Text(this, SWT.BORDER);
		mongoUriField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		createLabel(Messages.MongoDbDataAdapterComposite_labelUsername);
		usernameField = new Text(this, SWT.BORDER);
		usernameField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		createLabel(Messages.MongoDbDataAdapterComposite_labelPassword);
		passwordField = new WSecretText(this, SWT.BORDER | SWT.PASSWORD);
		passwordField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	private void createLabel(String text) {
		Label label = new Label(this, SWT.NONE);
		label.setText(text);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	}

	public DataAdapterDescriptor getDataAdapter() {
		if (dataAdapterDescriptor == null) {
			dataAdapterDescriptor = new MongoDbDataAdapterDescriptor();
		}
		return dataAdapterDescriptor;
	}

	@Override
	public void setDataAdapter(DataAdapterDescriptor dataAdapterDescriptor) {
		super.setDataAdapter(dataAdapterDescriptor);
		this.dataAdapterDescriptor = (MongoDbDataAdapterDescriptor) dataAdapterDescriptor;
		MongoDbDataAdapter dataAdapter = (MongoDbDataAdapter) dataAdapterDescriptor.getDataAdapter();
		if (!passwordField.isWidgetConfigured()) {
			passwordField.loadSecret(DataAdaptersSecretsProvider.SECRET_NODE_ID, passwordField.getText());
		}
		bindWidgets(dataAdapter);
	}

	@Override
	protected void bindWidgets(DataAdapter dataAdapter) {
		bindingContext.bindValue(SWTObservables.observeText(mongoUriField, SWT.Modify), PojoObservables.observeValue(dataAdapter, "mongoURI")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeText(usernameField, SWT.Modify), PojoObservables.observeValue(dataAdapter, "username")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeText(passwordField, SWT.Modify), PojoObservables.observeValue(dataAdapter, "password")); //$NON-NLS-1$
	}

	@Override
	public void performAdditionalUpdates() {
		if (JaspersoftStudioPlugin.shouldUseSecureStorage()) {
			passwordField.persistSecret();
			// update the "password" replacing it with the UUID key saved in secure
			// preferences
			MongoDbDataAdapter mongoDataAdapter = (MongoDbDataAdapter) dataAdapterDesc.getDataAdapter();
			mongoDataAdapter.setPassword(passwordField.getUUIDKey());
		}
	}
	
	@Override
	public String getHelpContextId() {
		return PREFIX.concat("adapter_mongodb"); //$NON-NLS-1$
	}
}
