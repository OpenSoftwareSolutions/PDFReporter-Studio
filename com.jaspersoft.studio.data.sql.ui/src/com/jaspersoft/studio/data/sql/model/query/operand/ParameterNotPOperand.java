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
package com.jaspersoft.studio.data.sql.model.query.operand;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.data.sql.model.query.expression.AMExpression;

public class ParameterNotPOperand extends ParameterPOperand {
	public ParameterNotPOperand(AMExpression<?> mexpr) {
		super(mexpr);
	}

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	@Override
	public String toSQLString() {
		if (jrParameter != null)
			return "$P!{" + jrParameter.getName() + "}";
		return "$P!{}";
	}

}
