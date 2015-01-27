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

import java.text.MessageFormat;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.viewers.StyledString;

import com.jaspersoft.studio.data.sql.model.enums.Operator;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.operand.AOperand;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;

public class MExpression extends AMExpression<Object> {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MExpression(ANode parent, Object value, int newIndex) {
		super(parent, value, newIndex);
	}

	@Override
	public MExpression clone() {
		MExpression clone = (MExpression) super.clone();
		for (AOperand op : clone.getOperands())
			op.setExpression(clone);
		return clone;
	}

	@Override
	public String getDisplayText() {
		String dt = "";
		if (!isFirst()) {
			if (getParent() instanceof MFromTableJoin && getParent().getValue() instanceof MQueryTable) {
				MFromTableJoin mftj = (MFromTableJoin) getParent();
				dt += ") " + mftj.addAlias() + " ON ";
			} else
				dt += prevCond + " ";
		}
		String[] ops = null;
		if (operator.getNrOperands() > 3) {
			ops = new String[] { "", "" };
			String sep = "";
			for (int i = 0; i < operands.size(); i++) {
				if (i == 0)
					ops[i] = operands.get(i).toSQLString();
				else {
					ops[1] += sep + operands.get(i).toSQLString();
					sep = ",";
				}
			}
		} else {
			ops = new String[operands.size()];
			for (int i = 0; i < ops.length; i++)
				ops[i] = operands.get(i).toSQLString();
		}
		return dt + MessageFormat.format(operator.getFormat(operator), (Object[]) ops) + isLastInGroup(getParent(), this);
	}

	@Override
	public StyledString getStyledDisplayText() {
		String dt = getDisplayText();
		StyledString ss = new StyledString(dt);
		if (!isFirst()) {
			if (getParent() instanceof MFromTableJoin && getParent().getValue() instanceof MQueryTable) {
				int ind = dt.indexOf(" AS ");
				if (ind >= 0)
					ss.setStyle(ind, " AS ".length(), FontUtils.KEYWORDS_STYLER);
				ind = (dt).indexOf(" ON ");
				if (ind >= 0)
					ss.setStyle(ind, " ON ".length(), FontUtils.KEYWORDS_STYLER);
			} else
				ss.setStyle(0, (prevCond + " ").length(), FontUtils.KEYWORDS_STYLER);
		}
		if (operator.getNrOperands() != 2 || (operator.getNrOperands() == 2 && operator == Operator.LIKE)) {
			String sqlname = " " + operator.getSqlname() + " ";
			int ind = dt.indexOf(sqlname);
			if (ind >= 0)
				ss.setStyle(ind, sqlname.length(), FontUtils.KEYWORDS_STYLER);
		}
		if (operator.getNrOperands() == 3 && (operator == Operator.BETWEEN || operator == Operator.NOTBETWEEN))
			ss.setStyle(dt.indexOf(" AND "), " AND ".length(), FontUtils.KEYWORDS_STYLER);
		return ss;
	}

	private Operator operator = Operator.EQUALS;

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

}
