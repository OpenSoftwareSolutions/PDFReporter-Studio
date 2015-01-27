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
package com.jaspersoft.studio.doc.handlers;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.action.server.EditServerAction;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.MServers;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.wizard.ServerProfileWizard;
import com.jaspersoft.studio.server.wizard.ServerProfileWizardDialog;

public class CreateServerCheatAction extends AsyncAction {

	private ServerProfileWizard wizardDialog;

	private INode selectedElement;

	@Override
	protected void loadDialog() {
		TreeViewer treeViewer = HandlersUtil.getRepositoryView().getTreeViewer();
		MRoot root = (MRoot) treeViewer.getInput();
		List<INode> lst = root.getChildren();
		for (INode n : lst) {
			if (n instanceof MServers) {
				ServerProfile srv = new ServerProfile();
				srv.setName("JasperReports Server");
				srv.setUrl("http://localhost:8080/jasperserver/services/repository");
				srv.setUser("username");
				ServerProfileWizard wizard = new ServerProfileWizard(new MServerProfile(null, srv));
				ServerProfileWizardDialog dialog = new ServerProfileWizardDialog(Display.getDefault().getActiveShell(), wizard);
				wizard.bindTestButton(dialog);
				dialog.create();
				dialogToOpen = dialog;
				wizardDialog = wizard;
				selectedElement = n;
				break;
			}
		}
	}

	public void run() {
		TreeViewer treeViewer = HandlersUtil.getRepositoryView().getTreeViewer();
		MRoot root = (MRoot) treeViewer.getInput();
		List<INode> lst = root.getChildren();
		for (INode n : lst) {
			if (n instanceof MServers) {
				ServerProfile srv = new ServerProfile();
				srv.setName("JasperReports Server");
				srv.setUrl("http://localhost:8080/jasperserver/services/repository");
				srv.setUser("username");
				ServerProfileWizard wizard = new ServerProfileWizard(new MServerProfile(null, srv));
				ServerProfileWizardDialog dialog = new ServerProfileWizardDialog(UIUtils.getShell(), wizard);
				wizard.bindTestButton(dialog);
				dialog.create();
				if (dialog.open() == Dialog.OK) {
					MServerProfile mservprof = wizard.getServerProfile();
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
			}
		}
	}

	@Override
	public void doAction() {
		TreeViewer treeViewer = HandlersUtil.getRepositoryView().getTreeViewer();
		if (dialogToOpen.open() == Dialog.OK) {
			MServerProfile mservprof = wizardDialog.getServerProfile();
			MServerProfile newprofile = new MServerProfile((MServers) selectedElement, mservprof.getValue());
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
	}
}
