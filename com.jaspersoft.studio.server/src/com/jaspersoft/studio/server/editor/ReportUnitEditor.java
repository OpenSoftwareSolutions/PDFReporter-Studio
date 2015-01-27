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
package com.jaspersoft.studio.server.editor;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.IParametrable;
import com.jaspersoft.studio.editor.preview.IRunReport;
import com.jaspersoft.studio.editor.preview.MultiPageContainer;
import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.AViewsFactory;
import com.jaspersoft.studio.editor.report.CachedSelectionProvider;
import com.jaspersoft.studio.editor.report.CommonSelectionCacheProvider;
import com.jaspersoft.studio.editor.util.StringInput;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.server.editor.action.RunStopAction;
import com.jaspersoft.studio.swt.toolbar.ToolItemContribution;
import com.jaspersoft.studio.swt.widgets.CSashForm;

public class ReportUnitEditor extends PreviewJRPrint implements IRunReport, IParametrable, CachedSelectionProvider {
	public static final String ID = "com.jaspersoft.studio.server.editor.ReportUnitEditor";
	private String reportUnitURI;

	public ReportUnitEditor() {
		super(false);
	}

	@Override
	protected void loadJRPrint(IEditorInput input) throws PartInitException {
		try {
			reportUnitURI = FileUtils.readInputStreamAsString(((StringInput) getEditorInput()).getStorage().getContents());
		} catch (Exception e1) {
			throw new PartInitException(e1.getMessage(), e1);
		}
		UIUtils.getDisplay().asyncExec(new Runnable() {

			public void run() {
				runReport();
			}
		});

	}

	@Override
	public void setCurrentViewer(String viewerKey, boolean refresh) {
		super.setCurrentViewer(viewerKey, refresh);

		getTopToolBarManager(null).contributeItems(getRightContainer().getViewer(viewerKey));
	}

	@Override
	protected void afterRightSwitchView() {
		runReport();
	}

	public void runReport() {
		if (isNotRunning()) {
			// check if we can run the report
			topToolBarManager.setEnabled(false);
			topToolBarManager1.setEnabled(false);
			leftToolbar.setEnabled(false);
			getLeftContainer().setEnabled(false);
			getLeftContainer().switchView(null, ReportRunControler.FORM_PARAMETERS);

			reportControler.setReportUnit(reportUnitURI);
		}
	}

	@Override
	protected PreviewTopToolBarManager getTopToolBarManager1(Composite container) {
		if (topToolBarManager1 == null)
			topToolBarManager1 = new PreviewTopToolBarManager(this, container);
		return (PreviewTopToolBarManager) topToolBarManager1;
	}

	private CSashForm sashform;
	private LeftToolBarManager leftToolbar;

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));

		getTopToolBarManager1(container);
		getTopToolBarManager(container);

		Button lbutton = new Button(container, SWT.PUSH);
		lbutton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		lbutton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/application-sidebar-expand.png"));
		lbutton.setToolTipText("Show Parameters");
		lbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sashform.upRestore();
			}
		});

		sashform = new CSashForm(container, SWT.HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		sashform.setLayoutData(gd);

		createLeft(parent, sashform);

		createRight(sashform);

		sashform.setWeights(new int[] { 100, 150 });
	}

	protected void createLeft(Composite parent, SashForm sf) {
		Composite leftComposite = new Composite(sf, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		leftComposite.setLayout(layout);

		leftToolbar = new LeftToolBarManager(this, leftComposite);

		final Composite cleftcompo = new Composite(leftComposite, SWT.NONE);
		cleftcompo.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		cleftcompo.setLayoutData(new GridData(GridData.FILL_BOTH));
		cleftcompo.setLayout(new StackLayout());

		Composite bottom = new Composite(leftComposite, SWT.NONE);
		bottom.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		bottom.setLayout(new GridLayout(2, false));

		ToolBar tb = new ToolBar(bottom, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(new RunStopAction(this));
		ToolItemContribution tireset = new ToolItemContribution("", SWT.PUSH); //$NON-NLS-1$
		tbm.add(tireset);
		tbm.update(true);
		ToolItem toolItem = tireset.getToolItem();
		toolItem.setText(Messages.PreviewContainer_resetactiontitle);
		toolItem.setToolTipText(Messages.PreviewContainer_resetactiontooltip);
		toolItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				reportControler.resetParametersToDefault();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		tbm.update(true);

		getLeftContainer().populate(cleftcompo, getReportControler().createControls(cleftcompo, jrContext));
		getLeftContainer().switchView(null, ReportRunControler.FORM_PARAMETERS);
	}

	private MultiPageContainer leftContainer;

	public MultiPageContainer getLeftContainer() {
		if (leftContainer == null)
			leftContainer = new MultiPageContainer() {
				@Override
				public void switchView(Statistics stats, APreview view) {
					super.switchView(stats, view);
					for (String key : pmap.keySet()) {
						if (pmap.get(key) == view) {
							leftToolbar.setLabelText(key);
							break;
						}
					}
				}
			};
		return leftContainer;
	}

	@Override
	public void setNotRunning(boolean stopRunning) {
		super.setNotRunning(stopRunning);
		if (stopRunning) {
			getLeftContainer().setEnabled(true);
			leftToolbar.setEnabled(true);
		}
	}

	@Override
	public boolean switchRightView(APreview view, Statistics stats, MultiPageContainer container) {
		reportControler.viewerChanged(view);
		return super.switchRightView(view, stats, container);
	}

	public String getDefaultViewerKey() {
		if (currentViewer == null)
			currentViewer = ReportUnitViewsFactory.DEFAULT;
		return currentViewer;
	}

	@Override
	public AViewsFactory getViewFactory() {
		if (viewFactory == null)
			viewFactory = new ReportUnitViewsFactory();
		return viewFactory;
	}

	public void showParameters(boolean showprm) {
		if (showprm)
			sashform.upRestore();
		else
			sashform.upHide();
	}

	private ReportRunControler reportControler;

	private ReportRunControler getReportControler() {
		if (reportControler == null)
			reportControler = new ReportRunControler(this);
		return reportControler;
	}

	@Override
	public void setMode(String mode) {

	}

	private CommonSelectionCacheProvider selectionCache = new CommonSelectionCacheProvider();

	@Override
	public CommonSelectionCacheProvider getSelectionCache() {
		return selectionCache;
	}
}
