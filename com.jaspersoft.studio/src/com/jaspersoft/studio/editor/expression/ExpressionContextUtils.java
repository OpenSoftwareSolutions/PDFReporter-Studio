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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.util.JRResourcesUtil;

import org.eclipse.osgi.util.NLS;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Utility methods related to the {@link ExpressionContext} object.
 * <p>
 * 
 * <b>Implementation details</b> - Right now the following methods {@link #getAllDatasetsFields(ExpressionContext)},
 * {@link #getAllDatasetsParameters(ExpressionContext)}, {@link #getAllDatasetsVariables(ExpressionContext)}
 * return a list of "unique" elements using as key the name.<br/> 
 * This could not be the most wise solution.<br/>
 * However since we are presenting this information in the Expression Editor UI, the element name that will be
 * used in the expression is important.<br/>
 * An enhancement would be to present in Expression Editor UI also the Dataset nodes like we do for Crosstabs.
 * So we will have something like:
 * 
 * <pre>
 * 	&lt;DATASET_1&gt; 
 * 	- Parameters
 * 	- Fields
 * 	- Variables
 *	...
 * 	&lt;DATASET_n&gt; 
 * 	- Parameters
 * 	- Fields
 * 	- Variables
 * 	CROSSTAB(1) 
 *	...
 *	CROSSTAB(n)
 * </pre>
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public final class ExpressionContextUtils {

	private ExpressionContextUtils(){
		// PREVENT INSTANTIATION 
	}
	
	/**
	 * Returns the list of JasperReports parameters that can be used 
	 * in the specified expression context. Only dataset instances
	 * associated to the context are considered.
	 * 
	 * @param exprContext the expression context
	 * @return the list of parameters
	 */
	public static List<JRParameter> getAllDatasetsParameters(ExpressionContext exprContext){
		if(exprContext.hasDatasets() && exprContext.canShowParameters()){
			List<JRParameter> elements = new ArrayList<JRParameter>();
			for(JRDesignDataset d : exprContext.getDatasets()) {
				elements.addAll(d.getParametersList());
			}
			return getUniqueParameters(elements);
		}
		return new ArrayList<JRParameter>(0);
	}
	
	/**
	 * Returns the list of JasperReports variables that can be used 
	 * in the specified expression context. Only dataset instances
	 * associated to the context are considered.
	 * 
	 * @param exprContext the expression context
	 * @return the list of variables
	 */
	public static List<JRVariable> getAllDatasetsVariables(ExpressionContext exprContext){
		if(exprContext.hasDatasets() && exprContext.canShowVariables()){
			List<JRVariable> elements = new ArrayList<JRVariable>();
			for(JRDesignDataset d : exprContext.getDatasets()) {
				elements.addAll(d.getVariablesList());
			}
			return getUniqueVariables(elements);
		}
		return new ArrayList<JRVariable>(0);
	}
	
	/**
	 * Returns the list of JasperReports fields that can be used 
	 * in the specified expression context. Only dataset instances
	 * associated to the context are considered. 
	 * 
	 * @param exprContext the expression context
	 * @return the list of fields
	 */
	public static List<JRField> getAllDatasetsFields(ExpressionContext exprContext){
		if(exprContext.hasDatasets() && exprContext.canShowFields()){
			List<JRField> elements = new ArrayList<JRField>();
			for(JRDesignDataset d : exprContext.getDatasets()) {
				elements.addAll(d.getFieldsList());
			}
			return getUniqueFields(elements);
		}
		return new ArrayList<JRField>(0);
	}
	
	/**
	 * Returns the list of JasperReports variables that can be used 
	 * in the specified expression context. Only crosstab instances
	 * associated to the context are considered. 
	 * 
	 * @param exprContext the expression context
	 * @return the list of variables
	 */
	public static List<JRVariable> getAllCrosstabsVariables(ExpressionContext exprContext) {
		if(exprContext.hasCrosstabs() && exprContext.canShowVariables()) {
			List<JRVariable> elements = new ArrayList<JRVariable>();
			for(JRDesignCrosstab c : exprContext.getCrosstabs()) {
				elements.addAll(Arrays.asList(c.getVariables()));
			}
			return elements;
		}
		return new ArrayList<JRVariable>(0);
	}
	
	/**
	 * Returns the list of JasperReports parameters that can be used 
	 * in the specified expression context. Only dataset instances
	 * associated to the context are considered.
	 * 
	 * @param exprContext the expression context
	 * @return the list of parameters
	 */
	public static List<JRParameter> getAllCrosstabsParameters(ExpressionContext exprContext) {
		if(exprContext.hasCrosstabs() && exprContext.canShowParameters()) {
			List<JRParameter> elements = new ArrayList<JRParameter>();
			for(JRDesignCrosstab c : exprContext.getCrosstabs()) {
				elements.addAll(Arrays.asList(c.getParameters()));
			}
			return elements;
		}
		return new ArrayList<JRParameter>(0);
	}

	/*
	 * Returns a list of "unique" variables. From the input collection we get rid of the
	 * "duplicate" variables. We use as key the variable name.
	 */
	private static List<JRVariable> getUniqueVariables(List<JRVariable> variables){
		Set<JRVariable> results = new TreeSet<JRVariable>(new Comparator<JRVariable>() {
			@Override
			public int compare(JRVariable o1, JRVariable o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		results.addAll(variables);
		return new ArrayList<JRVariable>(results);
	}
	
	/*
	 * Returns a list of "unique" parameters. From the input collection we get rid of the
	 * "duplicate" parameters. We use as key the parameter name.
	 */
	private static List<JRParameter> getUniqueParameters(List<JRParameter> parameters){
		Set<JRParameter> results = new TreeSet<JRParameter>(new Comparator<JRParameter>() {
			@Override
			public int compare(JRParameter o1, JRParameter o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		results.addAll(parameters);
		return new ArrayList<JRParameter>(results);
	}
	
	/*
	 * Returns a list of "unique" fields. From the input collection we get rid of the
	 * "duplicate" fields. We use as key the field name.
	 */
	private static List<JRField> getUniqueFields(List<JRField> fields){
		Set<JRField> results = new TreeSet<JRField>(new Comparator<JRField>() {
			@Override
			public int compare(JRField o1, JRField o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		results.addAll(fields);
		return new ArrayList<JRField>(results);
	}
	
	/**
	 * Returns the list of potential keys for the resource bundle visible
	 * to the specified expression context.
	 * 
	 * @param expContext the expression context
	 * @return
	 */
	public static List<String> getResourceBundleKeys(ExpressionContext expContext) {
		Set<String> keys=new HashSet<String>();
		for(JRDataset ds : expContext.getDatasets()) {
			ResourceBundle rb = getResourceBundle(ds, expContext.getJasperReportsConfiguration());
			if(rb!=null) {
				keys.addAll(Collections.list(rb.getKeys()));
			}
		}
		ArrayList<String> keysLst = new ArrayList<String>(keys);
		Collections.sort(keysLst);
		return keysLst;
	}
	
	/**
	 * Returns the {@link ResourceBundle} associated to the specified dataset.
	 * As fallback solution we look in the main dataset.
	 * 
	 * @param dataset the dataset to look into
	 * @param jconfig the {@link JasperReportsContext} instance
	 * @return the {@link ResourceBundle} if any, <code>null</code> otherwise
	 */
	public static ResourceBundle getResourceBundle(JRDataset dataset, JasperReportsConfiguration jconfig) {
		String baseName = dataset.getResourceBundle();
		ResourceBundle loadedBundle = null;
		if(baseName==null) {
			baseName = jconfig.getJasperDesign().getMainDataset().getResourceBundle();
		}
		if(!Misc.isNullOrEmpty(baseName)) {
			Locale locale = Locale.getDefault();
			Object obj = jconfig.getJRParameters().get(JRParameter.REPORT_LOCALE);
			if (obj instanceof Locale) {
				locale = (Locale) obj;
			}
			try {
				loadedBundle = JRResourcesUtil.loadResourceBundle(baseName, locale, jconfig.getClassLoader());
			}
			catch (MissingResourceException e) {
				JaspersoftStudioPlugin.getInstance().logError(
						NLS.bind("Unable to get the resource bundle with base name {0}.",baseName), e);
			}
		}
		return loadedBundle;
	}

	/**
	 * Returns the list of parameters that are related to the specified dataset.
	 * The list is populated only if the dataset is itself used in the input expression context.
	 * 
	 * @param exprContext the expression context to look into
	 * @param ds the dataset
	 * @return the list of parameters
	 */
	public static List<JRParameter> getDatasetParameters(ExpressionContext exprContext, JRDesignDataset ds) {
		List<JRParameter> parameters = new ArrayList<JRParameter>();
		for(JRDesignDataset d : exprContext.getDatasets()){
			if(d == ds) {
				parameters.addAll(d.getParametersList());
				break;
			}
		}
		return parameters;
	}
	
	/**
	 * Returns the list of variables that are related to the specified dataset.
	 * The list is populated only if the dataset is itself used in the input expression context.
	 * 
	 * @param exprContext the expression context to look into
	 * @param ds the dataset
	 * @return the list of variables
	 */
	public static List<JRVariable> getDatasetVariables(ExpressionContext exprContext, JRDesignDataset ds) {
		List<JRVariable> variables = new ArrayList<JRVariable>();
		for(JRDesignDataset d : exprContext.getDatasets()){
			if(d == ds) {
				variables.addAll(d.getVariablesList());
				break;
			}
		}
		return variables;
	}

	/**
	 * Returns the list of fields that are related to the specified dataset.
	 * The list is populated only if the dataset is itself used in the input expression context.
	 * 
	 * @param exprContext the expression context to look into
	 * @param ds the dataset
	 * @return the list of fields
	 */
	public static List<JRField> getDatasetFields(ExpressionContext exprContext, JRDesignDataset ds) {
		List<JRField> fields = new ArrayList<JRField>();
		for(JRDesignDataset d : exprContext.getDatasets()){
			if(d == ds) {
				fields.addAll(d.getFieldsList());
				break;
			}
		}
		return fields;
	}
		
}
