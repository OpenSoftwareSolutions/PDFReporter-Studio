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

import java.awt.Color;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.translation.resources.AbstractResourceDefinition;

/**
 * 
 * This class specialize the TemplateStyle to handle a text element (static text or text field). 
 * 
 * @author Orlandin Marco
 *
 */
public class TextStyle extends TemplateStyle {


	private static final long serialVersionUID = 1539973461820002113L;

	private final static String TRANSPARENT = "is_transparent";
	
	private final static String FOREGROUND_COLOR = "foreground_color";
	
	private final static String BACKGROUND_COLOR = "background_color";
	
	private final static String VERTICAL_ALIGNMENT = "vertical_alignment";
	
	private final static String HORIZONTAL_ALIGNMENT = "horizontal_alignment";
	
	private final static String ROTATION = "rotation";
	
	private final static String BORDER_BOX = "linebox";

	private final static String FONT = "font";
	
	public TextStyle(){
		super(null,null);
	}
	
	public TextStyle(JRStyle style){
		super(null,null);
		setTransparent(style.getOwnModeValue() != null ? ModeEnum.TRANSPARENT.equals(style.getOwnModeValue()) : true);
		setBackGround(style.getOwnBackcolor());
		setForeGround(style.getOwnForecolor());
		setVerticalAlignmen(style.getOwnVerticalAlignmentValue() != null ? style.getOwnVerticalAlignmentValue() : VerticalAlignEnum.TOP);
		setHorizontalAlignmen(style.getOwnHorizontalAlignmentValue() != null ? style.getOwnHorizontalAlignmentValue() : HorizontalAlignEnum.LEFT);
		setRotation(style.getOwnRotationValue() != null ? style.getOwnRotationValue() : RotationEnum.NONE);
		
		JRFont font = new JRBaseFont();
		font.setBold(style.isOwnBold() != null ? style.isOwnBold() : false);
		font.setItalic(new Boolean(style.isOwnItalic() != null ? style.isOwnItalic() : false));
		style.isItalic();
		font.setUnderline(new Boolean(style.isOwnUnderline() != null ? style.isOwnUnderline() : false));
		font.setStrikeThrough(new Boolean(style.isOwnStrikeThrough() != null ? style.isOwnStrikeThrough() : false));
		font.setFontName(new String(style.getOwnFontName()));
		font.setFontSize(new Float(style.getOwnFontsize()));
		setFont(font);
		
		JRLineBox originBox = style.getLineBox();
		JRBaseLineBox copyBox = new JRBaseLineBox(null);
		copyBox.setPadding(originBox.getOwnPadding() != null ? new Integer(originBox.getOwnPadding()): null);
		copyBox.setTopPadding(originBox.getOwnTopPadding() != null ? new Integer(originBox.getOwnTopPadding()): null);
		copyBox.setBottomPadding(originBox.getOwnBottomPadding() != null ? new Integer(originBox.getOwnBottomPadding()): null);
		copyBox.setLeftPadding(originBox.getOwnLeftPadding() != null ? new Integer(originBox.getOwnLeftPadding()): null);
		copyBox.setRightPadding(originBox.getOwnRightPadding() != null ? new Integer(originBox.getOwnRightPadding()): null);
		copyLinePen(originBox.getPen(), copyBox.getPen());
		copyLinePen(originBox.getLeftPen(), copyBox.getLeftPen());
		copyLinePen(originBox.getRightPen(), copyBox.getRightPen());
		copyLinePen(originBox.getBottomPen(), copyBox.getBottomPen());
		copyLinePen(originBox.getTopPen(), copyBox.getTopPen());
		setBorders(copyBox);
		
		String name = style.getName();
		if (name != null && !name.isEmpty()) setDescription(name);
	}
	
	public Boolean isTransparent(){
		Object value = getProperty(TRANSPARENT);
		return value != null? (Boolean)value : false;
	}
	
	public AlfaRGB getBackGround(){
		return (AlfaRGB)getProperty(BACKGROUND_COLOR);
	}

	public AlfaRGB getForeGround(){
		return (AlfaRGB)getProperty(FOREGROUND_COLOR);
	}
	
	public Color getBackGroundColor(){
		return alfaRGBtoColor((AlfaRGB)getProperty(BACKGROUND_COLOR));
	}

	public Color getForeGroundColor(){
		return alfaRGBtoColor((AlfaRGB)getProperty(FOREGROUND_COLOR));
	}


	public VerticalAlignEnum getVerticalAlignmen(){
		return (VerticalAlignEnum)getProperty(VERTICAL_ALIGNMENT);
	}
	
