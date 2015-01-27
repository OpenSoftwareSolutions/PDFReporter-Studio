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
package com.jaspersoft.studio.preferences.theme;

import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;

public class DarkTheme extends UITheme {
	public static final String NAME = "dark";

	public DarkTheme() {
		setName(NAME);
		addProperty(RulersGridPreferencePage.P_PAGE_GRID_COLOR, "255,255,255");
		addProperty(DesignerPreferencePage.P_CONTAINER_MARGIN_COLOR, "0,255,0");
		addProperty(DesignerPreferencePage.P_PAGE_BACKGROUND, "0,0,0");
		addProperty(DesignerPreferencePage.P_PAGE_MARGIN_COLOR, "255,0,0");
		addProperty(DesignerPreferencePage.P_ELEMENT_DESIGN_BORDER_COLOR, "255,255,0");
	}
}
