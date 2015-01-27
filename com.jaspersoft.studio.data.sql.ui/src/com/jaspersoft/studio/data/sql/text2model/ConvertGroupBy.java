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

import com.jaspersoft.studio.data.sql.GroupByColumnFull;
import com.jaspersoft.studio.data.sql.OrGroupByColumn;
import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.impl.DbObjectNameImpl;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupBy;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupByColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.KeyValue;

public class ConvertGroupBy {
	public static void convertGroupBy(SQLQueryDesigner designer, ANode qroot, OrGroupByColumn cols) {
		if (cols == null)
			return;
		if (cols instanceof GroupByColumnFull)
			doColumn(designer, Util.getKeyword(qroot, MSelect.class), (GroupByColumnFull) cols);
		else if (cols instanceof OrGroupByColumn) {
			MSelect msel = Util.getKeyword(qroot, MSelect.class);
			for (GroupByColumnFull fcol : cols.getEntries())
				doColumn(designer, msel, fcol);
		}
	}

	private static void doColumn(SQLQueryDesigner designer, MSelect msel, GroupByColumnFull tf) {
		try {
			EList<EObject> eContents = tf.eContents();
			String column = null;
			if (tf instanceof DbObjectNameImpl)
				column = ((DbObjectNameImpl) tf).getDbname();
			else
				column = ConvertUtil.getDbObjectName(eContents, 1);
			String table = ConvertUtil.getDbObjectName(eContents, 2);
			String schema = ConvertUtil.getDbObjectName(eContents, 3);
			// String catalog = getDbObjectName(eContents, 3);
			KeyValue<MSQLColumn, MFromTable> kv = ConvertUtil.findColumn(msel, schema, table, column);
			if (kv != null)
				new MGroupByColumn(Util.getKeyword(msel.getParent(), MGroupBy.class), kv.key, kv.value);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
