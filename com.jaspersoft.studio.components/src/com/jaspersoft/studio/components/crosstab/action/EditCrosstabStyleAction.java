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
package com.jaspersoft.studio.components.crosstab.action;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.crosstab.command.UpdateCrosstabStyleCommand;
import com.jaspersoft.studio.components.crosstab.model.crosstab.command.wizard.CrosstabStyleWizard;
import com.jaspersoft.studio.components.crosstab.model.dialog.CrosstabStyle;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;

/**
 * Action to open the Style dialog and use it to change the style of a Crosstab
 * 
 * @author Orlandin Marco
 *
 */
public class EditCrosstabStyleAction extends ACachedSelectionAction {
	
	/**
	 * The id of the action
	 */
	public static final String ID = "com.jaspersoft.studio.components.crosstab.action.EditStyle";  //$NON-NLS-1$
	
	public EditCrosstabStyleAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.CrosstabStyleWizard_actionName);
		setId(ID);
		setImageDescriptor(Activator.getDefault().getImageDescriptor("icons/crosstab-style-16.png")); //$NON-NLS-1$
	}

	/**
	 * The action is enable only if enabled if and only if the first element of the selection 
	 * is a CrosstabEditPart with inside an MCrosstab
	 */
	@Override
	protected boolean calculateEnabled() {
		List<Object> crosstabs = editor.getSelectionCache().getSelectionModelForType(MCrosstab.class);
		return (crosstabs.size() ==1);
	}

	/**
	 * Execute the action
	 */
	@Override
	public void run() {
		//Create the wizard
		CrosstabStyleWizard wizard = new CrosstabStyleWizard();
		WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard){
			//Ovverride this method to change the default text of the finish button with another text
			@Override
			protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
				Button button = super.createButton(parent, id, label, defaultButton);
				if (id == IDialogConstants.FINISH_ID) button.setText(Messages.EditCrosstabStyleAction_okButton);
				return button;
			}
		};
		if (dialog.open() == Dialog.OK){
			//If the user close the dialog with ok then a message box is shown to ask how to edit the styles
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			MessageDialog question = new MessageDialog(shell, Messages.EditCrosstabStyleAction_questionTitle, null, Messages.EditCrosstabStyleAction_questionText, MessageDialog.QUESTION, 
														new String[]{Messages.EditCrosstabStyleAction_questionUpdate, 
																	 Messages.EditCrosstabStyleAction_questionNewStyles, 
																	 Messages.EditCrosstabStyleAction_questionCancel}, 0);
			int response = question.open();
			//response == 0 update the old styles, response == 1 create new styles, response == 2 cancel the operation
			if (response == 0 || response == 1){
				CrosstabStyle selectedStyle = wizard.getTableStyle();
				List<Object> crosstabs = editor.getSelectionCache().getSelectionModelForType(MCrosstab.class);
				MCrosstab tableModel = (MCrosstab)crosstabs.get(0);
				execute(changeStyleCommand(tableModel, selectedStyle,response == 0));
			} 
		}
	}

	
	/**
	 * 
	 * Return the command to change the crosstab style
	 * 
	 * @param crosstab the model of the crosstab
	 * @param newStyle the new CrosstabStyle defined by the user
	 * @param updateOldStyles true if the new styles will overwrite the old ones, false if the old ones will keep and 
	 * the new ones will have a different name
	 * @return the command to update the styles of the crosstab
	 */
	protected Command changeStyleCommand(MCrosstab crosstab, CrosstabStyle newStyle, boolean updateOldStyles) {
		JSSCompoundCommand command = new JSSCompoundCommand(crosstab);
		command.setDebugLabel(getText());
		UpdateCrosstabStyleCommand updateCommand = new UpdateCrosstabStyleCommand(crosstab, newStyle,updateOldStyles);
		command.add(updateCommand);
		return command;
	}
}
