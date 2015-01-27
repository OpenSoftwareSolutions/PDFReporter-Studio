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
package com.jaspersoft.studio.data.sql.ui.gef.layout;

import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class GraphLayoutManager extends AbstractLayout {
	private AbstractGraphicalEditPart diagram;

	public GraphLayoutManager(AbstractGraphicalEditPart diagram) {
		this.diagram = diagram;
	}

	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		container.validate();
		List<IFigure> children = container.getChildren();
		Rectangle result = new Rectangle().setLocation(container.getClientArea().getLocation());
		for (IFigure c : children)
			result.union(c.getBounds());
		Insets ins = container.getInsets();
		result.resize(ins.getWidth(), ins.getHeight());
		return result.getSize();
	}

	public void layout(IFigure container) {
		new DirectedGraphLayoutVisitor().layoutDiagram(diagram);
	}
}
