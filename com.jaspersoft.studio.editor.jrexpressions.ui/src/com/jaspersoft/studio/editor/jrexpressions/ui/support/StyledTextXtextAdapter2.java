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
package com.jaspersoft.studio.editor.jrexpressions.ui.support;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.custom.StyledText;

import com.google.inject.Injector;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.jrexpressions.ui.JRExpressionsActivator;
import com.jaspersoft.studio.editor.jrexpressions.ui.JRExpressionsUIPlugin;
import com.jaspersoft.studio.editor.jrexpressions.ui.messages.Messages;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;

import de.itemis.xtext.utils.jface.viewers.StyledTextXtextAdapter;

/**
 * This is a custom adapter that extends the functionalities of 
 * the default {@link StyledTextXtextAdapter}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class StyledTextXtextAdapter2 extends StyledTextXtextAdapter {

	/**
	 * Creates the adapter.
	 * 
	 * @param injector the injector
	 */
	public StyledTextXtextAdapter2(Injector injector) {
		super(injector);
	}

	/**
	 * Sets the information related to the expression context as
	 * viewer attribute.
	 * 
	 * <p>
	 * NOTE: this gives the ability to re-use this information when needed in
	 * specific context.<br>
	 * EXAMPLE OF USE: inside the proposal provider class when giving 
	 * code completion for parameter, field or variable tokens.
	 * 
	 * @param exprContext the expression context
	 */
	public void configureExpressionContext(ExpressionContext exprContext){
		getXtextSourceviewer().setData(ExpressionContext.ATTRIBUTE_EXPRESSION_CONTEXT, exprContext);
	}
	
	/**
	 * Tries to tell to the Xtext viewer whether the registered
	 * auto edit strategies should be ignored.
	 * 
	 * @param ignore <code>true</code> if the strategies should be ignored.
	 */
	public void ignoreAutoEditStrategies(boolean ignore){
		try {
			// org.eclipse.jface.text.TextViewer#ignoreAutoEditStrategies(boolean) is protected by definition.
			// XtextSourceViewer does not extend its visibility so we have to bypass it
			// invoking the method via Reflection API.
			// N.B: This way of using reflection is a "violation" of OOP basis but
			// it is also a trick that works fine.
			Method declaredMethod = TextViewer.class.getDeclaredMethod("ignoreAutoEditStrategies",boolean.class); //$NON-NLS-1$
			declaredMethod.setAccessible(true);
			declaredMethod.invoke(getXtextSourceviewer(), ignore);
		} catch (Exception e) {
			JRExpressionsActivator.getInstance().getLog().log(
					new Status(IStatus.ERROR, JRExpressionsUIPlugin.PLUGIN_ID, 
							Messages.StyledTextXtextAdapter2_AutoEditStrategiesError, e));
		}
	}
	
	/**
	 * Adapt the XText editor behavior to the specified {@link StyledText} widget.
	 * <p>
	 * It also allows to perform additional operation based on the input expression
	 * context.
	 * 
	 * @param styledText the widget to be adapted
	 * @param exprContext the current expression context available
	 */
	public void adapt(StyledText styledText, ExpressionContext exprContext) {
		super.adapt(styledText);
		if(exprContext!=null){
			styledText.setFont(FontUtils.getEditorsFont(exprContext.getJasperReportsConfiguration()));
		}
		else {
			styledText.setFont(FontUtils.getEditorsFont(null));
		}
	}
}
