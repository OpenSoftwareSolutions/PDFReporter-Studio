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
package com.jaspersoft.studio.components.crosstab.figure;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.crosstab.command.UpdateCrosstabStyleCommand;
import com.jaspersoft.studio.components.crosstab.model.dialog.CrosstabStyle;
import com.jaspersoft.studio.components.crosstab.part.CrosstabEditPart;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.utils.IOUtils;
import com.jaspersoft.studio.utils.SelectionHelper;

/**
 * Class to handle a transfer of a table style, and apply it to the table
 * 
 * @author Orlandin Marco
 *
 */
public class CrosstabStyleTransferDropListener extends AbstractTransferDropTargetListener{

	public CrosstabStyleTransferDropListener(EditPartViewer viewer){
		super(viewer);
	 	setTransfer(CrosstrabRestrictedTransferType.getInstance());
	}
	
	/**
	 * Updates the target EditPart.
	 */
	protected void updateTargetEditPart() {
		setTargetEditPart(calculateTargetEditPart());
	}
	
	/**
	 * Override of the leave because the for some reason
	 * SWT call it before the drop action when the mouse button is 
	 * released. and this normally call the unload (removed from the override)
	 * that set the target to null
	 */
	public void dragLeave(DropTargetEvent event) {
		setCurrentEvent(event);
	}

	
	/**
	 * Drop action, get a TableStyle from the event, the style that will be applied to the table
	 */
	@Override
	protected void handleDrop() {
		if (getTargetEditPart() instanceof CrosstabEditPart) {
			MCrosstab crosstabModel = (MCrosstab) ((EditPart)getTargetEditPart()).getModel();
			TemplateStyle style = (TemplateStyle) IOUtils.readFromByteArray((byte[])getCurrentEvent().data);
			if (style != null && style instanceof CrosstabStyle){
				CrosstabStyle selectedStyle = (CrosstabStyle) style;
				if (crosstabModel != null) {
					Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
					MessageDialog question = new MessageDialog(shell, Messages.EditCrosstabStyleAction_questionTitle, null, Messages.EditCrosstabStyleAction_questionText, MessageDialog.QUESTION, 
							new String[]{Messages.EditCrosstabStyleAction_questionUpdate, 
										 Messages.EditCrosstabStyleAction_questionNewStyles, 
										 Messages.EditCrosstabStyleAction_questionCancel}, 0);
					int response = question.open();
					// response == 0 update the old styles, response == 1 create new styles, response == 2 cancel the operation
					if (response == 0 || response == 1) {
						UpdateCrosstabStyleCommand updateCommand = new UpdateCrosstabStyleCommand(crosstabModel, selectedStyle,response == 0);
						CommandStack cs = getCommandStack();
						if (cs!=null) cs.execute(updateCommand);
						else updateCommand.execute();
					}
				}
			}
		}
	}

	/**
	 * Get the command stack from the active editor. A command stack is used to allow the undo operation
	 * 
	 * @return a commandstack of the editor, or null if it can not be found.
	 */
	protected CommandStack getCommandStack() {
		IEditorPart activeJRXMLEditor = SelectionHelper.getActiveJRXMLEditor();
		if (activeJRXMLEditor != null && activeJRXMLEditor instanceof JrxmlEditor) {
			JrxmlEditor editor = (JrxmlEditor)activeJRXMLEditor;
			return (CommandStack)editor.getAdapter(CommandStack.class);
		}
		return null;
	}
	

	/**
	 * Find the edit part to return using the mouse cursor actual position, and return it. But only if 
	 * the part is a TableEditPart, otherwise return null, disabling the drop
	 * 
	 * @return a reference to an editpart under the mouse cursor, if it is a TableEditPart, otherwise 
	 * null;
	 */
	protected EditPart calculateTargetEditPart() {	
		EditPart ep = getViewer().findObjectAt(getDropLocation());
		if (ep instanceof CrosstabEditPart) {
			return ep;
		}
		return null;
	}


	@Override
	protected void updateTargetRequest() {
		// TODO Auto-generated method stub
	}
}
