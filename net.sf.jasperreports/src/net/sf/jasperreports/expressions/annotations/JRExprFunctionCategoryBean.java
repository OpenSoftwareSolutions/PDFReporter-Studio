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


/**
 * Bean to describe a parameter of a particular function of the expressions library.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 *
 */
public class JRExprFunctionCategoryBean 
{
	
	private String id;
//	private String messagesBundle;
	private String name;
	private String description;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
//	public String getMessagesBundle() {
//		return messagesBundle;
//	}
//	public void setMessagesBundle(String messagesBundle) {
//		this.messagesBundle = messagesBundle;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
