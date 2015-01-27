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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.ModelUtils;

public class QuerySection extends ASection {
	private Combo clang;
	private Text tsql;

	@Override
	protected void createSectionControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		AbstractSection
				.createLabel(parent, getWidgetFactory(), "Language", 120);

		clang = getWidgetFactory().createCombo(parent, SWT.BORDER);
		// clang.setItems(ModelUtils.getQueryLanguages());

		AbstractSection.createLabel(parent, getWidgetFactory(), "Query", 120);

		tsql = getWidgetFactory().createText(parent, "",
				SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 100;
		tsql.setLayoutData(gd);

	}

	@Override
	public void enableFields(boolean enable) {
		clang.setEnabled(enable);
		tsql.setEditable(enable);
	}

	@Override
	protected void bind() {
		bindingContext.bindValue(SWTObservables.observeText(clang),
				PojoObservables.observeValue(getProxy(res.getValue()),
						"language"));
		bindingContext.bindValue(SWTObservables.observeText(tsql, SWT.Modify),
				PojoObservables.observeValue(res.getValue(), "sql"));
	}

	private QProxy getProxy(ResourceDescriptor rd) {
		proxy.setResourceDescriptor(rd);
		return proxy;
	}

	private QProxy proxy = new QProxy();

	class QProxy {
		private ResourceDescriptor rd;

		public void setResourceDescriptor(ResourceDescriptor rd) {
			this.rd = rd;
		}

		public void setLanguage(String lang) {
			lang = ModelUtils.getLanguage(lang);
			rd.setResourceProperty(ResourceDescriptor.PROP_QUERY_LANGUAGE, lang);
		}

		public String getLanguage() {
			return rd
					.getResourcePropertyValue(ResourceDescriptor.PROP_QUERY_LANGUAGE);
		}
	}
}
