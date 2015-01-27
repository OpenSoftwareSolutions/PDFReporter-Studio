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
package com.jaspersoft.studio.editor.preview.view.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.utils.UIUtil;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class VSimpleErrorPreview extends APreview {

	public VSimpleErrorPreview(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	private Label tmessage;

	@Override
	public Control createControl(final Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		createMessages(container);

		return container;
	}

	public void setFocus() {
		container.setFocus();
	}

	protected void createMessages(Composite composite) {
		tmessage = new Label(composite, SWT.PUSH | SWT.CENTER | SWT.WRAP);
		tmessage.setText("Starting to generate a new report, please wait ...");
		// GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalAlignment = SWT.CENTER;
		layoutData.verticalAlignment = SWT.CENTER;
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalSpan = 1;
		layoutData.verticalSpan = 1;

		layoutData.heightHint = UIUtil.getCharHeight(tmessage) * 2 + 50;
		tmessage.setLayoutData(layoutData);
	}

	public void setMessage(String msg) {
		tmessage.setText(msg);
	}

	public void addMessage(String msg) {
		tmessage.setText(tmessage.getText() + msg + "\n");
		tmessage.getParent().update();
		tmessage.getParent().layout();
	}

	private Composite container;

	public void clear() {
		tmessage.setText("");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}
