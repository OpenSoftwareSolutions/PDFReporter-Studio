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
package com.jaspersoft.studio.components.table.figure;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle;
import com.jaspersoft.studio.components.table.model.table.command.UpdateStyleCommand;
import com.jaspersoft.studio.components.table.part.TableEditPart;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.utils.IOUtils;
import com.jaspersoft.studio.utils.SelectionHelper;

/**
 * Class to handle a plugin transfer of a table style, and apply it to the table
 * 
 * @author Orlandin Marco
 *
 */
public class TableStyleTransferDropListener extends AbstractTransferDropTargetListener{

	public TableStyleTransferDropListener(EditPartViewer viewer){
		super(viewer);
	 	setTransfer(TableRestrictedTransferType.getInstance());
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
		if (getTargetEditPart() instanceof TableEditPart) {
			MTable tableModel = (MTable) ((EditPart)getTargetEditPart()).getModel();
			TemplateStyle style = (TemplateStyle) IOUtils.readFromByteArray((byte[])getCurrentEvent().data);
			if (style != null && style instanceof TableStyle){
				TableStyle selectedStyle = (TableStyle)style;
				if (tableModel != null) {
					Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
					MessageDialog question = new MessageDialog(shell,
							Messages.EditStyleAction_dialogTitle, null,
							Messages.EditStyleAction_dialogText,
							MessageDialog.QUESTION, new String[] {
									Messages.EditStyleAction_dialogUpdateButton,
									Messages.EditStyleAction_dialogNewButton,
									Messages.EditStyleAction_dialogCancelButton },
							0);
					int response = question.open();
					// response == 0 update the old styles, response == 1 create new styles, response == 2 cancel the operation
					if (response == 0 || response == 1) {
						UpdateStyleCommand updateCommand = new UpdateStyleCommand(tableModel, selectedStyle, response == 0);
						CommandStack cs = getCommandStack();
						if (cs!=null) cs.execute(updateCommand);
						else updateCommand.execute();
					}
				}
			}
		}
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
		if (ep instanceof TableEditPart) {
			return ep;
		}
		return null;
	}
	
	protected CommandStack getCommandStack() {
		IEditorPart activeJRXMLEditor = SelectionHelper.getActiveJRXMLEditor();
		if (activeJRXMLEditor != null && activeJRXMLEditor instanceof JrxmlEditor) {
			JrxmlEditor editor = (JrxmlEditor)activeJRXMLEditor;
			return (CommandStack)editor.getAdapter(CommandStack.class);
		}
		return null;
	}


	@Override
	protected void updateTargetRequest() {
		// TODO Auto-generated method stub
		
	}
}
