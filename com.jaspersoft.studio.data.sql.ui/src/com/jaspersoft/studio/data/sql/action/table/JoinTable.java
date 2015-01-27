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
package com.jaspersoft.studio.data.sql.action.table;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.dialogs.JoinFromTableDialog;
import com.jaspersoft.studio.data.sql.messages.Messages;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpression;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.from.TableJoin;
import com.jaspersoft.studio.data.sql.model.query.operand.FieldOperand;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class JoinTable extends AAction {
	private SQLQueryDesigner designer;

	public JoinTable(SQLQueryDesigner designer, TreeViewer treeViewer) {
		super(Messages.JoinTable_0, treeViewer);
		this.designer = designer;
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection != null && selection.length == 1 && selection[0] instanceof ANode && isColumn((ANode) selection[0]);
	}

	protected boolean isColumn(ANode element) {
		boolean b = element instanceof MFromTable;// && !(element instanceof
																							// MFromTableJoin);
		if (b) {
			MFrom mfrom = null;
			if (element instanceof MFromTable && element.getValue() instanceof MQueryTable)
				mfrom = Util.getKeyword(element.getParent(), MFrom.class);
			else
				mfrom = Util.getKeyword(element, MFrom.class);
			b = b && mfrom.getChildren().size() > 1;
		}
		return b;
	}

	@Override
	public void run() {
		MFromTable mfromTable = null;
		for (Object obj : selection) {
			if (obj instanceof MFromTable) {
				mfromTable = (MFromTable) obj;
				break;
			}
		}
		JoinFromTableDialog dialog = new JoinFromTableDialog(UIUtils.getShell(), designer, true);
		dialog.setValue(mfromTable);
		if (dialog.open() == Dialog.OK) {
			MFromTable destTbl = getFromTable(mfromTable, dialog);

			if (mfromTable instanceof MFromTableJoin) {
				mfromTable = (MFromTable) mfromTable.getParent();

				MFromTable tmp = destTbl;
				destTbl = mfromTable;
				mfromTable = tmp;
			}

			doRun(null, mfromTable, null, destTbl, destTbl);
		}
	}

	public void doRun(MSQLColumn src, MFromTable srcTbl, MSQLColumn dest, MFromTable destTbl, MFromTable fromTbl) {
		if (src == null)
			src = getColumn(srcTbl.getValue());
		if (dest == null)
			dest = getColumn(destTbl.getValue());
		srcTbl.setParent(null, -1);

		boolean onlyExpression = false;
		for (INode n : fromTbl.getChildren()) {
			if (n == destTbl) {
				onlyExpression = true;
				break;
			}
		}
		MFromTableJoin mtbljoin = null;
		if (!onlyExpression) {
			mtbljoin = new MFromTableJoin(fromTbl, srcTbl.getValue());
			mtbljoin.setNoEvents(true);
			mtbljoin.setPropertyValue(MFromTable.PROP_X, srcTbl.getPropertyActualValue(MFromTable.PROP_X));
			mtbljoin.setPropertyValue(MFromTable.PROP_Y, srcTbl.getPropertyActualValue(MFromTable.PROP_Y));
			mtbljoin.setNoEvents(false);
			mtbljoin.setAlias(srcTbl.getAlias());
			mtbljoin.setAliasKeyword(srcTbl.getAliasKeyword());

			fromTbl.removeTableJoin(mtbljoin.getTableJoin());

			mtbljoin.setTableJoin(new TableJoin(mtbljoin, (MFromTable) destTbl));
			Util.copySubQuery(srcTbl, mtbljoin);
		} else
			mtbljoin = (MFromTableJoin) destTbl;

		MExpression mexpr = new MExpression(mtbljoin, src, -1);
		mexpr.getOperands().add(new FieldOperand(src, mtbljoin, mexpr));
		mexpr.getOperands().add(new FieldOperand(dest, destTbl, mexpr));
		selectInTree(mexpr);

		Util.cleanTableVersions(mtbljoin, srcTbl);
	}

	private MSQLColumn getColumn(MSqlTable tbl) {
		if (!tbl.getChildren().isEmpty()) {
			for (INode n : tbl.getChildren())
				if (((MSQLColumn) n).getPrimaryKey() != null)
					return (MSQLColumn) n;
			return (MSQLColumn) tbl.getChildren().get(0);
		}
		return null;
	}

	public static MFromTable getFromTable(MFromTable mcol, JoinFromTableDialog dialog) {
		String ft = dialog.getFromTable().replace(",", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
		MFromTable mFromTable = null;
		for (MFromTable mft : Util.getFromTables(Util.getKeyword(mcol, MFrom.class))) {
			if (mft == mcol)
				continue;
			String alias = ""; //$NON-NLS-1$
			if (mft.getAlias() != null)
				alias = mft.getAliasKeyString() + mft.getAlias();
			if ((mft.getValue().getDisplayText() + alias).trim().equals(ft)) {
				mFromTable = mft;
				break;
			}
		}
		if (mFromTable instanceof MFromTableJoin)
			mFromTable = (MFromTable) mFromTable.getParent();
		return mFromTable;
	}
}
