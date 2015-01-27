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
package com.jaspersoft.studio.editor.outline.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.gef.parts.band.BandEditPart;
import com.jaspersoft.studio.editor.outline.part.TreeEditPart;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.band.MBandGroupFooter;
import com.jaspersoft.studio.model.band.MBandGroupHeader;
import com.jaspersoft.studio.model.group.command.DeleteGroupCommand;

/*
 * The Class DeleteGroupReportAction.
 */
public class DeleteGroupReportAction extends DeleteAction {

	/** The Constant ID. */
	public static final String ID = "delete_group_report"; //$NON-NLS-1$

	/**
	 * Instantiates a new delete group report action.
	 * 
	 * @param editor
	 *          the editor
	 */
	public DeleteGroupReportAction(IEditorPart editor) {
		super(editor.getSite().getPart());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.DeleteAction#init()
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.DeleteGroupReportAction_delete_group);
		setToolTipText(Messages.DeleteGroupReportAction_delete_group_tool_tip);
		setId(DeleteGroupReportAction.ID);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		setEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.DeleteAction#createDeleteCommand(java.util.List)
	 */
	@Override
	public Command createDeleteCommand(@SuppressWarnings("rawtypes") List objects) {
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof EditPart))
			return null;

		GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_DELETE);
		deleteReq.setEditParts(objects);

		JSSCompoundCommand compoundCmd = new JSSCompoundCommand("Delete Report Group", null); //$NON-NLS-1$
		for (int i = 0; i < objects.size(); i++) {
			EditPart part = (EditPart) objects.get(i);
			Command cmd = null;
			if (part instanceof TreeEditPart || part instanceof BandEditPart) {
				ANode node = (ANode) part.getModel();
				compoundCmd.setReferenceNodeIfNull(node);
				if (node instanceof MBandGroupHeader) {
					cmd = new DeleteGroupCommand((MReport) node.getParent(), (MBandGroupHeader) node);
				}
				if (node instanceof MBandGroupFooter) {
					cmd = new DeleteGroupCommand((MReport) node.getParent(), (MBandGroupFooter) node);
				}
			}
			if (cmd != null)
				compoundCmd.add(cmd);
		}

		return compoundCmd;
	}
}
