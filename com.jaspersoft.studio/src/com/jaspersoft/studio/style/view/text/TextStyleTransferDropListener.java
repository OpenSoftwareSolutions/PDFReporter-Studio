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
package com.jaspersoft.studio.style.view.text;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorPart;

import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.gef.parts.text.StaticTextFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.text.TextFieldFigureEditPart;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.model.text.MTextElement;
import com.jaspersoft.studio.utils.IOUtils;
import com.jaspersoft.studio.utils.SelectionHelper;

/**
 * Class to handle a plugin transfer of a text style, and apply it to the text element
 * 
 * @author Orlandin Marco
 *
 */
public class TextStyleTransferDropListener extends AbstractTransferDropTargetListener{

	public TextStyleTransferDropListener(EditPartViewer viewer){
		super(viewer);
	 	setTransfer(TextRestrictedTransferType.getInstance());
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
	 * Drop action, get a TextStyle from the event, the style that will be applied to the text element
	 */
	@Override
	protected void handleDrop() {
		if (getTargetEditPart() instanceof StaticTextFigureEditPart || getTargetEditPart() instanceof TextFieldFigureEditPart) {
			MTextElement textModel = (MTextElement) ((EditPart)getTargetEditPart()).getModel();
			TemplateStyle style = (TemplateStyle) IOUtils.readFromByteArray((byte[])getCurrentEvent().data);
			if (style != null && style instanceof TextStyle){
				TextStyle selectedStyle = (TextStyle)style;
				if (textModel != null) {
					CommandStack cs = getCommandStack();
					UpdateStyleCommand command = new UpdateStyleCommand(textModel, selectedStyle);
					if (cs!=null) cs.execute(command);
					else command.execute();
				}
			}
		}
	}
	
	/**
	 * Find the edit part to return using the mouse cursor actual position, and return it. But only if 
	 * the part is a StaticTextFigureEditPart or a TextFieldFigureEditPart, otherwise return null, disabling the drop
	 * 
	 * @return a reference to an editpart under the mouse cursor, if it is a StaticTextFigureEditPart or a TextFieldFigureEditPart, 
	 * otherwise null;
	 */
	protected EditPart calculateTargetEditPart() {	
	 EditPart ep = getViewer().findObjectAt(getDropLocation());
		if (ep instanceof StaticTextFigureEditPart || ep instanceof TextFieldFigureEditPart) {
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
	}
}
