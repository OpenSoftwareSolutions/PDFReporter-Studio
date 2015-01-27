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

import com.jaspersoft.studio.data.sql.FromTable;
import com.jaspersoft.studio.data.sql.FromTableJoin;
import com.jaspersoft.studio.data.sql.OrTable;
import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.TableFull;
import com.jaspersoft.studio.data.sql.TableOrAlias;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.impl.DbObjectNameImpl;
import com.jaspersoft.studio.data.sql.impl.OrTableImpl;
import com.jaspersoft.studio.data.sql.impl.SelectImpl;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.utils.Misc;

public class ConvertTables {
	public static void convertTables(SQLQueryDesigner designer, ANode qroot, OrTable tbls) {
		try {
			if (tbls == null)
				return;
			if (tbls instanceof FromTable)
				doTables(designer, Util.getKeyword(qroot, MFrom.class), (FromTable) tbls);
			else if (tbls instanceof OrTableImpl) {
				MFrom mfrom = Util.getKeyword(qroot, MFrom.class);
				for (FromTable ftbl : tbls.getEntries())
					doTables(designer, mfrom, ftbl);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static void doTables(SQLQueryDesigner designer, MFrom mfrom, FromTable ftbl) {
		TableOrAlias t = ftbl.getTable();
		if (t != null && t.getTfull() != null) {
			MFromTable mft = getFromTable(t, mfrom, designer);
			if (t.getTblAlias() != null)
				mft.setAlias(t.getTblAlias().getDbname());
			mft.setAliasKeyword(Misc.nvl(t.getAlias(), " "));
			if (ftbl.getFjoin() != null) {
				for (FromTableJoin ftj : ftbl.getFjoin()) {
					TableOrAlias onTbl = ftj.getOnTable();
					MFromTableJoin mftj = getFromTableJoin(onTbl, mft, designer);

					if (onTbl.getTblAlias() != null)
						mftj.setAlias(onTbl.getTblAlias().getDbname());
					mftj.setJoin(ftj.getJoin());
					mftj.setAliasKeyword(Misc.nvl(onTbl.getAlias(), " "));

					ConvertExpression.convertExpression(designer, mfrom.getParent(), mftj, ftj.getJoinExpr());
				}
			}
		}
	}

	private static MFromTableJoin getFromTableJoin(TableOrAlias t, MFromTable mfrom, SQLQueryDesigner designer) {
		MFromTableJoin mft = null;
		if (t.getSq() != null) {
			MQueryTable mqt = new MQueryTable(null);
			mft = new MFromTableJoin(mfrom, mqt);
			mqt.setSubquery(Util.createSelect(mft));
			Text2Model.convertSelect(designer, mft, (SelectImpl) t.getSq().getSel());
		} else {
			MRoot dbroot = designer.getDbMetadata().getRoot();
			MSqlTable msqlt = getTable(dbroot, t.getTfull(), designer);
			mft = new MFromTableJoin(mfrom, msqlt);
		}
		return mft;
	}

	private static MFromTable getFromTable(TableOrAlias t, MFrom mfrom, SQLQueryDesigner designer) {
		MFromTable mft = null;
		if (t.getSq() != null) {
			MQueryTable mqt = new MQueryTable(null);
			mft = new MFromTable(mfrom, mqt);
			mqt.setSubquery(Util.createSelect(mft));
			Text2Model.convertSelect(designer, mft, (SelectImpl) t.getSq().getSel());
		} else {
			MRoot dbroot = designer.getDbMetadata().getRoot();
			MSqlTable msqlt = getTable(dbroot, t.getTfull(), designer);
			mft = new MFromTable(mfrom, msqlt);
		}
		return mft;
	}

	private static MSqlTable getTable(MRoot dbroot, TableFull tf, SQLQueryDesigner designer) {
		EList<EObject> eContents = tf.eContents();
		String table = ConvertUtil.getDbObjectName(eContents, 1);
		String schema = ConvertUtil.getDbObjectName(eContents, 2);
		if (tf instanceof DbObjectNameImpl)
			table = ((DbObjectNameImpl) tf).getDbname();
		// String catalog = getDbObjectName(eContents, 3);
		MSqlTable msqlt = ConvertUtil.findTable(dbroot, schema, table, designer);
		if (msqlt == null)
			msqlt = ConvertUtil.getTableUnknown(dbroot, schema, table, designer);
		return msqlt;
	}

}
