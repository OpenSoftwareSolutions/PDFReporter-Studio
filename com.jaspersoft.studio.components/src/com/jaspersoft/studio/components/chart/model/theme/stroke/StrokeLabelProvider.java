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
package com.jaspersoft.studio.components.chart.model.theme.stroke;

import java.awt.BasicStroke;

import org.eclipse.jface.viewers.LabelProvider;

/*
 * @author Chicu Veaceslav
 * 
 */
public class StrokeLabelProvider extends LabelProvider {

	public StrokeLabelProvider() {
		super();
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "";
		if (element instanceof BasicStroke) {
			BasicStroke bs = (BasicStroke) element;
			float d = 1f;
			float[] da = bs.getDashArray();
			if (da != null && da.length > 0)
				d = da[0];
			return "[" + ((BasicStroke) element).getLineWidth() + "," + d + "," + ((BasicStroke) element).getDashPhase() + "]";
		}
		return element.toString();
	}

}
