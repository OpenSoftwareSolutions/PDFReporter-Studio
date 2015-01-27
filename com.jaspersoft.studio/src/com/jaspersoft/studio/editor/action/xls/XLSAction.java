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
 * Action used to set an XSL attribute
 * 
 * @author Orlandin Marco
 * 
 */
public class XLSAction extends CustomSelectionAction {

	/** Embedded attributes ids */
	public static String FIT_COL_ID = "net.sf.jasperreports.export.xls.auto.fit.column";

	public static String FIT_ROW_ID = "net.sf.jasperreports.export.xls.auto.fit.row";

	public static String BREAK_AFTER_ROW_ID = "net.sf.jasperreports.export.xls.break.after.row";

	public static String BREAK_BEFORE_ROW_ID = "net.sf.jasperreports.export.xls.break.before.row";

	public static String CELL_HIDDEN_ID = "net.sf.jasperreports.export.xls.cell.hidden";

	public static String CELL_LOCKED_ID = "net.sf.jasperreports.export.xls.cell.locked";

	public static String AUTOFILTER_ID = "net.sf.jasperreports.export.xls.auto.filter";

	public static String FREEZE_ROW_ID = "net.sf.jasperreports.export.xls.freeze.row.edge";

	public static String FREEZE_COL_ID = "net.sf.jasperreports.export.xls.freeze.column.edge";

	private String value;

	private String attributeId;

	private String[] attributeToRemove;

	public XLSAction(IWorkbenchPart part, String actionId, String value, String actionName) {
		this(part, actionId, actionId, value, actionName);
	}

	public XLSAction(IWorkbenchPart part, String actionId, String attributeId, String value, String actionName) {
		super(part, IAction.AS_CHECK_BOX);
		setId(actionId);
		this.attributeId = attributeId;
		this.value = value;
		// the property need to be registered
		PropertiesList.AddItem(actionId);
		setText(actionName);
		attributeToRemove = null;
	}

	/**
	 * Uses the attribute to remove parameter to define the attribute that should be removed when the attributeId is set.
	 * This is done to define attribute mutually exclusives with the others
	 */
	public XLSAction(IWorkbenchPart part, String actionId, String attributeId, String value, String actionName,
			String[] attributeToRemove) {
		this(part, actionId, actionId, value, actionName);
		this.attributeToRemove = attributeToRemove;
	}

	public boolean isChecked() {
		List<Object> graphicalElements = editor.getSelectionCache().getSelectionModelForType(MGraphicElement.class);
		if (graphicalElements.isEmpty()) {
			return false;
		}
		for (Object element : graphicalElements) {
			MGraphicElement model = (MGraphicElement) element;
			JRPropertiesMap v = (JRPropertiesMap) model.getPropertiesMap();
			if (v == null)
				return false;
			else {
				Object oldValue = v.getProperty(attributeId);
				if (oldValue == null || !oldValue.equals(value))
					return false;
			}
		}
		return true;
	}

	/**
	 * Remove from the property map all the attributes in the attributeToRemove array
	 * 
	 * @param map
	 */
	private void removeAttributes(JRPropertiesMap map) {
		if (attributeToRemove != null) {
			for (String attributeName : attributeToRemove)
				map.removeProperty(attributeName);
		}
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
		Object oldValue = null;
		if (v == null) {
			v = new JRPropertiesMap();
		} else {
			oldValue = v.getProperty(attributeId);
			v.removeProperty(attributeId);
		}
		if (value != null && !value.equals(oldValue)) {
			v.setProperty(attributeId, value);
			removeAttributes(v);
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
		freshChecked = false;
		return command;
	}

}
