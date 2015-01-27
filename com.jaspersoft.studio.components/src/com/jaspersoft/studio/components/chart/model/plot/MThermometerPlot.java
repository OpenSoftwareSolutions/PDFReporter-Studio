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

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.design.JRDesignDataRange;
import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;
import net.sf.jasperreports.charts.type.ValueLocationEnum;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.model.text.MFontUtil;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.FontPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

public class MThermometerPlot extends MChartPlot {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MThermometerPlot(JRThermometerPlot value) {
		super(value);
	}

	@Override
	public String getDisplayText() {
		return Messages.MThermometerPlot_thermometer_plot;
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

		ColorPropertyDescriptor mercuryColorD = new ColorPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_MERCURY_COLOR, Messages.MThermometerPlot_mercury_color, NullEnum.NULL);
		mercuryColorD.setDescription(Messages.MThermometerPlot_mercury_color_description);
		desc.add(mercuryColorD);

		ColorPropertyDescriptor valueColorD = new ColorPropertyDescriptor(
				JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_COLOR, Messages.common_value_color, NullEnum.NULL); //$NON-NLS-1$
		valueColorD.setDescription(Messages.MThermometerPlot_value_color_description);
		desc.add(valueColorD);
		valueColorD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#valueDisplay_color"));

		FontPropertyDescriptor valueFontD = new FontPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
				+ JRDesignValueDisplay.PROPERTY_FONT, Messages.common_value_font);
		valueFontD.setDescription(Messages.MThermometerPlot_value_font_description);
		desc.add(valueFontD);
		valueFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#font"));

		NTextPropertyDescriptor maskD = new NTextPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
				+ JRDesignValueDisplay.PROPERTY_MASK, Messages.common_value_mask);
		maskD.setDescription(Messages.MThermometerPlot_value_mask_description);
		desc.add(maskD);
		maskD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#valueDisplay_mask"));

		JRExpressionPropertyDescriptor dataRangeHighExprD = new JRExpressionPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION, //$NON-NLS-1$
				Messages.common_data_range_high_expression);
		dataRangeHighExprD.setDescription(Messages.MThermometerPlot_data_range_high_expression_description);
		desc.add(dataRangeHighExprD);
		dataRangeHighExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#highExpression"));

		JRExpressionPropertyDescriptor dataRangeLowExprD = new JRExpressionPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION, //$NON-NLS-1$
				Messages.common_data_range_low_expression);
		dataRangeLowExprD.setDescription(Messages.MThermometerPlot_data_range_low_expression_description);
		desc.add(dataRangeLowExprD);
		dataRangeLowExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#lowExpression"));

		JRExpressionPropertyDescriptor highRangeHighExprD = new JRExpressionPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_HIGH_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION, //$NON-NLS-1$
				Messages.MThermometerPlot_high_range_high_expression);
		highRangeHighExprD.setDescription(Messages.MThermometerPlot_high_range_high_expression_description);
		desc.add(highRangeHighExprD);
		highRangeHighExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#highExpression"));

		JRExpressionPropertyDescriptor highRangeLowExprD = new JRExpressionPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_HIGH_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION, //$NON-NLS-1$
				Messages.MThermometerPlot_high_range_low_expression);
		highRangeLowExprD.setDescription(Messages.MThermometerPlot_high_range_low_expression_description);
		desc.add(highRangeLowExprD);
		highRangeLowExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#lowExpression"));

		JRExpressionPropertyDescriptor lowRangeHighExprD = new JRExpressionPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_LOW_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION, //$NON-NLS-1$
				Messages.MThermometerPlot_low_range_high_expression);
		lowRangeHighExprD.setDescription(Messages.MThermometerPlot_low_range_high_expression_description);
		desc.add(lowRangeHighExprD);
		lowRangeHighExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#highExpression"));

		JRExpressionPropertyDescriptor lowRangeLowExprD = new JRExpressionPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_LOW_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION, //$NON-NLS-1$
				Messages.MThermometerPlot_low_range_low_expression);
		lowRangeLowExprD.setDescription(Messages.MThermometerPlot_low_range_low_expression_description);
		desc.add(lowRangeLowExprD);
		lowRangeLowExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#lowExpression"));

		JRExpressionPropertyDescriptor medRangeHighExprD = new JRExpressionPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_MEDIUM_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION, //$NON-NLS-1$
				Messages.MThermometerPlot_medium_range_high_expression);
		medRangeHighExprD.setDescription(Messages.MThermometerPlot_medium_range_high_expression_description);
		desc.add(medRangeHighExprD);
		medRangeHighExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#highExpression"));

		JRExpressionPropertyDescriptor medRangeLowExprD = new JRExpressionPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_MEDIUM_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION, //$NON-NLS-1$
				Messages.MThermometerPlot_medium_range_low_expression);
		medRangeLowExprD.setDescription(Messages.MThermometerPlot_medium_range_low_expression_description);
		desc.add(medRangeLowExprD);
		medRangeLowExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#lowExpression"));

		positionTypeD = new JSSEnumPropertyDescriptor(JRDesignThermometerPlot.PROPERTY_VALUE_LOCATION, Messages.MThermometerPlot_value_location, ValueLocationEnum.class, NullEnum.NOTNULL);
		positionTypeD.setDescription(Messages.MThermometerPlot_value_location_description);
		desc.add(positionTypeD);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#thermometerPlot");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		JRDesignThermometerPlot jrElement = (JRDesignThermometerPlot) getValue();
		JRDesignDataRange jrDataRange = (JRDesignDataRange) jrElement.getDataRange();
		JRDesignDataRange jrHighRange = (JRDesignDataRange) jrElement.getHighRange();
		JRDesignDataRange jrLowRange = (JRDesignDataRange) jrElement.getLowRange();
		JRDesignDataRange jrMedRange = (JRDesignDataRange) jrElement.getMediumRange();
		if (id.equals(JRDesignThermometerPlot.PROPERTY_MERCURY_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getMercuryColor());
		if (id.equals(JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_COLOR)) //$NON-NLS-1$
			return Colors.getSWTRGB4AWTGBColor(jrElement.getValueDisplay().getColor());

		if (id.equals(JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_MASK)) //$NON-NLS-1$
			return jrElement.getValueDisplay().getMask();
		if (id.equals(JRDesignThermometerPlot.PROPERTY_VALUE_LOCATION))
			return positionTypeD.getEnumValue(jrElement.getValueLocationValue());

		if (id.equals(JRDesignThermometerPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION))
			return ExprUtil.getExpression(jrDataRange.getHighExpression());
		if (id.equals(JRDesignThermometerPlot.PROPERTY_DATA_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION))
			return ExprUtil.getExpression(jrDataRange.getLowExpression());
		if (id.equals(JRDesignThermometerPlot.PROPERTY_HIGH_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION))
			return ExprUtil.getExpression(jrHighRange.getHighExpression());
		if (id.equals(JRDesignThermometerPlot.PROPERTY_HIGH_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION))
			return ExprUtil.getExpression(jrHighRange.getLowExpression());
		if (id.equals(JRDesignThermometerPlot.PROPERTY_LOW_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION))
			return ExprUtil.getExpression(jrLowRange.getHighExpression());
		if (id.equals(JRDesignThermometerPlot.PROPERTY_LOW_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION))
			return ExprUtil.getExpression(jrLowRange.getLowExpression());
		if (id.equals(JRDesignThermometerPlot.PROPERTY_MEDIUM_RANGE + "." + JRDesignDataRange.PROPERTY_HIGH_EXPRESSION))
			return ExprUtil.getExpression(jrMedRange.getHighExpression());
		if (id.equals(JRDesignThermometerPlot.PROPERTY_MEDIUM_RANGE + "." + JRDesignDataRange.PROPERTY_LOW_EXPRESSION))
			return ExprUtil.getExpression(jrMedRange.getLowExpression());

		if (id.equals(JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_FONT)) { //$NON-NLS-1$
			vtFont = MFontUtil.getMFont(vtFont, jrElement.getValueDisplay().getFont(), null, this);
			return vtFont;
		}

		return super.getPropertyValue(id);
	}

	private MFont vtFont;
	private static JSSEnumPropertyDescriptor positionTypeD;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignThermometerPlot jrElement = (JRDesignThermometerPlot) getValue();
		JRDesignDataRange jrDataRange = (JRDesignDataRange) jrElement.getDataRange();
		JRDesignDataRange jrHighRange = (JRDesignDataRange) jrElement.getHighRange();
		JRDesignDataRange jrLowRange = (JRDesignDataRange) jrElement.getLowRange();
		JRDesignDataRange jrMedRange = (JRDesignDataRange) jrElement.getMediumRange();
		if (id.equals(JRDesignThermometerPlot.PROPERTY_MERCURY_COLOR) && value instanceof AlfaRGB)
			jrElement.setMercuryColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_FONT)) { //$NON-NLS-1$
			JRDesignValueDisplay jrDesignValueDisplay = new JRDesignValueDisplay(jrElement.getValueDisplay(), jrElement.getChart());
			jrDesignValueDisplay.setFont(MFontUtil.setMFont(value));
			jrElement.setValueDisplay(jrDesignValueDisplay);
		} else if (id.equals(JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_COLOR) //$NON-NLS-1$
				&& value instanceof AlfaRGB) {
			JRDesignValueDisplay jrDesignValueDisplay = new JRDesignValueDisplay(jrElement.getValueDisplay(), jrElement.getChart());
			jrDesignValueDisplay.setColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
			jrElement.setValueDisplay(jrDesignValueDisplay);
		} else if (id.equals(JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." + JRDesignValueDisplay.PROPERTY_MASK)) { //$NON-NLS-1$
			JRDesignValueDisplay jrDesignValueDisplay = new JRDesignValueDisplay(jrElement.getValueDisplay(), jrElement.getChart());
			jrDesignValueDisplay.setMask((String) value);
			jrElement.setValueDisplay(jrDesignValueDisplay);
		} else if (id.equals(JRDesignThermometerPlot.PROPERTY_VALUE_LOCATION))
			jrElement.setValueLocation((ValueLocationEnum) positionTypeD.getEnumValue(value));
		else if (id.equals(id.equals(JRDesignThermometerPlot.PROPERTY_DATA_RANGE + "." //$NON-NLS-1$
				+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION)))
			jrDataRange.setHighExpression(ExprUtil.setValues(jrDataRange.getHighExpression(), value));
		else if (id.equals(id.equals(JRDesignThermometerPlot.PROPERTY_DATA_RANGE + "." //$NON-NLS-1$
				+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION)))
			jrDataRange.setLowExpression(ExprUtil.setValues(jrDataRange.getLowExpression(), value));
		else if (id.equals(id.equals(JRDesignThermometerPlot.PROPERTY_HIGH_RANGE + "." //$NON-NLS-1$
				+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION)))
			jrHighRange.setHighExpression(ExprUtil.setValues(jrHighRange.getHighExpression(), value));
		else if (id.equals(id.equals(JRDesignThermometerPlot.PROPERTY_HIGH_RANGE + "." //$NON-NLS-1$
				+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION)))
			jrHighRange.setLowExpression(ExprUtil.setValues(jrHighRange.getLowExpression(), value));
		else if (id.equals(id.equals(JRDesignThermometerPlot.PROPERTY_LOW_RANGE + "." //$NON-NLS-1$
				+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION)))
			jrLowRange.setHighExpression(ExprUtil.setValues(jrLowRange.getHighExpression(), value));
		else if (id.equals(id.equals(JRDesignThermometerPlot.PROPERTY_LOW_RANGE + "." //$NON-NLS-1$
				+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION)))
			jrLowRange.setLowExpression(ExprUtil.setValues(jrLowRange.getLowExpression(), value));
		else if (id.equals(id.equals(JRDesignThermometerPlot.PROPERTY_MEDIUM_RANGE + "." //$NON-NLS-1$
				+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION)))
			jrMedRange.setHighExpression(ExprUtil.setValues(jrMedRange.getHighExpression(), value));
		else if (id.equals(id.equals(JRDesignThermometerPlot.PROPERTY_MEDIUM_RANGE + "." //$NON-NLS-1$
				+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION)))
			jrMedRange.setLowExpression(ExprUtil.setValues(jrMedRange.getLowExpression(), value));
		else
			super.setPropertyValue(id, value);

	}
}
