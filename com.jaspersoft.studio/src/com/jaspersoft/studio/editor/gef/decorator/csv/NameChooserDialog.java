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
package com.jaspersoft.studio.editor.gef.decorator.csv;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * A simple dialog to ask a string value to the user
 * 
 * @author Orlandin Marco
 *
 */
public class NameChooserDialog extends Dialog {
	
	/**
	 * The textfield where the string is typed
	 */
	private Text columnName;
	
	/**
	 * Contain the value of inserted in the textfield after the button "ok" is pressed. 
	 * This field is used because after the button ok is pressed then all the widget in the dialog 
	 * are automatically disposed, and so the content of the text field need to be saved here
	 */
	private String choosenName;
	
	/**
	 * title of the dialog
	 */
	private String dialogName;
	
	
	public NameChooserDialog(Shell parentShell, String dialogName) {
		super(parentShell);
		choosenName = "";
		this.dialogName = dialogName;
	}
	
	/**
	 * Build the dialog with a title and an initial value for the text field
	 * @param parentShell
	 * @param dialogName
	 * @param textInitialValue
	 */
	public NameChooserDialog(Shell parentShell, String dialogName, String textInitialValue) {
		this(parentShell, dialogName);
		choosenName = textInitialValue;
	}
	
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(getDialogTitle());
	}

	/**
	 * @return the title for the dialog
	 */
	protected String getDialogTitle(){
		return dialogName;
	}
	
	/**
	 * Return the value in the text field after the button ok is pressed, or the value used 
	 * to initialize the textfield before the button ok is pressed
	 */
	public String getName(){
		return choosenName;
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		columnName = new Text(container, SWT.BORDER);
		columnName.setText(choosenName);
		GridData textData = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		textData.widthHint = 200;
		columnName.setLayoutData(textData);
		
		return area;
	}
	
	@Override
	protected void okPressed() {
		choosenName = columnName.getText();
		super.okPressed();
	}

}
