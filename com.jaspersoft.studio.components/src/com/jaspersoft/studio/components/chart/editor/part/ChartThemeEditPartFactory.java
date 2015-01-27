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
package com.jaspersoft.studio.components.chart.editor.part;

import org.eclipse.gef.EditPart;

import com.jaspersoft.studio.components.chart.model.theme.MChartThemeSettings;
import com.jaspersoft.studio.editor.AEditPartFactory;
import com.jaspersoft.studio.model.MRoot;

/*
 * A factory for creating JasperDesignEditPart objects.
 * 
 * @author Chicu Veaceslav
 */
public class ChartThemeEditPartFactory extends AEditPartFactory {

	@Override
	protected EditPart createEditPart(Object model) {
		EditPart editPart = null;
		if (model instanceof MRoot || model instanceof MChartThemeSettings)
			editPart = new ChartThemeEditPart();
		return editPart;
	}
}
