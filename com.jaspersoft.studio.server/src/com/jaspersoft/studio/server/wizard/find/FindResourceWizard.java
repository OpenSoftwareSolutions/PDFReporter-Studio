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
package com.jaspersoft.studio.server.wizard.find;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.MServerProfile;

public class FindResourceWizard extends Wizard {
	private MServerProfile sp;
	private FindResourcePage page0;

	public FindResourceWizard(MServerProfile sp) {
		super();
		setWindowTitle(Messages.FindResourceWizard_0 + sp.getDisplayText());
		setNeedsProgressMonitor(true);
		this.sp = sp;
	}

	private String[] itypes;
	private String[] etypes;

	public void setFilterTypes(String[] in, String[] excl) {
		this.itypes = in;
		this.etypes = excl;
		setFilters();
	}

	private void setFilters() {
		if (page0 != null)
			page0.setFilterTypes(itypes, etypes);
	}

	@Override
	public void addPages() {
		page0 = new FindResourcePage(sp);
		setFilters();
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	public ResourceDescriptor getValue() {
		return page0.getValue();
	}
}
