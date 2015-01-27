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
package com.jaspersoft.studio.property.descriptor.parameter.dialog;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;

public class ParameterEditor extends Wizard implements IExpressionContextSetter{
	private ParameterDTO value;
	private ParameterPage page0;
	private ExpressionContext exprContext;

	public ParameterDTO getValue() {
		if (page0 != null)
			return page0.getValue();
		return value;
	}

	public void setValue(ParameterDTO value) {
		if (page0 != null)
			page0.setValue(value);
		this.value = value;
	}

	public ParameterEditor() {
		super();
		setWindowTitle(Messages.common_properties);
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		page0 = new ParameterPage("Datasetparameters"); //$NON-NLS-1$
		page0.setValue(value);
		if(exprContext!=null){
			page0.setExpressionContext(exprContext);
		}
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
	public void setExpressionContext(ExpressionContext exprContext){
		this.exprContext=exprContext;
		if(page0!=null){
			page0.setExpressionContext(this.exprContext);
		}
	}

}
