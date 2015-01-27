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
package com.jaspersoft.studio.model.dataset;

import java.text.MessageFormat;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptors.AbstractJSSCellEditorValidator;

/**
 * Validator to check if a name for a field is valid. It is valid essentially if it 
 * is unique. If it is not then an error message is returned
 * 
 * @author Orlandin Marco
 *
 */
public class DatasetNameValidator extends AbstractJSSCellEditorValidator {
	
	/**
	 * The object must be the new name for the field, and using the target check if there are other fields
	 * with the same name, under the same dataset
	 */
	@Override
	public String isValid(Object value) {
		JasperDesign design = getTarget().getJasperDesign();
		if (design !=null ){
			JRDataset dataset = design.getDatasetMap().get(value);
			if (dataset != null && getTarget().getValue() != dataset){
				String message = MessageFormat.format(Messages.WizardDatasetNewPage_name_already_exists, new Object[] { value });
				return message;
			}
		}
		return null;
	}

}
