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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.actions.DuplicateDataAdapterAction;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.ServerProfile;

public class DuplicateServerAction extends Action {
	public static final String ID = "duplicateServerAction"; //$NON-NLS-1$
	private TreeViewer treeViewer;
	private EditServerAction editAction;

	public DuplicateServerAction(TreeViewer treeViewer) {
		super();
		this.treeViewer = treeViewer;
		setId(ID);
		setText(Messages.DuplicateServerAction_title);
		setDescription(Messages.DuplicateServerAction_desc);
		setToolTipText(Messages.DuplicateServerAction_desc);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(ISharedImages.IMG_TOOL_COPY)); //$NON-NLS-1$
		setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(ISharedImages.IMG_TOOL_COPY)); //$NON-NLS-1
	}

	@Override
	public boolean isEnabled() {
		Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		return firstElement != null && (firstElement instanceof MServerProfile);
	}

	@Override
	public void run() {
		TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		List<MServerProfile> copies = new ArrayList<MServerProfile>();
		for (int i = 0; i < p.length; i++) {
			Object obj = p[i].getLastSegment();
			if (obj instanceof MServerProfile) {
				try {
					MServerProfile oldMSP = (MServerProfile) obj;
					ServerProfile copy = (ServerProfile) oldMSP.getValue().clone();
					copy.setName(DuplicateDataAdapterAction.COPY_OF + copy.getName());

					MServerProfile copyDataAdapter = new MServerProfile((ANode) oldMSP.getParent(), copy);
					ServerManager.addServerProfile(copyDataAdapter);
					copies.add(copyDataAdapter);

				} catch (CloneNotSupportedException e) {
					UIUtils.showError(e);
				}

			}
		}
		if (!copies.isEmpty()) {
			treeViewer.refresh(true);
			if (copies.size() == 1) {
				MServerProfile copy = copies.get(0);
				treeViewer.setSelection(new StructuredSelection(copy));
				treeViewer.reveal(copy);
				runEditAction();
			}
		}
	}

	private void runEditAction() {
		if (editAction == null)
			editAction = new EditServerAction(treeViewer);
		editAction.run();
	}
}
