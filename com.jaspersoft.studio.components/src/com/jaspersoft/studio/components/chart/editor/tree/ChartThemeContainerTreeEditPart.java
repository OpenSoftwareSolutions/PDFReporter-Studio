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
package com.jaspersoft.studio.components.chart.editor.tree;


/*
 * The Class AContainerTreeEditPart.
 */
public class ChartThemeContainerTreeEditPart extends ChartThemeTreeEditPart {

	/**
	 * Creates and installs pertinent EditPolicies.
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		// installEditPolicy(EditPolicy.CONTAINER_ROLE, new
		// JDStyleContainerEditPolicy());
		// installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new
		// JDStyleTreeContainerEditPolicy());
		// // If this editpart is the contents of the viewer, then it is not
		// deletable!
		// if (getParent() instanceof RootEditPart)
		// installEditPolicy(EditPolicy.COMPONENT_ROLE, new
		// RootComponentEditPolicy());
	}
}
