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

import java.sql.Time;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.sql.model.query.operand.ScalarOperand;

public class TimeWidget extends DateWidget {

	public TimeWidget(Composite parent, ScalarOperand<Time> operand) {
		super(parent, operand);
	}

	@Override
	protected int getDateStyle() {
		return CDT.BORDER | CDT.TIME_MEDIUM | CDT.DROP_DOWN;
	}
}
