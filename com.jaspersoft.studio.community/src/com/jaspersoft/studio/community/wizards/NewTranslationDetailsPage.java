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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.community.CommunityConstants;
import com.jaspersoft.studio.community.JSSCommunityActivator;
import com.jaspersoft.studio.community.issues.IssueField;
import com.jaspersoft.studio.community.issues.enums.Category;
import com.jaspersoft.studio.community.issues.enums.Priority;
import com.jaspersoft.studio.community.issues.enums.Reproducibility;
import com.jaspersoft.studio.community.issues.enums.Resolution;
import com.jaspersoft.studio.community.issues.enums.Severity;
import com.jaspersoft.studio.community.issues.enums.Status;
import com.jaspersoft.studio.community.messages.Messages;
import com.jaspersoft.studio.community.requests.IssueRequest;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Page used the define the details of a published translation, the user could 
 * specify a name and a description for the thread into the community site. 
 * But all the other option are fixed and hidden with this values:
 * Category: Enhancement
 * Priority: Normal
 * Severity: Tweak
 * Reproducibility: Always
 * Status: New
 * 
 * @author Orlandin Marco
 *
 */
public class NewTranslationDetailsPage extends JSSHelpWizardPage {
	private static final int SHELL_INITIAL_HEIGHT = 470;
	private static final int SHELL_INITIAL_WIDTH = 610;
	private Text title;
	private Text description;
	
	/**
	 * Create the wizard.
	 */
	public NewTranslationDetailsPage() {
		super("issueDetailsWizardPage"); //$NON-NLS-1$
		setImageDescriptor(
				JSSCommunityActivator.getDefault().getImageDescriptor(CommunityConstants.ISSUE_SUBMISSION_WIZARD_IMG));
		setTitle(Messages.NewIssueDetailsPage_Title);
		setDescription(Messages.NewIssueDetailsPage_Description);
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_ISSUE_DETAIL;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		GridLayout glContainer = new GridLayout(4, false);
		glContainer.verticalSpacing = 10;
		glContainer.horizontalSpacing = 10;
		container.setLayout(glContainer);
		
		Label lblTitle = new Label(container, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTitle.setText(Messages.NewIssueDetailsPage_IssueTitle);
		
		title = new Text(container, SWT.BORDER);
		title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		title.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				checkPageComplete();
			}
		});
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText(Messages.NewIssueDetailsPage_IssueDescription);
		
		description = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		GridData gdDescription = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
		gdDescription.heightHint = 150;
		description.setLayoutData(gdDescription);
		description.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				checkPageComplete();
			}
		});
		
		// Resize and center shell
		UIUtils.resizeAndCenterShell(getShell(), SHELL_INITIAL_WIDTH, SHELL_INITIAL_HEIGHT);
		
		setPageComplete(false);
	}

	/**
	 * @return the issue request data
	 */
	public IssueRequest getIssueRequest(){
		IssueRequest issue = new IssueRequest(title.getText(), description.getText());
		issue.setCategory(new IssueField(Category.FIELD_NAME, Category.Enhancement.getValue()));
		issue.setPriority(new IssueField(Priority.FIELD_NAME, Priority.Normal.getStringValue()));
		issue.setSeverity(new IssueField(Severity.FIELD_NAME, Severity.Tweak.getStringValue()));
		issue.setReproducibility(new IssueField(Reproducibility.FIELD_NAME, Reproducibility.Always.getStringValue()));
		issue.setResolution(new IssueField(Resolution.FIELD_NAME,Resolution.Open.getStringValue()));
		issue.setStatus(new IssueField(Status.FIELD_NAME,Status.New.getStringValue()));
		issue.setProject(new IssueField("field_bug_project", String.valueOf(CommunityConstants.JSSPROJECT_COMMUNITY_ID)){ //$NON-NLS-1$
			@Override
			protected String getValueAttributeName() {
				return "target_id"; //$NON-NLS-1$
			}
		});
		
		return issue;
	}
	
	/*
	 * Checks for wizard page completion.
	 */
	private void checkPageComplete() {
		// To be complete all the fields must be set
		boolean pageComplete = 
				!title.getText().isEmpty() && 
				!description.getText().isEmpty();
		setPageComplete(pageComplete);
	}
}
