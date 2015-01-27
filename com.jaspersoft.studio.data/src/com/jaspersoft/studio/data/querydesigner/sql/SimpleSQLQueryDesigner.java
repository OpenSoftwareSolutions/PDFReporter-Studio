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
package com.jaspersoft.studio.data.querydesigner.sql;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.data.designer.QueryDesigner;
import com.jaspersoft.studio.data.querydesigner.sql.text.SQLLineStyler;

/**
 * Simple SQL query designer that simply provides syntax coloring support.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class SimpleSQLQueryDesigner extends QueryDesigner {

	// Line style for SQL code
	private SQLLineStyler sqlLineStyler;

	public SimpleSQLQueryDesigner() {
		super();
		sqlLineStyler = getSQLBasedLineStyler();
	}

	@Override
	public Control createControl(Composite parent) {
		super.createControl(parent);
		createLineStyler();
		return control;
	}

	protected void createLineStyler() {
		control.addLineStyleListener(sqlLineStyler);
		control.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				sqlLineStyler.parseBlockComments(control.getText());
			}
		});
	}

	/**
	 * @return the SQL line styler
	 */
	protected SQLLineStyler getSQLBasedLineStyler() {
		return new SQLLineStyler();
	}

}
