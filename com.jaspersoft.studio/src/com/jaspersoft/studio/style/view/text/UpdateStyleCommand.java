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
package com.jaspersoft.studio.style.view.text;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.ModeEnum;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.style.command.CreateStyleCommand;
import com.jaspersoft.studio.model.text.MTextElement;

/**
 * The command to add a TextStyle to a textual element, support the undo
 * 
 * @author Orlandin Marco
 *
 */
public class UpdateStyleCommand extends Command{
	
	/**
	 * The textual element
	 */
	private JRDesignTextElement jrElement;
	
	private MTextElement elementModel = null;
	
	
	/**
	 * The style of the text element before the change
	 */
	private String oldStyle;
	
	/**
	 * The new style
	 */
	private TextStyle newStyleTemplate;
	
	
	private boolean styleCreted = false;

	
	/**
	 * Create the command to apply the textual style to an element
	 * 
	 * @param element a textual element
	 * @param newStyle The new styles to apply to the textual element
	 */
	public UpdateStyleCommand(MTextElement element, TextStyle newStyle){
		this.jrElement = (JRDesignTextElement)element.getValue();
		this.newStyleTemplate = newStyle;
		this.elementModel = element;
		oldStyle = null;
	}

	private JRStyle checkIfExist(String styleName){
		for (JRStyle style :  elementModel.getJasperDesign().getStyles()){
			if (style.getName().contains(styleName)){
				TextStyle compareStyle = new TextStyle(style);
				if (compareStyle.equals(newStyleTemplate)) return style;
			}
		}
		return null;
	}
	
	private String createStyleName(String baseName){
		JasperDesign design = elementModel.getJasperDesign();
		if (!design.getStylesMap().containsKey(baseName)) return baseName;
		else {
			int i = 1;
			String newName = baseName + " " + i; 
			while (design.getStylesMap().containsKey(newName)){
				i++;
				newName = baseName + " " + i; 
			}
			return newName;
		}
	}
	
	
	
	private void copyTextStyleToStyle(JRDesignStyle style){
		style.setHorizontalAlignment(newStyleTemplate.getHorizontalAlignmen());
		style.setVerticalAlignment(newStyleTemplate.getVerticalAlignmen());
		style.setRotation(newStyleTemplate.getRotation());
		style.setBackcolor(newStyleTemplate.getBackGroundColor());
		style.setForecolor(newStyleTemplate.getForeGroundColor());
		style.setMode(newStyleTemplate.isTransparent() ? ModeEnum.TRANSPARENT : ModeEnum.OPAQUE);
		JRFont font = newStyleTemplate.getFont();
		style.setFontName(font.getOwnFontName());
		style.setFontSize(font.getOwnFontsize());
		style.setBold(font.isOwnBold());
		style.setItalic(font.isOwnItalic());
		style.setUnderline(font.isOwnUnderline());
		style.setStrikeThrough(font.isOwnStrikeThrough());
		JRLineBox sourceLineBox = newStyleTemplate.getBorders();
		JRLineBox destLineBox = style.getLineBox();
		destLineBox.setPadding(sourceLineBox.getOwnPadding());
		destLineBox.setLeftPadding(sourceLineBox.getOwnLeftPadding());
		destLineBox.setRightPadding(sourceLineBox.getOwnRightPadding());
		destLineBox.setTopPadding(sourceLineBox.getOwnTopPadding());
		destLineBox.setBottomPadding(sourceLineBox.getOwnBottomPadding());
		setPenValues(sourceLineBox.getPen(), destLineBox.getPen());
		setPenValues(sourceLineBox.getLeftPen(), destLineBox.getLeftPen());
		setPenValues(sourceLineBox.getRightPen(), destLineBox.getRightPen());
		setPenValues(sourceLineBox.getBottomPen(), destLineBox.getBottomPen());
		setPenValues(sourceLineBox.getTopPen(), destLineBox.getTopPen());
	}
	
	@Override
	public void execute() {
		oldStyle = jrElement.getStyle() != null ? jrElement.getStyle().getName() : null;;
		String styleName = newStyleTemplate.getDescription();
		JRStyle previousStyle = checkIfExist(styleName);
		if (previousStyle == null){
		 JRDesignStyle previousDesignStyle = new JRDesignStyle();
		 previousDesignStyle.setName(createStyleName(styleName));
		 copyTextStyleToStyle(previousDesignStyle);
		 CreateStyleCommand command = new CreateStyleCommand(elementModel.getJasperDesign(), previousDesignStyle);
		 styleCreted = true;
		 command.execute();
		 previousStyle = previousDesignStyle;
		}
		elementModel.setPropertyValue(JRDesignElement.PROPERTY_PARENT_STYLE, previousStyle.getName());
	}
	
	/**
	 * Copy the attribute of a JRBoxPen from the source to the destination.
	 * 
	 * @param source of the copy
	 * @param dest destination of the copy
	 */
	private static void setPenValues(JRBoxPen source, JRBoxPen dest){
		dest.setLineColor(source.getOwnLineColor());
		dest.setLineStyle(source.getOwnLineStyleValue());
		dest.setLineWidth(source.getOwnLineWidth());
	}
	
	/**
	 * Apply the passed style to the stored jrElement reference
	 * 
	 * @param style the style to apply
	 */
	public static void applayStyleToTextElement(TextStyle style, JRDesignTextElement element){
		element.setHorizontalAlignment(style.getHorizontalAlignmen());
		element.setVerticalAlignment(style.getVerticalAlignmen());
		element.setRotation(style.getRotation());
		element.setBackcolor(style.getBackGroundColor());
		element.setForecolor(style.getForeGroundColor());
		element.setMode(style.isTransparent() ? ModeEnum.TRANSPARENT : ModeEnum.OPAQUE);
		JRFont font = style.getFont();
		element.setFontName(font.getOwnFontName());
		element.setFontSize(font.getOwnFontsize());
		element.setBold(font.isOwnBold());
		element.setItalic(font.isOwnItalic());
		element.setUnderline(font.isOwnUnderline());
		element.setStrikeThrough(font.isOwnStrikeThrough());
		JRLineBox sourceLineBox = style.getBorders();
		if (sourceLineBox != null){
			JRLineBox destLineBox = element.getLineBox();
			destLineBox.setPadding(sourceLineBox.getOwnPadding());
			destLineBox.setLeftPadding(sourceLineBox.getOwnLeftPadding());
			destLineBox.setRightPadding(sourceLineBox.getOwnRightPadding());
			destLineBox.setTopPadding(sourceLineBox.getOwnTopPadding());
			destLineBox.setBottomPadding(sourceLineBox.getOwnBottomPadding());
			setPenValues(sourceLineBox.getPen(), destLineBox.getPen());
			setPenValues(sourceLineBox.getLeftPen(), destLineBox.getLeftPen());
			setPenValues(sourceLineBox.getRightPen(), destLineBox.getRightPen());
			setPenValues(sourceLineBox.getBottomPen(), destLineBox.getBottomPen());
			setPenValues(sourceLineBox.getTopPen(), destLineBox.getTopPen());
		}
	}
	
	@Override
	public void undo() {
		if (styleCreted){
			JRStyle style = jrElement.getStyle();
			elementModel.getJasperDesign().removeStyle(style);
		}
		elementModel.setPropertyValue(JRDesignElement.PROPERTY_PARENT_STYLE, oldStyle);
	}
	
	/**
	 * Undo is available if the text element and the style previous the update are available 
	 */
	@Override
	public boolean canUndo() {
		return (jrElement != null);
	}

}
