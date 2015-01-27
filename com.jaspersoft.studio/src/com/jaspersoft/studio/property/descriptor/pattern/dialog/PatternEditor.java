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
package com.jaspersoft.studio.property.descriptor.pattern.dialog;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.messages.Messages;

public class PatternEditor extends Wizard {
	private String value;
	private PatternPage page0;

	public String getValue() {
		if (page0 != null)
			return page0.getValue();
		return value;
	}

	public void setValue(String value) {
		if (page0 != null)
			page0.setValue(value);
		this.value = value;
	}

	public PatternEditor() {
		super();
		setWindowTitle(Messages.common_pattern);
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		page0 = new PatternPage(Messages.common_pattern);
		page0.setValue(value);
		page0.setDatePatterns(datePatterns);
		page0.setNumberPatterns(numberPatterns);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	private boolean datePatterns = true;
	private boolean numberPatterns = true;

	public boolean isDatePatterns() {
		return datePatterns;
	}

	public void setDatePatterns(boolean datePatterns) {
		this.datePatterns = datePatterns;
	}

	public boolean isNumberPatterns() {
		return numberPatterns;
	}

	public void setNumberPatterns(boolean numberPatterns) {
		this.numberPatterns = numberPatterns;
	}
}
