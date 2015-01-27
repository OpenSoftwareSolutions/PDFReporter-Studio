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
package com.jaspersoft.studio.data.sql.widgets;

import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.sql.model.query.operand.AOperand;

public abstract class AOperandWidget<T extends AOperand> extends Composite {
	private T value;
	private boolean exludeField = false;
	private Set<Class<? extends AOperand>> menuOperands;

	public AOperandWidget(Composite parent, int style, T operand) {
		super(parent, style);
		this.value = operand;
		createWidget(parent);
	}

	public boolean isMenuOperands(Class<? extends AOperand> op) {
		if (menuOperands != null) {
			return menuOperands.contains(op);
		}
		return true;
	}

	public void setMenuOperands(Set<Class<? extends AOperand>> menuOperands) {
		this.menuOperands = menuOperands;
	}

	public Set<Class<? extends AOperand>> getMenuOperands() {
		return menuOperands;
	}
	public void setExludeField(boolean exludeField) {
		this.exludeField = exludeField;
	}

	public boolean isExludeField() {
		return exludeField;
	}

	protected abstract void createWidget(Composite parent);

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	private Map<String, AOperand> operandMap;

	public void setOperandMap(Map<String, AOperand> opMap) {
		this.operandMap = opMap;
	}

	public Map<String, AOperand> getOperandMap() {
		return operandMap;
	}

}
