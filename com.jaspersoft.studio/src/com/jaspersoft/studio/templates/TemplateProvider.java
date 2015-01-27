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
package com.jaspersoft.studio.templates;

import java.util.List;

import net.sf.jasperreports.engine.design.JasperDesign;

import com.jaspersoft.templates.TemplateBundle;


public interface TemplateProvider {

		/**
		 * Return a list of TemplateBundle that could be handled by this engine
		 */
		public List<TemplateBundle> getTemplateBundles();

		/**
		 * Return the key that identify this engine
		 */
		public String getProviderKey();
		
		/**
		 * return a human readable name for the engine
		 */
		public String getProviderName();
		
		/**
		 * Get a JasperDesign and check if that JasperDesign can be used as Template and processed
		 * by the engine used inside this provider
		 * 
		 * @param design the design to check
		 * @return a List of founded error, the list is void if no error are found
		 */
		public List<String> validateTemplate(JasperDesign design);
}
