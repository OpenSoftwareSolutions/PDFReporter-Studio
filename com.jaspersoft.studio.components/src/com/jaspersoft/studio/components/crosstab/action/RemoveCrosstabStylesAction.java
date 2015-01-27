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

import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.model.command.ForceRefreshCommand;
import com.jaspersoft.studio.model.style.command.DeleteStyleCommand;

/**
 * Action to delete all the styles from a crosstab element
 * 
 * @author Orlandin Marco
 *
 */
public class RemoveCrosstabStylesAction extends ACachedSelectionAction {
	
	/**
	 * boolean flag to specify if the style element should be deleted or only be removed from the table
	 */
	private boolean deleteStyles = false;
	
	/**
	 * hashmap used internally to keep trace of the deleted styles
	 */
	private HashSet<String> deletedStyles;
	
	/**
	 * Jasperdesign of the actually handled table
	 */
	private JasperDesign design;
	
	/**
	 * The id of the action
	 */
	public static final String ID = "com.jaspersoft.studio.components.table.action.RemoveCrosstabStyles";  //$NON-NLS-1$
	
	public RemoveCrosstabStylesAction(IWorkbenchPart part) {
		super(part);
		setText(Messages.RemoveStylesAction_actionTitle);
		setId(RemoveCrosstabStylesAction.ID);
		setImageDescriptor(Activator.getDefault().getImageDescriptor("icons/crosstab-style-remove-16.png")); //$NON-NLS-1$
	}

	/**
	 * The action is enable only if enabled if and only if one of the edit part of the selection 
	 * has as model type an MCrosstab
	 */
	@Override
	protected boolean calculateEnabled() {
		List<Object> crosstabs = editor.getSelectionCache().getSelectionModelForType(MCrosstab.class);
		return crosstabs.size() > 0;
	}
	
	/**
	 * Return a list of the selected edit parts that has a model of type MCrosstab
	 * 
	 * @return a not null list of edit part with an MCrosstab as model
	 */
	private List<EditPart> getSelectedTables(){
		List<EditPart> result = editor.getSelectionCache().getSelectionModelPartForType(MCrosstab.class);
		return result;
	}

	/**
	 * Execute the action
	 */
	@Override
	public void run() {
		deleteStyles = false;
		MessageDialog dialog = new MessageDialog(null, Messages.RemoveStylesAction_messageTitle, null, Messages.RemoveStylesAction_messageText, MessageDialog.QUESTION, 
												 new String[] {Messages.RemoveStylesAction_option1, Messages.RemoveStylesAction_option2, Messages.RemoveStylesAction_option3  }, 2);
		int selection = dialog.open();
		if (selection != 2){
			deleteStyles = selection == 0;
			List<EditPart> parts = getSelectedTables();
			execute(changeStyleCommand(parts));
			for(EditPart part : parts){
				if (part instanceof FigureEditPart) ((FigureEditPart)part).refreshVisuals();
			}
		}
	}
	
	/**
	 * Create the command to remove the style from a single cell and to delete the style 
	 * itself if the deleteStyle flag is enabled and if the command to delete the style
	 * was not already generated
	 * 
	 * @param cell the cell from where the style must be removed 
	 * @param container compound command where the new commands will be stored
	 */
	protected void createCommand(JRCellContents cell, JSSCompoundCommand container){
		if (cell != null && cell instanceof JRDesignCellContents){
			container.add(new RemoveStyleCommand((JRDesignCellContents)cell));
			if (deleteStyles && cell.getStyle() != null){
				JRStyle style = cell.getStyle();
				if (!deletedStyles.contains(style.getName())){
					deletedStyles.add(style.getName());
					container.add(new DeleteStyleCommand(design, (JRDesignStyle)style));
				}
			}
		}
	}
	
	
	/**
	 * 
	 * Generate the command to remove all the styles from the crosstab, it's essentially a compound command
	 * composed of many commands
	 * 
	 * @param editParts the edit parts containing an MCrosstab as model
	 * 
	 * @return the command to remove all the styles
	 */
	protected Command changeStyleCommand(List<EditPart> editParts) {
		JSSCompoundCommand command = new JSSCompoundCommand(null);
		deletedStyles = new HashSet<String>();
		for(EditPart editPart : editParts){
			MCrosstab crosstabModel = (MCrosstab)editPart.getModel();
			command.setReferenceNodeIfNull(crosstabModel);
			design = crosstabModel.getJasperDesign();
			JRDesignCrosstab crosstab = (JRDesignCrosstab)crosstabModel.getValue();
			//This command is added before and after all the other commands to force its
			//refresh when the other commands are executed ore undone
			command.add(new ForceRefreshCommand(crosstabModel));
			for (JRCrosstabRowGroup rowGroup : crosstab.getRowGroupsList()){
				JRDesignCrosstabRowGroup designGroup = (JRDesignCrosstabRowGroup)rowGroup;
				createCommand(designGroup.getTotalHeader(), command);
				createCommand(designGroup.getHeader(), command);
			}
			for (JRCrosstabColumnGroup colGroup : crosstab.getColumnGroupsList()){
				JRDesignCrosstabColumnGroup designGroup = (JRDesignCrosstabColumnGroup)colGroup;
				createCommand(designGroup.getTotalHeader(), command);
				createCommand(designGroup.getHeader(), command);
			}
			for(JRCrosstabCell dataCell : crosstab.getCellsList()){
				createCommand(dataCell.getContents(), command);
			}
			command.add(new ForceRefreshCommand(crosstabModel));
		}
		return command;
	}
}
