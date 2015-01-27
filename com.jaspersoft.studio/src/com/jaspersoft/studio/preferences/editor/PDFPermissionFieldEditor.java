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
package com.jaspersoft.studio.preferences.editor;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.jaspersoft.studio.messages.Messages;
import com.lowagie.text.pdf.PdfWriter;

public class PDFPermissionFieldEditor extends FieldEditor {
	private Composite container;
	private Button aPrint;
	private Button mAnnot;
	private Button aAssem;
	private Button mCont;
	private Button aFillin;
	private Button aDegPrint;
	private Button aCopy;
	private Button aSRead;

	public PDFPermissionFieldEditor() {
		super();
	}

	public PDFPermissionFieldEditor(String name, String labelText, Composite parent) {
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
		container.setText(Messages.PDFPermissionFieldEditor_permissionsTitle);
		container.setLayout(new GridLayout(2, false));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		container.setLayoutData(gridData);

		aPrint = new Button(container, SWT.CHECK);
		aPrint.setText(Messages.PDFPermissionFieldEditor_allowPrinting);

		mAnnot = new Button(container, SWT.CHECK);
		mAnnot.setText(Messages.PDFPermissionFieldEditor_allowModifyAnnotations);

		aAssem = new Button(container, SWT.CHECK);
		aAssem.setText(Messages.PDFPermissionFieldEditor_allowAssembly);

		mCont = new Button(container, SWT.CHECK);
		mCont.setText(Messages.PDFPermissionFieldEditor_allowModifyContents);

		aFillin = new Button(container, SWT.CHECK);
		aFillin.setText(Messages.PDFPermissionFieldEditor_allowFillIn);

		aDegPrint = new Button(container, SWT.CHECK);
		aDegPrint.setText(Messages.PDFPermissionFieldEditor_allowDegradedPrinting);

		aCopy = new Button(container, SWT.CHECK);
		aCopy.setText(Messages.PDFPermissionFieldEditor_allowCopy);

		aSRead = new Button(container, SWT.CHECK);
		aSRead.setText(Messages.PDFPermissionFieldEditor_allowScreenReader);

		return container;
	}

	@Override
	protected void doLoad() {
		String resourcePreference = getStoredValue();
		setProperty(resourcePreference);
	}

	private void setProperty(String resourcePreference) {
		Integer ires = 0;
		try {
			if (resourcePreference != null && !resourcePreference.isEmpty())
				ires = new Integer(resourcePreference);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		aPrint.setSelection((ires & PdfWriter.ALLOW_PRINTING) == PdfWriter.ALLOW_PRINTING);
		mAnnot.setSelection((ires & PdfWriter.ALLOW_MODIFY_ANNOTATIONS) != 0);
		aAssem.setSelection((ires & PdfWriter.ALLOW_ASSEMBLY) != 0);
		mCont.setSelection((ires & PdfWriter.ALLOW_MODIFY_CONTENTS) != 0);
		aFillin.setSelection((ires & PdfWriter.ALLOW_FILL_IN) != 0);
		aDegPrint.setSelection((ires & PdfWriter.ALLOW_DEGRADED_PRINTING) != 0);
		aCopy.setSelection((ires & PdfWriter.ALLOW_COPY) != 0);
		aSRead.setSelection((ires & PdfWriter.ALLOW_SCREENREADERS) != 0);

	}

	@Override
	protected void doLoadDefault() {
		setProperty("0"); //$NON-NLS-1$
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
		int res = 0;
		if (aPrint.getSelection())
			res |= PdfWriter.ALLOW_PRINTING;
		if (mAnnot.getSelection())
			res |= PdfWriter.ALLOW_MODIFY_ANNOTATIONS;
		if (aAssem.getSelection())
			res |= PdfWriter.ALLOW_ASSEMBLY;
		if (mCont.getSelection())
			res |= PdfWriter.ALLOW_MODIFY_CONTENTS;
		if (aFillin.getSelection())
			res |= PdfWriter.ALLOW_FILL_IN;
		if (aDegPrint.getSelection())
			res |= PdfWriter.ALLOW_DEGRADED_PRINTING;
		if (aCopy.getSelection())
			res |= PdfWriter.ALLOW_COPY;
		if (aSRead.getSelection())
			res |= PdfWriter.ALLOW_SCREENREADERS;
		return Integer.toString(res);
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
	@Override
	protected void doStore() {
		String prop = getProperty();
		if (hasSameProperty(prop)) {
			return;
		}
		getPreferenceStore().setValue(getPreferenceName(), prop);
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}

}
