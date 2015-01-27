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
package com.jaspersoft.studio.editor.preview.view.report.file;

import java.io.File;
import java.io.IOException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.eclipse.viewer.ReportViewer;
import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.editor.preview.actions.export.AExportAction;
import com.jaspersoft.studio.editor.preview.actions.export.ExportMenuAction;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.IPreferencePage;
import com.jaspersoft.studio.editor.preview.view.control.ReportControler;
import com.jaspersoft.studio.editor.preview.view.report.ExportMenu;
import com.jaspersoft.studio.editor.preview.view.report.IJRPrintable;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AFileViewer extends APreview implements IJRPrintable, IPreferencePage {

	private ReportViewer rptviewer;
	private Text txt;

	public AFileViewer(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	private AExportAction expAction;

	protected AExportAction createExporterAction(ReportViewer rptv) {
		if (expAction == null)
			expAction = createExporter(rptv);
		return expAction;
	}

	protected abstract AExportAction createExporter(ReportViewer rptv);

	protected abstract String getExtension();

	@Override
	public void contribute2ToolBar(IToolBarManager tmanager) {
		super.contribute2ToolBar(tmanager);
		if (jrprint != null) {
			ExportMenuAction exportMenu = ExportMenu.getExportMenu(rptviewer, jContext);
			setDefaultExporter(exportMenu, createExporterAction(rptviewer));
			tmanager.add(exportMenu);
		}
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		txt = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
		txt.setLayoutData(new GridData(GridData.FILL_BOTH));

		rptviewer = new ReportViewer(jContext);
		return composite;
	}

	private JasperPrint jrprint;

	public void setJRPRint(Statistics stats, JasperPrint jrprint) throws Exception {
		setJRPRint(stats, jrprint, false);
	}

	public void setJRPRint(final Statistics stats, final JasperPrint jrprint, boolean refresh) throws Exception {
		if (this.jrprint == null || this.jrprint != jrprint || refresh) {
			rptviewer.setReport(jrprint);

			Job job = new Job(Messages.AExportAction_exportreport) {
				@Override
				protected IStatus run(final IProgressMonitor monitor) {
					try {
						File tmpFile = File.createTempFile("report", getExtension());
						AExportAction exp = createExporterAction(rptviewer);
						stats.startCount(ReportControler.ST_EXPORTTIME);
						exp.doExport(tmpFile, jrprint, monitor);
						stats.endCount(ReportControler.ST_EXPORTTIME);
						stats.setValue(ReportControler.ST_REPORTSIZE, tmpFile.length());
						final String content = (FileUtils.readFileAsAString(tmpFile));
						UIUtils.getDisplay().asyncExec(new Runnable() {

							@Override
							public void run() {
								txt.setText(content);
							}
						});
					} catch (IOException e) {
						UIUtils.showError(e);
					}
					return Status.OK_STATUS;
				}
			};
			job.setPriority(Job.LONG);
			job.schedule();

		}
		this.jrprint = jrprint;
	}
}
