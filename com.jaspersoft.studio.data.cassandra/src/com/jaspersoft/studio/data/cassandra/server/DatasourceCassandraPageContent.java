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
package com.jaspersoft.studio.data.cassandra.server;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.data.cassandra.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceCustom;
import com.jaspersoft.studio.server.protocol.Version;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.UIUtil;

public class DatasourceCassandraPageContent extends APageContent {

	private Text tname;
	private Text hostname;
	private Text port;
	private Text keyspace;
	private Text username;
	private Text pass;

	public DatasourceCassandraPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public DatasourceCassandraPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.datasource.cassandra";
	}

	@Override
	public String getName() {
		return Messages.RDDatasourceCassandraPage_title;
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		if (Version.isGreaterThan(res.getWsClient().getServerInfo(), "5.6")) {
			UIUtil.createLabel(composite, "Hostname");
			hostname = new Text(composite, SWT.BORDER);
			hostname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			UIUtil.createLabel(composite, "Port");
			port = new Text(composite, SWT.BORDER);
			port.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			UIUtil.createLabel(composite, "Keyspace");
			keyspace = new Text(composite, SWT.BORDER);
			keyspace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			UIUtil.createLabel(composite, "Username");
			username = new Text(composite, SWT.BORDER);
			username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			UIUtil.createLabel(composite, "Password");
			pass = new Text(composite, SWT.BORDER | SWT.PASSWORD);
			pass.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		} else {
			UIUtil.createLabel(composite, Messages.CassandraDataAdapterComposite_labelurl);

			tname = new Text(composite, SWT.BORDER);
			tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		rebind();
		return composite;
	}

	@Override
	protected void rebind() {
		ResourceProperty resprop = ResourceDescriptorUtil.getProperty(MRDatasourceCustom.PROP_DATASOURCE_CUSTOM_PROPERTY_MAP, res.getValue().getProperties());

		if (Version.isGreaterThan(res.getWsClient().getServerInfo(), "5.6")) {
			ResourceProperty rsprop = ResourceDescriptorUtil.getProperty("hostname", resprop.getProperties());
			if (rsprop == null) {
				rsprop = new ResourceProperty("hostname", "");
				resprop.getProperties().add(rsprop);
			}
			bindingContext.bindValue(SWTObservables.observeText(hostname, SWT.Modify), PojoObservables.observeValue(rsprop, "value")); //$NON-NLS-1$

			rsprop = ResourceDescriptorUtil.getProperty("port", resprop.getProperties());
			if (rsprop == null) {
				rsprop = new ResourceProperty("port", "");
				resprop.getProperties().add(rsprop);
			}
			bindingContext.bindValue(SWTObservables.observeText(port, SWT.Modify), PojoObservables.observeValue(rsprop, "value")); //$NON-NLS-1$

			rsprop = ResourceDescriptorUtil.getProperty("keyspace", resprop.getProperties());
			if (rsprop == null) {
				rsprop = new ResourceProperty("keyspace", "");
				resprop.getProperties().add(rsprop);
			}
			bindingContext.bindValue(SWTObservables.observeText(keyspace, SWT.Modify), PojoObservables.observeValue(rsprop, "value")); //$NON-NLS-1$

			rsprop = ResourceDescriptorUtil.getProperty("username", resprop.getProperties());
			if (rsprop == null) {
				rsprop = new ResourceProperty("username", "");
				resprop.getProperties().add(rsprop);
			}
			bindingContext.bindValue(SWTObservables.observeText(username, SWT.Modify), PojoObservables.observeValue(rsprop, "value")); //$NON-NLS-1$

			rsprop = ResourceDescriptorUtil.getProperty("password", resprop.getProperties());
			if (rsprop == null) {
				rsprop = new ResourceProperty("password", "");
				resprop.getProperties().add(rsprop);
			}
			bindingContext.bindValue(SWTObservables.observeText(pass, SWT.Modify), PojoObservables.observeValue(rsprop, "value")); //$NON-NLS-1$
		} else {
			ResourceProperty rsprop = ResourceDescriptorUtil.getProperty(MRDatasourceCassandra.JDBC_URL, resprop.getProperties());
			if (rsprop == null) {
				rsprop = new ResourceProperty(MRDatasourceCassandra.JDBC_URL, "");
				resprop.getProperties().add(rsprop);
			}
			bindingContext.bindValue(SWTObservables.observeText(tname, SWT.Modify), PojoObservables.observeValue(rsprop, "value")); //$NON-NLS-1$
		}
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.adapter_cassandra";
	}
}
