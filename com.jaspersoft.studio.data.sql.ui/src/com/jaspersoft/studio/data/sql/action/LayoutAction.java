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
package com.jaspersoft.studio.data.sql.action;

import org.eclipse.jface.action.Action;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelVisitor;

public class LayoutAction extends Action {
	private SQLQueryDesigner designer;

	public LayoutAction(SQLQueryDesigner designer) {
		super("&Layout Tables");
		this.designer = designer;
	}

	@Override
	public void run() {
		new ModelVisitor<Object>(designer.getRoot()) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MFromTable) {
					((MFromTable) n).setPropertyValue(MFromTable.PROP_X, null);
					((MFromTable) n).setPropertyValue(MFromTable.PROP_Y, null);
				}
				return true;
			}

		};
		designer.getDiagram().scheduleRefresh();
	}

}
