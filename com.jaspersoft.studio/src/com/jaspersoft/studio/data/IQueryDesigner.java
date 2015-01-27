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

import java.util.List;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.data.designer.AQueryDesignerContainer;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public interface IQueryDesigner {

	public Control getControl();

	public Control createControl(Composite parent);

	public Control getToolbarControl();

	public Control createToolbar(Composite parent);

	public void dispose();

	public void setQuery(JasperDesign jDesign, JRDataset jDataset, JasperReportsConfiguration jConfig);

	public String getQuery();

	public void setDataAdapter(DataAdapterDescriptor da);

	public void setParentContainer(AQueryDesignerContainer dataQueryAdapters);

	public void setFields(List<JRDesignField> fields);

	public void setParameters(List<JRDesignParameter> fields);

	public String getContextHelpId();

	public void setJasperConfiguration(JasperReportsConfiguration jConfig);

}
