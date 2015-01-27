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

/**
 * Interface shared among different kind of classes that need 
 * a setter method for an expression context information.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public interface IExpressionContextSetter {
	
	/**
	 * Sets the expression context. 
	 * 
	 * <p>
	 * Internal state of the interested class should be updated inside this method.
	 * For example a complex widget that implements this interface should take care
	 * of propagating the update of the expression context to its internal widgets too.
	 * 
	 * @param expContext the new expression context
	 */
	void setExpressionContext(ExpressionContext expContext);
}
