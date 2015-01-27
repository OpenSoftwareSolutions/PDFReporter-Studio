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
package com.jaspersoft.studio.editor.preview.view.report.system;

import java.io.File;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.preview.actions.export.AExportAction;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.control.ReportControler;
import com.jaspersoft.studio.editor.preview.view.report.swt.SWTViewer;
import com.jaspersoft.studio.utils.Callback;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class ASystemViewer extends SWTViewer {

	public ASystemViewer(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	protected abstract String getExtension(JasperPrint jrprint);

	private boolean lastError = false;

	@Override
	public void setJRPRint(final Statistics stats, JasperPrint jrprint, boolean refresh) {
		boolean same = this.jrprint == jrprint && !lastError;
		super.setJRPRint(stats, jrprint, refresh);
		if (this.jrprint != null && (!same || refresh)) {
			try {
				final String ext = getExtension(jrprint);
				final File tmpFile = File.createTempFile("report", ext);
				AExportAction exp = createExporterAction(rptviewer);
				stats.startCount(ReportControler.ST_EXPORTTIME);
				exp.export(tmpFile, new Callback<File>() {

					@Override
					public void completed(File value) {
						stats.endCount(ReportControler.ST_EXPORTTIME);
						stats.setValue(ReportControler.ST_REPORTSIZE, tmpFile.length());

						Program p = Program.findProgram(ext);
						if (p != null)
							p.execute(tmpFile.getAbsolutePath());
						else
							// TODO here we can propose a better dialog, like open with...(create association, etc.)
							UIUtils.showWarning(String
									.format(
											"No file association defined in your sistem for: %s\nFile is located at: \n\n%s\n\nPlease open it manually or fix file association and retry.",
											ext, tmpFile.getAbsolutePath()));
						lastError = false;
					}
				});

			} catch (Exception e) {
				UIUtils.showError(e);
				lastError = true;
			}
		}
	}
}
