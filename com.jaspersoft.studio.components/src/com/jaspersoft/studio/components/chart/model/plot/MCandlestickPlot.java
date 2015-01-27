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

import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.design.JRDesignCandlestickPlot;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.model.text.MFontUtil;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.FontPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

public class MCandlestickPlot extends MChartPlot {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MCandlestickPlot(JRCandlestickPlot value) {
		super(value);
	}

	@Override
	public String getDisplayText() {
		return Messages.MCandlestickPlot_candlestick_plot;
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

		ColorPropertyDescriptor catAxisLabelColorD = new ColorPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_COLOR, Messages.common_category_axis_label_color, NullEnum.NULL);
		catAxisLabelColorD.setDescription(Messages.MCandlestickPlot_category_axis_label_color_description);
		desc.add(catAxisLabelColorD);

		JRExpressionPropertyDescriptor catAxisLabelExprD = new JRExpressionPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_EXPRESSION, Messages.common_category_axis_label_expression);
		catAxisLabelExprD.setDescription(Messages.MCandlestickPlot_category_axis_label_expression_description);
		desc.add(catAxisLabelExprD);
		catAxisLabelExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#timeAxisLabelExpression"));

		FontPropertyDescriptor catAxisLabelFontD = new FontPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_FONT, Messages.common_category_axis_label_font);
		catAxisLabelFontD.setDescription(Messages.MCandlestickPlot_category_axis_label_font_description);
		desc.add(catAxisLabelFontD);
		catAxisLabelFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#labelFont"));

		ColorPropertyDescriptor catAxisTickLabelColorD = new ColorPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_COLOR, Messages.common_category_axis_tick_label_color,
				NullEnum.INHERITED);
		catAxisTickLabelColorD.setDescription(Messages.MCandlestickPlot_category_axis_tick_label_color_description);
		desc.add(catAxisTickLabelColorD);

		FontPropertyDescriptor catAxisTickLabelFontD = new FontPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_FONT, Messages.common_category_axis_tick_label_font);
		catAxisTickLabelFontD.setDescription(Messages.MCandlestickPlot_category_axis_tick_label_font_description);
		desc.add(catAxisTickLabelFontD);
		catAxisTickLabelFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#tickLabelFont"));

		ColorPropertyDescriptor catAxisLineColorD = new ColorPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LINE_COLOR, Messages.common_category_axis_line_color, NullEnum.NULL);
		catAxisLineColorD.setDescription(Messages.MCandlestickPlot_category_axis_line_color_description);
		desc.add(catAxisLineColorD);

		ColorPropertyDescriptor valAxisLabelColorD = new ColorPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_COLOR, Messages.common_value_axis_label_color, NullEnum.NULL);
		valAxisLabelColorD.setDescription(Messages.MCandlestickPlot_value_axis_label_color_description);
		desc.add(valAxisLabelColorD);

		JRExpressionPropertyDescriptor valAxisLabelExprD = new JRExpressionPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_EXPRESSION,
				Messages.common_category_value_axis_label_expression);
		valAxisLabelExprD.setDescription(Messages.MCandlestickPlot_category_value_axis_label_expression_description);
		desc.add(valAxisLabelExprD);
		valAxisLabelExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#valueAxisLabelExpression"));

		FontPropertyDescriptor valAxisLabelFontD = new FontPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_FONT, Messages.common_value_axis_label_font);
		valAxisLabelFontD.setDescription(Messages.MCandlestickPlot_value_axis_label_font_description);
		desc.add(valAxisLabelFontD);
		valAxisLabelFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#labelFont"));

		ColorPropertyDescriptor valAxisTickLabelColorD = new ColorPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_COLOR, Messages.common_value_axis_tick_label_color,
				NullEnum.NULL);
		valAxisTickLabelColorD.setDescription(Messages.MCandlestickPlot_value_axis_tick_label_color_description);
		desc.add(valAxisTickLabelColorD);

		FontPropertyDescriptor valAxisTickLabelFontD = new FontPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_FONT, Messages.common_value_axis_tick_label_font);
		valAxisTickLabelFontD.setDescription(Messages.MCandlestickPlot_value_axis_tick_label_font_description);
		desc.add(valAxisTickLabelFontD);
		valAxisTickLabelFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#tickLabelFont"));

		ColorPropertyDescriptor valAxisLineColorD = new ColorPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LINE_COLOR, Messages.common_value_axis_line_color, NullEnum.NULL);
		valAxisLineColorD.setDescription(Messages.MCandlestickPlot_value_axis_line_color_description);
		desc.add(valAxisLineColorD);

		JRExpressionPropertyDescriptor rangeAxisMinExprD = new JRExpressionPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_RANGE_AXIS_MINVALUE_EXPRESSION,
				Messages.common_range_axis_minvalue_expression);
		rangeAxisMinExprD.setDescription(Messages.MCandlestickPlot_range_axis_minvalue_expression_description);
		desc.add(rangeAxisMinExprD);
		rangeAxisMinExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#rangeAxisMinValueExpression"));

		JRExpressionPropertyDescriptor rangeAxisMaxExprD = new JRExpressionPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_RANGE_AXIS_MAXVALUE_EXPRESSION,
				Messages.common_range_axis_maxvalue_expression);
		rangeAxisMaxExprD.setDescription(Messages.MCandlestickPlot_range_axis_maxvalue_expression_description);
		desc.add(rangeAxisMaxExprD);
		rangeAxisMaxExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#rangeAxisMaxValueExpression"));

		JRExpressionPropertyDescriptor domainAxisMinExprD = new JRExpressionPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_DOMAIN_AXIS_MINVALUE_EXPRESSION,
				Messages.common_domain_axis_minvalue_expression);
		domainAxisMinExprD.setDescription(Messages.MCandlestickPlot_domain_axis_minvalue_expression_description);
		desc.add(domainAxisMinExprD);
		domainAxisMinExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#domainAxisMinValueExpression"));

		JRExpressionPropertyDescriptor domainAxisMaxExprD = new JRExpressionPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_DOMAIN_AXIS_MAXVALUE_EXPRESSION,
				Messages.common_domain_axis_maxvalue_expression);
		domainAxisMaxExprD.setDescription(Messages.MCandlestickPlot_domain_axis_maxvalue_expression_description);
		desc.add(domainAxisMaxExprD);
		domainAxisMaxExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#domainAxisMaxValueExpression"));

		CheckBoxPropertyDescriptor catAxisVertTickLabelD = new CheckBoxPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_VERTICAL_TICK_LABELS,
				Messages.common_category_axis_vertical_tick_labels, NullEnum.NULL);
		catAxisVertTickLabelD.setDescription(Messages.MCandlestickPlot_category_axis_vertical_tick_labels_description);
		catAxisVertTickLabelD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#axisFormat_verticalTickLabels"));
		desc.add(catAxisVertTickLabelD);

		CheckBoxPropertyDescriptor valAxisVertTickLabelD = new CheckBoxPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_VERTICAL_TICK_LABELS,
				Messages.common_value_axis_vertical_tick_labels, NullEnum.NULL);
		valAxisVertTickLabelD.setDescription(Messages.MCandlestickPlot_value_axis_vertical_tick_labels_description);
		valAxisVertTickLabelD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#axisFormat_verticalTickLabels"));
		desc.add(valAxisVertTickLabelD);

		NTextPropertyDescriptor catAxisTickLabelMaskD = new NTextPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_MASK, Messages.common_category_axis_tick_label_mask);
		catAxisTickLabelMaskD.setDescription(Messages.MCandlestickPlot_category_axis_tick_label_mask_description);
		catAxisTickLabelMaskD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#axisFormat_tickLabelMask"));
		desc.add(catAxisTickLabelMaskD);

		NTextPropertyDescriptor valAxisTickLabelMaskD = new NTextPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_MASK, Messages.common_value_axis_tick_label_mask);
		valAxisTickLabelMaskD.setDescription(Messages.MCandlestickPlot_value_axis_tick_label_mask_description);
		valAxisTickLabelMaskD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#axisFormat_tickLabelMask"));
		desc.add(valAxisTickLabelMaskD);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#axisFormat");

		CheckBoxPropertyDescriptor showVolumeD = new CheckBoxPropertyDescriptor(JRDesignCandlestickPlot.PROPERTY_SHOW_VOLUME, Messages.MCandlestickPlot_show_volume, NullEnum.NULL);
		showVolumeD.setDescription(Messages.MCandlestickPlot_show_volume_description);
		desc.add(showVolumeD);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#candlestickPlot");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		JRDesignCandlestickPlot jrElement = (JRDesignCandlestickPlot) getValue();
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnTimeAxisLabelColor());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnTimeAxisTickLabelColor());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LINE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnTimeAxisLineColor());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnValueAxisLabelColor());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnValueAxisTickLabelColor());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LINE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnValueAxisLineColor());

		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_VERTICAL_TICK_LABELS))
			return jrElement.getTimeAxisVerticalTickLabels();
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_VERTICAL_TICK_LABELS))
			return jrElement.getValueAxisVerticalTickLabels();

		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_MASK))
			return jrElement.getTimeAxisTickLabelMask();
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_MASK))
			return jrElement.getValueAxisTickLabelMask();
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_SHOW_VOLUME))
			return jrElement.getShowVolume();

		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getTimeAxisLabelExpression());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getValueAxisLabelExpression());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_RANGE_AXIS_MAXVALUE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getRangeAxisMaxValueExpression());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_RANGE_AXIS_MINVALUE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getRangeAxisMinValueExpression());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_DOMAIN_AXIS_MAXVALUE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getDomainAxisMaxValueExpression());
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_DOMAIN_AXIS_MINVALUE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getDomainAxisMinValueExpression());

		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_FONT)) {
			clFont = MFontUtil.getMFont(clFont, jrElement.getTimeAxisLabelFont(), null, this);
			return clFont;
		}
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_FONT)) {
			ctFont = MFontUtil.getMFont(ctFont, jrElement.getTimeAxisTickLabelFont(), null, this);
			return ctFont;
		}
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_FONT)) {
			vlFont = MFontUtil.getMFont(vlFont, jrElement.getValueAxisLabelFont(), null, this);
			return vlFont;
		}
		if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_FONT)) {
			vtFont = MFontUtil.getMFont(vtFont, jrElement.getValueAxisTickLabelFont(), null, this);
			return vtFont;
		}

		return super.getPropertyValue(id);
	}

	private MFont clFont;
	private MFont ctFont;
	private MFont vlFont;
	private MFont vtFont;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignCandlestickPlot jrElement = (JRDesignCandlestickPlot) getValue();

		if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_FONT)) {
			jrElement.setTimeAxisLabelFont(MFontUtil.setMFont(value));
		} else if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_FONT)) {
			jrElement.setTimeAxisTickLabelFont(MFontUtil.setMFont(value));
		} else if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_FONT)) {
			jrElement.setValueAxisLabelFont(MFontUtil.setMFont(value));
		} else if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_FONT)) {
			jrElement.setValueAxisTickLabelFont(MFontUtil.setMFont(value));
		} else if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_COLOR) && value instanceof AlfaRGB)
			jrElement.setTimeAxisLabelColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_COLOR) && value instanceof AlfaRGB)
			jrElement.setTimeAxisTickLabelColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LINE_COLOR) && value instanceof AlfaRGB)
			jrElement.setTimeAxisLineColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_COLOR) && value instanceof AlfaRGB)
			jrElement.setValueAxisLabelColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_COLOR) && value instanceof AlfaRGB)
			jrElement.setValueAxisTickLabelColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LINE_COLOR) && value instanceof AlfaRGB)
			jrElement.setValueAxisLineColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));

		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_VERTICAL_TICK_LABELS))
			jrElement.setTimeAxisVerticalTickLabels((Boolean) value);
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_VERTICAL_TICK_LABELS))
			jrElement.setValueAxisVerticalTickLabels((Boolean) value);

		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_TICK_LABEL_MASK))
			jrElement.setTimeAxisTickLabelMask((String) value);
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_MASK))
			jrElement.setValueAxisTickLabelMask((String) value);
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_SHOW_VOLUME))
			jrElement.setShowVolume((Boolean) value);

		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_TIME_AXIS_LABEL_EXPRESSION))
			jrElement.setTimeAxisLabelExpression(ExprUtil.setValues(jrElement.getTimeAxisLabelExpression(), value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_VALUE_AXIS_LABEL_EXPRESSION))
			jrElement.setValueAxisLabelExpression(ExprUtil.setValues(jrElement.getValueAxisLabelExpression(), value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_RANGE_AXIS_MAXVALUE_EXPRESSION))
			jrElement.setRangeAxisMaxValueExpression(ExprUtil.setValues(jrElement.getRangeAxisMaxValueExpression(), value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_RANGE_AXIS_MINVALUE_EXPRESSION))
			jrElement.setRangeAxisMinValueExpression(ExprUtil.setValues(jrElement.getRangeAxisMinValueExpression(), value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_DOMAIN_AXIS_MAXVALUE_EXPRESSION))
			jrElement.setDomainAxisMaxValueExpression(ExprUtil.setValues(jrElement.getDomainAxisMaxValueExpression(), value));
		else if (id.equals(JRDesignCandlestickPlot.PROPERTY_DOMAIN_AXIS_MINVALUE_EXPRESSION))
			jrElement.setDomainAxisMinValueExpression(ExprUtil.setValues(jrElement.getDomainAxisMinValueExpression(), value));
		else
			super.setPropertyValue(id, value);
	}
}
