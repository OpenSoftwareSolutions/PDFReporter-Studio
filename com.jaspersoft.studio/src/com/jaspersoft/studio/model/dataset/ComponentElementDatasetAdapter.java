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

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;


/**
 * Adapter for a dataset instance used in a generic component element.
 * The dataset used MUST be a valid subclass of the {@link JRDesignElementDataset} class.
 * This adapter can be used as input when dealing with generic dialogs/wizards/forms that modify an a "generic" dataset (instance of {@link JRElementDataset}).
 *  
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 *  @see JRElementDataset
 *  @see JRDesignElementDataset
 * 	@see JRDataset
 */
public class ComponentElementDatasetAdapter implements IEditableDataset{
	/* The wrapped dataset instance for the component element */
	private JRDesignElementDataset componentElementDataset;
	
	/* The JasperDesign object for the component element*/
	private JasperReportsConfiguration jConfig;
	
	public ComponentElementDatasetAdapter(JRDesignElementDataset widgetDataset, JasperReportsConfiguration jasperDesign){
		this.componentElementDataset=widgetDataset;
		this.jConfig=jasperDesign;
	}

	public void setDatasetRun(JRDatasetRun newDatasetRun) {
		componentElementDataset.setDatasetRun(newDatasetRun);
	}

	public void setIncrementGroup(JRGroup newIncrementGroup) {
		componentElementDataset.setIncrementGroup(newIncrementGroup);
	}

	public void setIncrementType(IncrementTypeEnum newIncrementType) {
		componentElementDataset.setIncrementType(newIncrementType);
	}

	public void setIncrementWhenExpression(JRExpression newIncrementWhenExpression) {
		componentElementDataset.setIncrementWhenExpression(newIncrementWhenExpression);
	}

	public void setResetGroup(JRGroup newResetGroup) {
		componentElementDataset.setResetGroup(newResetGroup);
	}

	public void setResetType(ResetTypeEnum newResetType) {
		componentElementDataset.setResetType(newResetType);
	}

	public JRElementDataset getJRElementDataset() {
		return componentElementDataset;
	}

	public JasperDesign getJasperDesign() {
		return jConfig.getJasperDesign();
	}
}
