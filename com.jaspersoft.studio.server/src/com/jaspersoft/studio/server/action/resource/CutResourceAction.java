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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;

public class CutResourceAction extends Action {
	private TreeViewer treeViewer;

	public CutResourceAction(TreeViewer treeViewer) {
		super();
		setId(ActionFactory.CUT.getId());
		setText(Messages.common_cut);
		setToolTipText(Messages.common_cut);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		this.treeViewer = treeViewer;
	}

	@Override
	public boolean isEnabled() {
		Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		boolean b = firstElement != null && (firstElement instanceof MResource);
		if (b) {
			MResource mres = (MResource) firstElement;
			int pmask = mres.getValue().getPermissionMask(mres.getWsClient());
			b = b && (pmask == 1 || ((pmask & 2) == 2 && (pmask & 16) == 16));
		}
		return b;
	}

	@Override
	public void run() {
		TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		List<MResource> rlist = new ArrayList<MResource>();
		for (int i = 0; i < p.length; i++) {
			final Object obj = p[i].getLastSegment();
			if (obj instanceof MResource) {
				System.out.println(((MResource) obj).getToolTip());
				((MResource) obj).setCut(true);
				rlist.add((MResource) obj);
			}
		}
		if (!rlist.isEmpty())
			Clipboard.getDefault().setContents(rlist);
	}
}
