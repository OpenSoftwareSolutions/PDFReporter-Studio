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

/**
 * This interface is supposed to be implemented in order to provide 
 * facilities for the editor of {@link JRExpression} elements.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public interface IExpressionEditorSupportFactory {
	
	/**
	 * Returns a support class for the specified language (i.e: Java, Groovy, etc.). 
	 * 
	 * @param language the language for which is needed support
	 * @return the editor support class
	 */
	ExpressionEditorSupport getExpressionEditorSupport(String language);
}
