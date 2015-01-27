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
package com.jaspersoft.studio.editor.style;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.style.editpolicy.StyleTemplateLayoutEditPolicy;

public class StylesTemplateEditPart extends FigureEditPart {

	@Override
	protected IFigure createFigure() {
		RectangleFigure f = new RectangleFigure() {
			@Override
			protected void outlineShape(Graphics graphics) {

			}
		};

		GridLayout lm = new GridLayout(2, true);
		lm.horizontalSpacing = 10;
		lm.verticalSpacing = 10;
		f.setLayoutManager(lm);
		f.setBounds(new Rectangle(10, 10, 600, 600));
		f.setBorder(null);
		
		//Event to refresh the editor when a styles is added or removed
		getModel().getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				//To avoid to refresh removed elements
				if (getViewer() != null){ 
						refresh();			
				} else {
					getModel().getPropertyChangeSupport().removePropertyChangeListener(this);
				}
			}
		});
		return f;
	}

	@Override
	protected void setupFigure(IFigure rect) {
		super.setupFigure(rect);
		int size = getModelChildren() == null ? rect.getChildren().size() : getModelChildren().size();
		rect.setSize(750, (size / 2 + 1) * 150 + 20);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new StyleTemplateLayoutEditPolicy());
	}

	@Override
	public boolean isSelectable() {
		return true;
	}

}
