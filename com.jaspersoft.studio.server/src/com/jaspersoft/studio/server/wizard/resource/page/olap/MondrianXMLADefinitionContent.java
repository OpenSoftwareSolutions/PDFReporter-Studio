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
package com.jaspersoft.studio.server.wizard.resource.page.olap;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.utils.UIUtil;

public class MondrianXMLADefinitionContent extends MondrianConnectionContent {

	public MondrianXMLADefinitionContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public MondrianXMLADefinitionContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public Control createContent(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));

		UIUtil.createLabel(cmp, "Catalog");

		Text tCatalog = new Text(cmp, SWT.BORDER);
		tCatalog.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		List<ResourceProperty> props = res.getValue().getProperties();
		ResourceProperty resprop = ResourceDescriptorUtil.getProperty(ResourceDescriptor.PROP_XMLA_CATALOG, props);

		bindingContext.bindValue(SWTObservables.observeText(tCatalog, SWT.Modify), PojoObservables.observeValue(resprop, "value"));

		Control c = super.createContent(cmp);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		c.setLayoutData(gd);
		return cmp;
	}
}
