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
package com.jaspersoft.studio.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the recent expressions.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class RecentExpressions {

	private static List<String> recentExpressions=new ArrayList<String>();
	
	/**
	 * Returns the expression found at the specified index.
	 * <p>
	 * Example: expression with index 1 is the oldest in the list.
	 * 
	 * @param index 
	 * @return the expression string at the specified position, <code>null</code> otherwise
	 */
	public static String getExpression(int index){
		if(recentExpressions.size()>=index && index>0){
			return recentExpressions.get(index-1);
		}
		else {
			return null;
		}
	}
	
	/**
	 * @return the whole list of cached expressions
	 */
	public static List<String> getRecentExpressionsList(){
		return recentExpressions;
	}
	
	/**
	 * Add new expression string to the "recent" list.
	 * <p>
	 * If the string is already present the one in the old position
	 * is removed and then it is re-inserted.
	 * 
	 * @param expression the expression string to add
	 */
	public static void addNewExpression(String expression){
		if(!recentExpressions.contains(expression)){
			recentExpressions.remove(expression);
			recentExpressions.add(expression);
		}
	}
	
	/**
	 * Clears the list of recent expressions.
	 */
	public static void clear(){
		recentExpressions.clear();
	}
	
}
