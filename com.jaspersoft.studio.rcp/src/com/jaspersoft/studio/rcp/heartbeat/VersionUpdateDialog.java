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
package com.jaspersoft.studio.rcp.heartbeat;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.rcp.messages.Messages;

public class VersionUpdateDialog extends Dialog {
	private boolean isNotShowAgain = false;
	private String newVersion;
	private String optMessage;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public VersionUpdateDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Version Update");
		newShell.setSize(600, 250);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public void setNewVersion(String msg) {
		this.newVersion = msg;
	}

	public void setOptionalMessage(String msg) {
		this.optMessage = msg;
	}

	public boolean isNotShowAgain() {
		return isNotShowAgain;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout());

		Link link = new Link(container, SWT.WRAP);
		link.setText(new MessageFormat(Messages.VersionUpdateDialog_0)
				.format(new Object[] { newVersion }));
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("You have selected: " + e.text);
				try {
					PlatformUI.getWorkbench().getBrowserSupport()
							.getExternalBrowser().openURL(new URL(e.text));
				} catch (PartInitException ex) {
					UIUtils.showError(ex);
				} catch (MalformedURLException ex) {
					UIUtils.showError(ex);
				}
			}
		});
		link.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (optMessage != null) {
			Label lblOptMessage = new Label(container, SWT.WRAP);
			lblOptMessage.setText(optMessage);
			lblOptMessage.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		final Button btnShowAgain = new Button(container, SWT.CHECK);
		btnShowAgain.setText(Messages.VersionUpdateDialog_1);
		btnShowAgain.setSelection(isNotShowAgain);
		btnShowAgain.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isNotShowAgain = btnShowAgain.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

}
