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

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.sql.model.query.operand.ScalarOperand;
import com.jaspersoft.studio.data.sql.widgets.AOperandWidget;

public abstract class AScalarWidget extends AOperandWidget<ScalarOperand<?>> {

	public AScalarWidget(Composite parent, int style, ScalarOperand<?> operand) {
		super(parent, style, operand);
	}

}
