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
package com.jaspersoft.studio.repository;

import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.KeyEvent;

import com.jaspersoft.studio.model.ANode;

public interface IRepositoryViewProvider {
	public Action[] getActions(TreeViewer treeViewer);

	public ANode getNode(ANode root);

	public List<IAction> fillContextMenu(TreeViewer treeViewer, ANode node);

	public void hookKeyEvent(TreeViewer treeViewer, KeyEvent event);

	public void doubleClick(TreeViewer treeViewer);
	
	/**
	 * Returns the list of {@link TransferDragSourceListener}s that should be added to the viewer.
	 */
	public List<TransferDragSourceListener> getTransferDragSourceListeners(TreeViewer treeViewer);
	
	/**
	 * Returns the list of {@link TransferDropTargetListener}s that should be added to the viewer.
	 */
	public List<TransferDropTargetListener> getTransferDropTargetListeners(TreeViewer treeViewer);

	public void handleTreeEvent(TreeExpansionEvent event);

	public void addPropertyChangeListener(PropertyChangeListener pcl);

	public void removePropertyChangeListener(PropertyChangeListener pcl);
}
