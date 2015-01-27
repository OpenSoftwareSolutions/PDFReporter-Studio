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
package com.jaspersoft.studio.components.crosstab.model.dialog;

import java.awt.Color;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.property.color.ColorSchemaGenerator.SCHEMAS;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * 
 * This class specialize the TemplateStyle to handle the crosstab element. 
 * 
 * @author Orlandin Marco
 *
 */
public class CrosstabStyle extends TemplateStyle {

	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -2866538585051431701L;

	/**
	 * Key for the boolean attribute that specify when the grid is white
	 */
	private final static String WHITE_GRID = "white_grid";
	
	/**
	 * Key for the boolean attribute that specify to show or not the grid
	 */
	private final static String SHOW_GRID = "show_grid";
	
	/**
	 * Key for the total row and column color
	 */
	public final static String COLOR_TOTAL = "color_total";
	
	/**
	 * Key for the Color for the group row and column
	 */
	public final static String COLOR_GROUP = "color_group";
	
	/**
	 * Key for the detail cells color
	 */
	public final static String COLOR_DETAIL = "color_detail";
	
	/**
	 * Key for the color of the measure cells
	 */
	public final static String COLOR_MEASURES = "color_measures";
	
	public CrosstabStyle(AlfaRGB baseColor, SCHEMAS variation, boolean whiteGrid) {
		super(baseColor, variation);
		storePropertiy(WHITE_GRID, whiteGrid);
		storePropertiy(SHOW_GRID, true);
		generateAndStoreColor(COLOR_TOTAL, 1);
		generateAndStoreColor(COLOR_GROUP, 2);
		generateAndStoreColor(COLOR_MEASURES, 3);
		storeColor(COLOR_DETAIL, AlfaRGB.getFullyOpaque(ColorConstants.white.getRGB()));
	}
	
	public CrosstabStyle(AlfaRGB colorTotal, AlfaRGB colorGroup, AlfaRGB colorMeasures, AlfaRGB colorDetail, boolean whiteGrid) {
		super(null, null);
		storePropertiy(WHITE_GRID, whiteGrid);
		storePropertiy(SHOW_GRID, true);
		storeColor(COLOR_TOTAL, colorTotal);
		storeColor(COLOR_GROUP, colorGroup);
		storeColor(COLOR_MEASURES, colorMeasures);
		storeColor(COLOR_DETAIL, colorDetail);
	}
	
	
	public CrosstabStyle(){
		super(null,null);
	}
	
	/**
	 * Check if the crosstab has a white grid
	 * 
	 * @return true if the corsstab has a white grid, false otherwise
	 */
	public Boolean getWhiteGrid(){
		return (Boolean)getProperty(WHITE_GRID);
	}
	
	/**
	 * Check if the crosstab has to has show the grid
	 * 
	 * @return true if the corsstab has to show, false otherwise
	 */
	public Boolean isShowGrid(){
		return (Boolean)getProperty(SHOW_GRID);
	}
	
	/**
	 * Choose to show or not the grid
	 * 
	 * @param value true to show the grid, false otherwise
	 */
	public void setShowGrid(boolean value){
		storePropertiy(SHOW_GRID, value);
	}
	
	/**
	 * Set if the crosstab has a white grid
	 *  
	 * @param value true if the corsstab has a white grid, false otherwise
	 */
	public void setWhiteGrid(Boolean value){
		storePropertiy(WHITE_GRID, value);
	}
	
	/**
	 * Read a color properties and return it as an AWT color
	 * 
	 * @param name the name of the color properties
	 * @return the color read, in AWT.Color format
	 */
	public Color getColorValue(String name){
		AlfaRGB alphaColor =  super.getColor(name);
		RGB rgbColor =  alphaColor.getRgb();
		return new Color(rgbColor.red, rgbColor.green, rgbColor.blue, alphaColor.getAlfa());
	}
	
	/**
	 * Return a string unique representation for the crosstab style
	 */
	@Override
	public String toString() {
		String color1 = getColor(COLOR_TOTAL).toString();
		String color2 = getColor(COLOR_GROUP).toString();
		String color3 = getColor(COLOR_MEASURES).toString();
		String color4 = getColor(COLOR_DETAIL).toString();
		return color1.concat(color2).concat(color3).concat(color4).concat(getWhiteGrid().toString());
	}
	
	/**
	 * Return an XML representation of the crosstab style
	 * 
	 * @return a string containing the xml representation of the crosstab style
	 */
	@Override
	public String getXMLData() {
		String result = "<"+getTemplateName()+" type=\"" + getTemplateName() +"\" ";
		result += "whiteGrid=\""+getWhiteGrid().toString()+"\">";
		result += "<description>".concat(getDescription()).concat("</description>");
		result += xmlColor("colorTotal",getColor(COLOR_TOTAL));
		result += xmlColor("colorGroup",getColor(COLOR_GROUP));
		result += xmlColor("colorMeasures",getColor(COLOR_MEASURES));
		result += xmlColor("colorDetail",getColor(COLOR_DETAIL));
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
			boolean whiteGrid = rootAttributes.getNamedItem("whiteGrid").getNodeValue().equals("true"); 
			
			Node schemasNode = rootAttributes.getNamedItem("colorSchema");
			SCHEMAS variation =  schemasNode != null ? SCHEMAS.valueOf(schemasNode.getNodeValue()) : null;
			
			Node firstChild = xmlNode.getFirstChild();
			String description = null;
			
			AlfaRGB baseColor = null;
			AlfaRGB colorTotal = null;
			AlfaRGB colorGroup = null;
			AlfaRGB colorMeasures = null;
			AlfaRGB colorDetail = null;
			while(firstChild!=null){
				if (firstChild.getNodeName().equals("baseColor")){
					baseColor = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("description")) {
					Node descriptionNode = firstChild.getChildNodes().item(0);
					description = descriptionNode != null ? descriptionNode.getNodeValue() : "";				
				} else if (firstChild.getNodeName().equals("colorTotal")) {
					colorTotal = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("colorGroup")) {
					colorGroup = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("colorMeasures")) {
					colorMeasures = rgbColor(firstChild);
				} else if (firstChild.getNodeName().equals("colorDetail")) {
					colorDetail = rgbColor(firstChild);
				}
				firstChild = firstChild.getNextSibling();
			}
			CrosstabStyle result = null;
			if (variation != null && baseColor != null) result = new CrosstabStyle(baseColor, variation, whiteGrid);
			else result = new CrosstabStyle(colorTotal, colorGroup, colorMeasures, colorDetail, whiteGrid);
			result.setDescription(description);
			return result;
		} catch(Exception ex){
			System.out.println("Unable to rebuild the crosstab style");
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Return an unique identifier of the crosstab template type
	 * 
	 * @return a string representing the type of the crosstab template
	 */
	@Override
	public String getTemplateName() {
		return "crosstabStyle";
	}
}
