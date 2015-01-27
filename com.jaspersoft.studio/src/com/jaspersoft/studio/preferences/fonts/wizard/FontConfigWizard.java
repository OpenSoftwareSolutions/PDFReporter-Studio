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
package com.jaspersoft.studio.preferences.fonts.wizard;

import net.sf.jasperreports.engine.fonts.FontFamily;

import org.eclipse.jface.wizard.Wizard;

public class FontConfigWizard extends Wizard {
	private FontFamily font;

	private FontFamilyPage step1;
	private FontMappingPage step2;
	private FontLocalesPage step3;

	public FontConfigWizard() {
		super();
		setWindowTitle("Font Family");
	}

	public FontFamily getFont() {
		return font;
	}

	public void setFont(FontFamily font) {
		this.font = font;
	}

	@Override
	public void addPages() {
		step1 = new FontFamilyPage(font);
		addPage(step1);

		step2 = new FontMappingPage(font);
		addPage(step2);

		step3 = new FontLocalesPage(font);
		addPage(step3);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
