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

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.data.sql.model.query.expression.AMExpression;

public abstract class AOperand implements Serializable, Cloneable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	protected AMExpression<?> expression;

	public AOperand(AMExpression<?> mexpr) {
		this.expression = mexpr;
	}

	public AMExpression<?> getExpression() {
		return expression;
	}

	public void setExpression(AMExpression<?> expression) {
		this.expression = expression;
	}

	public abstract String toXString();

	public abstract String toSQLString();

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
