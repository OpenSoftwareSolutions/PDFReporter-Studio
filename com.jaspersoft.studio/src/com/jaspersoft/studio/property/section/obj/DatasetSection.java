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
package com.jaspersoft.studio.property.section.obj;

import java.text.MessageFormat;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

public class DatasetSection extends AbstractSection {
	
	private ASPropertyWidget nameWidget = null;
	
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(3, false));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		nameWidget = createWidget4Property(parent, JRDesignDataset.PROPERTY_NAME);
		nameWidget.getControl().setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(parent, JRDesignDataset.PROPERTY_WHEN_RESOURCE_MISSING_TYPE).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent, JRDesignDataset.PROPERTY_FILTER_EXPRESSION).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		createWidget4Property(parent, JRDesignDataset.PROPERTY_SCRIPTLET_CLASS).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		createWidget4Property(parent, JRDesignDataset.PROPERTY_RESOURCE_BUNDLE).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = SWT.CENTER;
		createWidget4Property(parent, JRDesignDataset.PROPERTY_QUERY, false).getControl().setLayoutData(gd);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignDataset.PROPERTY_NAME, Messages.common_name);
		addProvidedProperties(JRDesignDataset.PROPERTY_WHEN_RESOURCE_MISSING_TYPE, Messages.MDataset_when_resource_missing_type);
		addProvidedProperties(JRDesignDataset.PROPERTY_FILTER_EXPRESSION, Messages.MDataset_filter_expression);
		addProvidedProperties(JRDesignDataset.PROPERTY_SCRIPTLET_CLASS, Messages.MDataset_scriplet_class);
		addProvidedProperties(JRDesignDataset.PROPERTY_RESOURCE_BUNDLE, Messages.MDataset_resource_bundle);
		addProvidedProperties(JRDesignDataset.PROPERTY_QUERY, Messages.common_query);
	}

	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MReport) {
			MDataset dataset = (MDataset) md.getPropertyValue(JasperDesign.PROPERTY_MAIN_DATASET);
			return dataset;
		}
		return md;
	}
	
	/**
	 * Check if the property changed is the name and in this case check that the 
	 * new name is different from any existing dataset. If it is different the change
	 * is done, otherwise a warning message is shown and the original name is 
	 * restored
	 */
	@Override
	public boolean changeProperty(Object property, Object newValue) {
		if (JRDesignDataset.PROPERTY_NAME.equals(property)){
			JasperDesign jd = getElement().getJasperDesign();
			String oldName = getElement().getPropertyValue(JRDesignDataset.PROPERTY_NAME).toString();
			//If the new name is equals to the actual one the there is no need to change
			if (oldName.equals(newValue)) return true;
			if (jd != null && jd.getDatasetMap().get(newValue) != null) {
				nameWidget.setData(getElement(), oldName);
				String message = MessageFormat.format(Messages.WizardDatasetNewPage_name_already_exists, new Object[] { newValue });
				MessageDialog dialog = new MessageDialog(UIUtils.getShell(), Messages.DatasetSection_nameNotValidTitle, null,
						message, MessageDialog.WARNING, new String[] { "Ok"}, 0); //$NON-NLS-1$
				dialog.open();
				return false;
			}
		}
		return super.changeProperty(property, newValue);
	}
}
