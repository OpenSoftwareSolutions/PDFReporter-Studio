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
package com.jaspersoft.studio.data.sql.text2model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.jaspersoft.studio.data.sql.OrOrderByColumn;
import com.jaspersoft.studio.data.sql.OrderByColumnFull;
import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.impl.DbObjectNameImpl;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderBy;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByExpression;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.model.util.KeyValue;

public class ConvertOrderBy {
	public static void convertOrderBy(SQLQueryDesigner designer, OrOrderByColumn cols) {
		if (cols == null)
			return;
		if (cols instanceof OrderByColumnFull)
			doColumn(designer, Util.getKeyword(designer.getRoot(), MSelect.class), (OrderByColumnFull) cols);
		else if (cols instanceof OrOrderByColumn) {
			MSelect msel = Util.getKeyword(designer.getRoot(), MSelect.class);
			for (OrderByColumnFull fcol : cols.getEntries())
				doColumn(designer, msel, fcol);
		}
	}

	private static void doColumn(SQLQueryDesigner designer, MSelect msel, OrderByColumnFull tf) {
		if (tf.getColOrder() != null) {
			EList<EObject> eContents = tf.getColOrder().eContents();
			String column = null;
			if (tf instanceof DbObjectNameImpl)
				column = ((DbObjectNameImpl) tf).getDbname();
			else if (tf.getColOrder() instanceof DbObjectNameImpl)
				column = ((DbObjectNameImpl) tf.getColOrder()).getDbname();
			else
				column = ConvertUtil.getDbObjectName(eContents, 1);
			String table = ConvertUtil.getDbObjectName(eContents, 2);
			String schema = ConvertUtil.getDbObjectName(eContents, 3);
			// String catalog = getDbObjectName(eContents, 3);
			MOrderByColumn mocol = findColumn(msel, schema, table, column);
			if (mocol != null) {
				String direction = tf.getDirection();
				if (direction != null)
					mocol.setDesc(direction.trim().equalsIgnoreCase(AMKeyword.DESCENDING_KEYWORD.trim()));
			}
		} else if (tf.getColOrderInt() > 0)
			new MOrderByExpression(Util.getKeyword(msel.getParent(), MOrderBy.class), Integer.toString(tf.getColOrderInt()));
	}

	//
	// public static String getDbObjectName(EList<ColumnFull> eContents, int i) {
	// int size = eContents.size();
	// if (size >= i) {
	// EObject eobj = eContents.get(size - i);
	// if (eobj instanceof DbObjectName)
	// return ((DbObjectName) eobj).getDbname();
	// }
	// return null;
	// }

	private static MOrderByColumn findColumn(final MSelect msel, final String schema, final String table, final String column) {
		KeyValue<MSQLColumn, MFromTable> kv = ConvertUtil.findColumn(msel, schema, table, column);
		if (kv != null)
			return new MOrderByColumn(Util.getKeyword(msel.getParent(), MOrderBy.class), kv.key, kv.value);
		return null;
	}
}
