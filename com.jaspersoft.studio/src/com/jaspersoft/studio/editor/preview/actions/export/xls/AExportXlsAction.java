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
package com.jaspersoft.studio.editor.preview.actions.export.xls;

import java.io.File;

import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import com.jaspersoft.studio.editor.preview.actions.export.AExportAction;
import com.jaspersoft.studio.editor.preview.actions.export.ExportMenuAction;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AExportXlsAction extends AExportAction {

	public AExportXlsAction(IReportViewer viewer, JasperReportsConfiguration jContext, ExportMenuAction parentMenu) {
		super(viewer, jContext, parentMenu);
	}

	@Override
	protected JRXlsAbstractExporter<?, ?, ?> getExporter(JasperReportsConfiguration jContext,
			JRExportProgressMonitor monitor, File file) {
		JRXlsAbstractExporter<?, ?, ?> exp = createExporter(jContext, monitor);
		exp.setExporterOutput(new SimpleOutputStreamExporterOutput(file));

		return exp;
	}

	protected abstract JRXlsAbstractExporter<?, ?, ?> createExporter(JasperReportsConfiguration jContext,
			JRExportProgressMonitor monitor);
}
