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
package com.jaspersoft.studio.property.section.report;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class ReportSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(1, false));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		Composite firstSection = getWidgetFactory().createComposite(parent);
		firstSection.setLayout(new GridLayout(3, false));
		firstSection.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(firstSection, JasperDesign.PROPERTY_NAME).getControl().setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(firstSection, JasperDesign.PROPERTY_LANGUAGE).getControl().setLayoutData(gd);

		createWidget4Property(firstSection, JasperDesign.PROPERTY_IMPORTS);

		createWidget4Property(firstSection, JasperDesign.PROPERTY_FORMAT_FACTORY_CLASS);

		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(firstSection, JasperDesign.PROPERTY_WHEN_NO_DATA_TYPE).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(firstSection, JasperDesign.PROPERTY_TITLE_NEW_PAGE, false).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(firstSection, JasperDesign.PROPERTY_SUMMARY_NEW_PAGE, false).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(firstSection, JasperDesign.PROPERTY_SUMMARY_WITH_PAGE_HEADER_AND_FOOTER, false).getControl()
				.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(firstSection, JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER, false).getControl()
				.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(firstSection, JasperDesign.PROPERTY_IGNORE_PAGINATION, false).getControl().setLayoutData(gd);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JasperDesign.PROPERTY_NAME, Messages.MReport_report_name);
		addProvidedProperties(JasperDesign.PROPERTY_LANGUAGE, Messages.common_language);
		addProvidedProperties(JasperDesign.PROPERTY_IMPORTS, Messages.MReport_imports);
		addProvidedProperties(JasperDesign.PROPERTY_FORMAT_FACTORY_CLASS, Messages.MReport_format_factory_class);
		addProvidedProperties(JasperDesign.PROPERTY_WHEN_NO_DATA_TYPE, Messages.MReport_when_no_data_type);
		addProvidedProperties(JasperDesign.PROPERTY_TITLE_NEW_PAGE, Messages.MReport_title_on_a_new_page);
		addProvidedProperties(JasperDesign.PROPERTY_SUMMARY_NEW_PAGE, Messages.MReport_summary_on_a_new_page);
		addProvidedProperties(JasperDesign.PROPERTY_SUMMARY_WITH_PAGE_HEADER_AND_FOOTER, Messages.MReport_summary_with_page_header_and_footer);
		addProvidedProperties(JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER, Messages.MReport_float_column_footer);
		addProvidedProperties(JasperDesign.PROPERTY_IGNORE_PAGINATION, Messages.MReport_ignore_pagination);
	}
	

}
