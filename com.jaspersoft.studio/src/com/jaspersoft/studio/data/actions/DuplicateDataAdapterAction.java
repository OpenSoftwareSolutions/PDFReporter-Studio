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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.MDataAdapter;
import com.jaspersoft.studio.data.MDataAdapters;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.INode;

public class DuplicateDataAdapterAction extends Action {
	public static final String COPY_OF = Messages.DuplicateDataAdapterAction_newAdapterPrefix;
	public static final String ID = "duplicatedataAdapteraction"; //$NON-NLS-1$
	private TreeViewer treeViewer;
	private EditDataAdapterAction editAction;

	public DuplicateDataAdapterAction(TreeViewer treeViewer) {
		super();
		this.treeViewer = treeViewer;
		setId(ID);
		setText(Messages.DuplicateDataAdapterAction_duplicateName);
		setDescription(Messages.DuplicateDataAdapterAction_duplicateDescription);
		setToolTipText(Messages.DuplicateDataAdapterAction_duplicateToolTip);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(ISharedImages.IMG_TOOL_COPY)); //$NON-NLS-1$
		setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor(ISharedImages.IMG_TOOL_COPY)); //$NON-NLS-1
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
		List<DataAdapterDescriptor> copies = new ArrayList<DataAdapterDescriptor>();
		MDataAdapters mdas = null;
		for (int i = 0; i < p.length; i++) {
			Object obj = p[i].getLastSegment();
			if (obj instanceof MDataAdapter) {
				MDataAdapter mDataAdapter = (MDataAdapter) obj;
				mdas = (MDataAdapters) mDataAdapter.getParent();
				ADataAdapterStorage storage = ((MDataAdapters) mDataAdapter.getParent()).getValue();
				DataAdapterDescriptor copyDataAdapter = DataAdapterManager.cloneDataAdapter(mDataAdapter.getValue());
				String name = COPY_OF + copyDataAdapter.getName();
				for (int j = 1; j < 1000; j++) {
					if (storage.isDataAdapterNameValid(name))
						break;
					name = COPY_OF + copyDataAdapter.getName() + j;
				}
				copyDataAdapter.getDataAdapter().setName(name);
				storage.addDataAdapter("", copyDataAdapter); //$NON-NLS-1$
				copies.add(copyDataAdapter);
			}
		}
		if (!copies.isEmpty()) {
			treeViewer.refresh(true);
			if (copies.size() == 1) {
				DataAdapterDescriptor copy = copies.get(0);
				for (INode mDataAdapter : mdas.getChildren())
					if (mDataAdapter.getValue() == copy) {
						treeViewer.setSelection(new StructuredSelection(mDataAdapter));
						treeViewer.reveal(mDataAdapter);
						runEditAction();
						break;
					}
			}
		}
	}

	private void runEditAction() {
		if (editAction == null)
			editAction = new EditDataAdapterAction(treeViewer);
		editAction.run();
	}
}
