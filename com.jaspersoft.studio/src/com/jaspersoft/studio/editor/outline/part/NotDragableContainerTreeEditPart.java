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
package com.jaspersoft.studio.editor.outline.part;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.jaspersoft.studio.editor.outline.editpolicy.JDContainerEditPolicy;
import com.jaspersoft.studio.editor.outline.editpolicy.JDTreeContainerEditPolicy;
import com.jaspersoft.studio.model.MGraphicElement;

public class NotDragableContainerTreeEditPart extends NotDragableTreeEditPart {
	/**
	 * Creates and installs pertinent EditPolicies.
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new JDContainerEditPolicy());
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new JDTreeContainerEditPolicy());
		// If this editpart is the contents of the viewer, then it is not deletable!
		if (getParent() instanceof RootEditPart)
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
	}

	@Override
	public boolean understandsRequest(Request req) {
		if (getModel() instanceof MGraphicElement && req.getType() == RequestConstants.REQ_ALIGN)
			return true;
		return super.understandsRequest(req);
	}
}
