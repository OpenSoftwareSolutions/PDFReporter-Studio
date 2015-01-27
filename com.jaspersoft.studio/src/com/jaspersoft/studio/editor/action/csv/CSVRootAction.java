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
package com.jaspersoft.studio.editor.action.csv;

import net.sf.jasperreports.engine.JRPropertiesMap;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.gef.decorator.csv.NameChooserDialog;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.property.SetValueCommand;

/**
 * This action is performed on a static text element but the property is not set on the element
 * but on the root of the document. The value of the attribute is asked to the user trough a dialog
 * 
 * @author Orlandin Marco
 *
 */
public class CSVRootAction extends CSVAction {
	
	private String columnsName;
	
	public CSVRootAction(IWorkbenchPart part,String actionId, String actionName){
		super(part,actionId,actionName);
	}
	
	/**
	 * Return the dialog title using the action id to choose which one to return
	 * @return
	 */
	private String getDialogTitle(){
		if (getId() == CSVAction.FIELD_DELIMITER) return Messages.CSVRootAction_FieldDelimiterDialog;
		if (getId() == CSVAction.RECORD_DELIMITER) return Messages.CSVRootAction_RecordDelimiterDialog;
		return Messages.CSVRootAction_GeneralDialog;
	}
	
	/**
	 * Since this action is used to set a value for a field, it can't be considered as checked or unchecked, 
	 * so this method will return always false and the action will be show as unchecked
	 */
	@Override
	public boolean isChecked() {
			return false;
	}
	
	private String getPropertyValue(APropertyNode node, String nullValue){
		JRPropertiesMap v = (JRPropertiesMap)node.getPropertyValue(MGraphicElement.PROPERTY_MAP);
		if (v==null) v = new JRPropertiesMap();
		String result = v.getProperty(getId());
		return result != null ? result : nullValue;
	}
	
	
	/**
	 * Performs the create action on the root element, if the root is available
	 */
	@Override
	public void run() {
		APropertyNode root = getRoot();
		NameChooserDialog dialog = new NameChooserDialog(Display.getCurrent().getActiveShell(), getDialogTitle(), getPropertyValue(root,"")); //$NON-NLS-1$
		int dialogResult = dialog.open();
		if (dialogResult == NameChooserDialog.OK)
			columnsName = dialog.getName();
			execute(createCommand());
	}
	

	/**
	 * Returns the command to change the attribute of this action. the attribute is set 
	 * on the root of the report, if it is available
	 * 
	 * @param fieldValue the value inserted by the user for the field
	 * @return the command to set the value of the attribute to fieldValue
	 */
	@Override
	protected Command createCommand() {
		APropertyNode root = getRoot();
		JSSCompoundCommand command = new JSSCompoundCommand(root);
		command.setDebugLabel(getText());
		if (root != null){
			command.add(createCommand(root, columnsName));
		}
		return command;
	}
	
	/**
	 * Create the command for the selected action
	 * @param model Model of the selected item
	 * @return the command to execute
	 */
	public Command createCommand(APropertyNode model, String fieldValue){
		SetValueCommand cmd = new SetValueCommand();
		cmd.setTarget(model);
		cmd.setPropertyId(MGraphicElement.PROPERTY_MAP);
		JRPropertiesMap v = (JRPropertiesMap)model.getPropertyValue(MGraphicElement.PROPERTY_MAP);
		if (v==null) v = new JRPropertiesMap();
		v.setProperty(getId(), fieldValue);
		cmd.setPropertyValue(v);
		return cmd;
	}
}
