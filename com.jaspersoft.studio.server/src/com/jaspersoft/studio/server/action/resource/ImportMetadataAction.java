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
package com.jaspersoft.studio.server.action.resource;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.wizard.imp.ImportMetadataWizard;
import com.jaspersoft.studio.utils.Callback;

/**
 * Action for importing the selected DataSource in the JRS tree as Data Adapter
 * in JSS.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class ImportMetadataAction extends Action {
	public static final String ID = "IMPORT_METADATA_IN_JSS"; //$NON-NLS-1$
	private TreeViewer treeViewer;

	public ImportMetadataAction(TreeViewer treeViewer) {
		super();
		setId(ID);
		setText(Messages.ImportMetadataAction_0);
		setToolTipText(Messages.ImportMetadataAction_1);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(JaspersoftStudioPlugin.PLUGIN_ID, "/icons/resources/eclipse/etool16/import_wiz.gif")); //$NON-NLS-1$
		this.treeViewer = treeViewer;
	}

	@Override
	public boolean isEnabled() {
		boolean en = false;
		TreeSelection selection = (TreeSelection) treeViewer.getSelection();
		Object firstElement = selection.getFirstElement();
		if (firstElement != null) {
			MServerProfile msp = null;
			if (firstElement instanceof MServerProfile)
				msp = (MServerProfile) firstElement;
			else if (firstElement instanceof MResource) {
				INode n = ((MResource) firstElement).getRoot();
				if (n instanceof MServerProfile)
					msp = (MServerProfile) n;
			}
			try {
				IConnection c = msp.getWsClient(new Callback<IConnection>() {

					@Override
					public void completed(IConnection c) {
						ImportMetadataAction.this.setEnabled(c != null && c.isSupported(Feature.IMPORTMETADATA));
					}
				});
				if (c != null)
					en = msp != null && c.isSupported(Feature.IMPORTMETADATA);
			} catch (Exception e) {
				en = false;
			}
		}
		setEnabled(en);
		return en;
	}

	@Override
	public void run() {
		IConnection conn = null;

		TreeSelection selection = (TreeSelection) treeViewer.getSelection();
		Object firstElement = selection.getFirstElement();
		if (firstElement != null) {
			if (firstElement instanceof MServerProfile)
				conn = ((MServerProfile) firstElement).getWsClient();
			else if (firstElement instanceof MResource)
				conn = ((MResource) firstElement).getWsClient();
		}
		if (conn != null) {
			ImportMetadataWizard wizard = new ImportMetadataWizard(conn);
			WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
			dialog.create();
			if (dialog.open() == Dialog.OK) {
			}
		}
	}

}
