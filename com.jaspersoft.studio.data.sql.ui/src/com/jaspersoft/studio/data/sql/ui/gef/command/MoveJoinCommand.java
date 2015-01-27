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

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.ActionFactory;
import com.jaspersoft.studio.data.sql.action.table.DeleteTableJoin;
import com.jaspersoft.studio.data.sql.action.table.JoinTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.from.TableJoin;

public class MoveJoinCommand extends Command {
	private SQLQueryDesigner designer;
	private MFromTable destTbl, srcTbl;
	private TableJoin tjoin;

	public MoveJoinCommand(MFromTable destTbl, TableJoin tjoin, SQLQueryDesigner designer) {
		this.designer = designer;
		this.tjoin = tjoin;
		this.destTbl = destTbl;
		srcTbl = tjoin.getJoinTable();
	}

	public MoveJoinCommand(TableJoin tjoin, MFromTable destTbl, SQLQueryDesigner designer) {
		this.designer = designer;
		this.tjoin = tjoin;
		this.destTbl = tjoin.getFromTable();
		this.srcTbl = destTbl;
	}

	@Override
	public void execute() {
		ActionFactory afactory = designer.getOutline().getAfactory();
		DeleteTableJoin dtj = afactory.getAction(DeleteTableJoin.class);
		dtj.calculateEnabled(new Object[] { tjoin.getJoinTable() });
		if (srcTbl instanceof MFromTableJoin)
			srcTbl = dtj.runSilent();
		else
			dtj.runSilent();

		MFromTable fromTbl = destTbl;
		if (destTbl instanceof MFromTableJoin)
			fromTbl = JoinCommand.getParentFromTable((MFromTableJoin) destTbl);

		JoinTable jt = afactory.getAction(JoinTable.class);
		jt.doRun(null, srcTbl, null, destTbl, fromTbl);
	}
}
