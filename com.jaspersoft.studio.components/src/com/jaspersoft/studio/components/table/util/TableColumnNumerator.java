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
package com.jaspersoft.studio.components.table.util;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;

import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroupCell;
import com.jaspersoft.studio.model.INode;

public class TableColumnNumerator {

	public static void renumerateColumnNames(MTable mtable) {
		JRDesignComponentElement tbl = (JRDesignComponentElement) mtable.getValue();
		if (tbl.getComponent() instanceof StandardTable) {
			StandardTable table = (StandardTable) tbl.getComponent();
			List<BaseColumn> columns = TableUtil.getAllColumns(table.getColumns());
			setColNames(mtable, columns);
		}
	}

	public static void setColNames(INode n, List<BaseColumn> columns) {
		for (INode node : n.getChildren()) {
			if (node instanceof MColumn)
				setColumnName((MColumn) node, columns);
			if (node instanceof AMCollection || node instanceof MColumn)
				setColNames(node, columns);
		}
	}

	public static void setColumnName(MColumn col, List<BaseColumn> columns) {
		StandardBaseColumn bc = (StandardBaseColumn) col.getValue();
		int i = columns.indexOf(bc) + 1;
		if (col instanceof MColumnGroup || col instanceof MColumnGroupCell) {
			int size = TableUtil.getAllColumns(((StandardColumnGroup) bc).getColumns()).size();
			col.setName(Messages.common_columns + " [" + size + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (col instanceof MColumn)
			col.setName(Messages.common_column + i);
	}
}
