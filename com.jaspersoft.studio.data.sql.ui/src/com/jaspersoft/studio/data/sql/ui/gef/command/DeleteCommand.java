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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupBy;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderBy;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class DeleteCommand extends Command {
	private MFromTable node;
	private ANode parent;
	private Map<ANode, ANode> mapDel;
	private Map<ANode, Integer> mapDelIndex;

	private Map<ANode, ANode> mapAdd;
	private Map<ANode, Integer> mapAddIndex;

	public DeleteCommand(MFromTable node) {
		this.node = node;
		this.parent = node.getParent();
	}

	@Override
	public void execute() {
		if (mapDel == null) {
			mapDel = new HashMap<ANode, ANode>();
			mapDelIndex = new HashMap<ANode, Integer>();

			mapAdd = new HashMap<ANode, ANode>();
			mapAddIndex = new HashMap<ANode, Integer>();

			mapDel.put(node, parent);
			int indx = node.getParent().getChildren().indexOf(node);
			mapDelIndex.put(node, indx);
			if (!(node instanceof MFromTableJoin) && !node.getChildren().isEmpty()) {
				int i = 0;
				for (INode n : node.getChildren()) {
					if (n instanceof MFromTable) {
						MFromTable mft = new MFromTable(null, ((MFromTable) n).getValue());
						mft.setAlias(((MFromTable) n).getAlias());
						mft.setAliasKeyword(((MFromTable) n).getAliasKeyword());
						mapAdd.put(mft, node.getParent());
						mapAddIndex.put(mft, indx + i);
					}
					i++;
				}
			}
			doDeleteMore(parent, node);
		}
		for (ANode mft : mapDel.keySet())
			mft.setParent(null, -1);
		for (ANode mft : mapAdd.keySet())
			mft.setParent(mapAdd.get(mft), mapAddIndex.get(mft));
	}

	public void undo() {
		for (ANode mft : mapAdd.keySet())
			mft.setParent(null, -1);
		for (ANode key : mapDel.keySet())
			key.setParent(mapDel.get(key), mapDelIndex.get(key));
	}

	protected void doDeleteMore(ANode parent, MFromTable todel) {
		if (parent.getRoot() != null)
			for (INode n : parent.getRoot().getChildren()) {
				List<ANode> toRemove = new ArrayList<ANode>();
				if (n instanceof MSelect) {
					for (INode gb : n.getChildren()) {
						MSelectColumn gbc = (MSelectColumn) gb;
						if (gbc.getMFromTable() != null && gbc.getMFromTable().equals(todel))
							toRemove.add(gbc);
					}
					((MSelect) n).removeChildren(toRemove);
				} else if (n instanceof MGroupBy) {
					for (INode gb : n.getChildren()) {
						MGroupByColumn gbc = (MGroupByColumn) gb;
						if (gbc.getMFromTable() != null && gbc.getMFromTable().equals(todel))
							toRemove.add(gbc);
					}

					((MGroupBy) n).removeChildren(toRemove);
				} else if (n instanceof MOrderBy) {
					for (INode gb : n.getChildren()) {
						if (gb instanceof MOrderByColumn) {
							MOrderByColumn gbc = (MOrderByColumn) gb;
							if (gbc.getMFromTable() != null && gbc.getMFromTable().equals(todel))
								toRemove.add(gbc);
						}
					}
					((MOrderBy) n).removeChildren(toRemove);
				}
				for (ANode rem : toRemove) {
					ANode p = rem.getParent();
					if (p == null)
						continue;
					mapDel.put(rem, p);
					mapDelIndex.put(rem, p.getChildren().indexOf(rem));
				}
			}
	}
}
