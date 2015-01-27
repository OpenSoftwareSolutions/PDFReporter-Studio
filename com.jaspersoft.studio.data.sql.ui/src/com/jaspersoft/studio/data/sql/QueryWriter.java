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
package com.jaspersoft.studio.data.sql;

import com.jaspersoft.studio.data.sql.model.ISubQuery;
import com.jaspersoft.studio.data.sql.model.query.AMQueryAliased;
import com.jaspersoft.studio.data.sql.model.query.IQueryString;
import com.jaspersoft.studio.data.sql.model.query.MUnion;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderBy;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelVisitor;

public class QueryWriter {
	public static String writeQuery(ANode root) {
		final StringBuffer sb = new StringBuffer();
		new ModelVisitor<INode>(root) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof ISubQuery)
					sb.append("(");
				else if (n instanceof IQueryString) {
					if (n instanceof MFromTable && n.getValue() instanceof MQueryTable && !((MFromTable) n).isFirst())
						sb.append(",\n\t");
					sb.append(((IQueryString) n).toSQLString());
				}
				return true;
			}

			@Override
			protected void postChildIteration(INode n) {
				boolean b = n instanceof MFromTable && n.getValue() instanceof MQueryTable;
				if (n instanceof ISubQuery || b) {
					if (b && n instanceof MFromTableJoin)
						return;
					sb.append(")");
					if (n instanceof AMQueryAliased<?>)
						sb.append(((AMQueryAliased<?>) n).addAlias());
				}
			}
		};
		return sb.toString();
	}

	public static String writeSubQuery(ANode root) {
		final StringBuffer sb = new StringBuffer();
		new ModelVisitor<INode>(root) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MUnion || n instanceof MOrderBy)
					stop();
				if (n instanceof ISubQuery)
					sb.append("(");
				else if (n instanceof IQueryString)
					sb.append(((IQueryString) n).toSQLString());
				return true;
			}

			@Override
			protected void postChildIteration(INode n) {
				boolean b = n instanceof MFromTable && n.getValue() instanceof MQueryTable;
				if (n instanceof ISubQuery || b) {
					sb.append(")");
					if (b && n instanceof MFromTableJoin)
						return;
					if (n instanceof AMQueryAliased<?>)
						sb.append(((AMQueryAliased<?>) n).addAlias());
				}
			}
		};
		return sb.toString();
	}
}
