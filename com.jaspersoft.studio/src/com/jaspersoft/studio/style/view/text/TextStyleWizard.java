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
package com.jaspersoft.studio.style.view.text;

import com.jaspersoft.studio.wizards.JSSWizard;

/**
 * A wizard only with the step to define the text style
 * 
 * @author Orlandin Marco
 *
 */
public class TextStyleWizard extends JSSWizard {

	/**
	 * The layout step of the wizard
	 */
	private TextStyleWizardPage layoutPage;
	
	/**
	 * Create the wizard
	 * @param showTitle True if the dialog should have the title field
	 * @param templateToOpen open the dialog in edit mode, with the field set at the value 
	 * of this template. Pass null if you don't want to edit an existing template
	 */
	public TextStyleWizard(boolean showTitle, TextStyle templateToOpen) {
		super();
		setWindowTitle("Define the Text Styles Attributes");
		setNeedsProgressMonitor(true);
		layoutPage = new TextStyleWizardPage("textStylePage", templateToOpen);
	}
	
	public TextStyleWizard() {
		this(false, null);
	}

	/**
	 * Return the TableStyle selected in the layout step
	 * 
	 * @return a TableStyle
	 */
	public TextStyle getTableStyle(){
		return layoutPage.getStyle();
	}
	
	@Override
	public void addPages() {
		addPage(layoutPage);
		//Hide the Next and Previous buttons
		setForcePreviousAndNextButtons(false);
	}
}
