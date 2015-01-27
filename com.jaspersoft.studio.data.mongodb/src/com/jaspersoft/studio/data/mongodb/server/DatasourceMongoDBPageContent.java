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
package com.jaspersoft.studio.data.mongodb.server;

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
import com.jaspersoft.studio.data.mongodb.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceCustom;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.UIUtil;

public class DatasourceMongoDBPageContent extends APageContent {

	private Text turi;
	private Text tusername;
	private Text tpass;

	public DatasourceMongoDBPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public DatasourceMongoDBPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.datasource.mongodb";
	}

	@Override
	public String getName() {
		return Messages.RDDatasourceMongoDBPage_title;
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		UIUtil.createLabel(composite, Messages.RDDatasourceMongoDBPage_labelurl);

		turi = new Text(composite, SWT.BORDER);
		turi.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, Messages.RDDatasourceMongoDBPage_username);

		tusername = new Text(composite, SWT.BORDER);
		tusername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (res.getValue().getIsNew()) {
			UIUtil.createLabel(composite, Messages.RDDatasourceMongoDBPage_pass);

			tpass = new Text(composite, SWT.BORDER | SWT.PASSWORD);
			tpass.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		rebind();
		return composite;
	}

	@Override
	protected void rebind() {
		ResourceProperty resprop = ResourceDescriptorUtil.getProperty(MRDatasourceCustom.PROP_DATASOURCE_CUSTOM_PROPERTY_MAP, res.getValue().getProperties());

		ResourceProperty rsp = ResourceDescriptorUtil.getProperty(MRDatasourceMongoDB.MONGO_URI, resprop.getProperties());
		rsp.setValue(Misc.nvl(rsp.getValue()));
		bindingContext.bindValue(SWTObservables.observeText(turi, SWT.Modify), PojoObservables.observeValue(rsp, "value")); //$NON-NLS-1$

		rsp = ResourceDescriptorUtil.getProperty(MRDatasourceMongoDB.USERNAME, resprop.getProperties());
		rsp.setValue(Misc.nvl(rsp.getValue()));
		bindingContext.bindValue(SWTObservables.observeText(tusername, SWT.Modify), PojoObservables.observeValue(rsp, "value")); //$NON-NLS-1$

		if (tpass != null) {
			rsp = ResourceDescriptorUtil.getProperty(MRDatasourceMongoDB.PASSWORD, resprop.getProperties());
			if (rsp == null)
				rsp = new ResourceProperty(MRDatasourceMongoDB.PASSWORD);
			rsp.setValue(Misc.nvl(rsp.getValue()));
			bindingContext.bindValue(SWTObservables.observeText(tpass, SWT.Modify), PojoObservables.observeValue(rsp, "value")); //$NON-NLS-1$
		}
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.adapter_mongodb";
	}
}
