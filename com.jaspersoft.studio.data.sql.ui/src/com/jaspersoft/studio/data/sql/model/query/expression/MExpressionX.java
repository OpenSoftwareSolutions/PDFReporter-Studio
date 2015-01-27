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

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuter;

import org.eclipse.jface.viewers.StyledString;

import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.operand.AOperand;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;

public class MExpressionX extends AMExpression<Object> {
	public static final String[] FUNCTIONS = new String[] { JRJdbcQueryExecuter.CLAUSE_ID_EQUAL, JRJdbcQueryExecuter.CLAUSE_ID_NOTEQUAL, JRJdbcQueryExecuter.CLAUSE_ID_GREATER,
			JRJdbcQueryExecuter.CLAUSE_ID_GREATER_OR_EQUAL, JRJdbcQueryExecuter.CLAUSE_ID_LESS, JRJdbcQueryExecuter.CLAUSE_ID_LESS_OR_EQUAL, JRJdbcQueryExecuter.CLAUSE_ID_BETWEEN,
			JRJdbcQueryExecuter.CLAUSE_ID_BETWEEN_CLOSED, JRJdbcQueryExecuter.CLAUSE_ID_BETWEEN_LEFT_CLOSED, JRJdbcQueryExecuter.CLAUSE_ID_BETWEEN_RIGHT_CLOSED, JRJdbcQueryExecuter.CLAUSE_ID_IN,
			JRJdbcQueryExecuter.CLAUSE_ID_NOTIN };

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MExpressionX(ANode parent, Object value, int newIndex) {
		super(parent, value, newIndex);
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

		dt += "$X{" + function;
		String sep = ",";
		for (AOperand op : operands)
			if (op.toXString().contains(",")) {
				sep = "|";
				break;
			}
		for (AOperand op : operands)
			dt += sep + op.toXString();
		dt += "}";
		return dt + isLastInGroup(getParent(), this);
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
		ss.setStyle(dt.lastIndexOf("$X{"), 3, FontUtils.CLASSTYPE_STYLER);
		ss.setStyle(dt.lastIndexOf("}"), 1, FontUtils.CLASSTYPE_STYLER);
		return ss;
	}

	private String function = JRJdbcQueryExecuter.CLAUSE_ID_EQUAL;

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

}
