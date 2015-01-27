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

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.protocol.IConnection;

public class PasteResourceAsLinkAction extends PasteResourceAction {
	public static final String PASTE_ASLINK = "PASTEASLINK"; //$NON-NLS-1$

	public PasteResourceAsLinkAction(TreeViewer treeViewer) {
		super(treeViewer);
		setId(PASTE_ASLINK);
		setText(Messages.PasteResourceAsLinkAction_1);
		setToolTipText(Messages.PasteResourceAsLinkAction_2);
	}

	@Override
	public boolean isEnabled() {
		boolean res = false;
		ANode n = getSelected();
		if (n instanceof MReportUnit) {
			res = super.isEnabled();
			if (res && contents != null && contents instanceof List<?>) {
				List<?> list = (List<?>) contents;
				for (Object obj : list)
					if (obj instanceof MResource && obj instanceof ICopyable) {
						ICopyable c = (ICopyable) obj;
						if (c.isCopyable2(n)) {
							if (((MResource) obj).isCut())
								res = false;
							else
								res = true;
							break;
						}
					}
			}
		}
		return res;
	}

	@Override
	protected void saveToReportUnit(IProgressMonitor monitor, ANode parent, IConnection ws, ResourceDescriptor origin) throws IOException, Exception {
		ResourceDescriptor prd = (ResourceDescriptor) parent.getValue();
		ResourceDescriptor rd = null;
		rd = new ResourceDescriptor();
		rd.setName(origin.getName());
		rd.setLabel(origin.getLabel());
		rd.setDescription(origin.getDescription());
		rd.setIsNew(true);
		rd.setIsReference(true);
		rd.setReferenceUri(origin.getUriString());
		rd.setParentFolder(prd.getParentFolder() + "/" + prd.getName() + "_files"); //$NON-NLS-1$ //$NON-NLS-2$
		if (ResourceFactory.isFileResourceType(origin))
			rd.setWsType(ResourceDescriptor.TYPE_REFERENCE);
		else
			rd.setWsType(origin.getWsType());
		rd.setUriString(prd.getParentFolder() + "/" + prd.getName() + "_files/" + prd.getName()); //$NON-NLS-1$ //$NON-NLS-2$

		prd.getChildren().add(rd);
		ws.addOrModifyResource(monitor, prd, null);
	}

}
