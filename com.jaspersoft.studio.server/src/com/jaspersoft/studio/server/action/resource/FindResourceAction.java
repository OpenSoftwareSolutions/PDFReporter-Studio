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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.ServerProvider;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.wizard.find.FindResourceJob;
import com.jaspersoft.studio.utils.Callback;

public class FindResourceAction extends Action {
	public static final String ID = "findResourceAction"; //$NON-NLS-1$
	private TreeViewer treeViewer;
	private ServerProvider sp;

	public FindResourceAction(TreeViewer treeViewer, ServerProvider sp) {
		super();
		this.sp = sp;
		setId(ID);
		setText("Search");
		setDescription("Search Resources on Jasperreports Server by name.");
		setToolTipText(getDescription());
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/find_obj.gif")); //$NON-NLS-1$
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
				if (msp != null){
					IConnection c = msp.getWsClient(new Callback<IConnection>() {
	
						@Override
						public void completed(IConnection c) {
							FindResourceAction.this.setEnabled(c != null && c.isSupported(Feature.SEARCHREPOSITORY));
						}
					});
					if (c != null) en = msp != null && c.isSupported(Feature.SEARCHREPOSITORY);
				}
			} catch (Exception e) {
				en = false;
			}
		}
		setEnabled(en);
		return en;
	}

	@Override
	public void run() {
		FindResourceJob.doFindResource(sp, treeViewer);
	}

}
