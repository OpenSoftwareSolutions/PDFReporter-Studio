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
package com.jaspersoft.studio.editor.preview.view.report.html;

import java.io.File;
import java.io.IOException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.eclipse.viewer.ReportViewer;
import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.editor.preview.actions.export.AExportAction;
import com.jaspersoft.studio.editor.preview.actions.export.ExportMenuAction;
import com.jaspersoft.studio.editor.preview.actions.export.html.ExportAsLHtmlAction;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.IPreferencePage;
import com.jaspersoft.studio.editor.preview.view.control.ReportControler;
import com.jaspersoft.studio.editor.preview.view.report.ExportMenu;
import com.jaspersoft.studio.editor.preview.view.report.IJRPrintable;
import com.jaspersoft.studio.preferences.exporter.HTMLExporterPreferencePage;
import com.jaspersoft.studio.utils.Callback;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class HTMLViewer extends ABrowserViewer implements IJRPrintable, IPreferencePage {

	public HTMLViewer(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	@Override
	protected Control createControl(Composite parent) {
		Control composite = super.createControl(parent);
		rptviewer = new ReportViewer(jContext);
		return composite;
	}

	@Override
	public void contribute2ToolBar(IToolBarManager tmanager) {
		super.contribute2ToolBar(tmanager);
		if (jrprint != null) {
			ExportMenuAction exportMenu = ExportMenu.getExportMenu(rptviewer, jContext);
			setDefaultExporter(exportMenu, createExporter(rptviewer));
			tmanager.add(exportMenu);
		}
	}

	private JasperPrint jrprint;
	private ReportViewer rptviewer;

	public void setJRPRint(Statistics stats, JasperPrint jrprint) throws Exception {
		setJRPRint(stats, jrprint, false);
	}

	public void setJRPRint(final Statistics stats, JasperPrint jrprint, boolean refresh) throws Exception {
		if (this.jrprint != jrprint || refresh) {
			rptviewer.setReport(jrprint);
			if (tmpFile == null)
				tmpFile = File.createTempFile("report", getExtension(), getTmpPath());

			AExportAction exp = createExporter(rptviewer);
			stats.startCount(ReportControler.ST_EXPORTTIME);
			exp.export(tmpFile, new Callback<File>() {

				@Override
				public void completed(File value) {
					stats.endCount(ReportControler.ST_EXPORTTIME);
					stats.setValue(ReportControler.ST_REPORTSIZE, tmpFile.length());

					try {
						setURL(tmpFile.toURI().toASCIIString());
					} catch (Exception e) {
						UIUtils.showError(e);
					}
					doRefresh();
				}
			});
		}
		doRefresh();
		this.jrprint = jrprint;
	}

	protected String getExtension() {
		return ".html";
	}

	private AExportAction expAction;

	protected AExportAction createExporter(ReportViewer rptv) {
		if (expAction == null)
			expAction = new ExportAsLHtmlAction(rptv, jContext, null);
		return expAction;
	}

	private File tmpDir;
	private File tmpFile;

	private File getTmpPath() throws IOException {
		if (tmpDir != null)
			FileUtils.recursiveDelete(tmpDir);

		tmpDir = FileUtils.createTempDir();

		return tmpDir;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (tmpDir != null)
			FileUtils.recursiveDelete(tmpDir);
	}

	@Override
	public void pageGenerated(JasperPrint arg0, int arg1) {
		doRefresh();
	}

	@Override
	public void pageUpdated(JasperPrint arg0, int arg1) {
		doRefresh();
	}

	private boolean isRefresh = false;;
	private boolean newRequest = false;

	private void doRefresh() {
		if (isRefresh) {
			newRequest = true;
			return;
		}
		isRefresh = true;
		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				newRequest = false;
				browser.refresh();
				isRefresh = false;
				if (newRequest)
					doRefresh();
			}
		});
	}

	@Override
	public PreferencePage getPreferencePage() {
		return new HTMLExporterPreferencePage();
	}

}
