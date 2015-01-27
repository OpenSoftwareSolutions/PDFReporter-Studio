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

import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.export.XlsxExporterConfiguration;

import com.jaspersoft.studio.editor.preview.actions.export.ExportMenuAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ExportAsXlsxAction extends AExportXlsAction {

	public ExportAsXlsxAction(IReportViewer viewer, JasperReportsConfiguration jContext, ExportMenuAction parentMenu) {
		super(viewer, jContext, parentMenu);

		setText(Messages.ExportAsXlsxAction_title);
		setToolTipText(Messages.ExportAsXlsxAction_tooltip);

		setFileExtensions(new String[] { "*.xlsx;*.xlsm" }); //$NON-NLS-1$
		setFilterNames(new String[] { "XLSx (*.xlsx)", "XLSm (*.xlsm)" });
		setDefaultFileExtension("xlsx"); //$NON-NLS-1$
	}

	@Override
	protected void setFileExtensions() {
		JasperPrint jrPrint = getReportViewer().getReport();
		String ext = ".xlsx";
		if (jrPrint.getProperty(XlsxExporterConfiguration.PROPERTY_MACRO_TEMPLATE) != null)
			ext = ".xlsm";

		setDefaultFileExtension(ext);
		setFilterNames(new String[] { ext.toUpperCase() + " (*." + ext + ")" });
		setFileExtensions(new String[] { "*" + ext });
	}

	@Override
	protected JRXlsxExporter createExporter(JasperReportsConfiguration jContext, JRExportProgressMonitor monitor) {
		JRXlsxExporter exp = new JRXlsxExporter(jContext);

		SimpleXlsxReportConfiguration rconf = new SimpleXlsxReportConfiguration();
		setupReportConfiguration(rconf, monitor);
		exp.setConfiguration(rconf);

		return exp;
	}
}
