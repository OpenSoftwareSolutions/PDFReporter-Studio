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
package com.jaspersoft.studio.server.wizard.resource.page.datasource;

import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceAWS;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.utils.UIUtil;

public class DatasourceAWSPageContent extends DatasourceJDBCPageContent {

	private Text awsAccessKey;
	private Text awsSecretKey;
	private Text awsArn;
	private Binding bAccessKey;
	private Binding bSecretKey;
	private Binding bArn;
	private ResourceProperty pAccessKey;
	private ResourceProperty pSecretKey;
	private ResourceProperty pArn;
	private Button ec2Cred;
	private Button awsCred;

	public DatasourceAWSPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public DatasourceAWSPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.datasource.aws";
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		ec2Cred = new Button(composite, SWT.RADIO);
		ec2Cred.setText("Use EC2 instance credentials.");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		ec2Cred.setLayoutData(gd);
		ec2Cred.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ec2Cred.getSelection()) {
					ResourceDescriptor rd = res.getValue();
					if (pAccessKey != null)
						rd.getProperties().remove(pAccessKey);
					if (pSecretKey != null)
						rd.getProperties().remove(pSecretKey);
					if (pArn != null)
						rd.getProperties().remove(pArn);
					unbindAWS();
					setEC2Settings(true);
				}
			}
		});

		awsCred = new Button(composite, SWT.RADIO);
		awsCred.setText("Use AWS Credentials (Click here to generate using CloudFormation, see Outputs for keys after stack completes).");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		awsCred.setLayoutData(gd);
		awsCred.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (awsCred.getSelection()) {
					ResourceDescriptor rd = res.getValue();
					if (pAccessKey == null)
						pAccessKey = new ResourceProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_ACCESS_KEY, "");
					rd.getProperties().add(pAccessKey);
					if (pSecretKey == null)
						pSecretKey = new ResourceProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_SECRET_KEY, "");
					rd.getProperties().add(pSecretKey);
					if (pArn == null)
						pArn = new ResourceProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_ROLE_ARN, "");
					rd.getProperties().add(pArn);

					bindAWS();
					setEC2Settings(false);
				}
			}
		});

		UIUtil.createLabel(composite, "AWS Access Key");

		awsAccessKey = new Text(composite, SWT.BORDER);
		awsAccessKey.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, "AWS Secret Key");

		awsSecretKey = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		awsSecretKey.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, "ARN");

		awsArn = new Text(composite, SWT.BORDER);
		awsArn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, "AWS Region");

		Text awsRegion = new Text(composite, SWT.BORDER);
		awsRegion.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, "AWS Datasource");

		Text awsService = new Text(composite, SWT.BORDER);
		awsService.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, "AWS Instance DB Identifier");

		Text awsDBInstance = new Text(composite, SWT.BORDER);
		awsDBInstance.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, "Data Source DB Name");

		Text awsDSDBName = new Text(composite, SWT.BORDER);
		awsDSDBName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Control c = super.createContent(composite);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		c.setLayoutData(gd);

		List<ResourceProperty> props = res.getValue().getProperties();

		bindAWS();
		ResourceProperty resprop = ResourceDescriptorUtil.getProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_ACCESS_KEY, props);
		setEC2Settings(resprop == null);

		resprop = ResourceDescriptorUtil.getProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_REGION, props);

		bindingContext.bindValue(SWTObservables.observeText(awsRegion, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$

		resprop = ResourceDescriptorUtil.getProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_DB_SERVICE, props);

		bindingContext.bindValue(SWTObservables.observeText(awsService, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$

		resprop = ResourceDescriptorUtil.getProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_DB_INSTANCE_IDENTIFIER, props);

		bindingContext.bindValue(SWTObservables.observeText(awsDBInstance, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$

		resprop = ResourceDescriptorUtil.getProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_DB_NAME, props);

		bindingContext.bindValue(SWTObservables.observeText(awsDSDBName, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$
		return composite;
	}

	private void setEC2Settings(boolean ec2) {
		ec2Cred.setSelection(ec2);

		awsCred.setSelection(!ec2);
		awsAccessKey.setEnabled(!ec2);
		awsSecretKey.setEnabled(!ec2);
		awsArn.setEnabled(!ec2);
	}

	private void bindAWS() {
		List<ResourceProperty> props = res.getValue().getProperties();

		ResourceProperty resprop = ResourceDescriptorUtil.getProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_ACCESS_KEY, props);
		if (resprop != null)
			bAccessKey = bindingContext.bindValue(SWTObservables.observeText(awsAccessKey, SWT.Modify), PojoObservables.observeValue(resprop, "value"));

		resprop = ResourceDescriptorUtil.getProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_SECRET_KEY, props);
		if (resprop != null)
			bSecretKey = bindingContext.bindValue(SWTObservables.observeText(awsSecretKey, SWT.Modify), PojoObservables.observeValue(resprop, "value"));

		resprop = ResourceDescriptorUtil.getProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_ROLE_ARN, props);

		if (resprop != null)
			bArn = bindingContext.bindValue(SWTObservables.observeText(awsArn, SWT.Modify), PojoObservables.observeValue(resprop, "value"));
	}

	private void unbindAWS() {
		if (bAccessKey != null)
			bindingContext.removeBinding(bAccessKey);
		if (bSecretKey != null)
			bindingContext.removeBinding(bSecretKey);
		if (bArn != null)
			bindingContext.removeBinding(bArn);
	}

	@Override
	protected void createImportButton(Composite composite, Text tdriver, Text turl, Text tuser, Text tpass) {
	}
}
