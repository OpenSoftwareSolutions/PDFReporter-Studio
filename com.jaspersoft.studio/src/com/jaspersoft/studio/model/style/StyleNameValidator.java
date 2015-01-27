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
package com.jaspersoft.studio.model.style;

import java.text.MessageFormat;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptors.AbstractJSSCellEditorValidator;

/**
 * Validator to check if a name for a style name is valid. It is valid essentially if it 
 * is unique. If it is not then an error message is returned
 * 
 * @author Orlandin Marco
 *
 */
public class StyleNameValidator extends AbstractJSSCellEditorValidator {
	
	/**
	 * The object must be the new name for the variable, and using the target check if there are other styles with the same name
	 */
	@Override
	public String isValid(Object value) {
		JasperDesign d = getTarget().getJasperDesign();
		if (d !=null ){
			JRStyle style = d.getStylesMap().get(value);
			if (style != null) System.out.println(style.getName());
			if (style != null && getTarget().getValue() != style){
				String message = MessageFormat.format(Messages.VariableNameValidator_variableDuplicatedName, new Object[] { value });
				return message;
			}
		}
		return null;
	}

}
