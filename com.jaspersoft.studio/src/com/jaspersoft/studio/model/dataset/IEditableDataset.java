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

import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

/**
 * This interface gives the ability to change the information regarding a dataset (usually a {@link JRElementDataset} instance).
 * A list of setters is provided so that when needed the user can implement a custom adapter to work with a specific dataset.
 * 
 * @author mrabbi
 * 
 * @see JRElementDataset
 *
 */
public interface IEditableDataset {

	/**
	 * Sets the new dataset run information for the dataset.
	 * 
	 * @param newDatasetRun
	 */
	void setDatasetRun(JRDatasetRun newDatasetRun);	
	
	/**
	 * Sets the new group for the increment feature.
	 * 
	 * @param newIncrementGroup
	 */
	void setIncrementGroup(JRGroup newIncrementGroup);
	
	/**
	 * Sets the new kind of increment. 
	 * 
	 * @param newIncrementType
	 */
	void setIncrementType(IncrementTypeEnum newIncrementType);
	
	/**
	 * Sets the new conditional increment expression.
	 * 
	 * @param newIncrementWhenExpression
	 */
	void setIncrementWhenExpression(JRExpression newIncrementWhenExpression);
	
	/**
	 * Sets the new reset group for the dataset.
	 * 
	 * @param newResetGroup
	 */
	void setResetGroup(JRGroup newResetGroup);
	
	/**
	 * Sets the new kind of reset.
	 * 
	 * @param newResetType
	 */
	void setResetType(ResetTypeEnum newResetType);
	
	/**
	 * Returns the instance of the element dataset we are modifying.
	 * 
	 * @return the edited dataset
	 */
	JRElementDataset getJRElementDataset();
	
	/**
	 * Returns the {@link JasperDesign} instance of the component, to which the element dataset belongs to. 
	 * 
	 * @return
	 */
	JasperDesign getJasperDesign();
}
