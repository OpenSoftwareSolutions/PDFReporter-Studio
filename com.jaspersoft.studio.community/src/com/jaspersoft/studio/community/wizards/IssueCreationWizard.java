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
import com.jaspersoft.studio.community.utils.CommunityAPIUtils;
import com.jaspersoft.studio.community.utils.CommunityUser;
import com.jaspersoft.studio.community.zip.ZipEntry;

/**
 * Wizard for the issue creation.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public final class IssueCreationWizard extends Wizard {
	
	private static IssueCreationWizard instance = null;
	private NewIssueDetailsPage page1;
	private IssueAttachmentDetailsPage page2;
	private NewIssueAuthenticationPage page3;
	private boolean isPublished;
	private String issuePath;

	private IssueCreationWizard() {
		setWindowTitle(Messages.IssueCreationWizard_Title);
	}

	@Override
	public void addPages() {
		page1 = new NewIssueDetailsPage();
		addPage(page1);
		page2 = new IssueAttachmentDetailsPage();
		addPage(page2);
		page3 = new NewIssueAuthenticationPage();
		addPage(page3);
	}

	@Override
	public boolean performFinish() {
		// List of entries for the final zip attachment
		final List<ZipEntry> zipEntries = page2.getZipEntries();
		// Issue request that still needs the attachment file id
		final IssueRequest issueRequest = page1.getIssueRequest();
		// Authentication information
		final CommunityUser authInfo = page3.getCommunityUserInformation();
		// Let's save credentials if required		
		if(page3.shouldSaveCredentials()){
			JSSCommunityActivator.getDefault().storeCommunityUserInformation(authInfo);
		}
		// Tries to save issue
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					monitor.beginTask(Messages.IssueCreationWizard_TaskName, IProgressMonitor.UNKNOWN);
					isPublished = publishNewIssue(issueRequest,zipEntries,authInfo);
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

	private boolean publishNewIssue(IssueRequest issueRequest,
			List<ZipEntry> zipEntries, CommunityUser authInfo) {
		HttpClient client = new HttpClient();
		try {
			// Gets the authentication cookie
			Cookie authCookie = 
					RESTCommunityHelper.getAuthenticationCookie(client, authInfo.getUsername(), authInfo.getPassword());
			
			// Create the attachment file if any
			List<String> attachmentsIDs = new ArrayList<String>();
			if(!zipEntries.isEmpty()){
				File zipAttachment = CommunityAPIUtils.createZipFile(zipEntries);
				String fileID = RESTCommunityHelper.uploadFile(client, zipAttachment, authCookie);
				attachmentsIDs.add(fileID);
			}
			
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

	/**
	 * Static method for creating the {@link IssueCreationWizard} instance.
	 * 
	 * @return the wizard instance newly created, <code>null</code> if an
	 *         instance already exists
	 */
	public static synchronized IssueCreationWizard createWizard() {
		if(instance==null){
			instance = new IssueCreationWizard();
			return instance;
		}
		else {
			return null;
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		instance = null;
	}
}
