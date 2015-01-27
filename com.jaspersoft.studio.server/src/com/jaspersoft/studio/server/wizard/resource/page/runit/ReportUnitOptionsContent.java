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
package com.jaspersoft.studio.server.wizard.resource.page.runit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MReportUnitOptions;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.UIUtil;

public class ReportUnitOptionsContent extends APageContent {

	private ResourceProperty resprop;
	private Text tname;

	public ReportUnitOptionsContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public ReportUnitOptionsContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getName() {
		return Messages.RDReportUnitOptionsPage_title;
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.runitoption";
	}

	@Override
	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		UIUtil.createLabel(composite, "Report Unit");

		tname = new Text(composite, SWT.BORDER);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tname.setEnabled(false);

		Composite cmp = new Composite(composite, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		cmp.setLayoutData(gd);

		// TODO we should show input controls from the report unit, and give
		// possibility to change values
		// this is possible only with rest interface
		resprop = ResourceDescriptorUtil.getProperty("PROP_VALUES", res.getValue().getProperties());
		List<ResourceProperty> props = resprop.getProperties();
		for (ResourceProperty rp : props) {
			Label lbl = new Label(cmp, SWT.NONE);
			lbl.setText(rp.getName());
			if (rp.getValue() != null) {
				Combo c = new Combo(cmp, SWT.READ_ONLY);
				c.setItems(new String[] { rp.getValue() });
				c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				c.select(0);
			} else if (rp.getProperties() != null && !rp.getProperties().isEmpty()) {
				Combo c = new Combo(cmp, SWT.READ_ONLY);
				List<String> sprops = new ArrayList<String>();
				for (Object obj : rp.getProperties()) {
					if (obj instanceof ResourceProperty) {
						ResourceProperty p = (ResourceProperty) obj;
						sprops.add(p.getName() + " : " + p.getValue());
					}
				}
				c.setItems(sprops.toArray(new String[sprops.size()]));
				c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				c.select(0);
			} else {
				gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				lbl.setLayoutData(gd);
			}
		}
		rebind();
		return composite;
	}

	@Override
	protected void rebind() {
		resprop = ResourceDescriptorUtil.getProperty(MReportUnitOptions.PROP_RU_URI, res.getValue().getProperties());
		bindingContext.bindValue(SWTObservables.observeText(tname, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$
	}
}
