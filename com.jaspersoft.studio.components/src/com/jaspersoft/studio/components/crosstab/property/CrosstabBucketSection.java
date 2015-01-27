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
package com.jaspersoft.studio.components.crosstab.property;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabGroup;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MBucket;
import com.jaspersoft.studio.components.crosstab.model.MCrosstabGroup;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class CrosstabBucketSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, "Bucket", false, 3);

		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(parent, JRDesignCrosstabBucket.PROPERTY_ORDER)
				.getControl().setLayoutData(gd);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent,
				JRDesignCrosstabBucket.PROPERTY_ORDER_BY_EXPRESSION)
				.getControl().setLayoutData(gd);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent,
				JRDesignCrosstabBucket.PROPERTY_COMPARATOR_EXPRESSION)
				.getControl().setLayoutData(gd);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent,
				JRDesignCrosstabBucket.PROPERTY_EXPRESSION).getControl()
				.setLayoutData(gd);
		createWidget4Property(parent,
				JRDesignCrosstabBucket.PROPERTY_VALUE_CLASS);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignCrosstabBucket.PROPERTY_ORDER, Messages.common_order);
		addProvidedProperties(JRDesignCrosstabBucket.PROPERTY_ORDER_BY_EXPRESSION, Messages.MBucket_order_by_expression);
		addProvidedProperties(JRDesignCrosstabBucket.PROPERTY_COMPARATOR_EXPRESSION, Messages.MBucket_comparator_expression);
		addProvidedProperties(JRDesignCrosstabBucket.PROPERTY_EXPRESSION, Messages.MBucket_expression);
		addProvidedProperties(JRDesignCrosstabBucket.PROPERTY_VALUE_CLASS, Messages.MBucket_valueClassTitle);
	}
	

	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MCrosstabGroup) {
			MBucket dataset = (MBucket) md
					.getPropertyValue(JRDesignCrosstabGroup.PROPERTY_BUCKET);
			return dataset;
		}
		return md;
	}
}
