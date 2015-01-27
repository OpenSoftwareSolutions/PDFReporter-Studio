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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.type.LineStyleEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.messages.MessagesByKeys;
import com.jaspersoft.studio.property.combomenu.ComboItem;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.FloatPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSPopupPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

public class MLinePen extends APropertyNode implements IPropertySource {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private static List<ComboItem> lineSpacingItems = null;
	
	public MLinePen(JRPen linePen) {
		super();
		setValue(linePen);
	}
	
	@Override
	public HashMap<String,Object> getStylesDescriptors() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (getValue() == null)
			return result;
		JRBasePen element = (JRBasePen) getValue();
		result.put(JRBasePen.PROPERTY_LINE_COLOR, element.getOwnLineColor());
		result.put(JRBasePen.PROPERTY_LINE_STYLE, element.getOwnLineStyleValue());
		result.put(JRBasePen.PROPERTY_LINE_WIDTH, element.getOwnLineWidth());
		return result;
	}
	
	private List<ComboItem> createLineSpacingItems(){
		if (lineSpacingItems == null){
			lineSpacingItems = new ArrayList<ComboItem>();
			LineStyleEnum[] values = LineStyleEnum.class.getEnumConstants();
			lineSpacingItems.add(new ComboItem(MessagesByKeys.getString("LineSpacing_nullEnum"), true,  ResourceManager.getImage(this.getClass(),"/icons/resources/inherited.png"), 0, NullEnum.INHERITED, 0));
			Image[] images = new Image[] { ResourceManager.getImage(this.getClass(), "/icons/resources/line-solid.png"),
																					ResourceManager.getImage(this.getClass(), "/icons/resources/line-dashed.png"),
																					ResourceManager.getImage(this.getClass(), "/icons/resources/line-dotted.png"),
																					ResourceManager.getImage(this.getClass(), "/icons/resources/line-double.png"), };
			for(int i=0; i<values.length; i++){
				LineStyleEnum value = values[i];
				lineSpacingItems.add(new ComboItem(MessagesByKeys.getString("LineStyle_".concat(value.getName())), true, images[i], i+1, value , i+1));
			}
		}
		return lineSpacingItems;
	}
	

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		// pen
		ColorPropertyDescriptor penLineColorD = new ColorPropertyDescriptor(JRBasePen.PROPERTY_LINE_COLOR,
				Messages.common_line_color, NullEnum.INHERITED);
		penLineColorD.setDescription(Messages.MLinePen_line_color_description);
		penLineColorD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#pen_lineColor"));
		desc.add(penLineColorD);

		FloatPropertyDescriptor penLineWidthD = new FloatPropertyDescriptor(JRBasePen.PROPERTY_LINE_WIDTH,
				Messages.MLinePen_line_width);
		penLineWidthD.setDescription(Messages.MLinePen_line_width_description);
		penLineWidthD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#pen_lineWidth"));
		desc.add(penLineWidthD);

		penLineStyleD = new JSSPopupPropertyDescriptor(JRBasePen.PROPERTY_LINE_STYLE, Messages.common_line_style,
				LineStyleEnum.class, NullEnum.INHERITED, createLineSpacingItems());
		penLineStyleD.setDescription(Messages.MLinePen_line_style_description);
		penLineStyleD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#pen_lineStyle"));
		desc.add(penLineStyleD);

		defaultsMap.put(JRBasePen.PROPERTY_LINE_STYLE, null);
		defaultsMap.put(JRBasePen.PROPERTY_LINE_COLOR, null);
		defaultsMap.put(JRBasePen.PROPERTY_LINE_WIDTH, null);
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static JSSPopupPropertyDescriptor penLineStyleD;

	@Override
	public Map<String, Object> getDefaultsMap() {
		return defaultsMap;
	}

	@Override
	public IPropertyDescriptor[] getDescriptors() {
		return descriptors;
	}

	@Override
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		// pen
		JRPen linePen = (JRPen) getValue();
		if (linePen != null) {
			if (id.equals(JRBasePen.PROPERTY_LINE_COLOR))
				return Colors.getSWTRGB4AWTGBColor(linePen.getOwnLineColor());
			if (id.equals(JRBasePen.PROPERTY_LINE_WIDTH))
				return linePen.getOwnLineWidth();
			if (id.equals(JRBasePen.PROPERTY_LINE_STYLE))
				return penLineStyleD.getEnumValue(linePen.getOwnLineStyleValue());
		}
		return null;
	}

	public Object getPropertyActualValue(Object id) {
		// pen
		JRPen linePen = (JRPen) getValue();
		if (linePen != null) {
			if (id.equals(JRBasePen.PROPERTY_LINE_COLOR))
				return Colors.getSWTRGB4AWTGBColor(linePen.getLineColor());
			if (id.equals(JRBasePen.PROPERTY_LINE_WIDTH))
				return linePen.getLineWidth();
			if (id.equals(JRBasePen.PROPERTY_LINE_STYLE))
				return penLineStyleD.getEnumValue(linePen.getLineStyleValue());
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRPen linePen = (JRPen) getValue();
		if (linePen != null) {
			if (id.equals(JRBasePen.PROPERTY_LINE_WIDTH))
				linePen.setLineWidth(((Float) value));
			else if (id.equals(JRBasePen.PROPERTY_LINE_COLOR)) {
				if (value == null)
					linePen.setLineColor(null);
				else if (value instanceof AlfaRGB)
					linePen.setLineColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
			} else if (id.equals(JRBasePen.PROPERTY_LINE_STYLE))
				linePen.setLineStyle((LineStyleEnum) penLineStyleD.getEnumValue(value));
		}
	}

	public String getDisplayText() {
		return null;
	}

	public ImageDescriptor getImagePath() {
		return null;
	}

}
