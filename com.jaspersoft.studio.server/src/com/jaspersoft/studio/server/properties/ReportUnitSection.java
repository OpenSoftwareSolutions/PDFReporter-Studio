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

public class ReportUnitSection extends ASection {
	private Button ispromp;

	private Combo cictype;
	private Text jspic;
	private Text jspview;

	@Override
	protected void createSectionControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		AbstractSection.createLabel(parent, getWidgetFactory(),
				"Controls Layout", 120);

		cictype = getWidgetFactory().createCombo(parent, SWT.BORDER);
		cictype.setItems(new String[] { "Popup screen", "Separate page",
				"Top of page", "In page" });

		AbstractSection.createLabel(parent, getWidgetFactory(), "", 120);

		ispromp = getWidgetFactory().createButton(parent, "Always Prompt",
				SWT.CHECK);
		ispromp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(),
				"JSP To Report View", 120);

		jspview = getWidgetFactory().createText(parent, "", SWT.BORDER);
		jspview.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jspview.setToolTipText("within /WEB-INF/jsp, leave blank for default");

		AbstractSection.createLabel(parent, getWidgetFactory(),
				"JSP To Run InputControls", 120);

		jspic = getWidgetFactory().createText(parent, "", SWT.BORDER);
		jspic.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jspic.setToolTipText("within /WEB-INF/jsp, leave blank for default");

	}

	@Override
	public void enableFields(boolean enable) {
		cictype.setEnabled(enable);
		ispromp.setEnabled(enable);
		jspic.setEditable(enable);
		jspview.setEditable(enable);
	}

	@Override
	protected void bind() {
		ReportProxy v = getProxy(res.getValue());
		bindingContext.bindValue(
				SWTObservables.observeSingleSelectionIndex(cictype),
				PojoObservables.observeValue(v, "layoutControl"));
		bindingContext.bindValue(
				SWTObservables.observeText(jspview, SWT.Modify),
				PojoObservables.observeValue(v, "jspView"));
		bindingContext.bindValue(SWTObservables.observeText(jspic, SWT.Modify),
				PojoObservables.observeValue(v, "jspIC"));
		bindingContext.bindValue(SWTObservables.observeSelection(ispromp),
				PojoObservables.observeValue(v, "allowPrompt"));
	}

	private ReportProxy getProxy(ResourceDescriptor rd) {
		proxy.setResourceDescriptor(rd);
		return proxy;
	}

	private ReportProxy proxy = new ReportProxy();

	class ReportProxy {
		private ResourceDescriptor rd;

		public void setResourceDescriptor(ResourceDescriptor rd) {
			this.rd = rd;
		}

		private int[] layouts = new int[] {
				ResourceDescriptor.RU_CONTROLS_LAYOUT_POPUP_SCREEN,
				ResourceDescriptor.RU_CONTROLS_LAYOUT_SEPARATE_PAGE,
				ResourceDescriptor.RU_CONTROLS_LAYOUT_TOP_OF_PAGE, 4 };

		public void setLayoutControl(int lang) {
			rd.setResourceProperty(ResourceDescriptor.PROP_RU_CONTROLS_LAYOUT,
					layouts[lang]);
		}

		public int getLayoutControl() {
			int lc = rd
					.getResourcePropertyValueAsInteger(ResourceDescriptor.PROP_RU_CONTROLS_LAYOUT);
			for (int i = 0; i < layouts.length; i++)
				if (layouts[i] == lc)
					return i;
			return -1;
		}

		public void setJspIC(String lang) {
			rd.setResourceProperty(
					ResourceDescriptor.PROP_RU_INPUTCONTROL_RENDERING_VIEW,
					lang);
		}

		public String getJspIC() {
			return rd
					.getResourcePropertyValue(ResourceDescriptor.PROP_RU_INPUTCONTROL_RENDERING_VIEW);
		}

		public void setJspView(String lang) {
			rd.setResourceProperty(
					ResourceDescriptor.PROP_RU_REPORT_RENDERING_VIEW, lang);
		}

		public String getJspView() {
			return rd
					.getResourcePropertyValue(ResourceDescriptor.PROP_RU_REPORT_RENDERING_VIEW);
		}

		public void setAllowPrompt(boolean lang) {
			rd.setResourceProperty(
					ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS, lang);
		}

		public boolean isAllowPrompt() {
			return rd
					.getResourcePropertyValueAsBoolean(ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS);
		}
	}
}
