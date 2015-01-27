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

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.util.JRExpressionUtil;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ExpressionUtil {
	
	/**
	 * Set the listener (only where they are not already set) to listen the changes
	 * to a dataset and discard the cached interpreter for that dataset when they happen.
	 * The listeners are set on both the dataset and his children
	 * 
	 * @param parentDataset
	 */
	private static void setDatasetListners(JRDesignDataset parentDataset){
			addEventIfnecessary(parentDataset, parentDataset);
			for(JRVariable var : parentDataset.getVariables()){
				if (var instanceof JRChangeEventsSupport)
					addEventIfnecessary((JRChangeEventsSupport)var, parentDataset);
			}
			
			for(JRSortField sortField : parentDataset.getSortFields()){
				if (sortField instanceof JRChangeEventsSupport)
					addEventIfnecessary((JRChangeEventsSupport)sortField, parentDataset);
			}
			
			for(JRParameter parameter : parentDataset.getParameters()){
				if (parameter instanceof JRChangeEventsSupport)
					addEventIfnecessary((JRChangeEventsSupport)parameter, parentDataset);
			}
			
			for(JRField field : parentDataset.getFields()){
				if (field instanceof JRChangeEventsSupport)
					addEventIfnecessary((JRChangeEventsSupport)field, parentDataset);
			}
	}
	
	/**
	 * Add to the a report design a design change listener, but only if
	 * it hasen't already a listener of this type
	 * 
	 * @param design the element
	 * @param parentDataset config the configuration of the report that will be used to remove
	 * from the cache every intepreter that was created for the report, when this event is called
	 */
	private static void setDesignListener(JasperDesign design, JasperReportsConfiguration config){
		if (!hasListener(design, DesignChanges.class)){
			design.getEventSupport().addPropertyChangeListener(new DesignChanges(config)); 
		}
	}
	
	/**
	 * Add to an object that support the event change a dataset change listener, but only if
	 * it hasen't already a listener of this type
	 * 
	 * @param support the element
	 * @param parentDataset the dataset that will be removed from the cache if the listener is called
	 */
	private static void addEventIfnecessary(JRChangeEventsSupport support, JRDesignDataset parentDataset){
		if (!hasListener(support, DatasetChanges.class)){
			support.getEventSupport().addPropertyChangeListener(new DatasetChanges(parentDataset));
		}
	}
	
	/**
	 * check if an object has a listener of a specific type
	 * 
	 * @param support object from where the listeners are obtained
	 * @param listenerClass class to search
	 * @return true if the support object has a listener that has exactly the type listenerClass, 
	 * otherwise false
	 */
	private static boolean hasListener(JRChangeEventsSupport support, Class<?> listenerClass){
		PropertyChangeListener[] listeners = support.getEventSupport().getPropertyChangeListeners();
		for(PropertyChangeListener listener : listeners){
			if (listener.getClass() == listenerClass) return true; 
		}
		return false;
	}
	
	/**
	 * Cache of the expression interpreter for every dataset, the key is the reference to the dataset for whose the interpreter was created
	 */
	private static HashMap<JRDesignDataset, ExpressionInterpreter> datasetsIntepreters = new HashMap<JRDesignDataset, ExpressionInterpreter>();
	
	/**
	 * Resolve an expression and return its value or null if it can not be resolve. First it will try to use a simple evaluation
	 * since it is much faster. If this can't resolve the expression then an interpreter for the current report is created 
	 * and cached (since create and interpreter is very slow) 
	 * 
	 * @param exp expression to resolve
	 * @param project project of the report
	 * @param jConfig Configuration of the report to evaluate the expression
	 * @param dataset the context of the expression resolution
	 * @return resolved expression or null it it can't be resolved
	 */
	public static String cachedExpressionEvaluation(JRExpression exp, JasperReportsConfiguration jConfig, JRDesignDataset dataset){	
		synchronized (datasetsIntepreters) {
			String evaluatedExpression = null;
			String expString = exp != null ? exp.getText() : "";
			try{
				evaluatedExpression = JRExpressionUtil.getSimpleExpressionText(exp);
				if (evaluatedExpression == null && dataset != null){
					//Unable to interpret the expression, lets try with a more advanced (and slow, so its cached) interpreter
					JasperDesign jd = jConfig.getJasperDesign();
					ExpressionInterpreter interpreter = datasetsIntepreters.get(dataset);
					if (interpreter == null){
						if (exp != null && jd != null){
							interpreter = new ExpressionInterpreter(dataset, jd, jConfig);
							datasetsIntepreters.put(dataset, interpreter);
							//The dataset was added to the cache, check if it has the listener and add them where are needed
							setDatasetListners(dataset);
							setDesignListener(jd, jConfig);
						}
					}
					if (interpreter != null){
						Object expressionValue = interpreter.interpretExpression(expString);
						if (expressionValue != null) evaluatedExpression = expressionValue.toString();
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return evaluatedExpression;
		}
	}
	
	/**
	 * Resolve an expression and return its value or null if it can not be resolve. First it will try to use a simple evaluation
	 * since it is much faster. If this can't resolve the expression then an interpreter for the current report is created 
	 * and cached (since create and interpreter is very slow) 
	 * 
	 * @param exp expression to resolve
	 * @param jConfig Configuration of the report to evaluate the expression
	 * @return resolved expression or null it it can't be resolved
	 */
	public static String cachedExpressionEvaluation(JRExpression exp, JasperReportsConfiguration jConfig){	
		JRDesignDataset dataset = null;
		if (jConfig.getJasperDesign() != null){
			dataset = jConfig.getJasperDesign().getMainDesignDataset();
		}
		return cachedExpressionEvaluation(exp, jConfig, dataset);
	}
	
	/**
	 * Remove an expression interpreter from the cache. An intepreter must be removed when something change in the dataset
	 * that has generated it
	 * 
	 * @param dataset dataset for whose the intepreter was created
	 */
	public static void removeCachedInterpreter(JRDesignDataset dataset){
		synchronized (datasetsIntepreters) {
			datasetsIntepreters.remove(dataset);
		}
	}
	
	/**
	 * Remove all the interpreters cached for a report 
	 * 
	 * @param reportsConfiguration Configuration for the report
	 */
	public static void removeAllReportInterpreters(JasperReportsConfiguration reportsConfiguration){
		synchronized (datasetsIntepreters) {
			List<JRDesignDataset> datasetsToRemove = new ArrayList<JRDesignDataset>();
			for(Entry<JRDesignDataset, ExpressionInterpreter> intepreter : datasetsIntepreters.entrySet()){
				if (intepreter.getValue().getJasperReportsConfiguration() == reportsConfiguration){
					datasetsToRemove.add(intepreter.getKey());
				}
			}
			for(JRDesignDataset dataset : datasetsToRemove){
				datasetsIntepreters.remove(dataset);
			}
		}
	}
	
	/**
	 * Remove an expression interpreter from the cache
	 * 
	 * @param jConfig JasperReportConfiguration project for which the interpreter should be removed
	 */
	public static void removeCachedInterpreter(JasperReportsConfiguration jConfig){
		if (jConfig != null){
			removeCachedInterpreter(jConfig.getJasperDesign().getMainDesignDataset());
		}
	}
	
	public static final String eval(JRExpression expr, JasperReportsConfiguration jConfig, JasperDesign jd) {
		if (expr == null)
			return null;
		if (jd == null)
			return JRExpressionUtil.getSimpleExpressionText(expr);

		Object eval = eval(expr, jd.getMainDesignDataset(), jConfig, jd);
		if (eval != null)
			return eval.toString();
		return null;
	}

	public static final String eval(JRExpression expr, JasperReportsConfiguration jConfig) {
		return eval(expr, jConfig, jConfig.getJasperDesign());
	}

	public static final Object eval(JRExpression expr, JRDataset jrd, JasperReportsConfiguration jConfig) {
		return eval(expr, jrd, jConfig, jConfig.getJasperDesign());
	}

	public static final Object eval(JRExpression expr, JRDataset jrd, JasperReportsConfiguration jConfig, JasperDesign jd) {
		if (expr == null || jrd == null || jd == null)
			return null;
		return getInterpreter((JRDesignDataset) jrd, jConfig, jd).interpretExpression(expr.getText());
	}

	public static final ExpressionInterpreter getInterpreter(JRDesignDataset jrd, JasperReportsConfiguration jConfig,
			JasperDesign jd) {
		if (jrd == null || jd == null)
			return null;
		return new ExpressionInterpreter(jrd, jd, jConfig);
	}

	/**
	 * @return an expression with empty string
	 */
	public static final JRDesignExpression getEmptyStringExpression() {
		return new JRDesignExpression("\"\"");
	}
}
