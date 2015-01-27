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
package com.jaspersoft.studio.data.hbase.querydesigner;

import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.data.designer.QueryDesigner;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

/**
 * Simple query designer for MongoDB query language that provides syntax
 * highlighting.
 * 
 */
public class HBaseQueryDesigner extends QueryDesigner {
	/* Text area where enter the query */
	protected StyledText queryTextArea;
	private HBaseLineStyler lineStyler = new HBaseLineStyler();

	public Control createControl(Composite parent) {
		control = (StyledText) super.createControl(parent);
		control.addLineStyleListener(lineStyler);
		return control;
	}

	protected void queryTextAreaModified() {
		// keep the query info updated
		((JRDesignQuery) jDataset.getQuery()).setText(queryTextArea.getText());
	}

	@Override
	public String getContextHelpId() {
		return ContextHelpIDs.PREFIX.concat("query_hbase");
	}
}
