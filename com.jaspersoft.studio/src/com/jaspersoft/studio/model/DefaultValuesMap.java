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
package com.jaspersoft.studio.model;

import java.awt.Color;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.base.JRBasePrintElement;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.fill.JRFillElement;
import net.sf.jasperreports.engine.fill.JRTemplateElement;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRProperties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.FloatPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.LineStylePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;

/**
 * This class store statically the default values of many element
 * @author Marco Orlandin
 *
 */
public class DefaultValuesMap {
	
	/**
	 * Hashmap that cache the properties for every requested type. Using this every request after the 
	 * first for the same type will not rebuild the default values map.
	 */
	private static HashMap<Class, HashMap<String,Object>> valuesMap = null;
	
	private static final Integer INTEGER_ZERO = Integer.valueOf(0);
	
	/**
	 * Initialize the static fields
	 */
	private static void initValuesMap(){
		if (valuesMap == null) valuesMap = new HashMap<Class, HashMap<String,Object>>();
	}
	
	private static MLinePen createBaseLinePen(final Object item){
		return 	new MLinePen(null){
			private static final long serialVersionUID = 1L;
			@Override
			public HashMap<String,Object> getStylesDescriptors() {
				HashMap<String, Object> result = new HashMap<String, Object>();
				if (item instanceof MGraphicElementLinePen){
					result.put(JRBasePen.PROPERTY_LINE_COLOR, Color.black);
					result.put(JRBasePen.PROPERTY_LINE_STYLE,LineStyleEnum.SOLID);
					result.put(JRBasePen.PROPERTY_LINE_WIDTH,JRPen.LINE_WIDTH_1);
				} else if (item instanceof MLineBox){
					result.put(JRBasePen.PROPERTY_LINE_COLOR, Color.black);
					result.put(JRBasePen.PROPERTY_LINE_STYLE,LineStyleEnum.SOLID);
					result.put(JRBasePen.PROPERTY_LINE_WIDTH,JRPen.LINE_WIDTH_0);
				} else {
					result.put(JRBasePen.PROPERTY_LINE_COLOR, null);
					result.put(JRBasePen.PROPERTY_LINE_STYLE,null);
					result.put(JRBasePen.PROPERTY_LINE_WIDTH,null);
				}
				return result;
			}
			@Override
			public IPropertyDescriptor getPropertyDescriptor(Object id) {
				if (id.equals(JRBasePen.PROPERTY_LINE_COLOR)){
					ColorPropertyDescriptor penLineColorD = new ColorPropertyDescriptor(JRBasePen.PROPERTY_LINE_COLOR,
							Messages.common_line_color, NullEnum.INHERITED);
					penLineColorD.setDescription(Messages.MLinePen_line_color_description);
					return penLineColorD;
				} else if (id.equals(JRBasePen.PROPERTY_LINE_STYLE)){
					LineStylePropertyDescriptor penLineStyleD = new LineStylePropertyDescriptor(JRBasePen.PROPERTY_LINE_STYLE, Messages.common_line_style,
							LineStyleEnum.class, NullEnum.INHERITED);
					penLineStyleD.setDescription(Messages.MLinePen_line_style_description);
					return penLineStyleD;
				} else  if (id.equals(JRBasePen.PROPERTY_LINE_WIDTH)){
					FloatPropertyDescriptor penLineWidthD = new FloatPropertyDescriptor(JRBasePen.PROPERTY_LINE_WIDTH,
							Messages.MLinePen_line_width);
					penLineWidthD.setDescription(Messages.MLinePen_line_width_description);
					return penLineWidthD;
				} else return null; 
			}

		};
	}
	
