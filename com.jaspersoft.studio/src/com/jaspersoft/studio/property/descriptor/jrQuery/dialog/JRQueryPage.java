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
package com.jaspersoft.studio.property.descriptor.jrQuery.dialog;

import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MQuery;
import com.jaspersoft.studio.utils.ModelUtils;

public class JRQueryPage extends WizardPage {
	private MQuery value;
	private Combo langCombo;
	private StyledText queryText;

	public MQuery getValue() {
		return value;
	}

	@Override
	public void dispose() {
		String lang = "";
		int selectionIndex = langCombo.getSelectionIndex();
		if (selectionIndex < 0)
			lang = langCombo.getText().trim();
		else
			lang = langCombo.getItem(selectionIndex);
		String text = queryText.getText();

		if (lang.equals("") && (text == null || text.equals("")))
			value = new MQuery(null, null);
		else {
			JRDesignQuery jrQuery = new JRDesignQuery();
			jrQuery.setLanguage(lang);
			jrQuery.setText(text);
			value = new MQuery(jrQuery, null);
		}
		super.dispose();
	}

	public void setValue(MQuery list) {
		this.value = list;
	}

	protected JRQueryPage(String pageName) {
		super(pageName);
		setTitle(Messages.common_query_editor);
		setDescription(Messages.JRQueryPage_description);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		setControl(composite);

		Label lbl1 = new Label(composite, SWT.NONE);
		lbl1.setText(Messages.common_language + ":"); //$NON-NLS-1$

		langCombo = new Combo(composite, SWT.DROP_DOWN | SWT.FLAT | SWT.BORDER);
		langCombo.setItems(ModelUtils.getQueryLanguages(value.getJasperConfiguration()));
		langCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// set value into the MQuery, attention, thru commands!

			}
		});

		Label lbl2 = new Label(composite, SWT.NONE);
		lbl2.setText(Messages.common_query + ":"); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		lbl2.setLayoutData(gd);

		queryText = new StyledText(composite, SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = 2;
		queryText.setLayoutData(gd);

		setWidgets();
		queryText.setFocus();
	}

	private void setWidgets() {
		String lang = (String) value.getPropertyValue(JRDesignQuery.PROPERTY_LANGUAGE);
		if (lang == null)
			lang = "sql"; //$NON-NLS-1$
		lang = ModelUtils.getLanguage(lang);
		String[] items = langCombo.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(lang)) {
				langCombo.select(i);
				break;
			}
		}
		if (langCombo.getSelectionIndex() < 0)
			langCombo.setText(lang);

		String text = (String) value.getPropertyValue(JRDesignQuery.PROPERTY_TEXT);
		if (text == null)
			text = ""; //$NON-NLS-1$
		queryText.setText(text);
	}

}
