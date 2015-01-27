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
package com.jaspersoft.studio.editor.preview.toolbar;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.IParametrable;
import com.jaspersoft.studio.editor.preview.PreviewContainer;
import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.editor.preview.actions.ViewBookmarksAction;
import com.jaspersoft.studio.editor.preview.actions.ViewExporterAction;
import com.jaspersoft.studio.editor.preview.actions.ViewParametersAction;
import com.jaspersoft.studio.editor.preview.actions.ViewReportParametersAction;
import com.jaspersoft.studio.editor.preview.actions.ViewSortFieldsAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.toolbar.ToolItemContribution;

public class LeftToolBarManager extends ATopToolBarManager {

	public LeftToolBarManager(PreviewJRPrint container, Composite parent) {
		super(container, parent);
	}

	private Label label;
	private Composite prmtbar;
	private ViewParametersAction vprmAction;
	private ViewReportParametersAction vprmrepAction;
	private ViewSortFieldsAction vsortAction;
	private ViewBookmarksAction vTocAction;
	// private ViewExecutionInfoAction vexecAction;
	private ViewExporterAction vexpAction;

	@Override
	protected void createToolBar(Composite parent) {
		prmtbar = new Composite(parent, SWT.NONE);
		prmtbar.setLayout(new GridLayout(2, false));
		prmtbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = new Label(prmtbar, SWT.NONE);
		label.setText(Messages.LeftToolBarManager_label);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		topToolBar = new ToolBar(prmtbar, SWT.FLAT | SWT.WRAP | SWT.RIGHT);

		tbManager = new ToolBarManager(topToolBar);

		fillToolbar(tbManager);

		refreshToolbar();
	}

	protected void fillToolbar(IToolBarManager tbManager) {
		PreviewContainer pvcont = (PreviewContainer) container;
		if (vprmAction == null)
			vprmAction = new ViewParametersAction(pvcont.getLeftContainer());
		tbManager.add(vprmAction);
		if (vprmrepAction == null)
			vprmrepAction = new ViewReportParametersAction(pvcont.getLeftContainer());
		tbManager.add(vprmrepAction);
		if (vsortAction == null)
			vsortAction = new ViewSortFieldsAction(pvcont.getLeftContainer());
		tbManager.add(vsortAction);
		// if (vexecAction == null)
		// vexecAction = new ViewExecutionInfoAction(pvcont);
		// tbManager.add(vexecAction);
		addExporterSettings(tbManager, pvcont);

		if (vTocAction == null)
			vTocAction = new ViewBookmarksAction(pvcont.getLeftContainer());
		tbManager.add(vTocAction);

		addPin(container, tbManager);
	}

	protected void addExporterSettings(IToolBarManager tbManager, IParametrable pvcont) {
		if (vexpAction == null)
			vexpAction = new ViewExporterAction(pvcont.getLeftContainer());
		tbManager.add(vexpAction);
	}

	public void addPin(final PreviewJRPrint container, IToolBarManager tbManager) {
		ToolItemContribution titem = new ToolItemContribution("id", SWT.CHECK); //$NON-NLS-1$
		tbManager.add(titem);

		tbManager.update(true);

		final ToolItem item = titem.getToolItem();

		item.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/eclipseicons/pin.png")); //$NON-NLS-1$
		item.setToolTipText(Messages.LeftToolBarManager_pintooltip);
		item.setSelection(!container.isHideParameters());
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				container.setHideParameters(!container.isHideParameters());
			}
		});
	}

	public void setLabelText(String key) {
		label.setText(key);
		prmtbar.layout();
	}

}
