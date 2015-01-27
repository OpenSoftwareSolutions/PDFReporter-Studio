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

import java.text.NumberFormat;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;

public class PercentagePattern extends NumericPattern {

	public PercentagePattern(Composite parent, String value) {
		super(parent, NumberFormat.getPercentInstance(), value);
		setDescription(Messages.PercentagePattern_description);
	}

	@Override
	protected java.util.List<String> getDefaults() {
		if (dList == null) {
			dList = new ArrayList<String>();
			dList.add("#,##0.##%"); //$NON-NLS-1$
			dList.add("#,##0.##\u2030"); //$NON-NLS-1$
			dList.add("#,##0.##%;(#,##0.###) %"); //$NON-NLS-1$
			dList.add("#,##0.##%;(-#,##0.###) %"); //$NON-NLS-1$
			dList.add("#,##0.##%;(#,##0.###-) %"); //$NON-NLS-1$
			setPattern(dList.get(0));
		}
		return dList;
	}
}
