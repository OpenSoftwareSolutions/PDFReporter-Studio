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
package com.jaspersoft.studio.property.descriptor.jrQuery.dialog;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MQuery;

public class JRQueryEditor extends Wizard {
	private MQuery mQuery;
	private JRQueryPage page0;

	public MQuery getValue() {
		if (page0 != null)
			return page0.getValue();
		return mQuery;
	}

	public void setValue(MQuery value) {
		if (page0 != null)
			page0.setValue(value);
		this.mQuery = value;
	}

	public JRQueryEditor() {
		super();
		setWindowTitle(Messages.common_query_editor);
	}

	@Override
	public void addPages() {
		page0 = new JRQueryPage("jrquery.editor"); //$NON-NLS-1$
		page0.setValue(mQuery);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
