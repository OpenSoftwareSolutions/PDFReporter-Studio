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
package com.jaspersoft.studio.components.table.model.column.action;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.editor.gef.util.CreateRequestUtil;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

/*
 * The Class CreateGroupAction.
 */
public class CreateColumnAfterAction extends CreateColumnAction {

	/** The Constant ID. */
	public static final String ID = "create_table_column_after"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *            The part for this action
	 */
	public CreateColumnAfterAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.CreateColumnAfterAction_title);
		setToolTipText(Messages.CreateColumnAfterAction_desc);
		setId(CreateColumnAfterAction.ID);
		setImageDescriptor(
				Activator.getDefault().getImageDescriptor("icons/table-insert-column-after.png")); //$NON-NLS-1$
		setDisabledImageDescriptor(
				Activator.getDefault().getImageDescriptor("icons/table-insert-column-after.png")); //$NON-NLS-1$
		setEnabled(false);
	}

	@Override
	protected boolean setExtendedData(Map<Object, Object> map, List<?> objects) {
		super.setExtendedData(map, objects);
		Object obj = objects.get(0);
		if (obj instanceof EditPart) {
			ANode n = (ANode) ((EditPart) obj).getModel();
			if (n instanceof MColumn) {
				List<INode> children = n.getParent().getChildren();
				int index = children.indexOf(n);
				map.put(CreateRequestUtil.NEWINDEX, index + 1);
			} else
				return false;
		}
		return true;
	}
}
