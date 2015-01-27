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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.jaspersoft.studio.data.sql.DbObjectName;
import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.impl.ColImpl;
import com.jaspersoft.studio.data.sql.model.metadata.INotInMetadata;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlSchema;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.metadata.MTables;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MDummy;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.util.KeyValue;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.utils.Misc;

public class ConvertUtil {

	public static String getDbObjectName(EList<EObject> eContents, int i) {
		int size = eContents.size();
		if (size >= i) {
			EObject eobj = eContents.get(size - i);
			if (eobj instanceof DbObjectName)
				return ((DbObjectName) eobj).getDbname();
		}
		if (!eContents.isEmpty() && eContents.get(0) instanceof ColImpl) {
			ColImpl c = (ColImpl) eContents.get(0);
			size = c.getEntries().size();
			int indx = size - i;
			if (size >= indx && indx >= 0)
				return c.getEntries().get(indx).getDbname();
		}
		return null;
	}

	public static void cleanDBMetadata(MRoot dbRoot) {
		new ModelVisitor<Object>(dbRoot) {

			@Override
			public boolean visit(INode n) {
				return true;
			}

			@Override
			public void iterate(INode node) {
				List<INode> toRemove = null;
				for (INode n : node.getChildren()) {
					if (n instanceof INotInMetadata && ((INotInMetadata) n).isNotInMetadata()) {
						if (toRemove == null)
							toRemove = new ArrayList<INode>();
						toRemove.add(n);
						continue;
					}
					if (visit(n))
						iterate(n);
				}
				if (toRemove != null)
					((ANode) node).removeChildren(toRemove);
			}
		};
	}

	public static boolean isInSchema(MSqlTable msqltable, String schema) {
		MSqlSchema mschema = null;
		if (schema != null) {
			schema = getDbName(schema);
			ANode parent = msqltable.getParent();
			if (parent != null) {
				if (parent instanceof MSqlSchema)
					mschema = (MSqlSchema) parent;
				else {
					parent = parent.getParent();
					if (parent != null && parent instanceof MSqlSchema)
						mschema = (MSqlSchema) parent;
				}
			}
			if (mschema != null && !mschema.getValue().equalsIgnoreCase(schema))
				return false;
		}
		return true;
	}

	public static KeyValue<MSQLColumn, MFromTable> findColumn(final MSelect msel, String sch, String tbl, String clmn) {
		final String schema = getDbName(sch);
		final String table = getDbName(tbl);
		final String column = getDbName(clmn);
		MFrom mfrom = Util.getKeyword(msel, MFrom.class);
		KeyValue<MSQLColumn, MFromTable> key = new ModelVisitor<KeyValue<MSQLColumn, MFromTable>>(mfrom) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MFromTable) {
					MFromTable ft = (MFromTable) n;
					MSqlTable msqltable = ft.getValue();
					if (!isInSchema(msqltable, schema))
						return true;
					if (table != null && !msqltable.getValue().equalsIgnoreCase(table)) {
						if (ft.getAlias() == null || (ft.getAlias() != null && !ft.getAlias().equalsIgnoreCase(table)))
							return true;
					}
					for (INode c : msqltable.getChildren()) {
						if (c instanceof MSQLColumn) {
							MSQLColumn mcol = (MSQLColumn) c;
							if (mcol.getValue().equalsIgnoreCase(column)) {
								setObject(new KeyValue<MSQLColumn, MFromTable>(mcol, ft));
								stop();
							}
						}
					}
					return true;
				}
				return false;
			}
		}.getObject();
		if (key == null) {
			for (MFromTable mft : Util.getFromTables(msel)) {
				if (mft.getValue().isNotInMetadata() && mft.getValue().getValue().equalsIgnoreCase(table))
					return new KeyValue<MSQLColumn, MFromTable>(new MSQLColumn(mft.getValue(), column, null), mft);
			}
		}
		return key;
	}

	public static MSqlTable getTableUnknown(MRoot dbroot, String schema, String table, SQLQueryDesigner designer) {
		table = getDbName(table);
		MSqlSchema msqlschem = ConvertUtil.findSchema(dbroot, Misc.nvl(schema), designer);
		MSqlTable mtbl = findTable(dbroot, schema, Misc.nvl(table), designer);
		if (mtbl == null) {
			for (INode n : msqlschem.getChildren())
				if (n instanceof MTables)
					return new MSqlTable((MTables) n, Misc.nvl(table), true);
			return new MSqlTable(new MTables(msqlschem, "Table"), Misc.nvl(table), true);
		}
		return mtbl;
	}

	public static MSqlSchema findSchema(MRoot dbroot, String schema, SQLQueryDesigner designer) {
		if (schema == null)
			schema = "";
		schema = getDbName(schema);
		boolean isNull = schema.isEmpty();
		for (INode n : dbroot.getChildren()) {
			MSqlSchema ms = (MSqlSchema) n;
			if ((!isNull && ms.getValue().equalsIgnoreCase(schema)) || (isNull && (ms.isCurrent() || ms.getValue().equals(schema)))) {
				designer.getDbMetadata().loadSchema(ms);
				return ms;
			}
		}
		for (INode n : dbroot.getChildren()) {
			MSqlSchema ms = (MSqlSchema) n;
			if (ms.getValue().equals(schema))
				return ms;
		}
		return new MSqlSchema(dbroot, schema, null, true);
	}

	public static MSqlTable findTable(MRoot dbroot, String schema, String table, final SQLQueryDesigner designer) {
		final String tbl = getDbName(table);
		MSqlSchema ms = findSchema(dbroot, schema, designer);
		return new ModelVisitor<MSqlTable>(ms) {
			@Override
			public boolean visit(INode n) {
				if (n instanceof MSqlTable) {
					MSqlTable mt = (MSqlTable) n;
					if (mt.getValue().equalsIgnoreCase(tbl)) {
						if (mt.getChildren().isEmpty() || mt.getChildren().get(0) instanceof MDummy)
							designer.getDbMetadata().loadTable(mt);
						setObject(mt);
						stop();
					}
				} else if (n instanceof MTables)
					return true;
				return false;
			}
		}.getObject();
	}

	public static String cleanDbNameFull(String dbname) {
		String r = "";
		String sep = "";
		for (String s : dbname.split("\\.")) {
			r += sep + getDbName(s);
			sep = ".";
		}
		return r;
	}

	public static String getDbName(String dbname) {
		if (dbname != null && !dbname.isEmpty()) {
			if (dbname.startsWith("\""))
				return dbname.replace("\"", "");
			if (dbname.startsWith("["))
				return dbname.replace("[", "").replace("]", "");
			if (dbname.startsWith("`"))
				return dbname.replace("`", "");
		}
		return dbname;
	}

	public static String getParamValue(String prm) {
		return prm.replace("$P{", "").replace("}", "");
	}

	public static String getParamExclamationValue(String prm) {
		return prm.replace("$P!{", "").replace("}", "");
	}
}
