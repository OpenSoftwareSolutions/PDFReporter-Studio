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
package com.jaspersoft.studio.editor.style.tree;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.jaspersoft.studio.editor.style.editpolicy.JDStyleContainerEditPolicy;
import com.jaspersoft.studio.editor.style.editpolicy.JDStyleTreeContainerEditPolicy;
/*
 * The Class AContainerTreeEditPart.
 */
public class StyleContainerTreeEditPart extends StyleTreeEditPart {

	/**
	 * Creates and installs pertinent EditPolicies.
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new JDStyleContainerEditPolicy());
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new JDStyleTreeContainerEditPolicy());
		// If this editpart is the contents of the viewer, then it is not deletable!
		if (getParent() instanceof RootEditPart)
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
	}
}
