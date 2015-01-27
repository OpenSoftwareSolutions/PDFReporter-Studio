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
package com.jaspersoft.studio.dnd;

import java.util.Iterator;

import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.PluginTransferData;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.model.ANode;

/**
 * Supports dragging gadgets from a structured viewer.
 */
public class NodeDragListener extends DragSourceAdapter {
	protected StructuredViewer viewer;

	public NodeDragListener(StructuredViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Method declared on DragSourceListener
	 */
	public void dragFinished(DragSourceEvent event) {
		if (!event.doit)
			return;
		// if the gadget was moved, remove it from the source viewer
		if (event.detail == DND.DROP_MOVE) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			for (Iterator<?> it = selection.iterator(); it.hasNext();) {
				Object obj = it.next();
				if (obj instanceof ANode) {
					ANode n = (ANode) obj;
					if (n.isCut())
						((ANode) obj).setParent(null, -1);
				}
			}
			viewer.refresh();
		}
	}

	/**
	 * Method declared on DragSourceListener
	 */
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if (NodeTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = selection.toList().toArray();
		} else if (PluginTransfer.getInstance().isSupportedType(event.dataType)) {
			ANode[] gadgets = (ANode[]) selection.toList().toArray(new ANode[selection.size()]);
			byte[] data = NodeTransfer.getInstance().toByteArray(gadgets);
			event.data = new PluginTransferData(JaspersoftStudioPlugin.getUniqueIdentifier(), data);
		} else if (TemplateTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = selection.toList().toArray();
		}
	}

	/**
	 * Method declared on DragSourceListener
	 */
	public void dragStart(DragSourceEvent event) {
		event.doit = !viewer.getSelection().isEmpty();
	}
}
