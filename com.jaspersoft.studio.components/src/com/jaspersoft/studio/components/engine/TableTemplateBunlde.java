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
package com.jaspersoft.studio.components.engine;

import java.net.URL;

import net.sf.jasperreports.engine.JasperReportsContext;

import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.templates.JrxmlTemplateBundle;

/**
 * TemplateBundle for the table based report styles
 * 
 * @author Orlandin Marco
 *
 */
public class TableTemplateBunlde extends JrxmlTemplateBundle {

	public TableTemplateBunlde(URL url, JasperReportsContext jrContext) throws Exception {
		super(url, jrContext);
	}
	
	public TableTemplateBunlde(URL url, boolean isExternal, JasperReportsContext jrContext) throws Exception {
		super(url, isExternal, jrContext);
	}

	/**
	 * For the table based templates return a Table Template Engine
	 */
	protected void readProperties()
	{
		super.readProperties();
		templateEngine = new TableTemplateEngine();
	}
	
	/**
	 * Return the name of a template, appending a word to identify that it is a table template
	 */
	@Override
	public String getLabel() {
		return super.getLabel() + Messages.TableTemplateBunlde_tableBasedString;
	}
	
}
