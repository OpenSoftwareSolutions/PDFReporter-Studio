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
package com.jaspersoft.studio.server.publish.action;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.properties.dialog.RepositoryDialog;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.publish.ResourcePublishMethod;
import com.jaspersoft.studio.server.wizard.find.FindResourceJob;

public class ResourceToFolderAction extends Action {
	private MResource mres;
	private TableViewer tableViewer;

	public ResourceToFolderAction(TableViewer tableViewer) {
		super();
		setText("Save To Folder");
		this.tableViewer = tableViewer;
	}

	public boolean calculateEnabled(MResource mres) {
		this.mres = mres;
		return mres.getPublishOptions().getPublishMethod() != ResourcePublishMethod.RESOURCE;
	}

	@Override
	public void run() {
		MServerProfile msp = ServerManager.getMServerProfileCopy((MServerProfile) mres.getRoot());
		if (mres.isSupported(Feature.SEARCHREPOSITORY)) {
			ResourceDescriptor rd = FindResourceJob.doFindResource(msp, new String[] { ResourceMediaType.FOLDER_CLIENT_TYPE }, null);
			if (rd != null) {
				mres.getPublishOptions().setReferencedResource(rd);
				mres.getPublishOptions().setPublishMethod(ResourcePublishMethod.RESOURCE);
			}
		} else {
			RepositoryDialog rd = new RepositoryDialog(UIUtils.getShell(), msp) {
				@Override
				public boolean isResourceCompatible(MResource r) {
					return r.getValue().getWsType().equals(ResourceDescriptor.TYPE_FOLDER);
				}
			};
			if (rd.open() == Dialog.OK) {
				MResource rs = rd.getResource();
				mres.getPublishOptions().setReferencedResource(rs.getValue());
				mres.getPublishOptions().setPublishMethod(ResourcePublishMethod.RESOURCE);
			}
		}
		tableViewer.refresh();
	}
}
