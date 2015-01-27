/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.editor;

import java.util.Map;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.part.EditorPart;

import com.jaspersoft.studio.utils.AContributorAction;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public interface IEditorContributor {

	public void onLoad(JasperDesign jd, EditorPart editor);

	public void onSave(JasperReportsContext jrConfig, IProgressMonitor monitor);

	public void onRun(JasperReportsConfiguration jrConfig, JasperReport jr, Map<String, Object> params);

	public AContributorAction[] getActions();

	public String getTitleToolTip(JasperReportsContext jrConfig, String toolTip);
}
