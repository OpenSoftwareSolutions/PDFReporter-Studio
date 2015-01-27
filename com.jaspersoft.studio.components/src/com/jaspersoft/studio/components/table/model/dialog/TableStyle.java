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
package com.jaspersoft.studio.components.table.model.dialog;

import java.awt.Color;

import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.property.color.ColorSchemaGenerator;
import com.jaspersoft.studio.property.color.ColorSchemaGenerator.SCHEMAS;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * 
 * This class specialize the TemplateStyle to handle the table. Essentially this is done 
 * to provide standard key to access the data and some conversion method.
 * 
 * @author Orlandin Marco
 *
 */
public class TableStyle extends TemplateStyle {

	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -6611750741729597106L;

	/**
	 * Key for the border style attribute
	 */
	public final static String BORDER_STYLE_KEY = "border_style";
	
	/**
	 * Key for the border color attribute
	 */
	public final static String BORDER_COLOR_KEY = "border_corlor";
	
	/**
	 * Key for the boolean attribute that identify if the color of the detail row
	 * is alternated
	 */
	public final static String ALTERNATE_COLOR_KEY = "alternate_corlor";
	
	/**
	 * Key for the color detail attribute, it will be used only if the attribute identified 
	 * by ALTERNATE_COLOR_KEY is true
	 */
	public final static String COLOR_DETAIL = "color_detail";
	
	/**
	 * Key for the color detail cells, overridden from the COLOR_DETAIL on the odd cell when ALTERNATE_COLOR_KEY
	 * is true
	 */
	public final static String STANDARD_COLOR_DETAIL = "color_detail_standard";
	
	/**
	 * Key for the color of the column header and footer attributes
	 */
	public final static String COLOR_COL_HEADER = "color_column_header";
	
	/**
	 * Key for the color of the table header and footer attributes
	 */
	public final static String COLOR_TABLE_HEADER = "color_table_header";
	
	/**
	 * Enumeration for the available type of borders for the table 
	 * 
	 * FULL: the table have both horizontal and vertical borders
	 * 
	 * PARTIAL_VERTICAL: like FULL but without the vertical border ad the vertical edges of the table
	 * 
	 * ONLY_HORIZONTAL: the table has only horizontal borders
	 * 
	 * @author Orlandin Marco
	 *
	 */
	public static enum BorderStyleEnum {FULL, PARTIAL_VERTICAL, ONLY_HORIZONTAL};
	
	/**
	 * Create an instance of the class
	 * 
	 * @param baseColor base color that will be used to color the cells
	 * @param variation key of the variation of the color
	 * @param borderStyle style of the border
	 * @param borderColor color of the border
	 * @param altenrateColor true if the color of the detail are alternated, false otherwise
	 */
	public TableStyle(AlfaRGB baseColor, ColorSchemaGenerator.SCHEMAS variation, BorderStyleEnum borderStyle, AlfaRGB borderColor, boolean altenrateColor) {
		super(baseColor, variation);
		setBorderStyle(borderStyle);
		setAlternateRowColor(altenrateColor);
		setBorderColor(borderColor);
		generateAndStoreColor(COLOR_COL_HEADER, 2);
		generateAndStoreColor(COLOR_TABLE_HEADER, 3);
		Color detail = getColorValue(COLOR_TABLE_HEADER);
		detail = ColorSchemaGenerator.overlayWhite(detail);
		detail = ColorSchemaGenerator.overlayWhite(detail);
		storeColor(COLOR_DETAIL, new AlfaRGB(new RGB(detail.getRed(), detail.getGreen(), detail.getBlue()), detail.getAlpha()));
		storeColor(STANDARD_COLOR_DETAIL, AlfaRGB.getFullyOpaque(new RGB(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue())));
	}
	
	/**
	 * Create an instance of the class
	 * 
	 * @param colorTHeader the color used in the table header cells
	 * @param colorCheader the color used in the column header cells
	 * @param colorDetail the color used in the detail cells
	 * @param colorAlternateDetail the color used in the detail cells, when the row is odd and the attribute alternate color is true
	 * @param borderStyle style of the border
	 * @param borderColor color of the border
	 * @param altenrateColor true if the color of the detail are alternated, false otherwise
	 */
	public TableStyle(AlfaRGB colorTHeader, AlfaRGB colorCheader, AlfaRGB colorDetail, AlfaRGB colorAlternateDetail, BorderStyleEnum borderStyle, AlfaRGB borderColor, boolean altenrateColor) {
		super(null, null);
		setBorderStyle(borderStyle);
		setAlternateRowColor(altenrateColor);
		setBorderColor(borderColor);
		storeColor(COLOR_COL_HEADER, colorCheader);
		storeColor(COLOR_TABLE_HEADER, colorTHeader);
		storeColor (COLOR_DETAIL, colorAlternateDetail);
		storeColor(STANDARD_COLOR_DETAIL, colorDetail);
	}
	
	
	public TableStyle(){
		super(null,null);
	}
	
	/**
	 * Set the border style
	 * 
	 * @param value the style of the borders of the table
	 */
	public void setBorderStyle(BorderStyleEnum value){
		storePropertiy(BORDER_STYLE_KEY, value);
	}
	
	/**
	 * get the border of the table
	 * 
	 * @return return the enumeration value that represent the style of the borders
	 */
	public BorderStyleEnum getBorderStyle(){
		return (BorderStyleEnum)getProperty(BORDER_STYLE_KEY);
	}
	
	/**
	 * Set the borders color
	 * 
	 * @param value an SWT RGB color
	 */
	public void setBorderColor(AlfaRGB value){
		storeColor(BORDER_COLOR_KEY, value);
	}
	
