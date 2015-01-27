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
package net.sf.jasperreports.eclipse.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.eclipse.viewer.DefaultHyperlinkHandler;
import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.eclipse.viewer.ReportViewer;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;

/*
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperDesignPreviewView.java 27 2009-11-11 12:40:27Z teodord $
 */
public class JasperReportsEditor extends EditorPart {
	private ReportViewer reportViewer = new ReportViewer(SWT.BORDER, new LocalJasperReportsContext(DefaultJasperReportsContext.getInstance()));
	private Control reportViewerControl;

	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);

		reportViewerControl = reportViewer.createControl(container);
		reportViewerControl.setLayoutData(new GridData(GridData.FILL_BOTH));

		reportViewer.addHyperlinkListener(new DefaultHyperlinkHandler());
	}

	/**
	 * Returns the report viewer used for viewing reports.
	 * 
	 * @return the report viewer
	 */
	public IReportViewer getReportViewer() {
		return reportViewer;
	}

	// private void initMenu() {
	// IMenuManager mm = getEditorSite().getActionBars().getMenuManager();
	//
	//		MenuManager export = new MenuManager("FIXMEMessages.getString(\"ViewerApp.exportMenuLabel\")"); //$NON-NLS-1$
	// export.add(new ExportAsPdfAction(reportViewer));
	// export.add(new ExportAsRtfAction(reportViewer));
	// export.add(new ExportAsJasperReportsAction(reportViewer));
	// export.add(new ExportAsHtmlAction(reportViewer));
	// export.add(new ExportAsSingleXlsAction(reportViewer));
	// export.add(new ExportAsMultiXlsAction(reportViewer));
	// export.add(new ExportAsCsvAction(reportViewer));
	// export.add(new ExportAsXmlAction(reportViewer));
	// export.add(new ExportAsXmlWithImagesAction(reportViewer));
	//
	//		MenuManager file = new MenuManager("FIXMEMessages.getString(\"ViewerApp.fileMenuLabel\")"); //$NON-NLS-1$
	// file.add(new ReloadAction(reportViewer));
	// file.add(new Separator());
	// file.add(export);
	// file.add(new Separator());
	// file.add(new PrintAction(reportViewer));
	// mm.add(file);
	//
	//		MenuManager view = new MenuManager("FIXMEMessages.getString(\"ViewerApp.viewMenuLabel\")"); //$NON-NLS-1$
	// view.add(new ZoomOutAction(reportViewer));
	// view.add(new ZoomInAction(reportViewer));
	// view.add(new Separator());
	// view.add(new ZoomActualSizeAction(reportViewer));
	// view.add(new ZoomFitPageAction(reportViewer));
	// view.add(new ZoomFitPageWidthAction(reportViewer));
	// mm.add(view);
	//
	//		MenuManager nav = new MenuManager("FIXMEMessages.getString(\"ViewerApp.navigateMenuLabel\")"); //$NON-NLS-1$
	// nav.add(new FirstPageAction(reportViewer));
	// nav.add(new PreviousPageAction(reportViewer));
	// nav.add(new NextPageAction(reportViewer));
	// nav.add(new LastPageAction(reportViewer));
	// mm.add(nav);
	// }

	// private void initToolBar() {
	// IToolBarManager tbManager =
	// getEditorSite().getActionBars().getToolBarManager();
	//
	// ExportMenuAction exportMenu = new ExportMenuAction(reportViewer);
	// IAction pdfAction = null;
	// exportMenu.getMenuManager().add(
	// pdfAction = new ExportAsPdfAction(reportViewer));
	// exportMenu.getMenuManager().add(
	// new ExportAsRtfAction(reportViewer));
	// exportMenu.getMenuManager().add(
	// new ExportAsJasperReportsAction(reportViewer));
	// exportMenu.getMenuManager().add(new ExportAsHtmlAction(reportViewer));
	// exportMenu.getMenuManager().add(
	// new ExportAsSingleXlsAction(reportViewer));
	// exportMenu.getMenuManager().add(
	// new ExportAsMultiXlsAction(reportViewer));
	// exportMenu.getMenuManager().add(new ExportAsCsvAction(reportViewer));
	// exportMenu.getMenuManager().add(new ExportAsXmlAction(reportViewer));
	// exportMenu.getMenuManager().add(
	// new ExportAsXmlWithImagesAction(reportViewer));
	// exportMenu.setDefaultAction(pdfAction);
	//
	// tbManager.add(exportMenu);
	// tbManager.add(new PrintAction(reportViewer));
	// tbManager.add(new Separator());
	// tbManager.add(new ZoomActualSizeAction(reportViewer));
	// tbManager.add(new ZoomFitPageAction(reportViewer));
	// tbManager.add(new ZoomFitPageWidthAction(reportViewer));
	// tbManager.add(new Separator());
	// tbManager.add(new ZoomOutAction(reportViewer));
	// tbManager.add(new ZoomComboContributionItem(reportViewer));
	// tbManager.add(new ZoomInAction(reportViewer));
	// }

	public void init(IEditorSite site, IEditorInput input) {
		setSite(site);
		setInput(input);

		InputStream is = null;
		String fileExtension = null;

		if (input instanceof IFileEditorInput) {
			try {
				IFile file = ((IFileEditorInput) input).getFile();
				is = file.getContents();
				fileExtension = file.getFileExtension();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else if (input instanceof FileStoreEditorInput) {
			try {
				FileStoreEditorInput fsei = (FileStoreEditorInput) input;
				is = new FileInputStream(((FileStoreEditorInput) input).getURI().getPath());
				fileExtension = fsei.getName().substring(fsei.getName().lastIndexOf('.') + 1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		if (is != null) {
			try {
				DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
				if (FileExtension.JRXML.equalsIgnoreCase(fileExtension)) {
					getReportViewer().setReport(new ReportConverter(context, JRXmlLoader.load(context, is), false).getJasperPrint());
				} else if (FileExtension.JASPER.equalsIgnoreCase(fileExtension)) {
					getReportViewer().setReport(new ReportConverter(context, (JRReport) JRLoader.loadObject(context, is), false).getJasperPrint());
				}
			} catch (JRException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	@Override
	public void setFocus() {
		reportViewerControl.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

}
