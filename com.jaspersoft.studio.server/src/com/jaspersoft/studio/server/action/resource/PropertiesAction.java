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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.model.IInputControlsContainer;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.wizard.resource.ResourceWizard;

public class PropertiesAction extends Action {
	private static final String ID = "RESOURCEPROPERTIES";
	private TreeViewer treeViewer;
	private MResource mres;

	public PropertiesAction(TreeViewer treeViewer) {
		super();
		setId(ID);
		setText(Messages.common_properties);
		setDescription(Messages.common_properties);
		setToolTipText(Messages.common_properties);
		this.treeViewer = treeViewer;
	}

	@Override
	public void run() {
		final TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		for (int i = 0; i < p.length; i++) {
			final Object obj = p[i].getLastSegment();
			if (obj instanceof MResource) {
				try {
					mres = (MResource) obj;
					if (!(mres instanceof MFolder)) {
						NullProgressMonitor monitor = new NullProgressMonitor();
						ResourceDescriptor rd = WSClientHelper.getResource(monitor, mres, mres.getValue());
						ANode parent = mres.getParent();
						int index = parent.getChildren().indexOf(mres);
						parent.removeChild(mres);
						mres = ResourceFactory.getResource(parent, rd, index);
						if (mres instanceof IInputControlsContainer)
							WSClientHelper.refreshContainer(mres, monitor);
						// if(ModelUtil.isEmpty(mres))
						WSClientHelper.fireResourceChanged(mres);
					}
					ResourceWizard wizard = new ResourceWizard(mres, mres);
					WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
					dialog.create();
					dialog.open();
				} catch (Exception e) {
					UIUtils.showError(e);
				}
				break;
			}
		}
	}

}
