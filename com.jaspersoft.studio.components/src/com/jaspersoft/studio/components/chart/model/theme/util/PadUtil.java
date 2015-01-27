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
package com.jaspersoft.studio.components.chart.model.theme.util;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.chartthemes.simple.ChartSettings;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jfree.ui.RectangleInsets;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptors.DoublePropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;

public class PadUtil {

	public static final RectangleInsets RECTANGLE_INSETS = new RectangleInsets(0, 0, 0, 0);
	public static final String PADDING_RIGHT = ChartSettings.PROPERTY_padding + "RIGHT";//$NON-NLS-1$
	public static final String PADDING_LEFT = ChartSettings.PROPERTY_padding + "LEFT";//$NON-NLS-1$
	public static final String PADDING_BOTTOM = ChartSettings.PROPERTY_padding + "BOTTOM";//$NON-NLS-1$
	public static final String PADDING_TOP = ChartSettings.PROPERTY_padding + "TOP";//$NON-NLS-1$

	public static void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		createPropertyDescriptors(desc, defaultsMap, Messages.common_padding);
	}

	public static void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap, String prefix) {
		createPropertyDescriptors(desc, defaultsMap, "", prefix);//$NON-NLS-1$
	}

	public static void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap, String preID, String prefix) {
		PropertyDescriptor pd = new DoublePropertyDescriptor(preID + PadUtil.PADDING_TOP, Messages.common_top);
		pd.setDescription(Messages.common_top);
		pd.setCategory(prefix);
		desc.add(pd);

		pd = new DoublePropertyDescriptor(preID + PadUtil.PADDING_BOTTOM, Messages.common_bottom);
		pd.setDescription(Messages.common_bottom);
		pd.setCategory(prefix);
		desc.add(pd);

		pd = new DoublePropertyDescriptor(preID + PadUtil.PADDING_LEFT, Messages.common_left);
		pd.setDescription(Messages.common_left);
		pd.setCategory(prefix);
		desc.add(pd);

		pd = new DoublePropertyDescriptor(preID + PadUtil.PADDING_RIGHT, Messages.common_right);
		pd.setDescription(Messages.common_right);
		pd.setCategory(prefix);
		desc.add(pd);

		defaultsMap.put(preID + PadUtil.PADDING_TOP, 0.0d);
		defaultsMap.put(preID + PadUtil.PADDING_BOTTOM, 0.0d);
		defaultsMap.put(preID + PadUtil.PADDING_LEFT, 0.0d);
		defaultsMap.put(preID + PadUtil.PADDING_RIGHT, 0.0d);
	}

	public static Object getPropertyValue(Object id, RectangleInsets ri) {
		return getPropertyValue(id, ri, "");
	}

	public static Object getPropertyValue(Object id, RectangleInsets ri, String preID) {
		if (ri == null)
			ri = PadUtil.RECTANGLE_INSETS;
		if (id.equals(preID + PadUtil.PADDING_TOP))
			return ri.getTop();
		if (id.equals(preID + PadUtil.PADDING_BOTTOM))
			return ri.getBottom();
		if (id.equals(preID + PadUtil.PADDING_LEFT))
			return ri.getLeft();
		if (id.equals(preID + PadUtil.PADDING_RIGHT))
			return ri.getRight();
		return null;
	}

	public static RectangleInsets setPropertyValue(Object id, Object value, RectangleInsets ri) {
		return setPropertyValue(id, value, ri, "");//$NON-NLS-1$
	}

	public static RectangleInsets setPropertyValue(Object id, Object value, RectangleInsets ri, String preID) {
		if (ri == null)
			ri = PadUtil.RECTANGLE_INSETS;
		if (id.equals(preID + PadUtil.PADDING_TOP))
			return new RectangleInsets((Double) value, ri.getLeft(), ri.getBottom(), ri.getRight());
		else if (id.equals(preID + PadUtil.PADDING_BOTTOM))
			return new RectangleInsets(ri.getTop(), ri.getLeft(), (Double) value, ri.getRight());
		else if (id.equals(preID + PadUtil.PADDING_LEFT))
			return new RectangleInsets(ri.getTop(), (Double) value, ri.getBottom(), ri.getRight());
		else if (id.equals(preID + PadUtil.PADDING_RIGHT))
			return new RectangleInsets(ri.getTop(), ri.getLeft(), ri.getBottom(), (Double) value);
		return null;
	}

	public static Composite createWidgets4Property(Composite parent, String preID, String prefix, AbstractSection section) {
		Composite group = section.getWidgetFactory().createSection(parent, prefix, true, 4);
		((Section) group.getParent()).setExpanded(false);

		section.createWidget4Property(group, preID + PadUtil.PADDING_TOP);
		section.createWidget4Property(group, preID + PadUtil.PADDING_BOTTOM);
		section.createWidget4Property(group, preID + PadUtil.PADDING_LEFT);
		section.createWidget4Property(group, preID + PadUtil.PADDING_RIGHT);
		
		return group;
	}
}
