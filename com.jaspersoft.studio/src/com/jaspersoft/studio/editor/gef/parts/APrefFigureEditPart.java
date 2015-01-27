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
package com.jaspersoft.studio.editor.gef.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.preferences.DesignerPreferencePage;

public abstract class APrefFigureEditPart extends FigureEditPart implements IPrefEditPart {
	@Override
	protected IFigure createFigure() {
		figure = super.createFigure();
		setMarginColor();
		return figure;
	}

	@Override
	protected void handlePreferenceChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
		String p = event.getProperty();
		if (p.equals(DesignerPreferencePage.P_CONTAINER_MARGIN_COLOR)) {
			setMarginColor();
		} else
			super.handlePreferenceChanged(event);
	}

	private Color marginColor;

	@Override
	public Color getMarginColor() {
		return marginColor;
	}

	protected void setMarginColor() {
		if (jConfig == null)
			jConfig = getModel().getJasperConfiguration();
		String mcolor = jConfig.getProperty(DesignerPreferencePage.P_CONTAINER_MARGIN_COLOR,
				DesignerPreferencePage.DEFAULT_MARGINCOLOR);
		marginColor = SWTResourceManager.getColor(StringConverter.asRGB(mcolor));
		if (figure != null) {
			setupMarginColor();
		}
	}

	protected void setupMarginColor() {
	}
}
