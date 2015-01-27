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
package com.jaspersoft.studio.preferences.fonts.utils;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.wb.swt.SWTResourceManager;

public class BoldStyler extends Styler {
	private String fForegroundColorName;
	private String fBackgroundColorName;

	public BoldStyler(String foregroundColorName, String backgroundColorName) {
		fForegroundColorName = foregroundColorName;
		fBackgroundColorName = backgroundColorName;
	}

	@Override
	public void applyStyles(TextStyle textStyle) {
		FontData fd = FontUtils.getTextEditorFontData();
		textStyle.font = SWTResourceManager.getBoldFont(SWTResourceManager.getFont(fd.getName(), fd.getHeight(),
				fd.getStyle()));
		ColorRegistry colorRegistry = JFaceResources.getColorRegistry();
		if (fForegroundColorName != null)
			textStyle.foreground = colorRegistry.get(fForegroundColorName);
		if (fBackgroundColorName != null)
			textStyle.background = colorRegistry.get(fBackgroundColorName);
	}
}
