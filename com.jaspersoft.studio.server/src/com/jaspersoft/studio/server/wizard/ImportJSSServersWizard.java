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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.data.wizard.ListInstallationPage;
import com.jaspersoft.studio.data.wizard.SelectWorkspacePage;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.repository.RepositoryView;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.action.server.EditServerAction;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.MServers;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.wizard.pages.ShowServersPage;

/**
 * Wizard to import one of more connections to JRS from other workspaces of JSS
 * 
 * @author Orlandin Marco
 * 
 */
public class ImportJSSServersWizard extends Wizard implements IImportWizard {

	/**
	 * Page that allow to select a workspace with inside a JSS configuration
	 * folder
	 */
	ListInstallationPage page0 = new SelectWorkspacePage();

	/**
	 * Page that list the available connection into a precise configurations
	 */
	ListJSSServer page1 = new ListJSSServer();

	/**
	 * Extends ShowServersPage and redefine the createCheckBoxData to build a
	 * ServerProfile list from a JSS configuration file
	 * 
	 * @author Orlandin Marco
	 * 
	 */
	private class ListJSSServer extends ShowServersPage {

		protected List<ServerProfile> createCheckBoxData(Properties prop) {
			List<ServerProfile> result = new ArrayList<ServerProfile>();
			String xmlString = prop.getProperty("serverprofiles");
			if (xmlString == null)
				return result;

			Document document;
			try {
				document = JRXmlUtils.parse(new InputSource(new StringReader(xmlString)));
				Node actualNode = document.getFirstChild();
				if (actualNode.hasChildNodes())
					actualNode = actualNode.getFirstChild();
				else
					actualNode = null;
				while (actualNode != null) {
					if (actualNode.getNodeName().equals("serverProfile")) {
						Node child = actualNode.getFirstChild();
						ServerProfile srv = new ServerProfile();
						while (child != null) {
							if (child.getNodeName().equals("name"))
								srv.setName(child.getTextContent());
							if (child.getNodeName().equals("jrVersion"))
								srv.setJrVersion(child.getTextContent());
							if (child.getNodeName().equals("url")) {
								String connectionString = child.getTextContent();
								if (connectionString.endsWith("/services/repository")) { //$NON-NLS-1$
									connectionString = connectionString.substring(0, connectionString.lastIndexOf("services/repository")); //$NON-NLS-1$
								}
								srv.setUrl(connectionString);
							}
							if (child.getNodeName().equals("user"))
								srv.setUser(child.getTextContent());
							if (child.getNodeName().equals("pass"))
								srv.setPass(child.getTextContent());
							if (child.getNodeName().equals("supportsDateRanges"))
								srv.setSupportsDateRanges(child.getTextContent().equals("true"));
							if (child.getNodeName().equals("chunked"))
								srv.setChunked(child.getTextContent().equals("true"));
							if (child.getNodeName().equals("timeout"))
								srv.setTimeout(Integer.parseInt(child.getTextContent()));
							child = child.getNextSibling();
						}
						result.add(srv);
					}
					actualNode = actualNode.getNextSibling();
				}
			} catch (JRException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	@Override
	public void addPages() {
		addPage(page0);
		addPage(page1);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	private RepositoryView getRepositoryView() {
		return (RepositoryView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.jaspersoft.studio.Repository");
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
		return true;
	}

}
