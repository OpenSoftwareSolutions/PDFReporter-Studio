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
package com.jaspersoft.studio.model.image.command.wizard;

import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.image.MImage;

public class ImageWizard extends Wizard {
	private MImage mimage;
	private WizardImagePage page5;

	public ImageWizard() {
		super();
		setWindowTitle(Messages.common_image);
	}

	@Override
	public void addPages() {

		mimage = new MImage(null, new JRDesignImage(jasperDesign), -1);

		page5 = new WizardImagePage();
		addPage(page5);
		page5.setMImage(mimage);
	}

	public MImage getImage() {
		return mimage;
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
