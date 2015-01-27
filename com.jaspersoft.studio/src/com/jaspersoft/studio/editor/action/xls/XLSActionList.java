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
package com.jaspersoft.studio.editor.action.xls;

import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.action.CustomSelectionAction;
import com.jaspersoft.studio.editor.action.pdf.PropertiesList;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.property.SetValueCommand;

/**
 * This type of action can be viewed as an aggregate of more action, it can set more than on attribute
 * 
 * @author Orlandin Marco
 * 
 */
public class XLSActionList extends CustomSelectionAction {

	/**
	 * The value to set on the attributes
	 */
	private String[] values;

	/**
	 * Ids of the attributes to set
	 */
	private String[] attributeIds;

	private int actionCheckId;

	public XLSActionList(IWorkbenchPart part, String actionId, String[] attributeIds, String value, String actionName) {
		this(part, actionId, attributeIds, initializeValuesArray(value, attributeIds.length), actionName);
	}

	public XLSActionList(IWorkbenchPart part, String actionId, String[] attributeIds, String[] values, String actionName) {
		super(part);
		this.attributeIds = attributeIds;
		setId(actionId);
		// the property need to be registered
		PropertiesList.AddItem(actionId);
		setText(actionName);
		this.values = values;
		actionCheckId = -1;
	}

	public XLSActionList(IWorkbenchPart part, String actionId, String[] attributeIds, String[] values, String actionName,
			int actionCheckId) {
		super(part, IAction.AS_CHECK_BOX);
		this.attributeIds = attributeIds;
		setId(actionId);
		PropertiesList.AddItem(actionId);
		setText(actionName);
		this.values = values;
		this.actionCheckId = actionCheckId;
	}

	public boolean isChecked() {
		List<Object> graphicalElements = editor.getSelectionCache().getSelectionModelForType(MGraphicElement.class);
		if (graphicalElements.isEmpty() || actionCheckId == -1) {
			return false;
		}
		String attributeId = attributeIds[actionCheckId];
		for (Object element : graphicalElements) {
			MGraphicElement model = (MGraphicElement) element;
			JRPropertiesMap v = (JRPropertiesMap) model.getPropertyValue(MGraphicElement.PROPERTY_MAP);
			if (v == null)
				return false;
			else {
				Object oldValue = v.getProperty(attributeId);
				if (oldValue == null || !oldValue.equals(values[actionCheckId]))
					return false;
			}
		}
		return true;
	}

	/**
	 * Inizialize the values array with a single value
	 * 
	 * @param value
	 *          the value to put into the array
	 * @param lenght
	 *          the lenght of the array
	 */
	private static String[] initializeValuesArray(String value, int lenght) {
		String[] result = new String[lenght];
		for (int i = 0; i < lenght; i++)
			result[i] = value;
		return result;
	}

	/**
	 * Create the command for the selected action
	 * 
	 * @param model
	 *          Model of the selected item
	 * @return the command to execute
	 */
	public Command createCommand(MGraphicElement model) {
		SetValueCommand cmd = new SetValueCommand();
		cmd.setTarget(model);
		cmd.setPropertyId(MGraphicElement.PROPERTY_MAP);
		JRPropertiesMap v = (JRPropertiesMap) model.getPropertyValue(MGraphicElement.PROPERTY_MAP);
		if (v == null) {
			v = new JRPropertiesMap();
			for (int i = 0; i < attributeIds.length; i++)
				v.setProperty(attributeIds[i], values[i]);
		} else {
			for (int i = 0; i < attributeIds.length; i++) {
				String name = attributeIds[i];
				String value = values[i];
				v.removeProperty(name);
				// Add the property only if it has a value
				if (value != null)
					v.setProperty(name, value);
			}
		}
		cmd.setPropertyValue(v);
		return cmd;
	}

	/**
	 * Performs the create action on the selected objects.
	 */
	@Override
	public void run() {
		execute(createCommand());
	}

	@Override
	protected Command createCommand() {
		List<Object> graphicalElements = editor.getSelectionCache().getSelectionModelForType(MGraphicElement.class);
		if (graphicalElements.isEmpty())
			return null;
		JSSCompoundCommand command = new JSSCompoundCommand(null);
		command.setDebugLabel(getText());
		for (Object element : graphicalElements) {
			MGraphicElement graphElement = (MGraphicElement) element;
			command.add(createCommand(graphElement));
			command.setReferenceNodeIfNull(graphElement);
		}
		return command;
	}

}
