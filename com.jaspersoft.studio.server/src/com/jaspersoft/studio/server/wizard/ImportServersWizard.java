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
package com.jaspersoft.studio.server.wizard;

import java.util.List;
import java.util.UUID;

import net.sf.jasperreports.eclipse.util.SecureStorageUtils;

import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.wizard.ListInstallationPage;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.repository.RepositoryView;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.action.server.EditServerAction;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.MServers;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.secret.JRServerSecretsProvider;
import com.jaspersoft.studio.server.wizard.pages.ShowServersPage;

/**
 * Wizard to import one of more connections to JRS from previous installations
 * of iReport
 * 
 * @author Orlandin Marco
 * 
 */
public class ImportServersWizard extends Wizard implements IImportWizard {

	/**
	 * Page that list the ireport installations
	 */
	ListInstallationPage page0 = new ListInstallationPage();

	/**
	 * Page that list the availabel connection into a precise configurations
	 */
	ShowServersPage page1 = new ShowServersPage();

	@Override
	public void addPages() {
		addPage(page0);
		addPage(page1);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	private RepositoryView getRepositoryView() {
		return (RepositoryView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.jaspersoft.studio.Repository"); //$NON-NLS-1$
	}

	/**
	 * Get the ServerProfile for the connections selected into the page 1 and add
	 * them to the configuration, and also to the treeview of the repository
	 * explorer, if this view is opened
	 */
	@Override
	public boolean performFinish() {

		// Get the treeview and the MServers node from the repository view if it is
		// available
		RepositoryView view = getRepositoryView();
		TreeViewer treeViewer = null;
		MServers serversNode = null;
		if (view != null) {
			treeViewer = view.getTreeViewer();
			MRoot root = (MRoot) treeViewer.getInput();
			List<INode> lst = root.getChildren();
			for (INode n : lst) {
				if (n instanceof MServers) {
					serversNode = (MServers) n;
					break;
				}
			}
		}

		// Create every server and if the repository view is open add also the nodes
		// to the tree view
		List<ServerProfile> servers = page1.getSelectedServers();
		for (ServerProfile srv : servers) {
			srv.setPass(getPasswordValue(srv.getPass()));
			MServerProfile mservprof = new MServerProfile(null, srv);
			if (serversNode == null)
				ServerManager.addServerProfile(mservprof);
			else {
				MServerProfile newprofile = new MServerProfile(serversNode, mservprof.getValue());
				// for (INode cn : mservprof.getChildren())
				// newprofile.addChild((ANode) cn);
				// try {
				newprofile.setWsClient(null);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				ServerManager.addServerProfile(newprofile);
				EditServerAction.fillServerProfile(newprofile, treeViewer);
			}
		}

		/*
		 * List<ServerProfile> servers = page1.getSelectedAdapter();
		 * for(ServerProfile srv : servers){ MServerProfile mservprof = new
		 * MServerProfile(null, srv); ServerManager.addServerProfile(mservprof);
		 * RepositoryView view = getRepositoryView(); if (view != null)
		 * EditServerAction.fillServerProfile(mservprof, view.getTreeViewer()); }
		 */

		return true;
	}

	/*
	 * Gets the secret storage key or the plain text password value.
	 */
	private String getPasswordValue(String passwordFieldTxt) {
		return JaspersoftStudioPlugin.shouldUseSecureStorage() ? getSecretStorageKey(passwordFieldTxt) : passwordFieldTxt;
	}

	/*
	 * Returns the key that will be used to retrieve the information from the
	 * secure preferences.
	 */
	private String getSecretStorageKey(String pass) {
		try {
			UUID uuidKey = UUID.randomUUID();
			SecureStorageUtils.saveToDefaultSecurePreferences(JRServerSecretsProvider.SECRET_NODE_ID, uuidKey.toString(), pass);
			return uuidKey.toString();
		} catch (StorageException e) {
			Activator.getDefault().logError(Messages.Common_ErrSecurePrefStorage, e);
		}
		;
		// in case something goes wrong return the clear-text password
		// we will rely on back-compatibility
		return pass;
	}
}