	/**
	 * Return the borders color
	 * 
	 * @return an AWT color
	 */
	public Color getBorderColor(){
		AlfaRGB alfaColor =  super.getColor(BORDER_COLOR_KEY);
		RGB rgbColor = alfaColor.getRgb();
		return new Color(rgbColor.red, rgbColor.green, rgbColor.blue, alfaColor.getAlfa());
	}
	
	/**
	 * Return the borders color
	 * 
	 * @return an AWT color
	 */
	public AlfaRGB getRGBBorderColor(){
		AlfaRGB rgbColor =  super.getColor(BORDER_COLOR_KEY);
		return rgbColor;
	}
	
	/**
	 * Set if the color of the detail rows are alternated
	 * 
	 * @param value true if the color of the detail rows are alternated, 
	 * false otherwise
	 */
	public void setAlternateRowColor(boolean value){
		storePropertiy(ALTERNATE_COLOR_KEY, value);
	}
	
	/**
	 * Check if the color of the rows are alternated
	 * 
	 * @return true if the color of the detail rows are alternated, 
	 * false otherwise
	 */
	public Boolean hasAlternateColor(){
		return (Boolean)getProperty(ALTERNATE_COLOR_KEY);
	}
	
	/**
	 * Read a color properties and return it as an AWT color
	 * 
	 * @param name the name of the color properties
	 * @return the color read, in AWT.Color format
	 */
	public Color getColorValue(String name){
		AlfaRGB alfaColor =  super.getColor(name);
		RGB rgbColor = alfaColor.getRgb();
		return new Color(rgbColor.red, rgbColor.green, rgbColor.blue, alfaColor.getAlfa());
	}
	
	
	/**
	 * Return a string unique representation for the style
	 */
	@Override
	public String toString() {
		String color1 = getColor(COLOR_TABLE_HEADER).toString();
		String color2 = getColor(COLOR_COL_HEADER).toString();
		String color3 = getColor(STANDARD_COLOR_DETAIL).toString();
		String color4 = getColor(COLOR_DETAIL).toString();
		Boolean alternate = hasAlternateColor();
		String borderStyle = getBorderStyle().toString();
		return color1.concat(color2).concat(color3).concat(color4).concat(borderStyle)
				.concat(getRGBBorderColor().toString()).concat(alternate.toString());
	}
	
	/**
	 * Return an XML representation of the template style
	 * 
	 * @return a string containing the xml representation of the style
	 */
	@Override
	public String getXMLData() {
		String result = "<"+getTemplateName()+" type=\"" + getTemplateName() +"\" ";
		result += "alternateColor=\""+hasAlternateColor().toString(); // colorSchema=\"" + variation.name();
		result += "\" borderStyle=\""+getBorderStyle().name();
		result += "\"><description>".concat(getDescription()).concat("</description>");
		//result += xmlColor("baseColor", baseColor);
		result += xmlColor("borderColor", getRGBBorderColor());
		result += xmlColor("tHeaderColor",getColor(COLOR_TABLE_HEADER));
		result += xmlColor("cHeaderColor",getColor(COLOR_COL_HEADER));
		result += xmlColor("detailColor",getColor(STANDARD_COLOR_DETAIL));
		result += xmlColor("altDetailColor",getColor(COLOR_DETAIL));
		result += "</"+getTemplateName()+">";
		return result;
	}
	
	/**
	 * Rebuild a TableStyle from its XML representation
	 * 
	 * @param xmlNode an XML node with the representation of a TableStyle
	 * @return the TemplateStyle builded from the xmlNode, or null if something goes wrong during the rebuilding
	 */
	@Override
	public TemplateStyle buildFromXML(Node xmlNode) {
		try{
			NamedNodeMap rootAttributes = xmlNode.getAttributes();
			boolean alternateColor = rootAttributes.getNamedItem("alternateColor").getNodeValue().equals("true"); 
			
			Node schemasNode = rootAttributes.getNamedItem("colorSchema");
			SCHEMAS variation =  schemasNode != null ? SCHEMAS.valueOf(schemasNode.getNodeValue()) : null;
			BorderStyleEnum borderStyle = BorderStyleEnum.valueOf(rootAttributes.getNamedItem("borderStyle").getNodeValue());
			Node firstChild = xmlNode.getFirstChild();
			String description = null;
			AlfaRGB baseColor = null;
			AlfaRGB borderColor = null;
			AlfaRGB colorTHeader = null;
			AlfaRGB colorCHeader = null;
			AlfaRGB colorDetail = null;
			AlfaRGB colorAlternateDetail = null;
			while(firstChild!=null){
				if (firstChild.getNodeName().equals("baseColor")){
					baseColor = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("borderColor")){
					borderColor = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("description")) {
					Node descriptionNode = firstChild.getChildNodes().item(0);
					description = descriptionNode != null ? descriptionNode.getNodeValue() : "";		
				} else if (firstChild.getNodeName().equals("tHeaderColor")) {
					colorTHeader = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("cHeaderColor")) {
					colorCHeader = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("detailColor")) {
					colorDetail = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("altDetailColor")) {
					colorAlternateDetail = rgbColor(firstChild);
				}
				firstChild = firstChild.getNextSibling();
			}
			TableStyle result = null;
			if (variation != null && baseColor != null) result = new TableStyle(baseColor, variation, borderStyle, borderColor, alternateColor);
			else result =  new TableStyle(colorTHeader, colorCHeader, colorDetail, colorAlternateDetail, borderStyle, borderColor, alternateColor);
			result.setDescription(description);
			return result;
		} catch(Exception ex){
			System.out.println("Unable to rebuild the table style");
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Return an unique identifier of the table template type
	 * 
	 * @return a string representing the type of the table template
	 */
	@Override
	public String getTemplateName() {
		return "tableStyle";
	}

}
