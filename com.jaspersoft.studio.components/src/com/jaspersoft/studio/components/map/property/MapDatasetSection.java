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
package com.jaspersoft.studio.components.map.property;

import net.sf.jasperreports.components.map.StandardItemData;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class MapDatasetSection extends AbstractSection {
	
	private ExpandableComposite section;
	
	@Override
	public void createControls(final Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent, StandardItemData.PROPERTY_ITEMS, false).getControl().setLayoutData(gd);
		
		final Button useMarkerDataset = new Button(parent, SWT.CHECK);
		useMarkerDataset.setText(Messages.MapDatasetSection_UseMarkersDatasetBtn);
		GridData gdBtn = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gdBtn.horizontalIndent = 2;
		useMarkerDataset.setLayoutData(gdBtn);
		boolean enableAndShowDSPanel = isDatasetSet();
		useMarkerDataset.setSelection(enableAndShowDSPanel);
		
		final Composite container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		GridData gdContainer = new GridData(SWT.FILL,SWT.FILL,true,true,2,1);
		gdContainer.horizontalIndent = 5;
		container.setLayoutData(gdContainer);
		container.setEnabled(enableAndShowDSPanel);
		container.setVisible(enableAndShowDSPanel);
		
		final Composite group = getWidgetFactory().createSection(container, com.jaspersoft.studio.messages.Messages.MElementDataset_dataset_run, true, 2, 2); //$NON-NLS-1$
		section = (ExpandableComposite)group.getParent();
		createWidget4Property(group, JRDesignElementDataset.PROPERTY_DATASET_RUN);
		
		useMarkerDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(useMarkerDataset.getSelection()) {
					toggleDSRunPanel(container, true);
					getElement().setPropertyValue(StandardItemData.PROPERTY_DATASET, new JRDesignElementDataset());
				}
				else {
					boolean answer = MessageDialog.openQuestion(
							UIUtils.getShell(), Messages.MapDatasetSection_DeleteConfirmationTitle, 
							Messages.MapDatasetSection_DeleteConfirmationMsg);
					if(!answer){
						useMarkerDataset.setSelection(true);
						return;
					}
					toggleDSRunPanel(container, false);
					getElement().setPropertyValue(StandardItemData.PROPERTY_DATASET, null);
				}
				parent.layout(new Control[]{container});
			}
		});
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded() && propertyId.equals(JRDesignElementDataset.PROPERTY_DATASET_RUN)) section.setExpanded(true);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(StandardItemData.PROPERTY_ITEMS, Messages.MMap_markersTitle);
		addProvidedProperties(JRDesignElementDataset.PROPERTY_DATASET_RUN, Messages.MMap_markerDatasetTitle);
	}
	
	
	
	private boolean isDatasetSet() {
		return getElement().getPropertyValue(StandardItemData.PROPERTY_DATASET)!=null;
	}
	
	private void toggleDSRunPanel(Composite panel, boolean show) {
		((GridData)panel.getLayoutData()).exclude = !show;
		panel.setEnabled(show);
		panel.setVisible(show);
	}
}
