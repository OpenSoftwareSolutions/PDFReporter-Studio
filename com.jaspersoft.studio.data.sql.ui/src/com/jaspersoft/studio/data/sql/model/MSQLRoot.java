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
package com.jaspersoft.studio.data.sql.model;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignDataset;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.utils.Misc;

public class MSQLRoot extends MRoot {

	public MSQLRoot(ANode parent, JRDesignDataset value) {
		super(parent, value);
	}

	@Override
	public JRDesignDataset getValue() {
		return (JRDesignDataset) super.getValue();
	}

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private String iq = "";
	private boolean quoteExceptions = true;

	public void setQuoteExceptions(boolean quoteExceptions) {
		this.quoteExceptions = quoteExceptions;
	}

	public boolean isQuoteExceptions() {
		return quoteExceptions;
	}

	public String getIdentifierQuote() {
		return iq;
	}

	public void setIdentifierQuote(String iq) {
		this.iq = Misc.nvl(iq);
	}
}
