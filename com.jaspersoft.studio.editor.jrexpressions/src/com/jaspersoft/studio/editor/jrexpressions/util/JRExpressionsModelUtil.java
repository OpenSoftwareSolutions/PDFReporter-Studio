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
package com.jaspersoft.studio.editor.jrexpressions.util;

import com.jaspersoft.studio.editor.expression.FunctionsLibraryUtil;
import com.jaspersoft.studio.editor.jrexpressions.functions.AdditionalStaticFunctions;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression;

/**
 * This class maintains a list of utility methods that deal directly with
 * JRExpression(s) model elements.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JRExpressionsModelUtil {

	/**
	 * Performs a simple check to know if the specified function name could
	 * belong to the contributed function library.
	 * 
	 * @param name the full method name to check
	 * @return <code>true</code> if the function name is a library valid one, <code>false</code> otherwise
	 */
	public static boolean isFunctionLibrary(FullMethodName name){
		if(name.eContainer() instanceof MethodInvocation){
			MethodInvocation method=(MethodInvocation)name.eContainer();
			// Need to check because it could be an ObjectCreation expression.
			if(method.eContainer() instanceof MethodsExpression){
				MethodsExpression expression=(MethodsExpression) method.eContainer();
				// TODO - Improve this check		
				// For sake of simplicity assume the library functions are the first method invocation
				if(!expression.isIncludeObjectInstatiation() && (name.getPrefixQMN()==null || name.getPrefixQMN().size()==0)) {
					if(expression.getObjectExpression()==null && 
							method.equals(expression.getMethodInvocations().get(0))){
						// Look in the function library
						if(!FunctionsLibraryUtil.existsFunction(name.getMethodName()) && 
								!AdditionalStaticFunctions.getAllNames().contains(name.getMethodName())){
							return false;
						}
						else{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
}
