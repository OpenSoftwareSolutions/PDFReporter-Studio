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
package com.jaspersoft.studio.components.barcode.command;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.components.barcode.model.MBarcode;

public class BarcodeWizard extends Wizard {
	private BarcodeWizardPage page0;
	private MBarcode barcode;

	public BarcodeWizard() {
		super();
		setWindowTitle(Messages.common_barcode_wizard);
	}

	@Override
	public void addPages() {
		page0 = new BarcodeWizardPage();
		addPage(page0);
	}

	public MBarcode getBarcode() {
		if (page0 != null)
			return page0.getBarcode();
		return barcode;
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
