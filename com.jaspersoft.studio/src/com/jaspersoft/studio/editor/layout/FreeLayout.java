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
package com.jaspersoft.studio.editor.layout;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRElement;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.messages.Messages;

public class FreeLayout implements ILayout {

	@Override
	public Map<JRElement, Rectangle> layout(JRElement[] elements, Dimension c) {
		return new HashMap<JRElement, Rectangle>();
	}

	@Override
	public String getName() {
		return Messages.FreeLayout_name;
	}

	@Override
	public String getToolTip() {
		return Messages.FreeLayout_tooltip;
	}

	@Override
	public String getIcon() {
		return "icons/layout.png"; //$NON-NLS-1$
	}

}
