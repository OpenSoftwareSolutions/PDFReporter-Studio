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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.design.JRDesignDataRange;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;
import net.sf.jasperreports.charts.type.MeterShapeEnum;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.property.descriptor.MeterIntervalPropertyDescriptor;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.model.text.MFontUtil;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.FontPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.DoublePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

public class MMeterPlot extends MChartPlot {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MMeterPlot(JRMeterPlot value) {
		super(value);
	}

	@Override
	public String getDisplayText() {
		return Messages.MMeterPlot_meter_plot;
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;

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

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		ColorPropertyDescriptor meterBackgroundColorD = new ColorPropertyDescriptor(JRDesignMeterPlot.PROPERTY_METER_BACKGROUND_COLOR, Messages.MMeterPlot_meter_background_color, NullEnum.NULL);
		meterBackgroundColorD.setDescription(Messages.MMeterPlot_meter_background_color_description);
		desc.add(meterBackgroundColorD);

		ColorPropertyDescriptor tickColorD = new ColorPropertyDescriptor(JRDesignMeterPlot.PROPERTY_TICK_COLOR, Messages.MMeterPlot_tick_color, NullEnum.NULL);
		tickColorD.setDescription(Messages.MMeterPlot_tick_color_description);
		desc.add(tickColorD);

		ColorPropertyDescriptor needleColorD = new ColorPropertyDescriptor(JRDesignMeterPlot.PROPERTY_NEEDLE_COLOR, Messages.MMeterPlot_needle_color, NullEnum.NULL);
		needleColorD.setDescription(Messages.MMeterPlot_needle_color_description);
		desc.add(needleColorD);

		ColorPropertyDescriptor valueColorD = new ColorPropertyDescriptor(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
				+ JRDesignValueDisplay.PROPERTY_COLOR, Messages.common_value_color, NullEnum.NULL);
		valueColorD.setDescription(Messages.MMeterPlot_value_color_description);
		desc.add(valueColorD);
		valueColorD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#valueDisplay_color"));

		FontPropertyDescriptor tickLabelFontD = new FontPropertyDescriptor(JRDesignMeterPlot.PROPERTY_TICK_LABEL_FONT, Messages.MMeterPlot_tick_label_font);
		tickLabelFontD.setDescription(Messages.MMeterPlot_tick_label_font_description);
		desc.add(tickLabelFontD);

		FontPropertyDescriptor valueFontD = new FontPropertyDescriptor(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
				+ JRDesignValueDisplay.PROPERTY_FONT, Messages.common_value_font);
		valueFontD.setDescription(Messages.MMeterPlot_value_font_description);
		desc.add(valueFontD);
		valueFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#font"));

		IntegerPropertyDescriptor meterAngleD = new IntegerPropertyDescriptor(JRDesignMeterPlot.PROPERTY_METER_ANGLE, Messages.MMeterPlot_meter_angle);
		meterAngleD.setDescription(Messages.MMeterPlot_meter_angle_description);
		desc.add(meterAngleD);

		DoublePropertyDescriptor tickIntervalD = new DoublePropertyDescriptor(JRDesignMeterPlot.PROPERTY_TICK_INTERVAL, Messages.MMeterPlot_tick_interval);
		tickIntervalD.setDescription(Messages.MMeterPlot_tick_interval_description);
		desc.add(tickIntervalD);

		shapeD = new JSSEnumPropertyDescriptor(JRDesignMeterPlot.PROPERTY_SHAPE, Messages.MMeterPlot_shape, MeterShapeEnum.class, NullEnum.NOTNULL);
		shapeD.setDescription(Messages.MMeterPlot_shape_description);
		desc.add(shapeD);

		JRExpressionPropertyDescriptor dataRangeHighExprD = new JRExpressionPropertyDescriptor(JRDesignMeterPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION, //$NON-NLS-1$
				Messages.common_data_range_high_expression);
		dataRangeHighExprD.setDescription(Messages.MMeterPlot_data_range_high_expression_description);
		desc.add(dataRangeHighExprD);
		dataRangeHighExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#highExpression"));

		JRExpressionPropertyDescriptor dataRangeLowExprD = new JRExpressionPropertyDescriptor(JRDesignMeterPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION, //$NON-NLS-1$
				Messages.common_data_range_low_expression);
		dataRangeLowExprD.setDescription(Messages.MMeterPlot_data_range_low_expression_description);
		desc.add(dataRangeLowExprD);
		dataRangeLowExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#lowExpression"));

		NTextPropertyDescriptor unitsD = new NTextPropertyDescriptor(JRDesignMeterPlot.PROPERTY_UNITS, Messages.MMeterPlot_units);
		unitsD.setDescription(Messages.MMeterPlot_units_description);
		desc.add(unitsD);

		NTextPropertyDescriptor maskD = new NTextPropertyDescriptor(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
				+ JRDesignValueDisplay.PROPERTY_MASK, Messages.common_value_mask);
		maskD.setDescription(Messages.MMeterPlot_value_mask_description);
		desc.add(maskD);
		maskD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#valueDisplay_mask"));

		MeterIntervalPropertyDescriptor mipd = new MeterIntervalPropertyDescriptor(JRDesignMeterPlot.PROPERTY_INTERVALS, "Intervals");
		mipd.setDescription("Meter Intervals");
		desc.add(mipd);
		mipd.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#meterInterval"));

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#meterPlot");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		JRDesignMeterPlot jrElement = (JRDesignMeterPlot) getValue();
		JRDesignDataRange jrDataRange = (JRDesignDataRange) jrElement.getDataRange();
		if (id.equals(JRDesignMeterPlot.PROPERTY_METER_BACKGROUND_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getMeterBackgroundColor());
		if (id.equals(JRDesignMeterPlot.PROPERTY_TICK_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getTickColor());
		if (id.equals(JRDesignMeterPlot.PROPERTY_NEEDLE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getNeedleColor());
		if (id.equals(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_COLOR)) //$NON-NLS-1$
			return Colors.getSWTRGB4AWTGBColor(jrElement.getValueDisplay().getColor());

		if (id.equals(JRDesignMeterPlot.PROPERTY_METER_ANGLE))
			return jrElement.getMeterAngleInteger();
		if (id.equals(JRDesignMeterPlot.PROPERTY_TICK_INTERVAL))
			return jrElement.getTickIntervalDouble();

		if (id.equals(JRDesignMeterPlot.PROPERTY_SHAPE))
			return shapeD.getEnumValue(jrElement.getShapeValue());
		if (id.equals(JRDesignMeterPlot.PROPERTY_UNITS))
			return jrElement.getUnits();
		if (id.equals(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_MASK)) //$NON-NLS-1$
			return jrElement.getValueDisplay().getMask();

		if (id.equals(JRDesignMeterPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION))
			return ExprUtil.getExpression(jrDataRange.getHighExpression());
		if (id.equals(JRDesignMeterPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION))
			return ExprUtil.getExpression(jrDataRange.getLowExpression());
		if (id.equals(JRDesignMeterPlot.PROPERTY_TICK_LABEL_FONT)) {
			tlFont = MFontUtil.getMFont(tlFont, jrElement.getTickLabelFont(), null, this);
			return tlFont;
		}
		if (id.equals(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_FONT)) { //$NON-NLS-1$
			vdFont = MFontUtil.getMFont(vdFont, jrElement.getValueDisplay().getFont(), null, this);
			return vdFont;
		}
		if (id.equals(JRDesignMeterPlot.PROPERTY_INTERVALS)) {
			List<JRMeterInterval> lst = jrElement.getIntervals();
			if (lst != null) {
				return new ArrayList<JRMeterInterval>(lst);
			}
			return lst;
		}

		return super.getPropertyValue(id);
	}

	private MFont tlFont;
	private MFont vdFont;
	private static JSSEnumPropertyDescriptor shapeD;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignMeterPlot jrElement = (JRDesignMeterPlot) getValue();
		JRDesignDataRange jrDataRange = (JRDesignDataRange) jrElement.getDataRange();
		if (id.equals(JRDesignMeterPlot.PROPERTY_INTERVALS)) {
			jrElement.setIntervals((Collection<JRMeterInterval>) value);
		} else if (id.equals(JRDesignMeterPlot.PROPERTY_TICK_LABEL_FONT)) {
			jrElement.setTickLabelFont(MFontUtil.setMFont(value));
		} else if (id.equals(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_FONT)) { //$NON-NLS-1$
			JRDesignValueDisplay jrDesignValueDisplay = new JRDesignValueDisplay(jrElement.getValueDisplay(), jrElement.getChart());
			jrDesignValueDisplay.setFont(MFontUtil.setMFont(value));
			jrElement.setValueDisplay(jrDesignValueDisplay);
		} else if (id.equals(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_COLOR) //$NON-NLS-1$
				&& value instanceof AlfaRGB) {
			JRDesignValueDisplay jrDesignValueDisplay = new JRDesignValueDisplay(jrElement.getValueDisplay(), jrElement.getChart());
			jrDesignValueDisplay.setColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
			jrElement.setValueDisplay(jrDesignValueDisplay);
		} else if (id.equals(JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_MASK)) { //$NON-NLS-1$
			JRDesignValueDisplay jrDesignValueDisplay = new JRDesignValueDisplay(jrElement.getValueDisplay(), jrElement.getChart());
			jrDesignValueDisplay.setMask((String) value);
			jrElement.setValueDisplay(jrDesignValueDisplay);

		} else if (id.equals(JRDesignMeterPlot.PROPERTY_METER_BACKGROUND_COLOR) && value instanceof AlfaRGB)
			jrElement.setMeterBackgroundColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignMeterPlot.PROPERTY_TICK_COLOR) && value instanceof AlfaRGB)
			jrElement.setTickColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignMeterPlot.PROPERTY_NEEDLE_COLOR) && value instanceof AlfaRGB)
			jrElement.setNeedleColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignMeterPlot.PROPERTY_METER_ANGLE))
			jrElement.setMeterAngle((Integer) value);
		else if (id.equals(JRDesignMeterPlot.PROPERTY_TICK_INTERVAL))
			jrElement.setTickInterval((Double) value);

		else if (id.equals(JRDesignMeterPlot.PROPERTY_SHAPE))
			try {
				jrElement.setShape((MeterShapeEnum) shapeD.getEnumValue(value));
			} catch (JRException e) {
				e.printStackTrace();
			}
		else if (id.equals(JRDesignMeterPlot.PROPERTY_UNITS))
			jrElement.setUnits((String) value);

		else if (id.equals(JRDesignMeterPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION))
			jrDataRange.setHighExpression(ExprUtil.setValues(jrDataRange.getHighExpression(), value));
		else if (id.equals(JRDesignMeterPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION))
			jrDataRange.setLowExpression(ExprUtil.setValues(jrDataRange.getLowExpression(), value));
		else
			super.setPropertyValue(id, value);
	}
}
