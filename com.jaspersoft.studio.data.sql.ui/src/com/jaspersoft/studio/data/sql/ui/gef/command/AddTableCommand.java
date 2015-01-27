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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.metadata.keys.ForeignKey;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpression;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.operand.FieldOperand;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class AddTableCommand extends Command {
	private Rectangle location;
	private MFrom parent;
	private Collection<MSqlTable> child;
	private List<MFromTable> fromTable;
	private Map<MFromTable, ANode> parentMap;

	public AddTableCommand(MFrom parent, Collection<MSqlTable> child, Rectangle location) {
		this.location = location;
		this.parent = parent;
		this.child = child;
	}

	@Override
	public void execute() {
		if (fromTable == null) {
			fromTable = new ArrayList<MFromTable>();
			Map<ForeignKey, MFromTable> keys = new HashMap<ForeignKey, MFromTable>();
			for (MSqlTable mtlb : child) {
				MFromTable ft = new MFromTable(parent, mtlb);
				if (location != null && child.size() == 1) {
					ft.setNoEvents(true);
					ft.setPropertyValue(MFromTable.PROP_X, location.x);
					ft.setPropertyValue(MFromTable.PROP_Y, location.y);
					ft.setNoEvents(false);
				}
				fromTable.add(ft);
				for (INode n : mtlb.getChildren()) {
					List<ForeignKey> lfk = ((MSQLColumn) n).getForeignKeys();
					if (lfk != null)
						for (ForeignKey fk : lfk)
							if (fk.getTable().equals(mtlb))
								keys.put(fk, ft);
				}
			}
			if (fromTable.size() > 1)
				for (ForeignKey fk : keys.keySet()) {
					for (MSQLColumn c : fk.getDestColumns()) {
						MFromTable src = keys.get(fk);
						MFromTable dest = hasTable(c);
						if (dest == null)
							break;
						if (src == dest)
							break;
						if (!(src instanceof MFromTableJoin)) {
							MFromTable p = dest instanceof MFromTableJoin ? (MFromTable) dest.getParent() : dest;

							src.setParent(null, -1);
							fromTable.remove(src);

							MFromTableJoin join = new MFromTableJoin(p, src.getValue());
							MExpression mexpr = new MExpression(join, dest, -1);
							mexpr.getOperands().add(new FieldOperand(fk.getSrcColumns()[0], join, mexpr));
							mexpr.getOperands().add(new FieldOperand(c, dest, mexpr));

							fromTable.add(join);
						}
						break;
					}
				}
		} else {
			for (MFromTable mft : fromTable)
				mft.setParent(parent, -1);
		}
	}

	private MFromTable hasTable(MSQLColumn c) {
		for (MFromTable ft : fromTable)
			if (ft.getValue().equals(c.getParent()))
				return ft;
		return null;
	}

	@Override
	public void undo() {
		if (parentMap == null)
			parentMap = new HashMap<MFromTable, ANode>();
		for (MFromTable p : parentMap.keySet()) {
			parentMap.put(p, p.getParent());
			p.setParent(null, -1);
		}
	}
}
