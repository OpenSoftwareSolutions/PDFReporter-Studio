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
package com.jaspersoft.studio.model.style.command;

import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.style.MConditionalStyle;
import com.jaspersoft.studio.model.style.MStyle;

/**
 * Command that set the attributes of a style to the default values. Support the undo
 * 
 * @author Orlandin Marco
 *
 */
public class ResetConditionalStyleCommand extends Command {

	/**
	 * Style to reset
	 */
	private MStyle oldStyle;

	/** 
	 * The jr design. 
	 */
	@SuppressWarnings("unused")
	private JasperDesign jrDesign;

	/**
	 * JR element of a style with the new default value
	 */
	private JRDesignConditionalStyle newStyle;
	
	/**
	 * JR element of a style with the old value
	 */
	private JRDesignConditionalStyle oldDesignStyle;
	

	public ResetConditionalStyleCommand(JasperDesign jd, MStyle oldStyle) {
		super();
		this.jrDesign = jd;
		this.oldStyle = oldStyle;
		oldDesignStyle = (JRDesignConditionalStyle)oldStyle.getValue();
	}

	/**
	 * Copy all the attributes of the source style to the destination style
	 * 
	 * @param source source style
	 * @param dest destination style
	 */
	private void copyStyleAttributes(JRDesignConditionalStyle source, JRDesignConditionalStyle dest){
		dest.setBackcolor(source.getOwnBackcolor());
		dest.setBlankWhenNull(source.isOwnBlankWhenNull());
		dest.setBold(source.isOwnBold());
		dest.setFill(source.getOwnFillValue());
		dest.setFontName(source.getOwnFontName());
		dest.setFontSize(source.getOwnFontSize());
		dest.setForecolor(source.getOwnForecolor());
		dest.setHorizontalAlignment(source.getOwnHorizontalAlignmentValue());
		dest.setItalic(source.isOwnItalic());
		dest.setMarkup(source.getOwnMarkup());
		dest.setMode(source.getOwnModeValue());
		dest.setParentStyle(source.getStyle());
		dest.setPattern(source.getOwnPattern());
		dest.setPdfEmbedded(source.isOwnPdfEmbedded());
		dest.setPdfEncoding(source.getOwnPdfEncoding());
		dest.setPdfFontName(source.getOwnPdfFontName());
		dest.setRadius(source.getOwnRadius());
		dest.setRotation(source.getOwnRotationValue());
		dest.setScaleImage(source.getOwnScaleImageValue());
		dest.setStrikeThrough(source.isOwnStrikeThrough());
		dest.setUnderline(source.isOwnUnderline());
		dest.setVerticalAlignment(source.getOwnVerticalAlignmentValue());
		dest.setConditionExpression(source.getConditionExpression());
	}
	
	@Override
	public void execute() {
		newStyle = null;
		if (oldStyle == null) return;
		JRDesignConditionalStyle defaultValueStyle = MConditionalStyle.createJRStyle();
		JRDesignConditionalStyle dummyStyle = MConditionalStyle.createJRStyle();
		copyStyleAttributes(oldDesignStyle, dummyStyle);
		newStyle = oldDesignStyle;
		oldDesignStyle = dummyStyle;
		copyStyleAttributes(defaultValueStyle, newStyle);
	}

	@Override
	public boolean canUndo() {
		return (oldStyle != null && newStyle != null);
	}

	@Override
	public void undo() {
		copyStyleAttributes(oldDesignStyle, newStyle);
		newStyle = null;
		oldDesignStyle = null;
	}
}
