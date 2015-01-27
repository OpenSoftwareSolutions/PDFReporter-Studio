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
package net.sf.jasperreports.expressions.annotations;

import java.util.ArrayList;
import java.util.List;


/**
 * Bean to describe the a function of the expression library.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see JRExprFunction
 */
public class JRExprFunctionBean implements Comparable<JRExprFunctionBean>{

	private String id;
	private String name;
	private String description;
	private List<JRExprFunctionParameterBean> parameters;
	private Class<?> returnType;
	private List<JRExprFunctionCategoryBean> categories;
	private String functionClassName;
	
	public JRExprFunctionBean(String functionClassName) {
		this.functionClassName=functionClassName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<JRExprFunctionParameterBean> getParameters() {
		if(parameters==null){
			parameters=new ArrayList<JRExprFunctionParameterBean>();
		}
		return parameters;
	}
	public void setParameters(List<JRExprFunctionParameterBean> parameters) {
		this.parameters = parameters;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	public List<JRExprFunctionCategoryBean> getCategories() {
		if(categories==null){
			categories=new ArrayList<JRExprFunctionCategoryBean>();
		}
		return categories;
	}
	public void setCategories(List<JRExprFunctionCategoryBean> categories) {
		this.categories = categories;
	}
	public String getFunctionClassName() {
		return functionClassName;
	}
	@Override
	public int compareTo(JRExprFunctionBean o) {
		return name.compareTo(o.getName());
	}
	
}