	public HorizontalAlignEnum getHorizontalAlignmen(){
		return (HorizontalAlignEnum)getProperty(HORIZONTAL_ALIGNMENT);
	}
	
	public RotationEnum getRotation(){
		return (RotationEnum)getProperty(ROTATION);
	}
	
	public JRFont getFont(){
		return (JRFont)getProperty(FONT);
	}
	
	public JRLineBox getBorders(){
		return (JRLineBox)getProperty(BORDER_BOX);
	}
	
	public void setTransparent(Boolean value){
		storePropertiy(TRANSPARENT, value);
	}
	
	public void setBackGround(AlfaRGB value){
		storePropertiy(BACKGROUND_COLOR,value);
	}

	public void setForeGround(AlfaRGB value){
		storePropertiy(FOREGROUND_COLOR,value);
	}
	
	
	public void setBackGround(Color value){
		AlfaRGB aColor = value != null ? new AlfaRGB(new RGB(value.getRed(), value.getGreen(), value.getBlue()), value.getAlpha()) : null;
		storePropertiy(BACKGROUND_COLOR,aColor);
	}

	public void setForeGround(Color value){
		AlfaRGB aColor = value != null ? new AlfaRGB(new RGB(value.getRed(), value.getGreen(), value.getBlue()), value.getAlpha()) : null;
		storePropertiy(FOREGROUND_COLOR,aColor);
	}

	public void setVerticalAlignmen(VerticalAlignEnum value){
		storePropertiy(VERTICAL_ALIGNMENT, value);
	}
	
	public void setHorizontalAlignmen(HorizontalAlignEnum value){
		storePropertiy(HORIZONTAL_ALIGNMENT, value);
	}
	
	public void setRotation(RotationEnum value){
		storePropertiy(ROTATION, value);
	}
	
	public void setFont(JRFont value){
		storePropertiy(FONT, value);
	}
	
	public void setBorders(JRLineBox value){
		storePropertiy(BORDER_BOX, value);
	}
	
	private String getFontXML(JRFont value){
		String result = "<font name=\"" + value.getOwnFontName() +"\" ";
		result += "size=\""+value.getOwnFontsize().toString()+"\" ";
		result += "isBold=\""+value.isOwnBold()+"\" isItalic=\""+value.isOwnItalic()+"\" ";
		result += "isUnderline=\""+value.isOwnUnderline()+"\" isStriketrought=\""+value.isOwnStrikeThrough()+"\"/>";
		return result;
	}
	
	private static JRFont buildFont(Node xmlFontNode){
		NamedNodeMap fontAttributes = xmlFontNode.getAttributes();
		String fontName = fontAttributes.getNamedItem("name").getNodeValue();
		float size = Float.parseFloat(fontAttributes.getNamedItem("size").getNodeValue());
		boolean isBold = fontAttributes.getNamedItem("isBold").getNodeValue().equals("true"); 
		boolean isItalic = fontAttributes.getNamedItem("isItalic").getNodeValue().equals("true"); 
		boolean isUnderline = fontAttributes.getNamedItem("isUnderline").getNodeValue().equals("true"); 
		boolean isStriketrought = fontAttributes.getNamedItem("isStriketrought").getNodeValue().equals("true"); 
		
		JRBaseFont result = new JRBaseFont();
		result.setFontName(fontName);
		result.setFontSize(size);
		result.setBold(isBold);
		result.setItalic(isItalic);
		result.setUnderline(isUnderline);
		result.setStrikeThrough(isStriketrought);
		return result;
	}
	
	private AlfaRGB colorToAlfaRGB(Color value){
		if (value == null) return null;
		int red = value.getRed();
		int green = value.getGreen();
		int blue = value.getBlue();
		return new AlfaRGB(new RGB(red,green,blue), value.getAlpha());
	}
	
	private static Color alfaRGBtoColor(AlfaRGB value){
		if (value == null) return null;
		RGB rgb = value.getRgb();
		if (rgb == null) return null;
		int red = rgb.red;
		int green = rgb.green;
		int blue = rgb.blue;
		return new Color(red, green, blue, value.getAlfa());
	}
	
	private String getPenXML(String tagName, JRBoxPen value){
		LineStyleEnum style = value.getOwnLineStyleValue() != null ? value.getOwnLineStyleValue() : LineStyleEnum.SOLID;
		float lineWidth = value.getOwnLineWidth() != null ? value.getOwnLineWidth() : 0f;
		Color color = value.getOwnLineColor() != null ? value.getOwnLineColor() : new Color(0, 0, 0);
		String result = "<"+tagName+" lineStyle=\"" + style.getValueByte() +"\" ";
		result += "lineWidth=\""+lineWidth+"\">";
		result += xmlColor("lineColor", colorToAlfaRGB(color));
		result += "</"+tagName+">";
		return result;
	}
	
