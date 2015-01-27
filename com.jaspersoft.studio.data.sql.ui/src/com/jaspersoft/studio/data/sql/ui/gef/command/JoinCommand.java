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
package com.jaspersoft.studio.data.sql.ui.gef.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.ActionFactory;
import com.jaspersoft.studio.data.sql.action.table.DeleteTableJoin;
import com.jaspersoft.studio.data.sql.action.table.JoinTable;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpression;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.operand.FieldOperand;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class JoinCommand extends Command {
	private SQLQueryDesigner designer;
	private MSQLColumn src, dest;
	private MFromTable srcTbl, destTbl;

	public JoinCommand(MSQLColumn src, MFromTable srcTbl, MSQLColumn dest, MFromTable destTbl, SQLQueryDesigner designer) {
		this.designer = designer;
		this.src = src;
		this.srcTbl = srcTbl;
		this.dest = dest;
		this.destTbl = destTbl;
	}

	public JoinCommand(MFromTable srcTbl, MFromTable destTbl, SQLQueryDesigner designer) {
		this.designer = designer;
		this.srcTbl = srcTbl;
		this.destTbl = destTbl;
	}

	@Override
	public void execute() {
		ActionFactory afactory = designer.getOutline().getAfactory();
		if (srcTbl instanceof MFromTableJoin && !(destTbl instanceof MFromTableJoin)) {
			MFromTable tmp = srcTbl;
			MSQLColumn tmpColumn = src;
			srcTbl = destTbl;
			src = dest;
			destTbl = tmp;
			dest = tmpColumn;
			// Object tobj = destTbl.getPropertyActualValue(MFromTable.PROP_X);
			// destTbl.setPropertyValue(MFromTable.PROP_X,
			// srcTbl.getPropertyActualValue(MFromTable.PROP_X));
			// srcTbl.setPropertyValue(MFromTable.PROP_X, tobj);
			// tobj = destTbl.getPropertyActualValue(MFromTable.PROP_Y);
			// destTbl.setPropertyValue(MFromTable.PROP_Y,
			// srcTbl.getPropertyActualValue(MFromTable.PROP_Y));
			// srcTbl.setPropertyValue(MFromTable.PROP_Y, tobj);
		}

		if (srcTbl instanceof MFromTableJoin) {
			DeleteTableJoin dtj = afactory.getAction(DeleteTableJoin.class);
			dtj.calculateEnabled(new Object[] { srcTbl });
			srcTbl = dtj.runSilent();
		}
		MFromTable fromTbl = destTbl;
		if (destTbl instanceof MFromTableJoin)
			fromTbl = getParentFromTable((MFromTableJoin) destTbl);
		if (srcTbl == destTbl)
			return;
		for (INode n : fromTbl.getChildren()) {
			if (n == destTbl) {
				MExpression mexpr = new MExpression(srcTbl, src, -1);
				mexpr.getOperands().add(new FieldOperand(src, srcTbl, mexpr));
				mexpr.getOperands().add(new FieldOperand(dest, destTbl, mexpr));
				return;
			}
		}

		JoinTable jt = afactory.getAction(JoinTable.class);
		jt.doRun(src, srcTbl, dest, destTbl, fromTbl);

		if (srcTbl instanceof MFromTable && !srcTbl.getChildren().isEmpty()) {
			List<MFromTableJoin> lst = new ArrayList<MFromTableJoin>();
			for (INode n : srcTbl.getChildren()) {
				if (n == destTbl)
					return;
				if (n instanceof MFromTableJoin)
					lst.add((MFromTableJoin) n);
			}
			for (MFromTable mft : lst)
				mft.setParent(destTbl, -1);
		}
		jt.selectInTree(destTbl);
	}

	public static MFromTable getParentFromTable(MFromTableJoin dest) {
		ANode res = dest.getParent();
		while (res != null) {
			if (res instanceof MFromTable && !(res instanceof MFromTableJoin))
				return (MFromTable) res;
			res = res.getParent();
		}
		return dest;
	}
}
