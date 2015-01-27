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
package com.jaspersoft.studio.server.properties;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class DatasourceJDBCSection extends ASection {
	private Text tdriver;
	private Text turl;
	private Text tuser;
	private Text tpass;

	@Override
	protected void createSectionControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		AbstractSection.createLabel(parent, getWidgetFactory(), "Driver", 120);

		tdriver = getWidgetFactory().createText(parent, "", SWT.BORDER);
		tdriver.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "URL", 120);

		turl = getWidgetFactory().createText(parent, "", SWT.BORDER);
		turl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "User", 120);

		tuser = getWidgetFactory().createText(parent, "", SWT.BORDER);
		tuser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection
				.createLabel(parent, getWidgetFactory(), "Password", 120);

		tpass = getWidgetFactory().createText(parent, "",
				SWT.BORDER | SWT.PASSWORD);
		tpass.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	@Override
	public void enableFields(boolean enable) {
		tdriver.setEditable(enable);
		turl.setEditable(enable);
		tuser.setEditable(enable);
		tpass.setEditable(enable);
	}

	@Override
	protected void bind() {
		bindingContext.bindValue(
				SWTObservables.observeText(tdriver, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "driverClass"));
		bindingContext.bindValue(SWTObservables.observeText(turl, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "connectionUrl"));
		bindingContext.bindValue(SWTObservables.observeText(tuser, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "username"));
		bindingContext.bindValue(SWTObservables.observeText(tpass, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "password"));
	}
}
