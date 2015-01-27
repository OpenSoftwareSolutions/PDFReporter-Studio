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
package com.jaspersoft.studio.data.xmla;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.messages.Messages;

/**
 * Dialog used to authenticate the user when he try to connect
 * to a Xmla endpoint
 * 
 * @author Orlandin Marco
 *
 */
public class AuthenticationDialog extends Dialog{

	/**
	 *  key of the username in the url
	 */
	private static final String usernameKey = "j_username="; //$NON-NLS-1$
	
	/**
	 *  key of the password in the url
	 */
	private static final String passwordKey = "j_password="; //$NON-NLS-1$
	
	/**
	 * Field where the username is typed
	 */
	private Text usernameText;
	
	/**
	 * Field where the password is typed
	 */
	private Text passwordText;
	
	/**
	 * The actual username
	 */
	private String username = ""; //$NON-NLS-1$

	/**
	 * The actual password
	 */
	private String password = ""; //$NON-NLS-1$
	
	/**
	 * Counter to keep trace to the number of attempt used to connect (must
	 * be updated manually)
	 */
	private int authenticationAttempt = 0;

	/**
	 * Flag to set if the operation was aborted by the user
	 */
	private boolean cancelOperation = false;

	/**
	 * Configure the shell to set the defined title if it is not null
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.AuthenticationDialog_title);
	}

    /**
     *  We have the URL.  See if the user name and password are on it.
     *  We find them by looking for j_username= and j_password=.  If they
     *  are found on the line, then extract them and use them as the first
     *  attempt to connect.  If the connect fails using them, then the dialog
     *  box will be displayed requesting a new username/password.
     *  
     * @param parent parent shell
     * @param urlString initial url
     */ 
	public AuthenticationDialog(Shell parent, String urlString) {
		super(parent);
		int userStart = urlString.indexOf(usernameKey);
		int passwordStart = urlString.indexOf(passwordKey);
		if (userStart == -1 || passwordStart == -1) {
			username = ""; //$NON-NLS-1$
			password = ""; //$NON-NLS-1$
		} else {
			userStart += usernameKey.length();
			passwordStart += passwordKey.length();

			// get the user name out of the URL
			int userEnd = urlString.indexOf('&', userStart + 1);
			if (userEnd == -1) {
				username = urlString.substring(userStart);
			} else {
				username = urlString.substring(userStart, userEnd);
			}

			// get the password out of the URL
			int passwordEnd = urlString.indexOf('&', passwordStart + 1);
			if (passwordEnd == -1) {
				password = urlString.substring(passwordStart);
			} else {
				password = urlString.substring(passwordStart, passwordEnd - 1);
			}
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		dialogArea.setLayout(new GridLayout(2, false));
		Label lblUserName = new Label(dialogArea, SWT.NONE);
		lblUserName.setText(Messages.AuthenticationDialog_usernameLbl);
		usernameText = new Text(dialogArea, SWT.BORDER);
		usernameText.setText(username);
		usernameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		usernameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				username = usernameText.getText();
			}
		});

		Label lblPassword = new Label(dialogArea, SWT.NONE);
		lblPassword.setText(Messages.AuthenticationDialog_passwordLbl);
		passwordText = new Text(dialogArea, SWT.BORDER | SWT.PASSWORD);
		passwordText.setText(password);
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		passwordText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				password = passwordText.getText();
			}
		});

		return dialogArea;
	}

	/**
	 * Return if the login operation was aborted
	 * 
	 * @return true if it was aborted, false otherwise
	 */
	public boolean cancelOperation() {
		return cancelOperation;
	}

	/**
	 * Open the dialog, and if the user close it with the cancel
	 * button the login operation is marked like aborted
	 */
	public void openDialog() {
		int returnCode = super.open();
		if (returnCode == Window.CANCEL)
			cancelOperation = true;
	}

	/**
	 * Return the not null actual password
	 * 
	 * @return the password string
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Return the not null actual password
	 * 
	 * @return the password as array of characters
	 */
	public char[] getPasswordCA() {
		return password != null ? password.toCharArray() : null;
	}

	/**
	 * Reset the fields value
	 */
	public void resetFields() {
		username = ""; //$NON-NLS-1$
		password = ""; //$NON-NLS-1$
		cancelOperation = false;
		authenticationAttempt = 0;
	}
	
	/**
	 * Reset the fields value, and initialize the fields username and password
	 * with a specific value
	 * 
	 * @param usernameInit the initial value for the username field, if it is null
	 * the value is considered the empty string
	 * @param passwordInit the initial value for the password field, if it is null
	 * the value is considered the empty string
	 */
	public void resetFields(String usernameInit, String passwordInit){
		username = usernameInit != null ? usernameInit : ""; //$NON-NLS-1$
		password = passwordInit != null ? passwordInit : ""; //$NON-NLS-1$
		cancelOperation = false;
		authenticationAttempt = 0;
	}
	
	/**
	 * Increment the value of authentication attempt by one
	 */
	public void incrementAuthAttempt(){
		authenticationAttempt ++;
	}
	
	/**
	 * Return the value of authentication attempt
	 * @return an integer greater or equal to 0
	 */
	public int getAuthenticationAttempt(){
		return authenticationAttempt;
	}

	/**
	 * Return the actual username
	 * 
	 * @return the username as string
	 */
	public String getUsername() {
		return username;
	}
}
