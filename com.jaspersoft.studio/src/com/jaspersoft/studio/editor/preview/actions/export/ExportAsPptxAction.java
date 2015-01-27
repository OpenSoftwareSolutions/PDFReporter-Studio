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
package com.jaspersoft.studio.editor.preview.actions.export;

import java.io.File;

import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePptxReportConfiguration;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ExportAsPptxAction extends AExportAction {

	public ExportAsPptxAction(IReportViewer viewer, JasperReportsConfiguration jContext, ExportMenuAction parentMenu) {
		super(viewer, jContext, parentMenu);

		setText("Export As PowerPoint");
		setToolTipText("Export as PowerPoint");

		setFileExtensions(new String[] { "*.pptx" }); //$NON-NLS-1$
		setFilterNames(new String[] { "PowerPoint (*.pptx)" });
		setDefaultFileExtension("pptx"); //$NON-NLS-1$
	}

	@Override
	protected JRPptxExporter getExporter(JasperReportsConfiguration jContext, JRExportProgressMonitor monitor, File file) {
		JRPptxExporter exp = new JRPptxExporter(jContext);
		exp.setExporterOutput(new SimpleOutputStreamExporterOutput(file));

		SimplePptxReportConfiguration rconf = new SimplePptxReportConfiguration();
		setupReportConfiguration(rconf, monitor);
		exp.setConfiguration(rconf);

		return exp;
	}
}
