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
package com.jaspersoft.studio.data.cassandra;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.conversion.NumberToStringConverter;
import org.eclipse.core.databinding.conversion.StringToNumberConverter;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.NumberFormat;
import com.jaspersoft.connectors.cassandra.adapter.CassandraDataAdapter;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.ADataAdapterComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.secret.DataAdaptersSecretsProvider;
import com.jaspersoft.studio.swt.widgets.WSecretText;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class CassandraDataAdapterComposite extends ADataAdapterComposite {
	private Text hostname;
	private Text port;
	private Text keyspace;
	private Text username;
	private WSecretText password;

	private CassandraDataAdapterDescriptor dataAdapterDescriptor;

	public CassandraDataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {
		super(parent, style, jrContext);
		initComponents();
	}

	private void initComponents() {
		setLayout(new GridLayout(2, false));

		createLabel("Hostname:");
		hostname = createTextField();

		createLabel("Port:");
		port = createTextField();

		createLabel("Keyspace:");
		keyspace = createTextField();
		
		createLabel("Username:");
		username = createTextField();
		
		createLabel("Password:");
		password = createPasswordField();
	}

	private void createLabel(String text) {
		Label label = new Label(this, SWT.NONE);
		label.setText(text);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));
	}

	private Text createTextField() {
		Text textField = new Text(this, SWT.BORDER);
		textField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		return textField;
	}
	
	private WSecretText createPasswordField() {
		WSecretText passwd = new WSecretText(this, SWT.BORDER | SWT.PASSWORD);
		passwd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		return passwd;
	}

	public DataAdapterDescriptor getDataAdapter() {
		if (dataAdapterDescriptor == null) {
			dataAdapterDescriptor = new CassandraDataAdapterDescriptor();
		}
		return dataAdapterDescriptor;
	}

	@Override
	public void setDataAdapter(DataAdapterDescriptor dataAdapterDescriptor) {
		super.setDataAdapter(dataAdapterDescriptor);

		if (!password.isWidgetConfigured()) {
			password.loadSecret(DataAdaptersSecretsProvider.SECRET_NODE_ID, password.getText());
		}
		
		this.dataAdapterDescriptor = (CassandraDataAdapterDescriptor) dataAdapterDescriptor;
	}

	@Override
	protected void bindWidgets(DataAdapter dataAdapter) {
		bindingContext.bindValue(
				SWTObservables.observeText(hostname, SWT.Modify),
				PojoObservables.observeValue(dataAdapter, "hostname")); //$NON-NLS-1$
		bindingContext.bindValue(
				SWTObservables.observeText(keyspace, SWT.Modify),
				PojoObservables.observeValue(dataAdapter, "keyspace")); //$NON-NLS-1$
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);
		IConverter targetToModelConverter = StringToNumberConverter.toInteger(numberFormat, true);
		IConverter modelToTargetConverter = NumberToStringConverter.fromInteger(numberFormat, true);
		bindingContext.bindValue(
				SWTObservables.observeText(port, SWT.Modify),
				PojoObservables.observeValue(dataAdapter, "port"),
				new UpdateValueStrategy().setConverter(targetToModelConverter),
				new UpdateValueStrategy().setConverter(modelToTargetConverter)); //$NON-NLS-1$
		bindingContext.bindValue(
				SWTObservables.observeText(username, SWT.Modify),
				PojoObservables.observeValue(dataAdapter, "username")); //$NON-NLS-1$
		bindingContext.bindValue(
				SWTObservables.observeText(password, SWT.Modify),
				PojoObservables.observeValue(dataAdapter, "password")); //$NON-NLS-1$
		
		port.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				for (char c : e.text.toCharArray()){
					if (!Character.isDigit(c)) {
						e.doit = false;
						return;
					}
				}
			}
		});
	}

	@Override
	public String getHelpContextId() {
		return PREFIX.concat("adapter_cassandra");
	}
	
	@Override
	public void performAdditionalUpdates() {
		if (JaspersoftStudioPlugin.shouldUseSecureStorage()) {
			password.persistSecret();
			// update the "password" replacing it with the UUID key saved in secure
			// preferences
			CassandraDataAdapter cassandraDA = (CassandraDataAdapter) dataAdapterDesc.getDataAdapter();
			cassandraDA.setPassword(password.getUUIDKey());
		}
	}
}
