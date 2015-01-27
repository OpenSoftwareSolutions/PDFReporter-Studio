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
package com.jaspersoft.studio.data.sql.ui.gef.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.jaspersoft.studio.data.sql.model.MSQLRoot;
import com.jaspersoft.studio.data.sql.model.query.MUnion;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.ui.gef.layout.GraphLayoutManager;
import com.jaspersoft.studio.data.sql.ui.gef.policy.FromContainerEditPolicy;
import com.jaspersoft.studio.model.INode;

public class QueryEditPart extends AbstractGraphicalEditPart {

	@Override
	protected IFigure createFigure() {
		FreeformLayer fig = new FreeformLayer();
		fig.setLayoutManager(new GraphLayoutManager(this));
		fig.setOpaque(true);
		return fig;
	}

	@Override
	public MSQLRoot getModel() {
		return (MSQLRoot) super.getModel();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new FromContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
	}

	@Override
	protected List<?> getModelChildren() {
		MSQLRoot root = getModel();
		List<MFrom> list = new ArrayList<MFrom>();
		for (INode n : root.getChildren()) {
			if (n instanceof MFrom && !n.getChildren().isEmpty())
				list.add((MFrom) n);
			else if (n instanceof MUnion)
				for (INode sn : n.getChildren())
					if (sn instanceof MFrom && !n.getChildren().isEmpty())
						list.add((MFrom) n);
		}
		return list;
	}

}