	private static MLineBox createBaseLineBox(final Object item){
		return 	new MLineBox(null){
			private static final long serialVersionUID = 1L;
			@Override
			public HashMap<String,Object> getStylesDescriptors() {
				HashMap<String, Object> result = new HashMap<String, Object>();
				if (item instanceof MGraphicElementLineBox){
					result.put(JRBaseLineBox.PROPERTY_PADDING,  0);
					result.put(JRBaseLineBox.PROPERTY_TOP_PADDING, 0);
					result.put(JRBaseLineBox.PROPERTY_BOTTOM_PADDING, 0);
					result.put(JRBaseLineBox.PROPERTY_LEFT_PADDING, 0);
					result.put(JRBaseLineBox.PROPERTY_RIGHT_PADDING,0);
					MLinePen linePen = createBaseLinePen(this);
					result.put(LINE_PEN, linePen);
					result.put(LINE_PEN_TOP, linePen);
					result.put(LINE_PEN_BOTTOM, linePen);
					result.put(LINE_PEN_LEFT, linePen);
					result.put(LINE_PEN_RIGHT, linePen);
				} else {
					result.put(JRBaseLineBox.PROPERTY_PADDING,  null);
					result.put(JRBaseLineBox.PROPERTY_TOP_PADDING, null);
					result.put(JRBaseLineBox.PROPERTY_BOTTOM_PADDING, null);
					result.put(JRBaseLineBox.PROPERTY_LEFT_PADDING, null);
					result.put(JRBaseLineBox.PROPERTY_RIGHT_PADDING,null);
					MLinePen linePen = createBaseLinePen(null);
					result.put(LINE_PEN, linePen);
					result.put(LINE_PEN_TOP, linePen);
					result.put(LINE_PEN_BOTTOM, linePen);
					result.put(LINE_PEN_LEFT, linePen);
					result.put(LINE_PEN_RIGHT, linePen);
				}
				return result;
			}
			@Override
			public IPropertyDescriptor getPropertyDescriptor(Object id) {
				if (id.equals(JRBaseLineBox.PROPERTY_PADDING)){
					PixelPropertyDescriptor paddingD = new PixelPropertyDescriptor(JRBaseLineBox.PROPERTY_PADDING,
							Messages.common_padding);
					paddingD.setCategory(Messages.common_padding);
					paddingD.setDescription(Messages.MLineBox_padding_description);
					return paddingD;
				} else if (id.equals(JRBaseLineBox.PROPERTY_LEFT_PADDING)){
					PixelPropertyDescriptor paddingLeftD = new PixelPropertyDescriptor(JRBaseLineBox.PROPERTY_LEFT_PADDING,
							Messages.MLineBox_left_padding);
					paddingLeftD.setCategory(Messages.common_padding);
					paddingLeftD.setDescription(Messages.MLineBox_left_padding_description);
					return paddingLeftD;
				} else if (id.equals(JRBaseLineBox.PROPERTY_RIGHT_PADDING)){
					PixelPropertyDescriptor paddingRightD = new PixelPropertyDescriptor(JRBaseLineBox.PROPERTY_RIGHT_PADDING,
							Messages.MLineBox_right_padding);
					paddingRightD.setCategory(Messages.common_padding);
					paddingRightD.setDescription(Messages.MLineBox_right_padding_description);
					return paddingRightD;
				} else if (id.equals(JRBaseLineBox.PROPERTY_TOP_PADDING)){
					PixelPropertyDescriptor paddingTopD = new PixelPropertyDescriptor(JRBaseLineBox.PROPERTY_TOP_PADDING,
							Messages.MLineBox_top_padding);
					paddingTopD.setCategory(Messages.common_padding);
					paddingTopD.setDescription(Messages.MLineBox_top_padding_description);
					return paddingTopD;
				} else if (id.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING)){
					PixelPropertyDescriptor paddingBottomD = new PixelPropertyDescriptor(JRBaseLineBox.PROPERTY_BOTTOM_PADDING,
							Messages.MLineBox_bottom_padding);
					paddingBottomD.setDescription(Messages.MLineBox_bottom_padding_description);
					return paddingBottomD;
				} else return null;
			}
		};
	}
	
	/**
	 * Create the default property map for a wide range of object
	 * @param type the type of the element for which the properties are requested
	 * @return the default properties of the requested element
	 */
	@SuppressWarnings("deprecation")
	private static HashMap<String, Object> initializeType(Object type){
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(JRDesignStyle.PROPERTY_FONT_NAME, JRProperties.getProperty(JRFont.DEFAULT_FONT_NAME));
		result.put(JRBaseLineBox.PROPERTY_PADDING, INTEGER_ZERO);
		result.put(JRBaseLineBox.PROPERTY_TOP_PADDING, INTEGER_ZERO);
		result.put(JRBaseLineBox.PROPERTY_RIGHT_PADDING, INTEGER_ZERO);
		result.put(JRDesignStyle.PROPERTY_PDF_FONT_NAME, JRProperties.getProperty(JRFont.DEFAULT_PDF_FONT_NAME));
		result.put(JRDesignStyle.PROPERTY_PDF_ENCODING,JRProperties.getProperty(JRFont.DEFAULT_PDF_ENCODING));
		result.put(JRDesignStyle.PROPERTY_PATTERN, null);
		result.put(JRDesignStyle.PROPERTY_MARKUP,JRCommonText.MARKUP_NONE);
		result.put(JRBasePen.PROPERTY_LINE_STYLE, LineStyleEnum.SOLID);
		
		if (type instanceof JRStyle){

			result.put(JRDesignStyle.PROPERTY_FONT_SIZE, null);
			result.put(JRDesignStyle.PROPERTY_BOLD, null);
			result.put(JRDesignStyle.PROPERTY_ITALIC, null);
			result.put(JRDesignStyle.PROPERTY_UNDERLINE, null);
			result.put(JRDesignStyle.PROPERTY_STRIKE_THROUGH, null);
			result.put(JRDesignStyle.PROPERTY_PDF_EMBEDDED,null);
			result.put(JRDesignStyle.PROPERTY_BLANK_WHEN_NULL,null);
			result.put(JRDesignStyle.PROPERTY_VERTICAL_ALIGNMENT,null);
			result.put(JRDesignStyle.PROPERTY_HORIZONTAL_ALIGNMENT,null);
			result.put(JRDesignStyle.PROPERTY_SCALE_IMAGE,null);
			result.put(JRDesignStyle.PROPERTY_ROTATION,null);
			result.put(JRDesignStyle.PROPERTY_RADIUS,null);
			result.put(JRBasePen.PROPERTY_LINE_WIDTH, null);
			result.put(JRBasePen.PROPERTY_LINE_COLOR, null);
			result.put(JRDesignStyle.PROPERTY_BACKCOLOR, null);
			result.put(JRDesignStyle.PROPERTY_FORECOLOR, null);
			result.put(JRDesignStyle.PROPERTY_FILL, null);
			result.put(JRDesignStyle.PROPERTY_MODE, null);
		} else {
			result.put(JRDesignStyle.PROPERTY_FONT_SIZE, JRProperties.getIntegerProperty(JRFont.DEFAULT_FONT_SIZE));
			result.put(JRDesignStyle.PROPERTY_BOLD, false);
			result.put(JRDesignStyle.PROPERTY_ITALIC, false);
			result.put(JRDesignStyle.PROPERTY_UNDERLINE, false);
			result.put(JRDesignStyle.PROPERTY_STRIKE_THROUGH, false);
			result.put(JRDesignStyle.PROPERTY_PDF_EMBEDDED,JRProperties.getBooleanProperty(JRFont.DEFAULT_PDF_EMBEDDED));
			result.put(JRDesignStyle.PROPERTY_BLANK_WHEN_NULL,false);
			result.put(JRDesignStyle.PROPERTY_VERTICAL_ALIGNMENT,VerticalAlignEnum.TOP);
			result.put(JRDesignStyle.PROPERTY_HORIZONTAL_ALIGNMENT,HorizontalAlignEnum.LEFT);
			result.put(JRDesignStyle.PROPERTY_SCALE_IMAGE,ScaleImageEnum.RETAIN_SHAPE);
			result.put(JRDesignStyle.PROPERTY_ROTATION,RotationEnum.NONE);
			result.put(JRDesignStyle.PROPERTY_RADIUS,INTEGER_ZERO);
			result.put(JRBasePen.PROPERTY_LINE_WIDTH,JRPen.LINE_WIDTH_0);
			result.put(JRBasePen.PROPERTY_LINE_COLOR, Color.black);
			result.put(MGraphicElementLinePen.LINE_PEN, createBaseLinePen(type));
			result.put(MGraphicElementLineBox.LINE_BOX, createBaseLineBox(type));
			result.put(JRDesignStyle.PROPERTY_BACKCOLOR, Color.white);
			result.put(JRDesignStyle.PROPERTY_FORECOLOR, Color.black);
			result.put(JRDesignStyle.PROPERTY_FILL, FillEnum.SOLID);
			ModeEnum opaqueValue = ModeEnum.getByValue(ModeEnum.OPAQUE.getValue());
			ModeEnum transparentValue = ModeEnum.getByValue(ModeEnum.TRANSPARENT.getValue());
			if (type instanceof JRDesignTextElement){
				result.put(JRDesignStyle.PROPERTY_MODE, transparentValue);
			} else if (type instanceof JRTemplateElement){
				result.put(JRDesignStyle.PROPERTY_MODE, opaqueValue);
			} else if (type instanceof JRBaseElement) {
				result.put(JRDesignStyle.PROPERTY_MODE, opaqueValue);
			} else if (type instanceof JRBasePrintElement) {
				result.put(JRDesignStyle.PROPERTY_MODE, opaqueValue);
			} else if (type instanceof JRFillElement){
				result.put(JRDesignStyle.PROPERTY_MODE, opaqueValue);
			} else {
				result.put(JRDesignStyle.PROPERTY_MODE, transparentValue);
			}
		}
		return result;
	}
	
	/**
	 * Return all the property for an element with a requested type
	 * @param type the type of the element
	 * @return An hashmap of the default property, where the key is the property name and the 
	 * value is an object representing the property value
	 */
	public static HashMap<String, Object> getPropertiesByType(Object type){
		 initValuesMap();
		 Object jrValue = type;
		 //The element is choose on the jrtype, if a model is passed then the jrtype will be extracted
		 if (type instanceof APropertyNode)
			 jrValue = ((APropertyNode)type).getValue();
		 //Check the requested properties are cached
		 HashMap<String, Object> result = valuesMap.get(jrValue.getClass());
		 if (result == null){
			 //The are not, so i need to create them
			 result = initializeType(jrValue);
			 valuesMap.put(jrValue.getClass(), result);
		 }
		 return result;
	}
	
	/**
	 * Return the default property value for an element with a specified type
	 * @param key the key of the property
	 * @param type the type of the element that contains the property
	 * @return an object representing the property
	 */
	public static Object getValue(String key, Object type){
		return getPropertiesByType(type.getClass()).get(key);
	}
}
