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
package com.jaspersoft.studio.data.actions;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.actions.ActionFactory;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.MDataAdapter;
import com.jaspersoft.studio.data.MDataAdapters;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.messages.Messages;

public class DeleteDataAdapterAction extends Action {
	private TreeViewer treeViewer;

	public DeleteDataAdapterAction(TreeViewer treeViewer) {
		super();
		setId(ActionFactory.DELETE.getId());
		this.treeViewer = treeViewer;
		setText(Messages.DeleteDataAdapterAction_deleteName);
		setDescription(Messages.DeleteDataAdapterAction_deleteDescription);
		setToolTipText(Messages.DeleteDataAdapterAction_deleteTooltip);
		setImageDescriptor(
				JaspersoftStudioPlugin.getInstance().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE)); //$NON-NLS-1$
		setDisabledImageDescriptor(
				JaspersoftStudioPlugin.getInstance().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE)); //$NON-NLS-1

	}

	@Override
	public boolean isEnabled() {
		Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		return firstElement != null && (firstElement instanceof MDataAdapter);
	}

	@Override
	public void run() {
		TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();

		if (!UIUtils.showDeleteConfirmation())
			return;
		ADataAdapterStorage storage = null;
		for (int i = 0; i < p.length; i++) {
			Object obj = p[i].getLastSegment();
			if (obj instanceof MDataAdapter) {
				MDataAdapter mDataAdapter = (MDataAdapter) obj;
				DataAdapterDescriptor m = mDataAdapter.getValue();
				if (storage == null)
					storage = ((MDataAdapters) mDataAdapter.getParent()).getValue();
				if (storage != null)
					storage.removeDataAdapter(m);
				treeViewer.refresh(true);
			}
		}
	}
}
