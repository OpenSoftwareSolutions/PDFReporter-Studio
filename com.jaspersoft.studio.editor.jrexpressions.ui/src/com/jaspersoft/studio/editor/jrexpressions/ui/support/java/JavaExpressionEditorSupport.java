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
package com.jaspersoft.studio.editor.jrexpressions.ui.support.java;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorComposite;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupport;
import com.jaspersoft.studio.editor.jrexpressions.ui.JRExpressionsActivator;
import com.jaspersoft.studio.editor.jrexpressions.ui.support.StyledTextXtextAdapter2;

/**
 * Expression editor support class provided by Jaspersoft Studio for the Java language.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JavaExpressionEditorSupport extends ExpressionEditorSupport {
	
	@Override
	public void configureExpressionWidget(StyledText widget, ExpressionContext exprContext) {
		StyledTextXtextAdapter2 xtextAdapter=new StyledTextXtextAdapter2(
				JRExpressionsActivator.getInstance().getInjector(JRExpressionsActivator.COM_JASPERSOFT_STUDIO_EDITOR_JREXPRESSIONS_JAVAJREXPRESSION));
		xtextAdapter.adapt(widget);
		xtextAdapter.configureExpressionContext(exprContext);
	}

	@Override
	public ExpressionEditorComposite createExpressionEditorComposite(
			Composite parent) {
		ExpressionEditorComposite content = new JavaExpressionEditorComposite(parent,SWT.NONE);
		content.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		return content;
	}

}
