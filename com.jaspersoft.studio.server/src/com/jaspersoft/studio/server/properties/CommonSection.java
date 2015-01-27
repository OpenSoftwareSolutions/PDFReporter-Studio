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

public class CommonSection extends ASection {
	public CommonSection() {
		super();
	}

	private Text tparent;
	private Text tid;
	private Text ttype;
	private Text tname;
	private Text tdesc;
	private Text tcdate;

	public void createSectionControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		AbstractSection.createLabel(parent, getWidgetFactory(), "ID", 120);
		tid = getWidgetFactory().createText(parent, "",
				SWT.BORDER | SWT.READ_ONLY);
		tid.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(),
				"Parent Folder", 120);
		tparent = getWidgetFactory().createText(parent, "",
				SWT.BORDER | SWT.READ_ONLY);
		tparent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "Type:", 120);
		ttype = getWidgetFactory().createText(parent, "",
				SWT.BORDER | SWT.READ_ONLY);
		ttype.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(),
				"Creation Date:", 120);
		tcdate = getWidgetFactory().createText(parent, "",
				SWT.BORDER | SWT.READ_ONLY);
		tcdate.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "Name", 120);
		tname = getWidgetFactory().createText(parent, "", SWT.BORDER);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "Description",
				120);
		tdesc = getWidgetFactory().createText(parent, "",
				SWT.BORDER | SWT.MULTI | SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 100;
		tdesc.setLayoutData(gd);
	}

	protected void bind() {
		bindingContext.bindValue(SWTObservables.observeText(tparent, SWT.NONE),
				PojoObservables.observeValue(res.getValue(), "parentFolder"));

		bindingContext.bindValue(SWTObservables.observeText(tid, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "name"));

		bindingContext.bindValue(SWTObservables.observeText(tcdate, SWT.NONE),
				PojoObservables.observeValue(res.getValue(), "creationDate"));

		bindingContext.bindValue(SWTObservables.observeText(ttype, SWT.NONE),
				PojoObservables.observeValue(res.getValue(), "wsType"));

		bindingContext.bindValue(SWTObservables.observeText(tname, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "label"));
		bindingContext.bindValue(SWTObservables.observeText(tdesc, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "description"));
	}

	public void enableFields(boolean enable) {
		tid.setEditable(enable && res.getValue().getIsNew());
		tname.setEditable(enable);
		tdesc.setEditable(enable);
	}

}
