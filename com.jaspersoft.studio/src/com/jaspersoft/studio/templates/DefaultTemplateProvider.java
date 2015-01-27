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

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.templates.TemplateLocationsPreferencePage;
import com.jaspersoft.studio.templates.engine.DefaultTemplateEngine;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.BuiltInCategories;
import com.jaspersoft.templates.TemplateBundle;

/**
 * The default implementation in JSS of template provider looks for templates inside the plugin's templates
 * directory and load the templates based on the typical characteristics of filesystem based templates
 * (the same used inside the Jaspersoft iReport Designer).
 *  
 * @author gtoffoli
 *
 */
public class DefaultTemplateProvider implements TemplateProvider {

	public static List<TemplateBundle> cache = null;
	
	/**
	 * Key used to distinguish the standard template from the other templates
	 */
	public static final String defaultEngineKey="default";//$NON-NLS-1$
	
	@Override
	public List<TemplateBundle> getTemplateBundles() {
		
		List<TemplateBundle> templates = new ArrayList<TemplateBundle>();
		
		if (cache == null)
		{
			  cache =  new ArrayList<TemplateBundle>();
			  
				Enumeration<?> en = JaspersoftStudioPlugin.getInstance().getBundle().findEntries("templates", "*.jrxml", false); //$NON-NLS-1$ //$NON-NLS-2$
				while (en.hasMoreElements()) {
					URL templateURL = (URL) en.nextElement();
					
					try {
	
					JrxmlTemplateBundle bundle = new JrxmlTemplateBundle(templateURL,
							JasperReportsConfiguration.getDefaultJRConfig());
						
						if (bundle != null)
						{
							cache.add(bundle);
						}
					} catch (Exception ex)
					{
						// Log error but continue...
						JaspersoftStudioPlugin.getInstance().getLog().log(
								new Status(IStatus.ERROR,JaspersoftStudioPlugin.PLUGIN_ID,
										MessageFormat.format(Messages.DefaultTemplateProvider_TemplateLoadingErr,new Object[]{templateURL}), ex));

					}
				}
		}
		
		templates.addAll(cache);

		loadAdditionalTemplateBundles(templates);
		
	  return templates;
	}
	
	/*
	 * Look for other templates inside the specified directories in the preferences.
	 */
	private void loadAdditionalTemplateBundles(List<TemplateBundle> templates) {
		String paths = JaspersoftStudioPlugin.getInstance().getPreferenceStore()
				.getString(TemplateLocationsPreferencePage.TPP_TEMPLATES_LOCATIONS_LIST);
		StringTokenizer st = new StringTokenizer(paths, File.pathSeparator + "\n\r");//$NON-NLS-1$
		ArrayList<String> pathsList = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			pathsList.add(st.nextToken());
		}

		for (String dir : pathsList) {
			File[] files = new File(dir).listFiles(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".jrxml"); //$NON-NLS-1$
				}
			});

			if (files != null) {
				for (File f : files) {
					try {
						JrxmlTemplateBundle bundle = new JrxmlTemplateBundle(f.toURI().toURL(), true,
								JasperReportsConfiguration.getDefaultJRConfig());
						Object engine = bundle.getProperty(BuiltInCategories.ENGINE_KEY);
						if (bundle != null && (engine == null || defaultEngineKey.equals(engine.toString().toLowerCase()))) {
							templates.add(bundle);
						}
					} catch (Exception ex) {
						// Log error but continue...
						JaspersoftStudioPlugin.getInstance().getLog().log(
								new Status(IStatus.ERROR,JaspersoftStudioPlugin.PLUGIN_ID,
										MessageFormat.format(Messages.DefaultTemplateProvider_TemplateLoadingErr,new Object[]{f.getAbsolutePath()}), ex));
					}
				}
			}
		}
	}

	/**
	 * Return the key that identify this engine
	 */
	@Override
	public String getProviderKey() {
		return defaultEngineKey;
	}

	/**
	 * return a human readable name for the engine
	 */
	@Override
	public String getProviderName() {
		return "Standard Report";
	}

	/**
	 * Get a JasperDesign and check if that JasperDesign can be used as Template and processed
	 * by the engine used inside this provider
	 * 
	 * @param design the design to check
	 * @return a List of founded error, the list is void if no error are found
	 */
	@Override
	public List<String> validateTemplate(JasperDesign design) {
		return DefaultTemplateEngine.validateJasperDesig(design);
	}

}
