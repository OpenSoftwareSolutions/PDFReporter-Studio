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
package com.jaspersoft.studio.server.action.server;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.MServers;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.wizard.ServerProfileWizard;
import com.jaspersoft.studio.server.wizard.ServerProfileWizardDialog;
import com.jaspersoft.studio.utils.BrandingInfo;

public class CreateServerAction extends Action implements ICheatSheetAction {
	public static final String ID = "createServerAction"; //$NON-NLS-1$
	private TreeViewer treeViewer;

	public CreateServerAction() {
		this(null);
	}

	public CreateServerAction(TreeViewer treeViewer) {
		super();
		setId(ID);
		setText(Messages.CreateServerAction_title);
		setDescription(Messages.CreateServerAction_desc);
		setToolTipText(Messages.CreateServerAction_desc);
		setImageDescriptor(Activator.getDefault().getImageDescriptor("icons/server--plus.png")); //$NON-NLS-1$
		this.treeViewer = treeViewer;
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Override
	public void run() {
		MRoot root = (MRoot) treeViewer.getInput();
		List<INode> lst = root.getChildren();
		for (INode n : lst) {
			if (n instanceof MServers) {
				ServerProfile srv = new ServerProfile();
				srv.setName(getJRSProposedName());
				srv.setUrl(getJRSProposedURL());
				srv.setUser("username"); //$NON-NLS-1$
				srv.setSupportsDateRanges(true);
				ServerProfileWizard wizard = new ServerProfileWizard(new MServerProfile(null, srv));
				ServerProfileWizardDialog dialog = new ServerProfileWizardDialog(UIUtils.getShell(), wizard);
				wizard.bindTestButton(dialog);
				dialog.create();
				if (dialog.open() == Dialog.OK) {
					mservprof = wizard.getServerProfile();
					MServerProfile newprofile = new MServerProfile((MServers) n, mservprof.getValue());
					newprofile.setWsClient(mservprof.getWsClient());
					// for (INode cn : mservprof.getChildren())
					// newprofile.addChild((ANode) cn);
					// try {
					// newprofile.setWsClient(mservprof.getWsClient());
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					ServerManager.addServerProfile(newprofile);

					EditServerAction.fillServerProfile(newprofile, treeViewer);
				}
				break;
			}
		}

	}

	private MServerProfile mservprof;

	public MServerProfile getNewServer() {
		return mservprof;
	}

	public void run(String[] params, ICheatSheetManager manager) {
		run();
		notifyResult(true);
	}

	private String getJRSProposedURL() {
		if (BrandingInfo.isProfessionalEdition()) {
			return "http://localhost:8080/jasperserver-pro/"; //$NON-NLS-1$
		} else {
			return "http://localhost:8080/jasperserver/"; //$NON-NLS-1$
		}
	}

	private String getJRSProposedName() {
		if (BrandingInfo.isProfessionalEdition()) {
			return Messages.CreateServerAction_name + " Pro"; //$NON-NLS-1$
		} else {
			return Messages.CreateServerAction_name;
		}
	}

}
