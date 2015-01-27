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
package com.jaspersoft.studio.components.chart.wizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;

/**
 * Hyperlink dialog used to edit\remove the content of an MHyperlink element
 * 
 * @author Orlandin Marco
 *
 */
public class HyperlinkPage extends FormDialog {

	/**
	 * Container of all the controls used to edit the hyperlink
	 */
	private HyperLinkPanel controlPanel;
	
	/**
	 * Title of the dialog
	 */
	private String dialogTitle;
	
	/** 
	 * @param shell
	 * @param hyperLinkNode Hyperlink node to edit, must be an MHyperlink
	 * @param dialogTitle title of the dialog
	 */
	public HyperlinkPage(Shell shell, APropertyNode hyperLinkNode, String dialogTitle) {
		super(shell);
		controlPanel = new HyperLinkPanel(hyperLinkNode);
		this.dialogTitle = dialogTitle;
	}
	
	/**
	 * Create an additional delete button after ok and cancel, that can be used to request
	 * the deletion of the hyperlink
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		Button deleteButton = createButton(parent, IDialogConstants.ABORT_ID, Messages.HyperlinkDialog_deleteHyperlinkAction, false);
		deleteButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(IDialogConstants.ABORT_ID);
				close();
			}
		});
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.HyperlinkDialog_hyperlinkDialogName);
	}
	
	/**
	 * Set the title and create the controls
	 */
	@Override
	protected void createFormContent(IManagedForm mform) {
		super.createFormContent(mform);
		mform.getForm().setText(dialogTitle);
		mform.getForm().getBody().setLayout(new GridLayout(1,false));
		controlPanel.createControls(mform.getForm().getBody());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(mform.getForm().getBody(),
															"com.jaspersoft.studio.doc.createHyperlinkSection");
	}
	
	/**
	 * When ok is pressed all the values defined inside the widget are set in 
	 * the edited MHyperlink also
	 */
	@Override
	protected void okPressed() {
		controlPanel.setAllExpressionValues();
		super.okPressed();
	}
	
	/**
	 * Return the edited Mhyperlink
	 *
	 */
	public APropertyNode getElement(){
		return controlPanel.getElement();
	}

}
