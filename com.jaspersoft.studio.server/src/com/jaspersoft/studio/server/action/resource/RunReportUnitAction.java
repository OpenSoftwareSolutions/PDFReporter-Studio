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
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.editor.ReportUnitEditor;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.utils.SelectionHelper;

public class RunReportUnitAction extends Action {
	private static final String ID = "RUNREPORTUNIT"; //$NON-NLS-1$
	private TreeViewer treeViewer;

	public RunReportUnitAction(TreeViewer treeViewer) {
		super();
		setId(ID);
		setText(Messages.RunReportUnitAction_1);
		setDescription(Messages.RunReportUnitAction_2);
		setToolTipText(Messages.RunReportUnitAction_3);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/start_task.gif")); //$NON-NLS-1$
		setDisabledImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/start_task.gif")); //$NON-NLS-1$
		this.treeViewer = treeViewer;
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && isRunnable();
	}

	private boolean isRunnable() {
		final TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		for (int i = 0; i < p.length; i++) {
			if (!isInReportUnit(p[i].getLastSegment()))
				return false;
		}
		return true;
	}

	private boolean isInReportUnit(Object obj) {
		if (obj == null)
			return false;
		MReportUnit mrepunit = null;
		if (obj instanceof MReportUnit)
			mrepunit = (MReportUnit) obj;
		else if (((ANode) obj).getParent() instanceof MReportUnit)
			mrepunit = (MReportUnit) ((ANode) obj).getParent();
		if (mrepunit == null)
			return false;
		int pmask = mrepunit.getValue().getPermissionMask(mrepunit.getWsClient());
		return pmask == 1 || (pmask & 32) == 32 || (pmask & 2) == 2;
	}

	@Override
	public void run() {
		final TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		for (int i = 0; i < p.length; i++) {
			Object obj = p[i].getLastSegment();
			if (obj instanceof MResource) {
				INode node = ((MResource) obj).getReportUnit();
				if (node != null) {
					final String key = ServerManager.getKey((MReportUnit) node);
					if (key != null)
						Display.getDefault().asyncExec(new Runnable() {

							public void run() {
								SelectionHelper.openEditor(key, ReportUnitEditor.ID);
							}
						});
				}
				break;
			}
		}
	}
}