	private static void buildPen(Node xmlPenNode, JRBoxPen sourcePen){
		NamedNodeMap penAttributes = xmlPenNode.getAttributes();
		LineStyleEnum lineStyle = LineStyleEnum.getByValue(Byte.valueOf(penAttributes.getNamedItem("lineStyle").getNodeValue()));
		float lineWidth = Float.parseFloat(penAttributes.getNamedItem("lineWidth").getNodeValue());
		AlfaRGB lineColor = null;
		Node firstChild = xmlPenNode.getFirstChild();
		if (firstChild != null && firstChild.getNodeName().equals("lineColor")){
			lineColor = rgbColor(firstChild);
		}
		if (lineColor != null) sourcePen.setLineColor(alfaRGBtoColor(lineColor));
		sourcePen.setLineStyle(lineStyle);
		sourcePen.setLineWidth(lineWidth);
	}
	
	
	private String getLineBoxXML(JRLineBox value){
		String result = "<linebox padding=\"" + value.getOwnPadding() +"\" ";
		result += "leftPadding=\""+value.getOwnLeftPadding()+"\" ";
		result += "rightPadding=\""+value.getOwnRightPadding()+"\" topPadding=\""+value.getOwnTopPadding()+"\" ";
		result += "bottomPadding=\""+value.getOwnBottomPadding()+"\">";
		result += getPenXML("pen", value.getPen());
		result += getPenXML("leftPen", value.getLeftPen());
		result += getPenXML("rightPen", value.getRightPen());
		result += getPenXML("topPen", value.getTopPen());
		result += getPenXML("bottomPen", value.getBottomPen());
		result += "</linebox>";
		return result;
	}
	
	private static Integer getSafeIntValue(String value){
		if (value != null && !value.equals("null")){
			try{
				Integer intValue = Integer.parseInt(value);
				return intValue;
			} catch (NumberFormatException ex){
				return null;
			}
		}
		return null;
	}
	
	private static JRLineBox buildBox(Node xmlBoxNode){
		NamedNodeMap boxAttributes = xmlBoxNode.getAttributes();
		Integer padding = getSafeIntValue(boxAttributes.getNamedItem("padding").getNodeValue());
		Integer leftPadding = getSafeIntValue(boxAttributes.getNamedItem("leftPadding").getNodeValue());
		Integer rightPadding = getSafeIntValue(boxAttributes.getNamedItem("rightPadding").getNodeValue());
		Integer bottomPadding = getSafeIntValue(boxAttributes.getNamedItem("bottomPadding").getNodeValue());
		Integer topPadding = getSafeIntValue(boxAttributes.getNamedItem("topPadding").getNodeValue());
		
		JRBaseLineBox result = new JRBaseLineBox(null);
		result.setPadding(padding);
		result.setTopPadding(topPadding);
		result.setBottomPadding(bottomPadding);
		result.setLeftPadding(leftPadding);
		result.setRightPadding(rightPadding);

		Node firstChild = xmlBoxNode.getFirstChild();
		while(firstChild!=null){
			if (firstChild.getNodeName().equals("pen")){
				buildPen(firstChild, result.getPen());
			} else if (firstChild.getNodeName().equals("leftPen")) {
				buildPen(firstChild, result.getLeftPen());
			} else if (firstChild.getNodeName().equals("rightPen")) {
				buildPen(firstChild, result.getRightPen());
			} else if (firstChild.getNodeName().equals("topPen")) {
				buildPen(firstChild, result.getTopPen());
			} else if (firstChild.getNodeName().equals("bottomPen")) {
				buildPen(firstChild, result.getBottomPen());
			} 
			firstChild = firstChild.getNextSibling();
		}
		return result;
	}
	
	/**
	 * Return an XML representation of the crosstab style
	 * 
	 * @return a string containing the xml representation of the crosstab style
	 */
	@Override
	public String getXMLData() {
		String result = "<"+getTemplateName()+" type=\"" + getTemplateName() +"\" ";
		result += "verticalAlignment=\""+getVerticalAlignmen().getValueByte()+"\" horizontalAlignment=\""+getHorizontalAlignmen().getValueByte()+"\" rotation=\""+getRotation().getValueByte()+"\" ";
		result += "isTransparent=\""+isTransparent().toString()+"\">";
		result += "<description>".concat(getDescription()).concat("</description>");
		if (getForeGround() != null) result += xmlColor("foreground",getForeGround());
		if (getBackGround() != null) result += xmlColor("background",getBackGround());
		result += getFontXML(getFont());
		result += getLineBoxXML(getBorders());
		result += "</"+getTemplateName()+">";
		return result;
	}


