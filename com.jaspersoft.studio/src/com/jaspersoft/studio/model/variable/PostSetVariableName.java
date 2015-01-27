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
package com.jaspersoft.studio.model.variable;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.property.IPostSetValue;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * Class used when a property is changed. Check if the changed property is a name 
 * of a variable and in this case search for SortField that are using that variable and 
 * update the reference name too
 * 
 * @author Orlandin Marco
 */
public class PostSetVariableName implements IPostSetValue {

	/**
	 * Get a list of all the Sortfield that are using a variable, and if one of them is using
	 * the renamed variable then its name also is updated with the new one
	 */
	@Override
	public Command postSetValue(IPropertySource target, Object prop, Object newValue, Object oldValue) {
		JSSCompoundCommand c = new JSSCompoundCommand(null);
		if (target instanceof MVariable && prop.equals(JRDesignVariable.PROPERTY_NAME)) {
			MVariable targetNode = (MVariable) target;
			c.setReferenceNodeIfNull(targetNode);
			JRDesignDataset d = ModelUtils.getDataset(targetNode);
			if (d != null){
				for(INode field : ModelUtils.getSortFields(targetNode)){
					APropertyNode sortField = (APropertyNode)field;
					JRDesignSortField jrField = (JRDesignSortField) sortField.getValue();
					if (jrField != null){
						Object fieldName = jrField.getName();
						Object fieldType = jrField.getType();
						if (SortFieldTypeEnum.VARIABLE.equals(fieldType) && fieldName != null && fieldName.equals(oldValue)){
							SetValueCommand updateSFCommand = new SetValueCommand();
							updateSFCommand.setTarget(sortField);
							updateSFCommand.setPropertyId(JRDesignSortField.PROPERTY_NAME);
							updateSFCommand.setPropertyValue(newValue);
							c.add(updateSFCommand);
						}
					}
				}
			}
		}
		return c;
	}

}
