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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class DataTypeSection extends ASection {
	private Text tpattern;
	private Text tmin;
	private Text tmax;

	private Button bmin;
	private Button bmax;

	private Combo ttype;

	public DataTypeSection() {
		super();
	}

	protected void createSectionControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		AbstractSection.createLabel(parent, getWidgetFactory(), "Data Type",
				120);

		ttype = getWidgetFactory().createCombo(parent,
				SWT.BORDER | SWT.READ_ONLY);
		ttype.setItems(new String[] { "Text", "Number", "Date", "Date/time" });

		AbstractSection.createLabel(parent, getWidgetFactory(), "Pattern", 120);

		tpattern = getWidgetFactory().createText(parent, "", SWT.BORDER);
		tpattern.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "Min Value",
				120);

		tmin = getWidgetFactory().createText(parent, "", SWT.BORDER);
		tmin.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "", 120);

		bmin = getWidgetFactory().createButton(parent, "Is Strict Minimum",
				SWT.CHECK);
		bmin.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "Max Value",
				120);

		tmax = getWidgetFactory().createText(parent, "", SWT.BORDER);
		tmax.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "", 120);

		bmax = getWidgetFactory().createButton(parent, "Is Strict Maximum",
				SWT.CHECK);
		bmax.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	}

	public void enableFields(boolean enable) {
		ttype.setEnabled(enable);
		tpattern.setEditable(enable);
		tmin.setEditable(enable);
		bmin.setEnabled(enable);
		tmax.setEditable(enable);
		bmax.setEnabled(enable);
	}

	protected void bind() {
		bindingContext.bindValue(
				SWTObservables.observeText(tpattern, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "pattern"));
		bindingContext.bindValue(SWTObservables.observeText(tmin, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "minValue"));
		bindingContext.bindValue(SWTObservables.observeText(tmax, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "maxValue"));
		bindingContext.bindValue(SWTObservables.observeSelection(bmin),
				PojoObservables.observeValue(res.getValue(), "strictMin"));
		bindingContext.bindValue(SWTObservables.observeSelection(bmax),
				PojoObservables.observeValue(res.getValue(), "strictMax"));

		bindingContext.bindValue(SWTObservables
				.observeSingleSelectionIndex(ttype), PojoObservables
				.observeValue(getProxy(res.getValue()), "dataType"));
	}

	private ShiftProxy getProxy(ResourceDescriptor rd) {
		proxy.setResourceDescriptor(rd);
		return proxy;
	}

	private ShiftProxy proxy = new ShiftProxy();

	class ShiftProxy {
		private ResourceDescriptor rd;
		private final int shift = 1;

		public void setResourceDescriptor(ResourceDescriptor rd) {
			this.rd = rd;
		}

		public void setDataType(int type) {
			rd.setDataType((byte) (type + shift));
		}

		public int getDataType() {
			return rd.getDataType() - shift;
		}
	}
}
