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
package com.jaspersoft.studio.editor;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;

import com.jaspersoft.studio.model.INode;

public interface IGraphicalEditor {
	public ActionRegistry getActionRegistry();

	public DefaultEditDomain getEditDomain();

	public INode getModel();

	public GraphicalViewer getGraphicalViewer();

	public SelectionSynchronizer getSelectionSynchronizer();

	public FigureCanvas getEditor();
}