	/**
	 * Rebuild a CrosstabStyle from its XML representation
	 * 
	 * @param xmlNode an XML node with the representation of a CrosstabStyle
	 * @return the CrosstabStyle builded from the xmlNode, or null if something goes wrong during the rebuilding
	 */
	@Override
	public TemplateStyle buildFromXML(Node xmlNode) {
		try{
			NamedNodeMap rootAttributes = xmlNode.getAttributes();
			VerticalAlignEnum verticalAlignment = VerticalAlignEnum.getByValue(Byte.valueOf(rootAttributes.getNamedItem("verticalAlignment").getNodeValue()));
			HorizontalAlignEnum horizontalAlignment = HorizontalAlignEnum.getByValue(Byte.valueOf(rootAttributes.getNamedItem("horizontalAlignment").getNodeValue()));
			RotationEnum rotation = RotationEnum.getByValue(Byte.valueOf(rootAttributes.getNamedItem("rotation").getNodeValue()));
			boolean transparent = rootAttributes.getNamedItem("isTransparent").getNodeValue().equals("true"); 
			
			AlfaRGB background = null;
			AlfaRGB foreground = null;
			JRFont font = null;
			JRLineBox box = null;
			
			Node firstChild = xmlNode.getFirstChild();
			String description = null;
			while(firstChild!=null){
				if (firstChild.getNodeName().equals("foreground")){
					foreground = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("background")) {
					background = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("font")) {
					font = buildFont(firstChild);
				} else if (firstChild.getNodeName().equals("linebox")) {
					box = buildBox(firstChild);
				}  else if (firstChild.getNodeName().equals("description")) {
					Node descriptionNode = firstChild.getChildNodes().item(0);
					description = descriptionNode != null ? descriptionNode.getNodeValue() : "";			
				}
				firstChild = firstChild.getNextSibling();
			}
			TextStyle result = new TextStyle();
			result.setVerticalAlignmen(verticalAlignment);
			result.setHorizontalAlignmen(horizontalAlignment);
			result.setRotation(rotation);
			result.setTransparent(transparent);
			result.setBackGround(background);
			result.setForeGround(foreground);
			result.setFont(font);
			result.setBorders(box);
			result.setDescription(description);
			return result;
		} catch(Exception ex){
			System.out.println("Unable to rebuild the text style");
			ex.printStackTrace();
			return null;
		}
	}
		
	public TextStyle clone(){
		TextStyle copy = new TextStyle();
		copy.setBackGround(getBackGround() != null ? getBackGround().clone() : null);
		copy.setForeGround(getForeGround() != null ? getForeGround().clone() : null);
		copy.setTransparent(new Boolean(isTransparent()));
		copy.setDescription(new String(getDescription()));
		copy.setRotation(getRotation());
		copy.setHorizontalAlignmen(getHorizontalAlignmen());
		copy.setVerticalAlignmen(getVerticalAlignmen());
		JRBaseFont copyFont = new JRBaseFont();
		JRFont originFont = getFont();
		copyFont.setBold(new Boolean(originFont.isOwnBold()));
		copyFont.setItalic(new Boolean(originFont.isOwnItalic()));
		copyFont.setUnderline(new Boolean(originFont.isOwnUnderline()));
		copyFont.setStrikeThrough(new Boolean(originFont.isOwnStrikeThrough()));
		copyFont.setFontName(new String(originFont.getOwnFontName()));
		copyFont.setFontSize(new Float(originFont.getOwnFontsize()));
		copy.setFont(copyFont);
		JRLineBox originBox = getBorders();
		JRBaseLineBox copyBox = new JRBaseLineBox(null);
		copyBox.setPadding(originBox.getOwnPadding() != null ? new Integer(originBox.getOwnPadding()): null);
		copyBox.setTopPadding(originBox.getOwnTopPadding() != null ? new Integer(originBox.getOwnTopPadding()): null);
		copyBox.setBottomPadding(originBox.getOwnBottomPadding() != null ? new Integer(originBox.getOwnBottomPadding()): null);
		copyBox.setLeftPadding(originBox.getOwnLeftPadding() != null ? new Integer(originBox.getOwnLeftPadding()): null);
		copyBox.setRightPadding(originBox.getOwnRightPadding() != null ? new Integer(originBox.getOwnRightPadding()): null);
		copyLinePen(originBox.getPen(), copyBox.getPen());
		copyLinePen(originBox.getLeftPen(), copyBox.getLeftPen());
		copyLinePen(originBox.getRightPen(), copyBox.getRightPen());
		copyLinePen(originBox.getBottomPen(), copyBox.getBottomPen());
		copyLinePen(originBox.getTopPen(), copyBox.getTopPen());
		copy.setBorders(copyBox);
		return copy;
	}
	
