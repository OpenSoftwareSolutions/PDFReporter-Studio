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
package com.jaspersoft.studio.components.chart.property.descriptor.seriescolor;

import java.util.Set;

import org.eclipse.jface.viewers.LabelProvider;
/*
 * @author Chicu Veaceslav
 * 
 */
public class SeriesColorLabelProvider extends LabelProvider {

	public SeriesColorLabelProvider() {
		super();
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "";
		if (element instanceof Set<?>)
			return "[Colors: " + ((Set<?>) element).size() + "]";
		return element.toString();
	}

}
