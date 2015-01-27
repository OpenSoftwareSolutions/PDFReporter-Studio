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
package com.jaspersoft.studio.model.textfield.command.wizard;

import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.eclipse.jface.wizard.Wizard;

public class PercentageWizard extends Wizard {

	private PercentagePage step1;

	public PercentageWizard() {
		super();
		setWindowTitle("Percentage configurator");
	}

	@Override
	public void addPages() {
		step1 = new PercentagePage();
		addPage(step1);
		step1.init(jasperDesign);
	}

	public JRCloneable getField() {
		return step1.getField();
	}

	public ResetTypeEnum getResetType() {
		return step1.getResetType();
	}

	public JRGroup getGroup() {
		return step1.getGroup();
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	private JasperDesign jasperDesign;

	public void init(JasperDesign jd) {
		this.jasperDesign = jd;
	}
}
