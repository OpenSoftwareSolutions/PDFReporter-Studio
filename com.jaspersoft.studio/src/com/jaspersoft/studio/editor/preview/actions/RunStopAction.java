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
package com.jaspersoft.studio.editor.preview.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.IRunReport;
import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.messages.Messages;

public class RunStopAction extends Action implements IMenuCreator {
	public static final String MODERUN_LOCAL = "RUNLOCAL";
	public static final String MODERUN_JIVE = "RUNJIVE";

	public static final String ID = "PREVIEWRELOADACTION"; //$NON-NLS-1$
	private PreviewJRPrint editor;

	public RunStopAction(PreviewJRPrint editor) {
		super();
		this.editor = editor;
		setId(ID);
		setMenuCreator(this);
		setDescription(Messages.RunStopAction_runreport_desc);
		setToolTipText(Messages.RunStopAction_runreport_desc);
		setImageDescriptor(
				JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/start_task.gif")); //$NON-NLS-1$
		setDisabledImageDescriptor(
				JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/start_task.gif")); //$NON-NLS-1$
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && editor.isNotRunning();
	}

	@Override
	public void run() {
		if (editor instanceof IRunReport)
			((IRunReport) editor).runReport();
	}

	private Menu listMenu;

	public void dispose() {
		if (listMenu != null)
			listMenu.dispose();
	}

	public Menu getMenu(Control parent) {
		if (listMenu != null)
			listMenu.dispose();
		listMenu = new Menu(parent);

		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MenuItem menuItem = (MenuItem) e.getSource();
				menuItem.setSelection(true);
				if (editor instanceof IRunReport)
					((IRunReport) editor).setMode((String) menuItem.getData("run.key"));
				run();
			}
		};

		MenuItem m1 = new MenuItem(listMenu, SWT.PUSH);
		m1.setText("Run Report");
		m1.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/start_task.gif"));
		m1.addSelectionListener(listener);
		m1.setData("run.key", MODERUN_LOCAL);

		m1 = new MenuItem(listMenu, SWT.RADIO);
		m1.setText("Run Interactive Report (Jive)");
		m1.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/start_task.gif"));
		m1.addSelectionListener(listener);
		m1.setData("run.key", MODERUN_JIVE);

		return listMenu;
	}

	public Menu getMenu(Menu parent) {
		return null;
	}
}
