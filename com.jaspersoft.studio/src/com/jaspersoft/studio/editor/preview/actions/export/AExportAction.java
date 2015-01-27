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
import java.text.MessageFormat;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleReportExportConfiguration;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.editor.pages.Pages;
import com.jaspersoft.studio.preferences.exporter.JRExporterPreferencePage;
import com.jaspersoft.studio.utils.Callback;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AExportAction extends AReportViewerAction {

	private String[] filterNames;
	private String[] fileExtensions;
	private String defaultFileExtension;
	private String fileName;
	private String filterPath;
	private JasperReportsConfiguration jContext;
	private ExportMenuAction parentMenu;

	public AExportAction(IReportViewer viewer, JasperReportsConfiguration jContext, ExportMenuAction parentMenu) {
		super(viewer);
		this.jContext = jContext;
		this.parentMenu = parentMenu;
	}

	public void setDefaultFileExtension(String defaultFileExtension) {
		this.defaultFileExtension = defaultFileExtension;
	}

	public String getDefaultFileExtension() {
		return defaultFileExtension;
	}

	protected void setFileExtensions() {
	}

	public void setFileExtensions(String[] fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public void setFilterNames(String[] filterNames) {
		this.filterNames = filterNames;
	}

	private static String getFileExtension(String fileName) {
		if (fileName != null) {
			int ind = fileName.lastIndexOf('.');
			if (ind != -1)
				return fileName.substring(ind + 1);
		}
		return null;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilterPath() {
		return filterPath;
	}

	public void setFilterPath(String filterPath) {
		this.filterPath = filterPath;
	}

	@Override
	public void run() {
		FileDialog dialog = new FileDialog(UIUtils.getShell(), SWT.SINGLE | SWT.SAVE);
		setFileExtensions();
		if (filterNames != null)
			dialog.setFilterNames(filterNames);
		if (fileExtensions != null)
			dialog.setFilterExtensions(fileExtensions);
		if (filterPath != null)
			dialog.setFilterPath(filterPath);
		if (fileName != null)
			dialog.setFileName(fileName);
		else
			dialog.setFileName(getReportViewer().getReport().getName());

		String filePath = dialog.open();
		if (filePath != null) {
			if (defaultFileExtension != null && fileExtensions != null) {
				String extension = getFileExtension(filePath);
				if (extension != null)
					for (String fe : fileExtensions) {
						if (fe.endsWith(extension)) {
							if (!filePath.toLowerCase().endsWith(defaultFileExtension.toLowerCase()))
								filePath += '.' + defaultFileExtension;
							break;
						}
					}
			}
			try {
				export(new File(filePath), new Callback<File>() {

					@Override
					public void completed(File value) {
					}
				});
			} catch (Throwable e) {
				UIUtils.showError(e);
			}
		}
		if (parentMenu != null)
			parentMenu.setDefaultAction(this);
	}

	public void export(final File file, final Callback<File> callback) throws Exception {
		final JasperPrint jrPrint = getReportViewer().getReport();
		if (jrPrint == null || jrPrint.getPages() == null)
			return;
		Job job = new Job(Messages.AExportAction_exportreport) {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				doExport(file, jrPrint, monitor);
				UIUtils.getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						callback.completed(file);
					}
				});
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.LONG);
		job.setUser(true);
		job.schedule();
	}

	@Override
	protected boolean calculateEnabled() {
		return getReportViewer().hasReport();
	}

	protected void exportWithProgress(File file, JRExportProgressMonitor monitor) throws Throwable {
		JRAbstractExporter<?, ?, ?, ?> exporter = getExporter(jContext, monitor, file);
		exporter.setExporterInput(new SimpleExporterInput(getReportViewer().getReport()));

		exporter.exportReport();
	}

	protected void setupReportConfiguration(SimpleReportExportConfiguration conf, JRExportProgressMonitor monitor) {
		conf.setProgressMonitor(monitor);

		String indPage = jContext.getProperty(JRExporterPreferencePage.EXPPARAM_INDEX_PAGE, "all"); //$NON-NLS-1$
		Pages p = new Pages().parseString(indPage);

		if (p.getPage() != null)
			conf.setPageIndex(p.getPage());
		else if (p.getFrom() != null) {
			conf.setStartPageIndex(p.getFrom());
			conf.setEndPageIndex(p.getTo());
		}
		conf.setOffsetX(jContext.getPropertyInteger(JRExporterPreferencePage.EXPPARAM_OFFSET_X));
		conf.setOffsetY(jContext.getPropertyInteger(JRExporterPreferencePage.EXPPARAM_OFFSET_Y));
	}

	protected abstract JRAbstractExporter<?, ?, ?, ?> getExporter(JasperReportsConfiguration jContext,
			JRExportProgressMonitor monitor, File file);

	public void doExport(File file, JasperPrint jrPrint, final IProgressMonitor monitor) {
		try {
			if (jrPrint != null && jrPrint.getPages() != null) {
				final Integer size = jrPrint.getPages().size();
				monitor.beginTask(Messages.AExportAction_exportreport, size);
				exportWithProgress(file, new JRExportProgressMonitor() {
					private int current = 0;

					@Override
					public void afterPageExport() {
						if (monitor.isCanceled())
							Thread.currentThread().interrupt();
						monitor.worked(1);
						monitor.subTask(MessageFormat
								.format(Messages.PageNumberContributionItem_page, new Integer(current++), size));
					}

				});
			}
		} catch (Throwable e) {
			UIUtils.showError(e);
		} finally {
			monitor.done();
		}
	}

}
