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

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;

/**
 * Adapter for the dataset run used in a generic component element (inside the dataset attribute).
 * This adapter can be used when dealing with generic dialogs/wizards/forms that modify a "generic" dataset run (instance of {@link JRDesignDatasetRun}).
 * This class can be extended by specific components (Maps, Charts, Widgets, etc.): however in most case scenarios there should
 * be no need to add further additional methods.
 *  
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 * @see JRDesignDatasetRun
 * @see IEditableDataset
 * @see IEditableDatasetRun
 */
public class ComponentElementDatasetRunAdapter implements IEditableDatasetRun {

	/* The wrapped JRDatasetRun instance for the dataset of the generic component */
	protected JRDesignDatasetRun datasetRun;
	
	/* A reference to the "editable version" of the dataset holding the dataset run information */
	protected IEditableDataset editableDataset;
	
	public ComponentElementDatasetRunAdapter(IEditableDataset editableDataset) {
		this.editableDataset = editableDataset;
		this.datasetRun = (JRDesignDatasetRun)editableDataset.getJRElementDataset().getDatasetRun();
	}

	public void setDatasetName(String newDatasetName) {
		if (!useReportMainDataset()) {
			datasetRun.setDatasetName(newDatasetName);
		}
	}

	public void setParametersMapExpression(JRExpression newParametersMapExp) {
		if (!useReportMainDataset()) {
			datasetRun.setParametersMapExpression(newParametersMapExp);
		}
	}

	public void setParameters(JRDatasetParameter[] newParameters) {
		if (!useReportMainDataset()) {
			// Remove all existing parameters
			JRDatasetParameter[] oldParameters = datasetRun.getParameters();
			for (JRDatasetParameter p : oldParameters){
				datasetRun.removeParameter(p);
			}
			// Add the new ones
			for (JRDatasetParameter p : newParameters){
				try {
					datasetRun.addParameter(p);
				} catch (JRException e) {
					UIUtils.showError(e);
				}
			}
		}
	}

	public void addParameter(JRDatasetParameter newParameter) {
		if (!useReportMainDataset()){
			try {
				datasetRun.addParameter(newParameter);
			} catch (JRException e) {
				UIUtils.showError(e);
			}
		}
	}

	public void removeParameter(JRDatasetParameter oldParameter) {
		if (!useReportMainDataset()){
			datasetRun.removeParameter(oldParameter);
		}
	}

	public void setConnectionExpression(JRExpression newConnectionExp) {
		if (!useReportMainDataset()) {
			datasetRun.setConnectionExpression(newConnectionExp);
		}
	}

	public void setDataSourceExpression(JRExpression newDataSourceExp) {
		if (!useReportMainDataset()) {
			datasetRun.setDataSourceExpression(newDataSourceExp);
		}
	}

	public IEditableDataset getEditableDataset() {
		return this.editableDataset;
	}

	public JRDatasetRun getJRDatasetRun() {
		return this.datasetRun;
	}

	public boolean useReportMainDataset() {
		if (this.datasetRun==null)
			return true;
		return false;
	}

	public void resetDatasetRun(boolean nullableFlag) {
		if (nullableFlag){
			this.datasetRun=null;
			this.editableDataset.setDatasetRun(null);
		}
		else {
			this.datasetRun=new JRDesignDatasetRun();
			this.editableDataset.setDatasetRun(datasetRun);
		}
	}

}
