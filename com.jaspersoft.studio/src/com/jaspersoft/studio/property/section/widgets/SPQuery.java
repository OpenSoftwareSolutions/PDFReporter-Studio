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
package com.jaspersoft.studio.property.section.widgets;

import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MQuery;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.property.dataset.dialog.DatasetDialog;
import com.jaspersoft.studio.property.descriptor.pattern.dialog.PatternEditor;
import com.jaspersoft.studio.property.section.AbstractSection;

public class SPQuery extends SPText {

	public SPQuery(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	protected void createComponent(Composite parent) {
		super.createComponent(parent);

		Button btn = section.getWidgetFactory().createButton(parent, "...", SWT.PUSH);
		btn.setToolTipText(pDescriptor.getDescription());
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PatternEditor wizard = new PatternEditor();
				wizard.setValue(ftext.getText());
				new DatasetDialog(ftext.getShell(), mdataset, mquery.getJasperConfiguration(), section.getEditDomain()
						.getCommandStack()).open();
			}
		});
	}

	protected void handleTextChanged(final AbstractSection section, final Object property, String text) {
		JRDesignQuery query = null;
		if (mquery.getValue() != null)
			query = (JRDesignQuery) mquery.getValue().clone();
		else
			query = new JRDesignQuery();
		query.setText(text);
		section.changePropertyOn(property, new MQuery(query, mdataset), mdataset);
	}

	private MQuery mquery;
	private MDataset mdataset;

	@Override
	public void setData(APropertyNode pnode, Object b) {
		mdataset = (MDataset) pnode;
		mquery = (MQuery) b;
		super.setData(mquery, mquery.getPropertyValue(JRDesignQuery.PROPERTY_TEXT));
	}

}
