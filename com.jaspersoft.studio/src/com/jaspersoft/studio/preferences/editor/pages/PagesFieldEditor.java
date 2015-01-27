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
package com.jaspersoft.studio.preferences.editor.pages;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.jaspersoft.studio.messages.Messages;

public class PagesFieldEditor extends FieldEditor {
	private Composite container;
	private Button btAll;
	private Button btRange;
	private Spinner pageFrom;
	private Spinner pageTo;
	private Button btPage;
	private Spinner page;

	public PagesFieldEditor() {
		super();
	}

	public PagesFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
		init(name, labelText);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		((GridData) container.getLayoutData()).horizontalSpan = numColumns;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		container = createEncodingGroup(parent, numColumns);
	}

	protected Composite createEncodingGroup(Composite parent, int numColumns) {
		Group container = new Group(parent, SWT.NONE);
		container.setText(Messages.PagesFieldEditor_pageToExportBox);
		container.setLayout(new GridLayout(5, false));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		container.setLayoutData(gridData);

		btAll = new Button(container, SWT.RADIO);
		btAll.setText(Messages.PagesFieldEditor_allPages);
		GridData gd = new GridData();
		gd.horizontalSpan = 5;
		btAll.setLayoutData(gd);

		createPage(container);

		createRange(container);

		return container;
	}

	private void createPage(Group container) {
		GridData gd;
		btPage = new Button(container, SWT.RADIO);
		btPage.setText(Messages.PagesFieldEditor_precisePages);

		page = new Spinner(container, SWT.BORDER);
		gd = new GridData();
		gd.horizontalSpan = 4;
		page.setLayoutData(gd);
		page.setEnabled(false);
		page.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);

		btPage.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				page.setEnabled(btPage.getSelection());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	private void createRange(Group container) {
		btRange = new Button(container, SWT.RADIO);
		btRange.setText(Messages.PagesFieldEditor_fromToPages);

		pageFrom = new Spinner(container, SWT.BORDER);
		pageFrom.setEnabled(false);
		pageFrom.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);

		Label lbl = new Label(container, SWT.NONE);
		lbl.setText(Messages.PagesFieldEditor_to);

		pageTo = new Spinner(container, SWT.BORDER);
		pageTo.setEnabled(false);
		pageTo.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);

		btRange.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				pageFrom.setEnabled(btRange.getSelection());
				pageTo.setEnabled(btRange.getSelection());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	@Override
	protected void doLoad() {
		String resourcePreference = getStoredValue();
		setProperty(resourcePreference);
	}

	private void setProperty(String resourcePreference) {
		Pages p = new Pages();
		p.parseString(resourcePreference);
		if (p.getPage() != null) {
			btPage.setSelection(true);
			page.setSelection(p.getPage());
			page.setEnabled(true);
		} else if (p.getFrom() != null) {
			btRange.setSelection(true);

			pageFrom.setSelection(p.getFrom());
			pageFrom.setEnabled(true);
			pageTo.setSelection(p.getTo());
			pageTo.setEnabled(true);
		} else
			btAll.setSelection(true);
	}

	@Override
	protected void doLoadDefault() {
		btAll.setSelection(true);
		btPage.setSelection(false);
		btRange.setSelection(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.ide.dialogs.AbstractEncodingFieldEditor#getStoredValue()
	 */
	protected String getStoredValue() {
		return getPreferenceStore().getString(getPreferenceName());
	}

	private String getProperty() {
		if (btAll.getSelection())
			return "all"; //$NON-NLS-1$
		if (btPage.getSelection())
			return Integer.toString(page.getSelection());
		int from = pageFrom.getSelection();
		int to = pageTo.getSelection();
		if (to < from)
			to = from;
		return Integer.toString(from) + ";" + Integer.toString(to); //$NON-NLS-1$
	}

	protected boolean hasSameProperty(String prop) {

		String current = getStoredValue();

		if (prop == null) {
			// Changed if default is selected and there is no setting
			return current == null || current.length() == 0;
		}
		return prop.equals(current);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		String prop = getProperty();

		if (hasSameProperty(prop)) {
			return;
		}

		if (prop.equals("all")) { //$NON-NLS-1$
			getPreferenceStore().setToDefault(getPreferenceName());
		} else {
			getPreferenceStore().setValue(getPreferenceName(), prop);
		}
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}

}
