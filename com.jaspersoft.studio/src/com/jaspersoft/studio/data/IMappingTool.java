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
package com.jaspersoft.studio.data;

import net.sf.jasperreports.engine.design.JRDesignDataset;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;

public interface IMappingTool {

	public String getName();

	public Control getControl();

	public Control createControl(Composite parent);

	public void setFields(IFieldSetter fsetter);

	public void setJRDataset(JRDesignDataset dataset);

	public JRDesignDataset getJRDataset();

	public void dispose();

	public void setParentContainer(DataQueryAdapters dataQueryAdapters);
}
