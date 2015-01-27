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

import org.eclipse.jface.viewers.StyledString;

import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;

public class MExpressionGroup extends AMKeyword {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MExpressionGroup(ANode parent) {
		super(parent, AMKeyword.AND_OPERATOR, null);
	}

	@Override
	public String getDisplayText() {
		String str = "";
		if (!isFirst()) {
			if (getParent() instanceof MFromTableJoin && getParent().getValue() instanceof MQueryTable) {
				MFromTableJoin mftj = (MFromTableJoin) getParent();
				str += ") " + mftj.addAlias() + " ON ";
			} else
				str += super.getDisplayText();
		}
		str += " (";
		if (getChildren().isEmpty())
			str += ")";
		return str;
	}

	@Override
	public StyledString getStyledDisplayText() {
		StyledString ss = new StyledString();
		if (isFirst())
			ss.append("(");
		else {
			ss.append(super.getDisplayText(), FontUtils.KEYWORDS_STYLER);
			ss.append(" (");
		}
		if (getChildren().isEmpty())
			ss.append(")");
		return ss;
	}

	@Override
	public String toSQLString() {
		return "\n\t " + getDisplayText() + " ";
	}
}
