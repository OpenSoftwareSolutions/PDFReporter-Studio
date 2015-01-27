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
package com.jaspersoft.studio.components.chart.property.section.theme;

import net.sf.jasperreports.chartthemes.simple.LegendSettings;
import net.sf.jasperreports.chartthemes.simple.TitleSettings;

import com.jaspersoft.studio.components.chart.model.theme.MLegendSettings;
import com.jaspersoft.studio.components.chart.model.theme.MTitleSettings;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.text.FontSection;

public class CTFontSection extends FontSection {
	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MTitleSettings)
			return (APropertyNode) md.getPropertyValue(TitleSettings.PROPERTY_font);
		if (md instanceof MLegendSettings)
			return (APropertyNode) md.getPropertyValue(LegendSettings.PROPERTY_font);
		return md;
	}
}
