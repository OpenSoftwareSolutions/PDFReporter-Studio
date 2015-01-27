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
package com.jaspersoft.studio.model.field;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSortField;
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
 * of a field and in this case search for SortField that are using that field and 
 * update the reference name too
 * 
 * @author Orlandin Marco
 */
public class PostSetFieldName implements IPostSetValue {

	/**
	 * Get a list of all the Sortfield that are using a field, and if one of them is using
	 * the renamed field then its name also is updated with the new one
	 */
	@Override
	public Command postSetValue(IPropertySource target, Object prop, Object newValue, Object oldValue) {
		JSSCompoundCommand c = new JSSCompoundCommand(null);
		if (target instanceof MField && prop.equals(JRDesignField.PROPERTY_NAME)) {
			MField targetNode = (MField) target;
			c.setReferenceNodeIfNull(targetNode);
			JRDesignDataset d = ModelUtils.getDataset(targetNode);
			if (d != null){
				for(INode field : ModelUtils.getSortFields(targetNode)){
					APropertyNode sortField = (APropertyNode)field;
					JRDesignSortField jrField = (JRDesignSortField) sortField.getValue();
					if (jrField != null){
						Object sortFieldName = jrField.getName();
						Object sortFieldType = jrField.getType();
						if (SortFieldTypeEnum.FIELD.equals(sortFieldType) && sortFieldName != null && sortFieldName.equals(oldValue)){
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
