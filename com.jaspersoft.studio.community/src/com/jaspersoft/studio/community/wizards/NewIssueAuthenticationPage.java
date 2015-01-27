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
package com.jaspersoft.studio.community.wizards;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.community.CommunityConstants;
import com.jaspersoft.studio.community.JSSCommunityActivator;
import com.jaspersoft.studio.community.messages.Messages;
import com.jaspersoft.studio.community.utils.CommunityUser;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Wizard page that allows to configure the authentication information when
 * submitting an new issue to the Community tracker.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class NewIssueAuthenticationPage extends JSSHelpWizardPage {
	private CommunityUser communityUserInformation;
	private boolean shouldSaveCredentials;
	
	private Text username;
	private Text password;
	private Button btnReuseStoredCredentials;
	private Button btnStoreCommunityUserCredentials;

	/**
	 * Create the wizard.
	 */
	public NewIssueAuthenticationPage() {
		super("authenticationInfoWizardPage"); //$NON-NLS-1$
		setImageDescriptor(
				JSSCommunityActivator.getDefault().getImageDescriptor(CommunityConstants.ISSUE_SUBMISSION_WIZARD_IMG));
		setTitle(Messages.NewIssueAuthenticationPage_Title);
		setDescription(Messages.NewIssueAuthenticationPage_Description);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		GridLayout glContainer = new GridLayout(2, false);
		glContainer.verticalSpacing = 10;
		glContainer.horizontalSpacing = 10;
		container.setLayout(glContainer);
		
		Link communityLink = new Link(container,SWT.NONE);
		communityLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		communityLink.setText(Messages.NewIssueAuthenticationPage_CredentialsLink);
		communityLink.addSelectionListener(new SelectionAdapter() {
			@Override	
			public void widgetSelected(SelectionEvent e) {
				Program.launch(CommunityConstants.NEW_COMMUNITY_TRACKER_ACCOUNT_URL);
			}
		});
		
		Label lblUsername = new Label(container, SWT.NONE);
		lblUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsername.setText(Messages.NewIssueAuthenticationPage_Username);
		
		username = new Text(container, SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		username.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				checkForPageComplete();
			}
		});
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText(Messages.NewIssueAuthenticationPage_Password);
		
		password = new Text(container, SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		password.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				checkForPageComplete();
			}
		});
		
		btnReuseStoredCredentials = new Button(container, SWT.CHECK);
		btnReuseStoredCredentials.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnReuseStoredCredentials.setText(Messages.NewIssueAuthenticationPage_ReuseCredentialsCheckbox);
		btnReuseStoredCredentials.setToolTipText(Messages.NewIssueAuthenticationPage_ReuseCredentialsCheckboxTooltip);
		btnReuseStoredCredentials.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reuseCredentialsSelected();
			}
		});
		
		btnStoreCommunityUserCredentials = new Button(container, SWT.CHECK);
		btnStoreCommunityUserCredentials.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnStoreCommunityUserCredentials.setText(Messages.NewIssueAuthenticationPage_StoreUserCredentialsCheckbox);
		btnStoreCommunityUserCredentials.setToolTipText(Messages.NewIssueAuthenticationPage_StoreUserCredentialsCheckboxTooltip);
		btnStoreCommunityUserCredentials.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				shouldSaveCredentials = 
						btnStoreCommunityUserCredentials.isEnabled() && btnStoreCommunityUserCredentials.getSelection();
				checkForPageComplete();
			}
		});

		setPageComplete(false);
		
		// Check for existing credentials
		if(JSSCommunityActivator.getDefault().getCommunityUserInformation() == null){
			btnReuseStoredCredentials.setEnabled(false);
		}
		else{
			// preselect the re-use of credentials
			btnReuseStoredCredentials.setSelection(true);
			// need to force the method invocation
			reuseCredentialsSelected();
		}
		
	}
	
	/*
	 * Invoked when the "Re-use stored credentials" checkbox is selected.
	 */
	private void reuseCredentialsSelected() {
		if(btnReuseStoredCredentials.getSelection()){
			btnStoreCommunityUserCredentials.setEnabled(false);
			btnStoreCommunityUserCredentials.setSelection(false);
			communityUserInformation = JSSCommunityActivator.getDefault().getCommunityUserInformation();
			if(communityUserInformation==null){
				MessageDialog.openWarning(UIUtils.getShell(), Messages.NewIssueAuthenticationPage_WarningDialogTitle, Messages.NewIssueAuthenticationPage_WarningDialogMsg);
				btnReuseStoredCredentials.setSelection(false);
				btnStoreCommunityUserCredentials.setEnabled(true);
			}
			else{
				username.setEnabled(false);
				username.setText(communityUserInformation.getUsername());
				password.setEnabled(false);
				password.setText(communityUserInformation.getPassword());
			}
		}
		else {
			btnStoreCommunityUserCredentials.setSelection(false);
			btnStoreCommunityUserCredentials.setEnabled(true);
			username.setEnabled(true);
			password.setEnabled(true);
			communityUserInformation=null;
		}
		checkForPageComplete();
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		// Let's set focus on the username field
		// We avoid the link to have the "selected" effect
		username.setFocus();
	}

	/*
	 * Checks if page is complete.
	 */
	private void checkForPageComplete() {
		boolean isComplete =
				!username.getText().isEmpty() &&
				!password.getText().isEmpty();
		setPageComplete(isComplete);
	}
	
	/**
	 * @return the authentication information for the community user
	 */
	public CommunityUser getCommunityUserInformation(){
		if(this.communityUserInformation==null){
			return new CommunityUser(username.getText(), password.getText());
		}
		return this.communityUserInformation;
	}
	
	/**
	 * @return <code>true</code> if the credentials should be saved in the
	 *         secure preference storage, <code>false</code> otherwise
	 */
	public boolean shouldSaveCredentials(){
		return this.shouldSaveCredentials;
	}

	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_ISSUE_LOGIN;
	}
}
