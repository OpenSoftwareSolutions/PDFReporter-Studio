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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.jaspersoft.studio.property.color.ColorSchemaGenerator;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * A template style are a series of attributes that can be applied to a wide range of objects (like tables, reports, crosstabs) 
 * to give a particular look to them. For example it can specify how a table is colored and with which colors and so on. 
 * This class could be used directly to represent a style, but it is suggested to subclass it for the specific element type on which the style is applied .
 * In this way it is possible to define specific method and key to access the data stored here. Look the class TableStyle for an example.
 * 
 * @author Orlandin Marco
 *
 */
public abstract class TemplateStyle implements Serializable {
	
	/**
	 * serial id for the serialization
	 */
	private static final long serialVersionUID = -7918944412275638526L;

	/**
	 * Key for the optional description of the style
	 */
	public final static String DESCRIPTION_KEY = "style_description";
	
	/**
	 * The base color of the style
	 */
	protected AlfaRGB baseColor;
	
	/**
	 * A key of a variation, for informations on the available variation look at the class 
	 * ColorSchemaGenerator
	 */
	protected ColorSchemaGenerator.SCHEMAS variation;
	
	/**
	 * An has map to store color, with the pair key of the color and the SWT RGB of the color
	 */
	private HashMap<String, AlfaRGB> storedColor;
	
	/**
	 * An hashmap to store generic properties, with the pair key of the property and value of the property
	 */
	private HashMap<String, Object> storedProperties;
	
	public TemplateStyle(AlfaRGB baseColor, ColorSchemaGenerator.SCHEMAS variation){
		this.baseColor = baseColor;
		this.variation = variation;
		storedProperties = new HashMap<String, Object>();
		storedColor = new HashMap<String, AlfaRGB>();
	}
	
	/**
	 * Return the base color
	 * 
	 * @return an rgb representing the base color
	 */
	public AlfaRGB getBaseColor(){
		return baseColor;
	}
	
	/**
	 * Return the color variation
	 * 
	 * @return the variation
	 */
	public ColorSchemaGenerator.SCHEMAS getVariation(){
		return variation;
	}
	
	/**
	 * Return a list of all the key of the properties stored
	 * 
	 * @return A list of string where every string is a key of a stored property
	 */
	public List<String> getStoredPropertiesName(){
		return new ArrayList<String>(storedProperties.keySet());
	}
	
	/**
	 * Return a list of all the key of the colors stored
	 * 
	 * @return A list of string where every string is a key of a stored color
	 */
	public List<String> getStoredColorsName(){
		return new ArrayList<String>(storedColor.keySet());
	}
	
	/**
	 * Store a specific color. If there is already stored a color with the same key defined for 
	 * this one, the old one will be overwritten
	 * 
	 * @param name the key of the color
	 * @param color the RGB of the color
	 */
	public void storeColor(String name, AlfaRGB color){
		storedColor.put(name, color);
	}
	
	/**
	 * Generate a gradations of the base color starting from the base color and it variation.
	 * This new color will be both stored and returned. If there is already stored a color with the same key defined for 
	 * this one, the old one will be overwritten
	 * 
	 * @param name key of the new color
	 * @param gradation gradation of the new color
	 * @return the SWT RGB of generated color
	 */
	public AlfaRGB generateAndStoreColor(String name, int gradation){
		AlfaRGB generatedColor = ColorSchemaGenerator.createColor(baseColor, gradation, variation);
		storedColor.put(name, generatedColor);
		return generatedColor;
	}
	
	/**
	 * Generate a gradations of the base color starting from the base color and it variation.
	 * This new color will be returned but not stored
	 * 
	 * @param gradation gradation of the new color
	 * @return the SWT RGB of generated color
	 */
	public AlfaRGB generateColor(int gradation){
		AlfaRGB generatedColor = ColorSchemaGenerator.createColor(baseColor, gradation, variation);
		return generatedColor;
	}
	
	/**
	 * Store a property. If there is already stored a property with the same key defined for 
	 * this one, the old one will be overwritten
	 * 
	 * @param name key of the property
	 * @param value the value of the property
	 */
	public void storePropertiy(String name, Object value){
		storedProperties.put(name, value);
	}
	
