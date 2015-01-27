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
package com.jaspersoft.studio.property.dataset.wizard;

import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.dataset.DatasetRunWidgetRadio;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

public class WizardConnectionPage extends JSSHelpWizardPage implements IExpressionContextSetter{
	

	private DatasetRunWidgetRadio dsRun;
	private JRDesignDatasetRun jrDsRun;
	private ExpressionContext expContext;

	
//	/**
//	 * Force this wizard page to work with the given dataset run...
//	 * If the dataset run does not contain any JRDesignDatasetRun,
//	 * a new one is created.
//	 * 
//	 * @param datasetrun
//	 */
//	public void setDataSetRun(MDatasetRun datasetrun) {
//		
//		if (datasetrun != null && datasetrun.getValue() != null)
//		{
//			jrDsRun = datasetrun.getValue();
//		}
//		
//		dsRun.setData(jrDsRun);
//		
//	}
	
	/*
	public void setDataSetRun(MDatasetRun datasetrun) {
		this.datasetrun = datasetrun;
		jrDsRun = (JRDesignDatasetRun) datasetrun.getValue();
		if (jrDsRun == null) {
			jrDsRun = new JRDesignDatasetRun();
			datasetrun.setValue(jrDsRun);
		}
		if (dsRun != null)
			dsRun.setData(jrDsRun);
	}

	public MDatasetRun getDataSetRun() {
		return datasetrun;
	}
	*/
	
	
	public WizardConnectionPage() {
		super("connectionpage"); //$NON-NLS-1$
		setTitle(Messages.common_connection);
		setDescription(Messages.WizardConnectionPage_description);
		
		jrDsRun = new JRDesignDatasetRun();
		
		// By default we set the dataset run to use the report connection...
		JRDesignExpression exp = new JRDesignExpression();
		exp.setText("$P{REPORT_CONNECTION}");
		jrDsRun.setConnectionExpression( exp );
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_DATASET_CONNECTION;
	}


	public void createControl(Composite parent) {
		dsRun = new DatasetRunWidgetRadio(parent);
		setControl(dsRun.getControl());
		dsRun.setData(jrDsRun);
		if(expContext!=null){
			dsRun.setExpressionContext(expContext);
		}
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext=expContext;
		if(dsRun!=null){
			dsRun.setExpressionContext(expContext);
		}
	}
	
	public JRDesignDatasetRun getJRDesignDatasetRun()
	{
		return jrDsRun;
	}
	
}
