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

import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.editor.dnd.ImageURLTransfer;
import com.jaspersoft.studio.server.model.MRImage;

/**
 * Implementation of a {@link TransferDragSourceListener} that is supposed to handle
 * the drag operation of an {@link MRImage} node from the repository view.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class RepositoryImageDragSourceListener implements TransferDragSourceListener{
	
	private StructuredViewer viewer;

	public RepositoryImageDragSourceListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public Transfer getTransfer() {
		return ImageURLTransfer.getInstance();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		// no clean-up needed
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		if (ImageURLTransfer.getInstance().isSupportedType(event.dataType)) {
			IStructuredSelection selection = (IStructuredSelection) viewer
					.getSelection();
			if (selection.getFirstElement() instanceof MRImage) {
				MRImage reportImg = (MRImage) selection.getFirstElement();
				ResourceDescriptor imgDescriptor = reportImg.getValue();
				event.data = "repo:" + imgDescriptor.getUriString(); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Valid only if an {@link MRImage} is selected.
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		event.doit = !viewer.getSelection().isEmpty()
				&& (selection.getFirstElement() instanceof MRImage);
	}
}
