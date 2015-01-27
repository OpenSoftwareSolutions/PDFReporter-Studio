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
package com.jaspersoft.studio.editor.action.snap;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class SizeGridAction extends AResourcePreferenceAction {
	private final class SizeDialog extends FormDialog {
		private int w;
		private int h;

		private SizeDialog(Shell shell, Dimension d) {
			super(shell);
			w = d.width;
			h = d.height;
		}

		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(Messages.SizeGridAction_grid_editor);
		}

		@Override
		protected void createFormContent(IManagedForm mform) {
			mform.getForm().setText(Messages.SizeGridAction_grid_size);

			FormToolkit toolkit = mform.getToolkit();

			mform.getForm().getBody().setLayout(new GridLayout(4, false));

			toolkit.createLabel(mform.getForm().getBody(), Messages.SizeGridAction_spacing_x + ":"); //$NON-NLS-1$
			final Spinner width = new Spinner(mform.getForm().getBody(), SWT.BORDER);
			width.setValues(w, 0, Integer.MAX_VALUE, 0, 1, 10);
			width.setToolTipText(Messages.SizeGridAction_grid_space_width_tool_tip);
			width.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					w = width.getSelection();
				}
			});

			toolkit.createLabel(mform.getForm().getBody(), Messages.SizeGridAction_spacing_y + ":"); //$NON-NLS-1$

			final Spinner height = new Spinner(mform.getForm().getBody(), SWT.BORDER);
			height.setValues(h, 0, Integer.MAX_VALUE, 0, 1, 10);
			height.setToolTipText(Messages.SizeGridAction_grid_space_height_tool_tip);
			height.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					h = height.getSelection();
				}
			});
		}

		public int getWidth() {
			return w;
		}

		public int getHeight() {
			return h;
		}
	}

	public static final String ID = "sizegridaction"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param diagramViewer
	 *          the GraphicalViewer whose grid enablement and visibility properties are to be toggled
	 */
	public SizeGridAction(JasperReportsConfiguration jrConfig) {
		super(jrConfig);
		setText(Messages.SizeGridAction_set_grid_size);
		setToolTipText(Messages.SizeGridAction_set_grid_size_tool_tip);
		setId(ID);
	}

	@Override
	protected void doRun() throws Exception {
		int x = getStore().getInt(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEX);
		int y = getStore().getInt(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEY);

		SizeDialog dlg = new SizeDialog(UIUtils.getShell(), new Dimension(x, y));
		if (dlg.open() == Window.OK) {
			ScopedPreferenceStore store = getStore();
			store.setValue(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEX, dlg.getWidth());
			store.setValue(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEY, dlg.getHeight());

			store.save();
		}
	}

}
