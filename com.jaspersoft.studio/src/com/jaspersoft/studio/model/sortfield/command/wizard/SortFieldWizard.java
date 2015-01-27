/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.model.sortfield.command.wizard;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSortField;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.sortfield.command.wizard.WizardSortFieldPage.SHOW_TYPE;

public class SortFieldWizard extends Wizard {
	private WizardSortFieldPage page0;

	private SHOW_TYPE shownElementsType = SHOW_TYPE.BOTH;

	public SortFieldWizard() {
		super();
		setWindowTitle(Messages.SortFieldWizard_Title);
	}

	@Override
	public void addPages() {
		page0 = new WizardSortFieldPage(jrDataSet, jrSortField, shownElementsType);
		addPage(page0);
	}

	public void setShownElementsType(SHOW_TYPE type) {
		shownElementsType = type;
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	private JRDesignDataset jrDataSet;
	private JRDesignSortField jrSortField;

	public void init(JRDesignDataset jd, JRDesignSortField sortField) {
		this.jrDataSet = jd;
		this.jrSortField = sortField;
	}
}
