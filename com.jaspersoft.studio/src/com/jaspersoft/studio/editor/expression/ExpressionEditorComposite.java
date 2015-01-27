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
package com.jaspersoft.studio.editor.expression;

import net.sf.jasperreports.engine.JRExpression;

import org.eclipse.swt.widgets.Composite;

/**
 * Sub-classes of this Composite can be used as main editing area 
 * of a dialog in an expression editor.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public abstract class ExpressionEditorComposite extends Composite implements IExpressionStatusNotifier{

	/**
	 * Creates the composite.
	 * 
	 * @param parent the parent of the newly created composite
	 * @param style style information of the newly created composite
	 */
	public ExpressionEditorComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Sets the expression context that is supposed to be used for
	 * operations on the jrexpression.
	 * 
	 * <p>
	 * This method should also be used when there is the need to
	 * update some context information that are useful to edit
	 * the expression (i.e: parameters, fields, variables, etc.).
	 * 
	 * @param exprContext the expression context
	 */
	public abstract void setExpressionContext(ExpressionContext exprContext);
	
	/**
	 * Returns the {@link JRExpression} currently being modified.
	 * 
	 * @return the edited expression
	 */
	public abstract JRExpression getExpression();
	
	/**
	 * Sets the expression to be edited.
	 * 
	 * @param expression the expression to be modified
	 */
	public abstract void setExpression(JRExpression expression);
	
}
