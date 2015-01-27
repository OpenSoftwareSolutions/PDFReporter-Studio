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
package com.jaspersoft.studio.editor.jrexpressions.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.expressions.annotations.JRExprFunctionBean;
import net.sf.jasperreports.expressions.annotations.JRExprFunctionCategoryBean;
import net.sf.jasperreports.expressions.annotations.JRExprFunctionParameterBean;

import com.jaspersoft.studio.editor.jrexpressions.messages.Messages;

/**
 * This class manages additional functions that can be used in the expression editor
 * even if not directly contributed via the standard extension mechanism.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class AdditionalStaticFunctions {
	
	private static List<String> names;
	private static List<JRExprFunctionBean> messageBundleFunctions;
	
	/**
	 * @return a list of functions meant for message bundles support
	 */
	public static List<JRExprFunctionBean> getMessageBundleFuntions() {
		if(messageBundleFunctions == null) {
			messageBundleFunctions = new ArrayList<JRExprFunctionBean>();
	
			JRExprFunctionCategoryBean msgBundleCategory = new JRExprFunctionCategoryBean();
			msgBundleCategory.setDescription(Messages.AdditionalStaticFunctions_MessageBundlesCategoryDescription);
			msgBundleCategory.setId("static.functions.msgbundle"); //$NON-NLS-1$
			msgBundleCategory.setName(Messages.AdditionalStaticFunctions_MessageBundlesCategoryName);
	
			// JREvaluator.str(java.lang.String key)
			JRExprFunctionBean strfunct = new JRExprFunctionBean("net.sf.jasperreports.engine.fill.JREvaluator.str"); //$NON-NLS-1$
			strfunct.setCategories(Arrays.asList(msgBundleCategory));
			strfunct.setDescription(Messages.AdditionalStaticFunctions_strFunctionDescription);
			strfunct.setId("str"); //$NON-NLS-1$
			strfunct.setName("str"); //$NON-NLS-1$
			strfunct.setReturnType(String.class);
			
			JRExprFunctionParameterBean rbkey = new JRExprFunctionParameterBean();
			rbkey.setName(Messages.AdditionalStaticFunctions_strFunctionParamName);
			rbkey.setMulti(false);
			rbkey.setOptional(false);
			rbkey.setDescription(Messages.AdditionalStaticFunctions_strFunctionParamDescription);
			rbkey.setParameterType(String.class);
			strfunct.setParameters(Arrays.asList(rbkey));
			messageBundleFunctions.add(strfunct);
			getAllNames().add("str"); //$NON-NLS-1$
			
			// JREvaluator.str(java.lang.String key)
			JRExprFunctionBean msgfunct = new JRExprFunctionBean("net.sf.jasperreports.engine.fill.JREvaluator.msg"); //$NON-NLS-1$
			msgfunct.setCategories(Arrays.asList(msgBundleCategory));
			msgfunct.setDescription(Messages.AdditionalStaticFunctions_msgFunctionDescription);
			msgfunct.setId("msg"); //$NON-NLS-1$
			msgfunct.setName("msg"); //$NON-NLS-1$
			msgfunct.setReturnType(String.class);
			
			JRExprFunctionParameterBean patternParam = new JRExprFunctionParameterBean();
			patternParam.setName(Messages.AdditionalStaticFunctions_msgFunctionParam1Name);
			patternParam.setMulti(false);
			patternParam.setOptional(false);
			patternParam.setDescription(Messages.AdditionalStaticFunctions_msgFunctionParam1Description);
			patternParam.setParameterType(String.class);

			JRExprFunctionParameterBean msgParameters = new JRExprFunctionParameterBean();
			msgParameters.setName(Messages.AdditionalStaticFunctions_msgFunctionParam2Name);
			msgParameters.setMulti(true);
			msgParameters.setOptional(true);
			msgParameters.setDescription(Messages.AdditionalStaticFunctions_msgFunctionParam2Description);
			msgParameters.setParameterType(Object.class);
			msgfunct.setParameters(Arrays.asList(patternParam,msgParameters));
			messageBundleFunctions.add(msgfunct);
			getAllNames().add("msg"); //$NON-NLS-1$
		}
		
		return messageBundleFunctions;
	}
	
	/**
	 * @return the list of all the function names
	 */
	public static List<String> getAllNames() {
		if(names == null) {
			names = new ArrayList<String>();
		}
		return names;
	}

	/**
	 * @return the list of all static functions
	 */
	public static List<JRExprFunctionBean> getAllFunctions() {
		List<JRExprFunctionBean> allFunctions = new ArrayList<JRExprFunctionBean>();
		allFunctions.addAll(getMessageBundleFuntions());
		return allFunctions;
	}

}
