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
package com.jaspersoft.studio.server.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.editor.report.UnitTransfer;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.model.MInputControl;
import com.jaspersoft.studio.server.model.MReportUnit;

/**
 * 
 * Drag listener for the report unit type, it generate and event serializing an list of string 
 * with some informations of the dragged unit (its uri and the name of its Resources descriptor).
 * 
 * @author Orlandin Marco
 *
 */
public class UnitDragSourceListener implements TransferDragSourceListener{

	private StructuredViewer viewer;

	public UnitDragSourceListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * check if the drag is done good
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {
		if (!event.doit || event.detail != DND.DROP_MOVE)
			return;
	}

	/**
	 * Read the selected report unit and generate the data for the event,
	 * serializing an array of string with some of the unit informations. The
	 * serializing is done using the UnitTransfer class
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		List<String> unitInfo = new ArrayList<String>();
		if (selection.getFirstElement() instanceof MReportUnit) {
			MReportUnit unit = (MReportUnit) selection.getFirstElement();
			ResourceDescriptor unitDescrtiptor = unit.getValue();
			unitInfo.add(unitDescrtiptor.getUriString());
			List<INode> childrens = unit.getChildren();
			for (INode children : childrens) {
				if (children instanceof MInputControl) {
					ResourceDescriptor desc = (ResourceDescriptor) children
							.getValue();
					unitInfo.add(desc.getName());
				}
			}
		}
		String[] gadgets = unitInfo.toArray(new String[unitInfo.size()]);
		if (UnitTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = gadgets;
		}
	}

	/**
	 * Valid only if it is selected a a MReportUnit
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		event.doit = !viewer.getSelection().isEmpty()
				&& (selection.getFirstElement() instanceof MReportUnit);
	}

	@Override
	public Transfer getTransfer() {
		return UnitTransfer.getInstance();
	}
}