	/**
	 * Return the type of a stored property
	 * 
	 * @param name the key of the property
	 * @return The class type of the property, or null if the property is not found with 
	 * the given key
	 */
	public Class<?> getPropertyType(String name){
		if (storedProperties.containsKey(name)){
			return (storedProperties.get(name).getClass());
		}
		return null;
	}
	
	/**
	 * Check if a property is already stored
	 * 
	 * @param name the key of the property
	 * @return true if the property is already stored, false otherwise
	 */
	public boolean hasProperty(String name){
		return storedProperties.containsKey(name);
	}
	
	/**
	 * Check if a color is already stored
	 * 
	 * @param name the key of the color
	 * @return true if the color is already stored, false otherwise
	 */
	public boolean hasColor(String name){
		return storedColor.containsKey(name);
	}
	
	/**
	 * Return the value of a property 
	 * 
	 * @param name the key of the property
	 * @return the value of the property, can be null if the property does not exist or 
	 * if the property exist but it has null value.
	 */
	public Object getProperty(String name){
		return storedProperties.get(name);
	}
	
	/**
	 * Return the value of a color 
	 * 
	 * @param name the key of the color
	 * @return the SWT RGB value of the color, can be null if the color does not exist 
	 */
	public AlfaRGB getColor(String name){
		return storedColor.get(name);
	}
	
	/**
	 * Set the description of the style
	 * 
	 * @param description text of the description
	 */
	public void setDescription(String description){
		storedProperties.put(DESCRIPTION_KEY, description);
	}
	
	/**
	 * Return the description of the file
	 * 
	 * @return the textual description. since it isn't mandatory to provide a 
	 * description, if it wasn't set the and empty string is returned.
	 */
	public String getDescription(){
		Object value = storedProperties.get(DESCRIPTION_KEY);
		if (value != null) return value.toString();
		else return "";
	}
	
	/**
	 * Return a XML representation of an rgb color. This consist into 
	 * a tag with a custom name with three attribute, r, g, and b. Each of the
	 * contains a color component
	 * 
	 * @param tagName the name of the tag that will contain the three components of the color
	 * @param color the color to convert
	 * @return A string that contains the XML representation of the color as <tagName r="red_value" g="green_value" b="blue_value"/>
	 */
	public static String xmlColor(String tagName, AlfaRGB color){
		RGB rgbColor = color.getRgb();
		String colorString ="<".concat(tagName).concat(" r=\"");
		colorString += rgbColor.red;
		colorString = colorString.concat("\" g=\"");
		colorString += rgbColor.green;
		colorString = colorString.concat("\" b=\"");
		colorString += rgbColor.blue;
		colorString = colorString.concat("\" alpha=\"");
		colorString += color.getAlfa();
		colorString = colorString.concat("\"/>");
		return colorString;
	}
	
	/**
	 * From an xml representation of a rgb color build an object of type RGB and return it
	 * 
	 * @param xmlColorNode  an xml node representation of a rgb color. This node must have three attribute named r, g and b
	 * @return an rgb color
	 */
	public static AlfaRGB rgbColor(Node xmlColorNode){
		NamedNodeMap colorAttributes = xmlColorNode.getAttributes();
		int r = Integer.parseInt(colorAttributes.getNamedItem("r").getNodeValue());
		int g = Integer.parseInt(colorAttributes.getNamedItem("g").getNodeValue());
		int b = Integer.parseInt(colorAttributes.getNamedItem("b").getNodeValue());
		int alpha = Integer.parseInt(colorAttributes.getNamedItem("alpha").getNodeValue());
		return new AlfaRGB(new RGB(r,g,b), alpha);
	}
	
	/**
	 * Return an XML representation of the template style
	 * 
	 * @return a string containing the xml representation of the style
	 */
	public abstract String getXMLData();
	
	/**
	 * Return an unique identifier of the template type
	 * 
	 * @return a string representing the type of the template
	 */
	public abstract String getTemplateName();
	
	/**
	 * Rebuild a TemplateStyle from its XML representation
	 * 
	 * @param xmlNode an XML node with the representation of a template style
	 * @return the TemplateStyle builded from the xmlNode
	 */
	public abstract TemplateStyle buildFromXML(Node xmlNode);
}
