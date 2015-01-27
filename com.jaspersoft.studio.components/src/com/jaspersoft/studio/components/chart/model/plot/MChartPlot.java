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
package com.jaspersoft.studio.components.chart.model.plot;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.type.PlotOrientationEnum;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.property.descriptor.seriescolor.SeriesColorPropertyDescriptor;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.FloatPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.TransparencyPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

public class MChartPlot extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MChartPlot(JRChartPlot value) {
		super();
		setValue(value);
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		ColorPropertyDescriptor backcolorD = new ColorPropertyDescriptor(JRBaseChartPlot.PROPERTY_BACKCOLOR, Messages.MChartPlot_backcolor, NullEnum.INHERITED);
		backcolorD.setDescription(Messages.MChartPlot_backcolor_description);
		desc.add(backcolorD);

		FloatPropertyDescriptor backAlphaD = new TransparencyPropertyDescriptor(JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA, Messages.MChartPlot_background_alpha_percent);
		backAlphaD.setDescription(Messages.MChartPlot_background_alpha_percent_description);
		desc.add(backAlphaD);

		FloatPropertyDescriptor foreAlphaD = new TransparencyPropertyDescriptor(JRBaseChartPlot.PROPERTY_FOREGROUND_ALPHA, Messages.MChartPlot_foreground_alpha_percent);
		foreAlphaD.setDescription(Messages.MChartPlot_foreground_alpha_percent_description);
		desc.add(foreAlphaD);

		orientationD = new JSSEnumPropertyDescriptor(JRBaseChartPlot.PROPERTY_ORIENTATION, Messages.MChartPlot_orientation, com.jaspersoft.studio.components.chart.model.enums.PlotOrientationEnum.class,
				NullEnum.NULL);
		orientationD.setDescription(Messages.MChartPlot_orientation_description);
		desc.add(orientationD);

		SeriesColorPropertyDescriptor scpd = new SeriesColorPropertyDescriptor(JRBaseChartPlot.PROPERTY_SERIES_COLORS, Messages.MChartPlot_series_colors);
		scpd.setDescription(Messages.MChartPlot_series_colors_description);
		desc.add(scpd);
		scpd.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#seriesColor"));

		defaultsMap.put(JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA, null);
		defaultsMap.put(JRBaseChartPlot.PROPERTY_FOREGROUND_ALPHA, null);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#plot");
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static JSSEnumPropertyDescriptor orientationD;

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
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRBaseChartPlot jrElement = (JRBaseChartPlot) getValue();
		if (id.equals(JRBaseChartPlot.PROPERTY_BACKCOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnBackcolor());
		if (id.equals(JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA))
			return jrElement.getBackgroundAlphaFloat();
		if (id.equals(JRBaseChartPlot.PROPERTY_FOREGROUND_ALPHA))
			return jrElement.getForegroundAlphaFloat();
		if (id.equals(JRBaseChartPlot.PROPERTY_ORIENTATION)) {
			if (jrElement.getOrientationValue() == null)
				return 0;
			if (jrElement.getOrientationValue() == PlotOrientationEnum.HORIZONTAL)
				return 1;
			else
				return 2;
		}
		if (id.equals(JRBaseChartPlot.PROPERTY_SERIES_COLORS))
			return jrElement.getSeriesColors();

		return null;
	}

	public Object getPropertyActualValue(Object id) {
		JRBaseChartPlot jrElement = (JRBaseChartPlot) getValue();
		if (id.equals(JRBaseChartPlot.PROPERTY_BACKCOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getBackcolor());
		if (id.equals(JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA)) {
			Float alpha = jrElement.getBackgroundAlphaFloat();
			return alpha != null ? alpha : 1.0f;
		}
		if (id.equals(JRBaseChartPlot.PROPERTY_FOREGROUND_ALPHA)) {
			Float alpha = jrElement.getForegroundAlphaFloat();
			return alpha != null ? alpha : 1.0f;
		}
		return super.getPropertyActualValue(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRBaseChartPlot jrElement = (JRBaseChartPlot) getValue();
		if (id.equals(JRBaseChartPlot.PROPERTY_BACKCOLOR)) {
			if (value instanceof AlfaRGB)
				jrElement.setBackcolor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA)) {
			jrElement.setBackgroundAlpha((Float) value);
		} else if (id.equals(JRBaseChartPlot.PROPERTY_FOREGROUND_ALPHA)) {
			jrElement.setForegroundAlpha((Float) value);
		} else if (id.equals(JRBaseChartPlot.PROPERTY_ORIENTATION)) {
			switch ((Integer) value) {
			case 0:
				jrElement.setOrientation((PlotOrientationEnum) null);
				break;
			case 1:
				jrElement.setOrientation(PlotOrientationEnum.HORIZONTAL);
				break;
			case 2:
				jrElement.setOrientation(PlotOrientationEnum.VERTICAL);
				break;
			}
			// jrElement.setOrientation((PlotOrientationEnum) orientationD
			// .getEnumValue(value));
		} else if (id.equals(JRBaseChartPlot.PROPERTY_SERIES_COLORS)) {
			jrElement.setSeriesColors((Collection<JRSeriesColor>) value);
			// jrElement.clearSeriesColors();
			// if (value instanceof SortedSet) {
			// SortedSet<JRSeriesColor> set = (SortedSet<JRSeriesColor>) value;
			// for (JRSeriesColor sc : set) {
			// jrElement.addSeriesColor(sc);
			// }
			// }
		}
	}

	public ImageDescriptor getImagePath() {
		return null;
	}

	public String getDisplayText() {
		return null;
	}

}
