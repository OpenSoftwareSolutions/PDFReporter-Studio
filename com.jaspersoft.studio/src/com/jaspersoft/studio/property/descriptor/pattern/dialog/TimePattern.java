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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;

public class TimePattern extends DatePattern {

	public TimePattern(Composite parent, String value) {
		super(parent, value);
		setDescription(Messages.TimePattern_description);
	}

	@Override
	protected List<String> getDefaults() {
		if (dList == null) {
			dList = new ArrayList<String>();
			dList.add("h:mm a"); //$NON-NLS-1$
			dList.add("h:mm:ss a"); //$NON-NLS-1$
			dList.add("h:mm:ss a z"); //$NON-NLS-1$
			dList.add("HH:mm a"); //$NON-NLS-1$
			dList.add("HH:mm:ss a"); //$NON-NLS-1$
			dList.add("HH:mm:ss zzzz"); //$NON-NLS-1$
			setPattern(dList.get(0));
		}
		return dList;
	}

}
