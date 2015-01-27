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

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.swt.widgets.WTextExpression;

/**
 *  Dialog used to edit the section Others of a pie chart. This dialog allow to define 
 *  the attribute like the label and the key of this section, and also its hyperlink, inside
 *  two distinct tabs
 * 
 * @author Orlandin Marco
 *
 */
public class OtherSectionPage extends FormDialog {

	/**
	 * Container of all the controls used to edit the hyperlink
	 */
	private HyperLinkPanel hyperlinkControls;
	
	/**
	 * Widget to input an expression for the key attribute of the section
	 */
	private WTextExpression otherKey;
	
	/**
	 * Widget to input an expression for the label attribute of the section
	 */
	private WTextExpression otherLabel;
	
	/**
	 * Expression with the value of the key attribute of the section
	 */
	private JRDesignExpression otherKeyExpression;
	
	/**
	 * Expression with the value of the label attribute of the section
	 */
	private JRDesignExpression otherLabelExpression;
	
	/**
	 * @param shell
	 * @param otherHyperlink the edited MHyperlink of the section
	 * @param otherKey the actual expression of the section for the property key
	 * @param otherLabel the actual expression of the section for the property label
	 */
	public OtherSectionPage(Shell shell, APropertyNode otherHyperlink, JRDesignExpression otherKey, JRDesignExpression otherLabel) {
		super(shell);
		hyperlinkControls = new HyperLinkPanel(otherHyperlink);
		this.otherKeyExpression = otherKey;
		this.otherLabelExpression = otherLabel;
	}
	
	@Override
	protected void createFormContent(IManagedForm mform) {
		super.createFormContent(mform);
		mform.getForm().setText(Messages.OtherSectionPage_dialogText);
		
		
		Composite body = mform.getForm().getBody();
		body.setLayout(new GridLayout(1,false));
		CTabFolder folder = new CTabFolder(body, SWT.BORDER);
		folder.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		CTabItem otherSectionValues = new CTabItem(folder, SWT.NONE);
		Composite valuesContainer = new Composite(folder, SWT.NONE); 
		valuesContainer.setLayout(new GridLayout(1,false));
		createValueControls(valuesContainer);
		otherSectionValues.setText(Messages.OtherSectionPage_groupValue);
		otherSectionValues.setControl(valuesContainer);
		
		CTabItem otherSectionHyperlink = new CTabItem(folder, SWT.NONE);
		Composite hyperlinkContainer = new Composite(folder, SWT.NONE); 
		hyperlinkContainer.setLayout(new GridLayout(1,false));
		createHyperlinkControls(hyperlinkContainer);
		otherSectionHyperlink.setText(Messages.OtherSectionPage_groupHyperlink);
		otherSectionHyperlink.setControl(hyperlinkContainer);
		
		folder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (((CTabFolder)e.getSource()).getSelectionIndex() == 1){
					if (hyperlinkControls.getElement() != null && hyperlinkControls.getElement().getValue() == null){
						hyperlinkControls.getElement().setValue(new JRDesignHyperlink());
					}
				}
			}
		});
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(body,"com.jaspersoft.studio.doc.editOtherSection");
	}
	
	/**
	 * Create the tab containing the controls to edit the key and label expression 
	 * 
	 * @param parent parent composite\tab of the controls
	 */
	private void createValueControls(Composite parent){
		Group otherKeyGroup = new Group(parent, SWT.NONE);
		otherKeyGroup.setText(Messages.OtherSectionPage_keyExpression);
		otherKeyGroup.setLayout(new GridLayout(1,false));
		otherKeyGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		otherKey = new WTextExpression(otherKeyGroup, SWT.NONE);
		otherKey.setLayoutData(new GridData(GridData.FILL_BOTH));
		otherKey.setExpression(otherKeyExpression);
		
		Group otherLabelGroup = new Group(parent, SWT.NONE);
		otherLabelGroup.setText(Messages.OtherSectionPage_labelExpression);
		otherLabelGroup.setLayout(new GridLayout(1,false));
		otherLabelGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		otherLabel = new WTextExpression(otherLabelGroup, SWT.NONE);
		otherLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		otherLabel.setExpression(otherLabelExpression);
	}
	
	/**
	 * Create the tab containing the controls to edit the hyperlink
	 * 
	 * @param parent parent composite\tab of the controls
	 */
	private void createHyperlinkControls(Composite parent){
		hyperlinkControls.createControls(parent);
	}
	
	
	/**
	 * Create an additional delete button after ok and cancel, that can be used to request
	 * the deletion of the hyperlink. Pressing this button return at the opener the ok result
	 * but the hyperlink value will be null
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		Button deleteButton = createButton(parent, IDialogConstants.ABORT_ID, Messages.HyperlinkDialog_deleteHyperlinkAction, false);
		deleteButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.OK);
				otherKeyExpression = otherKey.getExpression();
				otherLabelExpression = otherLabel.getExpression();
				getElement().setValue(null);
				close();
			}
		});
	}

	/**
	 * Return the defined JRDesignHyperlink, could be null
	 * 
	 * @return the hyperlink to insert the element
	 */
	public JRHyperlink getHyperlink(){
		return (JRHyperlink)getElement().getValue();
	}
	
	/**
	 * When ok is pressed all the values defined inside the widgets are set in 
	 * the edited MHyperlink and in the fields that store the expression for key and label
	 */
	@Override
	protected void okPressed() {
		hyperlinkControls.setAllExpressionValues();
		otherKeyExpression = otherKey.getExpression();
		otherLabelExpression = otherLabel.getExpression();
		super.okPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.OtherSectionPage_dialogTitle);
	}
	
	/**
	 * Return the edited hyperlink node
	 * @return the edited MHyperlink
	 */
	public APropertyNode getElement(){
		return hyperlinkControls.getElement();
	}
	
	/**
	 * Return the defined expression for the key
	 * 
	 * @return a JRDesignExpression, could be null
	 */
	public JRExpression getKeyExpression(){
		return otherKeyExpression;
	}
	
	/**
	 * Return the defined expression for the label
	 * 
	 * @return a JRDesignExpression, could be null
	 */
	public JRExpression getLabelExpression(){
		return otherLabelExpression;
	}

}
