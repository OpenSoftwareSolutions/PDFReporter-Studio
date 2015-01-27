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

import net.sf.jasperreports.eclipse.viewer.DefaultHyperlinkHandler;
import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.eclipse.viewer.ReportViewer;
import net.sf.jasperreports.eclipse.viewer.action.ZoomActualSizeAction;
import net.sf.jasperreports.eclipse.viewer.action.ZoomComboContributionItem;
import net.sf.jasperreports.eclipse.viewer.action.ZoomFitPageAction;
import net.sf.jasperreports.eclipse.viewer.action.ZoomFitPageWidthAction;
import net.sf.jasperreports.eclipse.viewer.action.ZoomInAction;
import net.sf.jasperreports.eclipse.viewer.action.ZoomOutAction;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/*
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JasperDesignPreviewView.java 27 2009-11-11 12:40:27Z teodord $
 */
public class ReportPreviewView extends ViewPart {
	public static final String ID = "net.sf.jasperreports.views.reportPreview"; //$NON-NLS-1$

	private Composite container = null;
	private ReportViewer reportViewer = new ReportViewer(SWT.BORDER | SWT.NO_FOCUS, new LocalJasperReportsContext(DefaultJasperReportsContext.getInstance()));
	// FIXME add IPartListener2 as follows
	// http://pookzilla.net/wp/2006/10/link-to-editor/

	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			boolean unset = true;

			if (selection instanceof IStructuredSelection) {
				IStructuredSelection strSel = (IStructuredSelection) selection;
				if (strSel.size() == 1) {
					final Object sel = strSel.getFirstElement();
					if (sel instanceof IFile) {
						ReportPreviewUtil.loadFileIntoViewer((IFile) sel, getReportViewer(), getSite().getShell().getDisplay());
						unset = false;
					}
				}
			}
			if (unset) {
				getReportViewer().setReport(null);
			}
		}
	};

	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, "com.jaspersoft.studio.doc.view_preview"); //$NON-NLS-1$

		Control reportViewerControl = reportViewer.createControl(container);
		reportViewerControl.setLayoutData(new GridData(GridData.FILL_BOTH));
		initToolBar();

		reportViewer.addHyperlinkListener(new DefaultHyperlinkHandler());
		getSite().getPage().addSelectionListener(listener);
	}

	/**
	 * Returns the report viewer used for viewing reports.
	 * 
	 * @return the report viewer
	 */
	public IReportViewer getReportViewer() {
		return reportViewer;
	}

	@Override
	public void setFocus() {
		container.setFocus();
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(listener);
	}

	private void initToolBar() {
		IToolBarManager tbManager = getViewSite().getActionBars().getToolBarManager();

		tbManager.add(new Separator());
		tbManager.add(new ZoomActualSizeAction(reportViewer));
		tbManager.add(new ZoomFitPageAction(reportViewer));
		tbManager.add(new ZoomFitPageWidthAction(reportViewer));
		tbManager.add(new Separator());
		tbManager.add(new ZoomOutAction(reportViewer));
		tbManager.add(new ZoomComboContributionItem(reportViewer));
		tbManager.add(new ZoomInAction(reportViewer));
	}
}
