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
package com.jaspersoft.studio.property.descriptor.pattern.dialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import com.jaspersoft.studio.messages.Messages;

public class DatePattern extends APattern {

	public DatePattern(Composite parent, String value) {
		super(parent, new SimpleDateFormat(), new Date(), value);
		setDescription(Messages.DatePattern_description);
	}

	@Override
	public Control createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		Label l = new Label(container, SWT.NONE);
		l.setText(Messages.DatePattern_template_formats);

		list = new List(container, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 3;
		gd.heightHint = 200;
		gd.widthHint = 100;
		list.setLayoutData(gd);

		SimpleDateFormat f = (SimpleDateFormat) getFormatter();
		for (String s : getDefaults()) {
			f.applyPattern(s);
			list.add(f.format(getSample()));
		}
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				formatChanged();
			}
		});
		return container;
	}

	protected java.util.List<String> dList;
	protected List list;

	protected java.util.List<String> getDefaults() {
		if (dList == null) {
			dList = new ArrayList<String>();
			dList.add("M/d/yy"); //$NON-NLS-1$
			dList.add("MMM d, yyyy"); //$NON-NLS-1$
			dList.add("MMMM d, yyyy"); //$NON-NLS-1$
			dList.add("M/d/yy h:mm a"); //$NON-NLS-1$
			dList.add("MMM d, yyyy h:mm:ss a"); //$NON-NLS-1$
			dList.add("MMM d, yyyy h:mm:ss a z"); //$NON-NLS-1$
			setPattern(dList.get(0));
		}
		return dList;
	}

	@Override
	protected void formatChanged() {
		int sel = list.getSelectionIndex();
		if (sel >= 0)
			setPattern(dList.get(sel));
		super.formatChanged();
	}
}
