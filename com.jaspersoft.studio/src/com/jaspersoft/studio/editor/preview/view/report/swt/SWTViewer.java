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
package com.jaspersoft.studio.editor.preview.view.report.swt;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.viewer.ReportViewer;
import net.sf.jasperreports.eclipse.viewer.action.ZoomActualSizeAction;
import net.sf.jasperreports.eclipse.viewer.action.ZoomComboContributionItem;
import net.sf.jasperreports.eclipse.viewer.action.ZoomFitPageAction;
import net.sf.jasperreports.eclipse.viewer.action.ZoomFitPageWidthAction;
import net.sf.jasperreports.eclipse.viewer.action.ZoomInAction;
import net.sf.jasperreports.eclipse.viewer.action.ZoomOutAction;
import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.editor.preview.actions.export.AExportAction;
import com.jaspersoft.studio.editor.preview.actions.export.ExportAsJasperReportsAction;
import com.jaspersoft.studio.editor.preview.actions.export.ExportMenuAction;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.IPreferencePage;
import com.jaspersoft.studio.editor.preview.view.report.ExportMenu;
import com.jaspersoft.studio.editor.preview.view.report.IJRPrintable;
import com.jaspersoft.studio.editor.preview.view.report.swt.action.ExportImageAction;
import com.jaspersoft.studio.editor.preview.view.report.swt.action.FirstPageAction;
import com.jaspersoft.studio.editor.preview.view.report.swt.action.LastPageAction;
import com.jaspersoft.studio.editor.preview.view.report.swt.action.NextPageAction;
import com.jaspersoft.studio.editor.preview.view.report.swt.action.PageNumberContributionItem;
import com.jaspersoft.studio.editor.preview.view.report.swt.action.PreviousPageAction;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class SWTViewer extends APreview implements IJRPrintable, IPreferencePage {

	protected ReportViewer rptviewer;

	public SWTViewer(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	private AExportAction expAction;

	protected AExportAction createExporterAction(ReportViewer rptv) {
		if (expAction == null)
			expAction = createExporter(rptv);
		return expAction;
	}

	protected AExportAction createExporter(ReportViewer rptv) {
		return new ExportAsJasperReportsAction(rptviewer, jContext, null);
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		rptviewer = new ReportViewer(jContext);
		Control ctrl = rptviewer.createControl(composite);
		ctrl.setLayoutData(new GridData(GridData.FILL_BOTH));

		return composite;
	}

	@Override
	public void contribute2ToolBar(IToolBarManager tmanager) {
		super.contribute2ToolBar(tmanager);
		tmanager.add(new FirstPageAction(rptviewer));
		tmanager.add(new PreviousPageAction(rptviewer));
		tmanager.add(new PageNumberContributionItem(rptviewer));
		tmanager.add(new NextPageAction(rptviewer));
		tmanager.add(new LastPageAction(rptviewer));
		tmanager.add(new Separator());

		tmanager.add(new ZoomInAction(rptviewer));
		tmanager.add(new ZoomOutAction(rptviewer));

		tmanager.add(new ZoomComboContributionItem(rptviewer));
		tmanager.add(new ZoomFitPageWidthAction(rptviewer));
		tmanager.add(new ZoomFitPageAction(rptviewer));
		tmanager.add(new ZoomActualSizeAction(rptviewer));
		tmanager.add(new Separator());

		tmanager.add(new ExportImageAction(rptviewer));
		tmanager.add(new Separator());

		ExportMenuAction exportMenu = ExportMenu.getExportMenu(rptviewer, jContext);
		setDefaultExporter(exportMenu, createExporterAction(rptviewer));
		tmanager.add(exportMenu);
	}

	protected JasperPrint jrprint;

	public void setJRPRint(Statistics stats, JasperPrint jrprint) throws Exception {
		setJRPRint(stats, jrprint, false);
	}

	public void setJRPRint(Statistics stats, JasperPrint jrprint, boolean refresh) {
		int ind = Math.max(0, rptviewer.getPageIndex());
		if (jrprint != null)
			ind = Math.max(ind, jrprint.getPages().size());
		// if (tmanager != null) {
		// contribute2ToolBar(tmanager);
		// tmanager.update(true);
		// ((ToolBarManager) tmanager).getControl().pack();
		// }
		rptviewer.setReport(jrprint);
		rptviewer.setPageIndex(ind);
		rptviewer.gotoFirstPage();

		this.jrprint = jrprint;
	}

	private boolean refresh = false;

	@Override
	public void pageGenerated(final JasperPrint arg0, int page) {
		if (refresh)
			return;
		refresh = true;
		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				int ind = rptviewer.getPageIndex();
				rptviewer.setReport(arg0);
				rptviewer.setPageIndex(ind);
				jrprint = arg0;
				refresh = false;
			}
		});
	}

	@Override
	public void pageUpdated(final JasperPrint arg0, final int page) {
		if (rptviewer.getPageIndex() == page) {
			UIUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					rptviewer.setReport(arg0);
					rptviewer.setPageIndex(page);
					jrprint = arg0;
				}
			});
		}
	}

	public void setPageNumber(final int page) {
		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				rptviewer.setPageIndex(page);
			}
		});
	}

	@Override
	public PreferencePage getPreferencePage() {
		return null;
	}

}
