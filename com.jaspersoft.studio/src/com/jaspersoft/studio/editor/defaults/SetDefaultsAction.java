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
package com.jaspersoft.studio.editor.defaults;

import java.text.MessageFormat;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MGraphicElement;

/**
 * 
 * Action to add an element to the currently selected template set file. If
 * there is not a selected template set the user is prompt to crate a new 
 * one and if it is created the element is added to it
 * 
 * @author Orlandin Marco
 *
 */
public class SetDefaultsAction extends ACachedSelectionAction {

	/**
	 * Custom MessageDialog to show a checkbox when the exported
	 * element uses styles (by his own or because there is a default style).
	 * The checkbox allow to choose if the attributes should be read also
	 * from the styles hierarchy or only on the element 
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class CheckboxMessageDialog extends MessageDialog {

		/**
		 * True if the attributes should be read also from the styles, false otherwise
		 */
		private boolean getFromStyles = false;
		
		public CheckboxMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
			super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels,defaultIndex);
		}
		
		/**
		 * Create the checkbox area but only if the element is using a style
		 */
		protected Control createCustomArea(Composite parent) {
			if (element != null && JRStyleResolver.getBaseStyle(element.getValue()) != null){
				Composite container = new Composite(parent, SWT.NONE);
				container.setLayout(new GridLayout(1,false));
				final Button checkButton = new Button(container, SWT.CHECK);
				checkButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						getFromStyles = checkButton.getSelection();
					}
				});
				checkButton.setText(Messages.SetDefaultsAction_exportStyleCheckbox);
				return container;
			} else return null;
		}
		
		/**
		 * Return if the style attribute should be preserved
		 * 
		 * @return True if the attributes should be read also from the styles, false otherwise
		 */
		public boolean isGetFromStyles(){
			return getFromStyles;
		}
		
	}
	
	/**
	 * Id of the action
	 */
  public static final String ID = "SetDefaultElementAction"; //$NON-NLS-1$

  /**
   * Element to add to the Template Set
   */
	private MGraphicElement element;
	
	public SetDefaultsAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	@Override
	protected void init() {
		super.init();
		setText(Messages.SetDefaultsAction_text);
		setToolTipText(Messages.SetDefaultsAction_tooltip);
		setId(ID);
		setEnabled(false);
	}
	
	@Override
	public void run() {
		if (DefaultManager.INSTANCE.hasDefault()){
			String message = MessageFormat.format(Messages.SetDefaultsAction_message1, new Object[]{DefaultManager.INSTANCE.getDefaultName()});
			CheckboxMessageDialog dialog = new CheckboxMessageDialog(UIUtils.getShell(), Messages.SetDefaultsAction_messageTitle, null, message, MessageDialog.QUESTION, new String[]{Messages.common_yes, Messages.common_no}, 1); 
			if (dialog.open() == 0){
				DefaultManager.INSTANCE.addElementToCurrentDefault(element, dialog.isGetFromStyles());
			}
		} else {
			CheckboxMessageDialog dialog = new CheckboxMessageDialog(UIUtils.getShell(), Messages.SetDefaultsAction_messageTitle, null, Messages.SetDefaultsAction_message2, MessageDialog.QUESTION, new String[]{Messages.common_yes,  Messages.common_no}, 1);  //$NON-NLS-1$
			if (dialog.open() == 0){
				DefaultNewWizard newWizard = new DefaultNewWizard();
				WizardDialog newDialog = new WizardDialog(Display.getDefault().getActiveShell(), newWizard);
				if (newDialog.open() == WizardDialog.OK){
					IFile templateFile = newWizard.getReportFile();
					String templatePath = templateFile.getRawLocation().makeAbsolute().toOSString();
					DefaultManager.INSTANCE.addDefaultFile(templatePath, true);
					DefaultManager.INSTANCE.addElementToCurrentDefault(element, dialog.isGetFromStyles());
				}
			}
		}
	}
		
	/**
	 * Only work if the selected element is an MGraphicalElement
	 */
	@Override
	protected boolean calculateEnabled() {
		List<Object> elements = editor.getSelectionCache().getSelectionModelForType(MGraphicElement.class);
		if (elements.size() == 1){
			element = (MGraphicElement)elements.get(0);
			return true;
		} {
			element = null;
			return false;
		}
	}

}
