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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.jaspersoft.studio.templates.engine.DefaultTemplateEngine;
import com.jaspersoft.templates.ReportBundle;
import com.jaspersoft.templates.TemplateBundle;
import com.jaspersoft.templates.TemplateEngineException;

/**
 * Template engine to build a report with a table in the summary, from a TableTemplate
 * 
 * @author Orlandin Marco
 *
 */
public class JasperServerTemplateEngine extends DefaultTemplateEngine {

	private static HashSet<String> requiredStyles = null;
	
	private static HashSet<String> initializeRequiredStyles(){
		HashSet<String> result = new HashSet<String>();
		result.add("ReportDefault");
		result.add("TableBaseFrameStyle");
		result.add("TableFrameStyle");
		result.add("TableColumnHeaderFrameStyle");
		result.add("TableColumnFooterFrameStyle");
		result.add("TableColumnHeaderTextStyle");
		result.add("TableGroupHeaderFrameStyle");
		result.add("TableGroupHeaderTextStyle");
		result.add("TableGroupFooterFrameStyle");
		result.add("TableGroupFooterTextStyle");
		result.add("TableSummaryFrameStyle");
		result.add("TableSummaryTextNoBorderStyle");
		result.add("TableSummaryTextStyle");
		result.add("TableTitleFrameStyle");
		result.add("TableTitleTextStyle");
		result.add("TableDetailFrameStyle");
		result.add("TableDetailTextStyle");
		result.add("ChartReportTitle");
		result.add("ChartTitle");
		result.add("ChartSubtitle");
		result.add("ChartLegend");
		result.add("ChartSeriesColor0");
		result.add("ChartSeriesColor1");
		result.add("ChartSeriesColor2");
		result.add("ChartSeriesColor3");
		result.add("ChartSeriesColor4");
		result.add("ChartSeriesColor5");
		result.add("ChartSeriesColor6");
		result.add("ChartSeriesColor7");
		result.add("ChartSeriesColor8");
		result.add("ChartSeriesColor9");
		result.add("ChartSeriesColor10");
		result.add("ChartSeriesColor11");
		result.add("ChartSeriesColor12");
		result.add("ChartSeriesColor13");
		result.add("ChartSeriesColor14");
		result.add("ChartSeriesColor15");
		result.add("ChartCategoryAxisLabelFormat");
		result.add("ChartCategoryAxisTickFormat");
		result.add("ChartValueAxisLabelFormat");
		result.add("ChartValueAxisTickFormat");
		result.add("ChartContext");
		result.add("CrosstabElementStyle");
		result.add("CrosstabBaseCellStyle");
		result.add("CrosstabBaseTextStyle");
		result.add("CrosstabTitleTextStyle");
		result.add("CrosstabHeaderTextStyle");
		result.add("CrosstabHeaderLabelStyle");
		result.add("CrosstabMeasureHeaderTextStyle");
		result.add("CrosstabMeasureHeaderLabelStyle");
		result.add("CrosstabColumnMeasureHeaderTextStyle");
		result.add("CrosstabRowMeasureHeaderTextStyle");
		result.add("CrosstabDimensionHeaderTextStyle");
		result.add("CrosstabDetailTextStyle");
		result.add("CrosstabDummyHeaderTextStyle");
		return result;
	}
	
	public static HashSet<String> getRequiredStyles(){
		if (requiredStyles == null) requiredStyles = initializeRequiredStyles();
		return requiredStyles;
	}


	/**
	 * Initialize the fields needed to build the style of the report
	 */
	@Override
	protected void processTemplate(JasperDesign jd, List<Object> fields, List<Object> groupFields) {
	}
	
	/*private void removeUnwantedBand(JasperDesign jd){
		jd.setColumnHeader(null);
		jd.setColumnFooter(null);
		JRDesignSection bandSection = (JRDesignSection)jd.getDetailSection();
		for(JRBand actualDetail : jd.getDetailSection().getBands())
			bandSection.removeBand(actualDetail);
		//Delete the groups
		while (jd.getGroupsList().size()>0)
			jd.getGroupsList().remove(0);
	}*/
	
	/**
	 * Find a JRDesignStaticText inside a table element having exp as text.
	 * 
	 * @param parent table where to search
	 * @param exp the text of the element
	 * @return the first matching element or null.
	 */
	public static JRDesignStaticText findStaticTextElement(StandardTable parent, String exp) {
		StandardColumn col = (StandardColumn)parent.getColumns().get(0);
		if (col != null){
			JRDesignStaticText result = DefaultTemplateEngine.findStaticTextElement(col.getTableHeader(), exp);
			if (result == null) result = DefaultTemplateEngine.findStaticTextElement(col.getColumnHeader(), exp);
			if (result == null) result = DefaultTemplateEngine.findStaticTextElement(col.getDetailCell(), exp);
			return result;
		}
		return null;
	}
	
	/**
	 * Find a JRDesignTextField inside a table element having exp as expression.
	 * 
	 * @param parent table where to search
	 * @param exp the expression of the element
	 * @return the first matching element or null.
	 */
	public static JRDesignTextField findTextFieldElement(StandardTable parent, String exp) {
		StandardColumn col = (StandardColumn)parent.getColumns().get(0);
		if (col != null){
			JRDesignTextField result = DefaultTemplateEngine.findTextFieldElement(col.getTableHeader(), exp);
			if (result == null) result = DefaultTemplateEngine.findTextFieldElement(col.getColumnHeader(), exp);
			if (result == null) result = DefaultTemplateEngine.findTextFieldElement(col.getDetailCell(), exp);
			return result;
		}
		return null;
	}
	

	/**
	 * Create the report with the table inside
	 */
	@Override
	public ReportBundle generateReportBundle(TemplateBundle template, Map<String, Object> settings, JasperReportsContext jContext)
			throws TemplateEngineException {
		//Generate the base report bundle
		ReportBundle reportBundle = super.generateReportBundle(template, settings, jContext);
		
		return reportBundle;
	}
	
	
	/**
	 * Get a JasperDesign and check if that JasperDesign can be used as Template and processed
	 * by this engine. 
	 * 
	 * @param design the design to check
	 * @return a List of founded error, the list is void if no error are found
	 */
	public static List<String> validateJasperDesig(JasperDesign design){
		
		List<String> errorsList = new ArrayList<String>();
		
		
		JRStyle[] styleArray = design.getStyles();
		
		HashSet<String> foundedStyles = (HashSet<String>)getRequiredStyles().clone();
		
		for(JRStyle style : styleArray){
			String styleName = style.getName();
			foundedStyles.remove(styleName);
		}
		for(String notFoundStyle : foundedStyles){
			errorsList.add("The style "+notFoundStyle+" was not found in the template");
		}
		return errorsList;
	}
	
}