	private boolean equalsPen(JRBoxPen pen1, JRBoxPen pen2){
		if (pen1 == null) return pen2 == null;
		if (pen2 == null) return pen1 == null;
		if (!AbstractResourceDefinition.safeEquals(pen1.getOwnLineColor(),pen2.getOwnLineColor())) return false;
		if (!AbstractResourceDefinition.safeEquals(pen1.getOwnLineStyleValue(),pen2.getOwnLineStyleValue())) return false;
		if (!AbstractResourceDefinition.safeEquals(pen1.getOwnLineWidth(), pen2.getOwnLineWidth())) return false;
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TextStyle){
			TextStyle element2 = (TextStyle)obj;
			if (!element2.isTransparent().equals(isTransparent())) return false;
			if (!AbstractResourceDefinition.safeEquals(getBackGround(), element2.getBackGround())) return false;
			if (!AbstractResourceDefinition.safeEquals(getForeGround(), element2.getForeGround())) return false;
			if (!AbstractResourceDefinition.safeEquals(getHorizontalAlignmen(), element2.getHorizontalAlignmen())) return false;
			if (!AbstractResourceDefinition.safeEquals(getVerticalAlignmen(), element2.getVerticalAlignmen())) return false;
			if (!AbstractResourceDefinition.safeEquals(getRotation(), element2.getRotation())) return false;
			JRFont font1 = getFont();
			JRFont font2 = element2.getFont();
			if (!AbstractResourceDefinition.safeEquals(font1.isOwnBold(), font2.isOwnBold())) return false;
			if (!AbstractResourceDefinition.safeEquals(font1.isOwnItalic(), font2.isOwnItalic())) return false;
			if (!AbstractResourceDefinition.safeEquals(font1.isOwnUnderline(), font2.isOwnUnderline())) return false;
			if (!AbstractResourceDefinition.safeEquals(font1.isOwnStrikeThrough(), font2.isOwnStrikeThrough())) return false;
			if (!AbstractResourceDefinition.safeEquals(font1.getOwnFontName(), font2.getOwnFontName())) return false;
			if (!AbstractResourceDefinition.safeEquals(font1.getOwnFontsize(), font2.getOwnFontsize())) return false;
			JRLineBox box1 = getBorders();
			JRLineBox box2 = element2.getBorders();
			if (!AbstractResourceDefinition.safeEquals(box1.getOwnPadding(), box2.getOwnPadding())) return false;
			if (!AbstractResourceDefinition.safeEquals(box1.getOwnTopPadding(), box2.getOwnTopPadding())) return false;
			if (!AbstractResourceDefinition.safeEquals(box1.getOwnBottomPadding(), box2.getOwnBottomPadding())) return false;
			if (!AbstractResourceDefinition.safeEquals(box1.getOwnLeftPadding(), box2.getOwnLeftPadding())) return false;
			if (!AbstractResourceDefinition.safeEquals(box1.getOwnRightPadding(), box2.getOwnRightPadding())) return false;
			if (!equalsPen(box1.getPen(), box2.getPen())) return false;
			if (!equalsPen(box1.getBottomPen(), box2.getBottomPen())) return false;
			if (!equalsPen(box1.getTopPen(), box2.getTopPen())) return false;
			if (!equalsPen(box1.getLeftPen(), box2.getLeftPen())) return false;
			if (!equalsPen(box1.getRightPen(), box2.getRightPen())) return false;
			return true;
		}
		return false;
	}
	
	private void copyLinePen(JRBoxPen originPen, JRBoxPen destinationPen){
		Color newColor = null;
		Color originLineColor = originPen.getOwnLineColor();
		if (originLineColor != null) newColor = new Color(originLineColor.getRed(), originLineColor.getGreen(), originLineColor.getBlue(), originLineColor.getAlpha());
		destinationPen.setLineColor(newColor);
		destinationPen.setLineStyle(originPen.getOwnLineStyleValue());
		destinationPen.setLineWidth(originPen.getOwnLineWidth() != null ? new Float(originPen.getOwnLineWidth()) : null);
	}

	/**
	 * Return an unique identifier of the crosstab template type
	 * 
	 * @return a string representing the type of the crosstab template
	 */
	@Override
	public String getTemplateName() {
		return "textStyle";
	}
}
