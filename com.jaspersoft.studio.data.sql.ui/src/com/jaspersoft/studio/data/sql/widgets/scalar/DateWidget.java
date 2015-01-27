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
package com.jaspersoft.studio.data.sql.widgets.scalar;

import java.util.Date;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.sql.model.query.operand.ScalarOperand;

public class DateWidget extends AScalarWidget {
	private CDateTime date;

	public DateWidget(Composite parent, ScalarOperand<?> operand) {
		super(parent, SWT.NONE, operand);
	}

	@Override
	protected void createWidget(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 2;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		setLayout(layout);

		date = new CDateTime(this, getDateStyle());
		date.setToolTipText(getValue().toSQLString());
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gd.widthHint = 150;
		date.setLayoutData(gd);
		date.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Date sdate = date.getSelection();
				((ScalarOperand<Date>) getValue()).setValue(sdate);
			}
		});

		date.setSelection((Date) getValue().getValue());
	}

	protected int getDateStyle() {
		return CDT.BORDER | CDT.DATE_SHORT | CDT.DROP_DOWN;
	}
}
