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
package com.jaspersoft.studio.wizards.obj2text;

import net.sf.jasperreports.engine.type.CalculationEnum;

import org.eclipse.jface.wizard.Wizard;

public class Obj2TextWizard extends Wizard {
	private CalculationEnum calculation;
	private String[] names;
	private Obj2TextPage step1;

	public Obj2TextWizard(String[] names) {
		super();
		this.names = names;
		setWindowTitle("TextField Wizard");
	}

	public CalculationEnum getCalculation() {
		return calculation;
	}

	@Override
	public void addPages() {
		step1 = new Obj2TextPage(names);
		addPage(step1);
	}

	@Override
	public boolean performFinish() {
		calculation = step1.getCalculation();
		return true;
	}

}
