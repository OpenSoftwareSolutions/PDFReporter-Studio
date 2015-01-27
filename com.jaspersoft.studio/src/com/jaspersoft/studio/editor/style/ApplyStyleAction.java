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
package com.jaspersoft.studio.editor.style;

import java.awt.Color;
import java.util.List;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * Abstract action to apply a TemplateStyle to an element
 * 
 * @author Orlandin Marco
 *
 */
public abstract class ApplyStyleAction {

	/**
	 * The style to apply
	 */
	private TemplateStyle style;
	
	/**
	 * Target element of the TemplateStyle
	 */
	private JRElement element;
	
	public ApplyStyleAction(TemplateStyle style, JRElement element){
		this.style = style;
		this.element = element;
	}

	/**
	 * Return the TemplateStyle
	 * 
	 * @return a Template Style
	 */
	protected TemplateStyle getStyle(){
		return style;
	}
	
	/**
	 * Set a new style
	 * 
	 * @param style the new TemplateStyle
	 */
	protected void setStyle(TemplateStyle style){
		this.style = style;
	}
	
	/**
	 * Return the target element
	 * 
	 * @return the model of the element
	 */
	protected JRElement getElement(){
		return element;
	}
	
	/**
	 * Set all the borders of a JR style to a precise width
	 * 
	 * @param element a JR style
	 * @param lineWidth the width
	 */
	protected void setBorderWidth(JRDesignStyle element, float lineWidth){
		JRLineBox box = element.getLineBox();
		box.getPen().setLineWidth(lineWidth);
		box.getLeftPen().setLineWidth(lineWidth);
		box.getRightPen().setLineWidth(lineWidth);
		box.getBottomPen().setLineWidth(lineWidth);
		box.getTopPen().setLineWidth(lineWidth);
	}
	
	/**
	 * Set all the borders of a JR style to a precise color
	 * 
	 * @param element a JR style
	 * @param lineWidth the width
	 */
	protected void setBorderColor(JRDesignStyle element, Color lineColor){
		JRLineBox box = element.getLineBox();
		box.getPen().setLineColor(lineColor);
		box.getLeftPen().setLineColor(lineColor);
		box.getRightPen().setLineColor(lineColor);
		box.getBottomPen().setLineColor(lineColor);
		box.getTopPen().setLineColor(lineColor);
	}
	
	/**
	 * apply the the style to the element
	 * 
	 * @param design the design of the report
	 */
	public abstract void applayStyle(JasperDesign design);
	
	/**
	 * create a list of JasperReports styles that will be used in the element. The styles must be 
	 * added to the report to be used
	 * 
	 * @param jd the design of the element
	 * @return a list of design style.
	 */
	public abstract List<JRDesignStyle> createStyles(JasperDesign jd);
}
