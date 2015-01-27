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
package com.jaspersoft.studio.jface.dialogs;

import java.util.TimeZone;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.WTimeZoneList;

public class TimeZoneDialog extends Dialog {
	
	private TimeZone timeZone = null;
	private WTimeZoneList wTimeZoneList;
	private Button btnDefaultTimeZone;

	/**
	 * Create the TimeZoneDialog.
	 * @param parentShell
	 * @param timeZone
	 */
	public TimeZoneDialog(Shell parentShell, TimeZone timeZone) {
		super(parentShell);
		this.timeZone = timeZone;
	}
	
	/**
	 * Configure Shell attributes like setText
	 */
	@Override
	protected void configureShell(Shell shell) {
    super.configureShell(shell);
    shell.setText(Messages.TimeZoneDialog_0);
  }

	/**
	 * Create contents of the TimeZoneDialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
	  // UI elements
		Composite container = (Composite) super.createDialogArea(parent);
		FillLayout fl_container = new FillLayout(SWT.HORIZONTAL);
		fl_container.marginWidth = 5;
		fl_container.marginHeight = 5;
		container.setLayout(fl_container);
		
		Group grpTimeZones = new Group(container, SWT.NONE);
		grpTimeZones.setText(Messages.TimeZoneDialog_1);
		FillLayout fl_grpTimeZones = new FillLayout(SWT.HORIZONTAL);
		fl_grpTimeZones.marginWidth = 5;
		fl_grpTimeZones.marginHeight = 5;
		grpTimeZones.setLayout(fl_grpTimeZones);
		
		wTimeZoneList = new WTimeZoneList(grpTimeZones, SWT.NONE);

	  // UI elements listeners
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				okPressed();
			}
		};
		wTimeZoneList.setListSelectionListener(selectionAdapter);
		
	  // init UI elements values
		initElements();
		
		return container;
	}

	private void initElements() {
		
		wTimeZoneList.setSelection(timeZone);
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		btnDefaultTimeZone = createButton(parent, 2, Messages.TimeZoneDialog_2, false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		
		btnDefaultTimeZone.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				wTimeZoneList.setSelection(TimeZone.getDefault());
				okPressed();
			}
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	@Override
	protected void okPressed() {
		timeZone = wTimeZoneList.getSelectedTimeZone();
		super.okPressed();
	}
	
  //GETTERS AND SETTERS
	/**
	 * Return the timeZone created by the TimeZoneDialog.
	 * If the Empty value has been selected, this method returns null.
	 * @return timeZone or null
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}
}
