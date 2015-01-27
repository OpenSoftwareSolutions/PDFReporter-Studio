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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.community.JSSCommunityActivator;
import com.jaspersoft.studio.community.RESTCommunityHelper;
import com.jaspersoft.studio.community.messages.Messages;
import com.jaspersoft.studio.community.requests.IssueRequest;
import com.jaspersoft.studio.community.utils.CommunityAPIException;
import com.jaspersoft.studio.community.utils.CommunityUser;

/**
 * Wizard to send a translation project into the community site. The user can specify
 * the name and the description for the new thread, and the login data on the community site.
 * The attachment is fixed and its the translation itself inside a zip file.
 * 
 * @author Orlandin Marco & Massimo Rabbi
 *
 */
public final class TranslationSendWizard extends Wizard {
	private NewTranslationDetailsPage page1;
	private NewIssueAuthenticationPage page2;
	private boolean isPublished;
	private String issuePath;
	private File zipAttachment;

	public TranslationSendWizard(File zipAttachment) {
		setWindowTitle(Messages.IssueCreationWizard_Title);
		this.zipAttachment = zipAttachment;
	}

	@Override
	public void addPages() {
		page1 = new NewTranslationDetailsPage();
		addPage(page1);
		page2 = new NewIssueAuthenticationPage();
		addPage(page2);
	}

	@Override
	public boolean performFinish() {
		// Issue request that still needs the attachment file id
		final IssueRequest issueRequest = page1.getIssueRequest();
		// Authentication information
		final CommunityUser authInfo = page2.getCommunityUserInformation();
		// Let's save credentials if required		
		if(page2.shouldSaveCredentials()){
			JSSCommunityActivator.getDefault().storeCommunityUserInformation(authInfo);
		}
		// Tries to save issue
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					monitor.beginTask(Messages.IssueCreationWizard_TaskName, IProgressMonitor.UNKNOWN);
					isPublished = publishNewIssue(issueRequest,authInfo);
					monitor.done();
					
				}
			});
		} catch (Exception e) {
			UIUtils.showError(e);
		}
		
		if(isPublished){
			new IssueCreatedDialog(
					getShell(), Messages.IssueCreationWizard_InfoDialogTitle, null, 
					Messages.IssueCreationWizard_InfoDialogMessage,
					MessageDialog.INFORMATION,new String[] { IDialogConstants.OK_LABEL }, 0).open();
		}
		
		return isPublished;
	}

	private boolean publishNewIssue(IssueRequest issueRequest, CommunityUser authInfo) {
		HttpClient client = new HttpClient();
		try {
			// Gets the authentication cookie
			Cookie authCookie = 
					RESTCommunityHelper.getAuthenticationCookie(client, authInfo.getUsername(), authInfo.getPassword());
			
			// Create the attachment file if any
			List<String> attachmentsIDs = new ArrayList<String>();
			String fileID = RESTCommunityHelper.uploadFile(client, zipAttachment, authCookie);
			attachmentsIDs.add(fileID);
			
			// Publish the issue to the community tracker
			issuePath = 
					RESTCommunityHelper.createNewIssue(client, issueRequest, attachmentsIDs, authCookie);
						
		} catch (CommunityAPIException e) {
			UIUtils.showError(e);
			return false;
		}
		return true;
	}

	/*
	 * Information dialog that shows a link to the newly created issue on the
	 * community tracker.
	 */
	private class IssueCreatedDialog extends MessageDialog {

		public IssueCreatedDialog(Shell parentShell, String dialogTitle,
				Image dialogTitleImage, String dialogMessage,
				int dialogImageType, String[] dialogButtonLabels,
				int defaultIndex) {
			super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
					dialogImageType, dialogButtonLabels, defaultIndex);
		}
		
		@Override
		protected Control createCustomArea(Composite parent) {
			final StyledText issueLink = new StyledText(parent, SWT.READ_ONLY);
			issueLink.setText(issuePath);
			issueLink.setBackground(parent.getBackground());
			issueLink.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,true,false,2,1));
			
			StyleRange style = new StyleRange();
			style.underline = true;
			style.underlineStyle = SWT.UNDERLINE_LINK;
			int[] ranges = {0, issuePath.length()};
			StyleRange[] styles = {style};
			issueLink.setStyleRanges(ranges, styles);
			
			issueLink.addListener(SWT.MouseDown, new Listener() {
				@Override
				public void handleEvent(Event event) {
					try {
						int offset = issueLink.getOffsetAtLocation(new Point (event.x, event.y));
						StyleRange style = issueLink.getStyleRangeAtOffset(offset);
						if (style != null && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
							Program.launch(issuePath);
						}
					} catch (IllegalArgumentException e) {
						// no character under event.x, event.y
					}
				}
			});
			
			return issueLink;
		}

	}

}
