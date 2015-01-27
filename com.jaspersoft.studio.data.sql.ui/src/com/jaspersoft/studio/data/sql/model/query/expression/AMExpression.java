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
package com.jaspersoft.studio.data.sql.model.query.expression;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.AMQueryObject;
import com.jaspersoft.studio.data.sql.model.query.operand.AOperand;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public abstract class AMExpression<T> extends AMQueryObject<T> {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public AMExpression(ANode parent, T value, int newIndex) {
		super(parent, value, null, newIndex);
	}

	@Override
	public ImageDescriptor getImagePath() {
		return null;
	}

	@Override
	public String toSQLString() {
		return "\n\t " + getDisplayText() + " ";
	}

	protected String isLastInGroup(ANode p, ANode child) {
		if (p == null)
			return "";
		String str = "";
		List<INode> ch = p.getChildren();
		if (p instanceof MExpressionGroup && ch.indexOf(child) == ch.size() - 1)
			str += ")" + isLastInGroup(p.getParent(), p);
		return str;
	}

	protected String prevCond = AMKeyword.AND_OPERATOR;
	protected List<AOperand> operands = new ArrayList<AOperand>();

	public List<AOperand> getOperands() {
		return operands;
	}

	public void setOperands(List<AOperand> operands) {
		this.operands = operands;
	}

	public String getPrevCond() {
		return prevCond;
	}

	public void setPrevCond(String prevCond) {
		this.prevCond = prevCond;
	}

	@Override
	public ANode clone() {
		AMExpression<?> clone = (AMExpression<?>) super.clone();
		clone.setOperands(new ArrayList<AOperand>());
		List<AOperand> ops = new ArrayList<AOperand>();
		for (AOperand op : operands)
			try {
				ops.add((AOperand) op.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		clone.setOperands(ops);
		return clone;
	}

}
