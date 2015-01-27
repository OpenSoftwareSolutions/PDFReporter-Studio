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
package com.jaspersoft.studio.components.chartspider.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.components.spiderchart.StandardSpiderDataset;
import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;
import net.sf.jasperreports.components.spiderchart.type.SpiderRotationEnum;
import net.sf.jasperreports.components.spiderchart.type.TableOrderEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.ChartNodeIconDescriptor;
import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MHyperLink;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.model.text.MFontUtil;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.classname.ClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.dialog.ParameterDTO;
import com.jaspersoft.studio.property.descriptor.text.FontPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.DoublePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.EdgePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.SpinnerPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.Misc;

/**
 * 
 * @author veaceslav chicu
 * 
 */
public class MSpiderChart extends MGraphicElement implements IDatasetContainer {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MSpiderChart() {
		super();
	}

	public MSpiderChart(ANode parent, JRDesignComponentElement jrObject, int newIndex) {
		super(parent, jrObject, newIndex);
	}

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new ChartNodeIconDescriptor("spiderchart"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getImagePath()
	 */
	@Override
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	private IPropertyDescriptor[] descriptors;
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

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		titlePositionD = new EdgePropertyDescriptor(StandardChartSettings.PROPERTY_TITLE_POSITION, Messages.MChart_title_position, EdgeEnum.class, NullEnum.NULL);
		titlePositionD.setDescription(Messages.MChart_title_position_description);
		desc.add(titlePositionD);

		legendPositionD = new EdgePropertyDescriptor(StandardChartSettings.PROPERTY_LEGEND_POSITION, Messages.MChart_legend_position, EdgeEnum.class, NullEnum.NULL);
		legendPositionD.setDescription(Messages.MChart_legend_position_description);
		desc.add(legendPositionD);

		ColorPropertyDescriptor titleColorD = new ColorPropertyDescriptor(StandardChartSettings.PROPERTY_TITLE_COLOR, Messages.MChart_title_color, NullEnum.INHERITED);
		titleColorD.setDescription(Messages.MChart_title_color_description);
		desc.add(titleColorD);

		JRExpressionPropertyDescriptor titleExprD = new JRExpressionPropertyDescriptor(StandardChartSettings.PROPERTY_TITLE_EXPRESSION, Messages.MChart_title_expression);
		titleExprD.setDescription(Messages.MChart_title_expression_description);
		desc.add(titleExprD);

		ColorPropertyDescriptor subtitleColorD = new ColorPropertyDescriptor(StandardChartSettings.PROPERTY_SUBTITLE_COLOR, Messages.MChart_subtitle_color, NullEnum.INHERITED);
		subtitleColorD.setDescription(Messages.MChart_subtitle_color_description);
		desc.add(subtitleColorD);

		JRExpressionPropertyDescriptor subtitleExprD = new JRExpressionPropertyDescriptor(StandardChartSettings.PROPERTY_SUBTITLE_EXPRESSION, Messages.MChart_subtitle_expression);
		subtitleExprD.setDescription(Messages.MChart_subtitle_expression_description);
		desc.add(subtitleExprD);

		ColorPropertyDescriptor legendColorD = new ColorPropertyDescriptor(StandardChartSettings.PROPERTY_LEGEND_COLOR, Messages.MChart_legend_color, NullEnum.INHERITED);
		legendColorD.setDescription(Messages.MChart_legend_color_description);
		desc.add(legendColorD);

		ColorPropertyDescriptor legendBackColorD = new ColorPropertyDescriptor(StandardChartSettings.PROPERTY_LEGEND_BACKGROUND_COLOR, Messages.MChart_legend_background_color, NullEnum.INHERITED);
		legendBackColorD.setDescription(Messages.MChart_legend_background_color_description);
		desc.add(legendBackColorD);

		ClassTypePropertyDescriptor classD = new ClassTypePropertyDescriptor(StandardChartSettings.PROPERTY_CUSTOMIZER_CLASS, Messages.MChart_customizer_class);
		classD.setDescription(Messages.MChart_customizer_class_description);
		desc.add(classD);

		CheckBoxPropertyDescriptor showLegendD = new CheckBoxPropertyDescriptor(StandardChartSettings.PROPERTY_SHOW_LEGEND, Messages.MChart_show_legend, NullEnum.NULL);
		showLegendD.setDescription(Messages.MChart_show_legend_description);
		desc.add(showLegendD);

		RWComboBoxPropertyDescriptor rendererTypeD = new RWComboBoxPropertyDescriptor(StandardChartSettings.PROPERTY_RENDER_TYPE, Messages.MChart_renderer_type,
				new String[] { "", "draw", "image", "svg" }, NullEnum.NULL); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		rendererTypeD.setDescription(Messages.MChart_renderer_type_description);
		desc.add(rendererTypeD);

		FontPropertyDescriptor titleFontD = new FontPropertyDescriptor(StandardChartSettings.PROPERTY_TITLE_FONT, Messages.MChart_title_font);
		titleFontD.setDescription(Messages.MChart_title_font_description);
		desc.add(titleFontD);

		FontPropertyDescriptor subtitleFontD = new FontPropertyDescriptor(StandardChartSettings.PROPERTY_SUBTITLE_FONT, Messages.MChart_subtitle_font);
		subtitleFontD.setDescription(Messages.MChart_subtitle_font_description);
		desc.add(subtitleFontD);

		FontPropertyDescriptor legendFontD = new FontPropertyDescriptor(StandardChartSettings.PROPERTY_LEGEND_FONT, Messages.MChart_legend_font);
		legendFontD.setDescription(Messages.MChart_legend_font_description);
		desc.add(legendFontD);

		evaluationTimeD = new JSSEnumPropertyDescriptor(SpiderChartComponent.PROPERTY_EVALUATION_TIME, "", EvaluationTimeEnum.class, //$NON-NLS-1$
				NullEnum.NOTNULL);
		evaluationTimeD.setDescription(""); //$NON-NLS-1$
		desc.add(evaluationTimeD);

		evaluationGroupNameD = new RComboBoxPropertyDescriptor(SpiderChartComponent.PROPERTY_EVALUATION_GROUP, "", //$NON-NLS-1$
				new String[] { "" }); //$NON-NLS-2$ //$NON-NLS-1$
		evaluationGroupNameD.setDescription(""); //$NON-NLS-1$
		desc.add(evaluationGroupNameD);

		JRExpressionPropertyDescriptor anchorNameExp = new JRExpressionPropertyDescriptor(StandardChartSettings.PROPERTY_ANCHOR_NAME_EXPRESSION,
				com.jaspersoft.studio.messages.Messages.MTextField_anchorNameLabel);
		anchorNameExp.setDescription(com.jaspersoft.studio.messages.Messages.MTextField_anchorNameDescription);
		desc.add(anchorNameExp);

		SpinnerPropertyDescriptor bookmarkLevel = new SpinnerPropertyDescriptor(StandardChartSettings.PROPERTY_BOOKMARK_LEVEL, com.jaspersoft.studio.messages.Messages.MTextField_bookmarkLevelLabel);
		bookmarkLevel.setDescription(com.jaspersoft.studio.messages.Messages.MTextField_bookmarkLevelDescription);
		bookmarkLevel.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chart_bookmarkLevel")); //$NON-NLS-1$
		desc.add(bookmarkLevel);

		if (mHyperLink == null)
			mHyperLink = new MHyperLink(null);
		mHyperLink.createPropertyDescriptors(desc, defaultsMap);

		ColorPropertyDescriptor axLineColorD = new ColorPropertyDescriptor(StandardSpiderPlot.PROPERTY_AXIS_LINE_COLOR, Messages.MSpiderChart_axisLineColorTitle, NullEnum.INHERITED);
		axLineColorD.setDescription(Messages.MSpiderChart_axisLineColorDesc);
		desc.add(axLineColorD);
		axLineColorD.setCategory(Messages.MChart_plot);

		NTextPropertyDescriptor axLineWidthD = new NTextPropertyDescriptor(StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH, Messages.MSpiderChart_axisLineWidthTitle);
		axLineWidthD.setDescription(Messages.MSpiderChart_axisLineWidthDescription);
		desc.add(axLineWidthD);
		axLineWidthD.setCategory(Messages.MChart_plot);

		JRExpressionPropertyDescriptor maxValExpr = new JRExpressionPropertyDescriptor(StandardSpiderPlot.PROPERTY_MAX_VALUE_EXPRESSION, Messages.MSpiderChart_maxValueExpTitle);
		maxValExpr.setDescription(Messages.MSpiderChart_maxValueExpDescription);
		desc.add(maxValExpr);

		rotation = new JSSEnumPropertyDescriptor(StandardSpiderPlot.PROPERTY_ROTATION, Messages.MChart_title_position, SpiderRotationEnum.class, NullEnum.NULL);
		rotation.setDescription(Messages.MChart_title_position_description);
		desc.add(rotation);

		tableOrder = new JSSEnumPropertyDescriptor(StandardSpiderPlot.PROPERTY_TABLE_ORDER, Messages.MSpiderChart_tableOrderTitle, TableOrderEnum.class, NullEnum.NULL);
		tableOrder.setDescription(Messages.MSpiderChart_tableOrderTitledescription);
		desc.add(tableOrder);

		CheckBoxPropertyDescriptor webFilled = new CheckBoxPropertyDescriptor(StandardSpiderPlot.PROPERTY_WEB_FILLED, Messages.MSpiderChart_webFilledTitle, NullEnum.NULL);
		webFilled.setDescription(Messages.MSpiderChart_webFilledDescription);
		desc.add(webFilled);

		DoublePropertyDescriptor startAngle = new DoublePropertyDescriptor(StandardSpiderPlot.PROPERTY_START_ANGLE, Messages.MSpiderChart_startAngleTitle);
		startAngle.setDescription(Messages.MSpiderChart_startAngleDescription);
		desc.add(startAngle);

		DoublePropertyDescriptor headPercent = new DoublePropertyDescriptor(StandardSpiderPlot.PROPERTY_HEAD_PERCENT, Messages.MSpiderChart_headPercentTitle);
		headPercent.setDescription(Messages.MSpiderChart_headPercentDescription);
		desc.add(headPercent);

		DoublePropertyDescriptor interiorGap = new DoublePropertyDescriptor(StandardSpiderPlot.PROPERTY_INTERIOR_GAP, Messages.MSpiderChart_interiorGapTitle);
		interiorGap.setDescription(Messages.MSpiderChart_interiorGapDescription);
		desc.add(interiorGap);

		startAngle.setCategory(Messages.MChart_chart_title_category);
		headPercent.setCategory(Messages.MChart_chart_title_category);
		interiorGap.setCategory(Messages.MChart_chart_title_category);
		webFilled.setCategory(Messages.MChart_chart_title_category);
		rotation.setCategory(Messages.MChart_chart_title_category);
		tableOrder.setCategory(Messages.MChart_chart_title_category);
		maxValExpr.setCategory(Messages.MChart_chart_title_category);
		titleFontD.setCategory(Messages.MChart_chart_title_category);
		titleColorD.setCategory(Messages.MChart_chart_title_category);
		titlePositionD.setCategory(Messages.MChart_chart_title_category);
		titleExprD.setCategory(Messages.MChart_chart_title_category);

		subtitleFontD.setCategory(Messages.MChart_chart_subtitle_category);
		subtitleExprD.setCategory(Messages.MChart_chart_subtitle_category);
		subtitleColorD.setCategory(Messages.MChart_chart_subtitle_category);

		evaluationGroupNameD.setCategory(Messages.MChart_common_chart_properties_category);

		classD.setCategory(Messages.MChart_common_chart_properties_category);

		legendFontD.setCategory(Messages.MChart_chart_legend_category);
		legendBackColorD.setCategory(Messages.MChart_chart_legend_category);
		legendColorD.setCategory(Messages.MChart_chart_legend_category);
		legendPositionD.setCategory(Messages.MChart_chart_legend_category);
		showLegendD.setCategory(Messages.MChart_chart_legend_category);

		evaluationTimeD.setCategory(Messages.MChart_common_chart_properties_category);

		rendererTypeD.setCategory(Messages.MChart_common_chart_properties_category);

		defaultsMap.put(StandardSpiderPlot.PROPERTY_START_ANGLE, null);
		defaultsMap.put(StandardSpiderPlot.PROPERTY_HEAD_PERCENT, null);
		defaultsMap.put(StandardSpiderPlot.PROPERTY_INTERIOR_GAP, null);
		defaultsMap.put(StandardSpiderPlot.PROPERTY_WEB_FILLED, null);
		defaultsMap.put(StandardSpiderPlot.PROPERTY_ROTATION, null);
		defaultsMap.put(StandardSpiderPlot.PROPERTY_TABLE_ORDER, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_CUSTOMIZER_CLASS, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_SHOW_LEGEND, new Boolean(true));
		defaultsMap.put(StandardChartSettings.PROPERTY_TITLE_COLOR, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_SUBTITLE_COLOR, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_LEGEND_COLOR, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_LEGEND_BACKGROUND_COLOR, null);

		defaultsMap.put(StandardChartSettings.PROPERTY_TITLE_FONT, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_SUBTITLE_FONT, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_LEGEND_FONT, null);

		defaultsMap.put(StandardChartSettings.PROPERTY_TITLE_POSITION, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_LEGEND_POSITION, null);
		defaultsMap.put(StandardChartSettings.PROPERTY_EVALUATION_TIME, EnumHelper.getValue(EvaluationTimeEnum.NOW, 1, false));

		defaultsMap.put(StandardSpiderPlot.PROPERTY_AXIS_LINE_COLOR, null);
		defaultsMap.put(StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH, null);
	}

	private MHyperLink mHyperLink;

	private ParameterDTO propertyDTO;

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		SpiderChartComponent component = (SpiderChartComponent) jrElement.getComponent();
		StandardChartSettings cs = (StandardChartSettings) component.getChartSettings();

		if (id.equals(StandardChartSettings.PROPERTY_TITLE_POSITION))
			return titlePositionD.getEnumValue(cs.getTitlePosition());
		if (id.equals(StandardChartSettings.PROPERTY_LEGEND_POSITION))
			return legendPositionD.getEnumValue(cs.getLegendPosition());

		if (id.equals(StandardChartSettings.PROPERTY_RENDER_TYPE))
			return cs.getRenderType();

		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS)) {
			if (propertyDTO == null) {
				propertyDTO = new ParameterDTO();
				propertyDTO.setJasperDesign(getJasperDesign());
				propertyDTO.setValue(cs.getHyperlinkParameters());
			}
			return propertyDTO;
		}
		if (id.equals(StandardChartSettings.PROPERTY_TITLE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(cs.getTitleColor());
		if (id.equals(StandardChartSettings.PROPERTY_SUBTITLE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(cs.getSubtitleColor());
		if (id.equals(StandardChartSettings.PROPERTY_LEGEND_COLOR))
			return Colors.getSWTRGB4AWTGBColor(cs.getLegendColor());
		if (id.equals(StandardChartSettings.PROPERTY_LEGEND_BACKGROUND_COLOR))
			return Colors.getSWTRGB4AWTGBColor(cs.getLegendBackgroundColor());
		if (id.equals(StandardChartSettings.PROPERTY_SHOW_LEGEND))
			return cs.getShowLegend();

		if (id.equals(StandardChartSettings.PROPERTY_CUSTOMIZER_CLASS))
			return cs.getCustomizerClass();

		// hyperlink --------------------------------------
		if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TARGET))
			return cs.getLinkTarget();
		if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TYPE))
			return cs.getLinkType();
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION))
			return ExprUtil.getExpression(cs.getHyperlinkAnchorExpression());
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION))
			return ExprUtil.getExpression(cs.getHyperlinkPageExpression());
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION))
			return ExprUtil.getExpression(cs.getHyperlinkReferenceExpression());
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION))
			return ExprUtil.getExpression(cs.getHyperlinkTooltipExpression());
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION)) {
			return ExprUtil.getExpression(cs.getHyperlinkWhenExpression());
		}
		if (id.equals(StandardChartSettings.PROPERTY_ANCHOR_NAME_EXPRESSION)) {
			return ExprUtil.getExpression(cs.getAnchorNameExpression());
		}
		if (id.equals(StandardChartSettings.PROPERTY_BOOKMARK_LEVEL)) {
			return cs.getBookmarkLevel();
		}

		if (id.equals(StandardChartSettings.PROPERTY_TITLE_FONT)) {
			tFont = MFontUtil.getMFont(tFont, cs.getTitleFont(), jrElement.getStyle(), this);
			return tFont;
		}
		if (id.equals(StandardChartSettings.PROPERTY_SUBTITLE_FONT)) {
			stFont = MFontUtil.getMFont(stFont, cs.getSubtitleFont(), jrElement.getStyle(), this);
			return stFont;
		}
		if (id.equals(StandardChartSettings.PROPERTY_LEGEND_FONT)) {
			lFont = MFontUtil.getMFont(lFont, cs.getLegendFont(), jrElement.getStyle(), this);
			return lFont;
		}

		if (id.equals(StandardChartSettings.PROPERTY_TITLE_EXPRESSION))
			return ExprUtil.getExpression(cs.getTitleExpression());
		if (id.equals(StandardChartSettings.PROPERTY_SUBTITLE_EXPRESSION))
			return ExprUtil.getExpression(cs.getSubtitleExpression());

		if (id.equals(SpiderChartComponent.PROPERTY_EVALUATION_TIME))
			return evaluationTimeD.getEnumValue(component.getEvaluationTime());
		if (id.equals(SpiderChartComponent.PROPERTY_EVALUATION_GROUP))
			return component.getEvaluationGroup();

		StandardSpiderPlot sp = (StandardSpiderPlot) component.getPlot();

		if (id.equals(StandardSpiderPlot.PROPERTY_AXIS_LINE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(sp.getAxisLineColor());
		if (id.equals(StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH))
			return sp.getAxisLineWidth();
		if (id.equals(StandardSpiderPlot.PROPERTY_LABEL_COLOR))
			return Colors.getSWTRGB4AWTGBColor(sp.getLabelColor());
		if (id.equals(StandardSpiderPlot.PROPERTY_LABEL_GAP))
			return sp.getLabelGap();
		if (id.equals(StandardSpiderPlot.PROPERTY_LABEL_FONT)) {
			plFont = MFontUtil.getMFont(plFont, sp.getLabelFont(), jrElement.getStyle(), this);
			return plFont;
		}
		if (id.equals(StandardSpiderPlot.PROPERTY_MAX_VALUE_EXPRESSION))
			return ExprUtil.getExpression(sp.getMaxValueExpression());
		if (id.equals(StandardSpiderPlot.PROPERTY_ROTATION))
			return rotation.getEnumValue(sp.getRotation());
		if (id.equals(StandardSpiderPlot.PROPERTY_TABLE_ORDER))
			return tableOrder.getEnumValue(sp.getTableOrder());
		if (id.equals(StandardSpiderPlot.PROPERTY_WEB_FILLED))
			return sp.getWebFilled();
		if (id.equals(StandardSpiderPlot.PROPERTY_START_ANGLE))
			return sp.getStartAngle();
		if (id.equals(StandardSpiderPlot.PROPERTY_HEAD_PERCENT))
			return sp.getHeadPercent();
		if (id.equals(StandardSpiderPlot.PROPERTY_INTERIOR_GAP))
			return sp.getInteriorGap();
		return super.getPropertyValue(id);
	}

	private MFont tFont;
	private MFont stFont;
	private MFont lFont;
	private MFont plFont;

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		SpiderChartComponent component = (SpiderChartComponent) jrElement.getComponent();
		StandardChartSettings cs = (StandardChartSettings) component.getChartSettings();
		StandardSpiderPlot cp = (StandardSpiderPlot) component.getPlot();

		if (id.equals(StandardSpiderPlot.PROPERTY_AXIS_LINE_COLOR)) {
			if (value instanceof AlfaRGB)
				cp.setAxisLineColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH)) {
			cp.setAxisLineWidth((Float) value);
		} else if (id.equals(StandardSpiderPlot.PROPERTY_LABEL_COLOR)) {
			if (value instanceof AlfaRGB)
				cp.setLabelColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(StandardSpiderPlot.PROPERTY_LABEL_GAP)) {
			cp.setLabelGap((Double) value);
		} else if (id.equals(StandardSpiderPlot.PROPERTY_LABEL_FONT)) {
			cp.setLabelFont(MFontUtil.setMFont(value));
		} else if (id.equals(StandardSpiderPlot.PROPERTY_MAX_VALUE_EXPRESSION))
			cp.setMaxValueExpression(ExprUtil.setValues(cp.getMaxValueExpression(), value));
		else if (id.equals(StandardSpiderPlot.PROPERTY_ROTATION)) {
			int pos = (Integer) value;
			if (pos == 0)
				cp.setRotation(null);
			else
				cp.setRotation(SpiderRotationEnum.values()[pos - 1]);
		} else if (id.equals(StandardSpiderPlot.PROPERTY_TABLE_ORDER)) {
			int pos = (Integer) value;
			if (pos == 0)
				cp.setTableOrder(null);
			else
				cp.setTableOrder(TableOrderEnum.values()[pos - 1]);
		} else if (id.equals(StandardSpiderPlot.PROPERTY_WEB_FILLED))
			cp.setWebFilled((Boolean) value);
		else if (id.equals(StandardSpiderPlot.PROPERTY_START_ANGLE))
			cp.setStartAngle((Double) value);
		else if (id.equals(StandardSpiderPlot.PROPERTY_INTERIOR_GAP))
			cp.setInteriorGap((Double) value);
		else if (id.equals(StandardSpiderPlot.PROPERTY_HEAD_PERCENT))
			cp.setHeadPercent((Double) value);

		else if (id.equals(StandardChartSettings.PROPERTY_TITLE_FONT)) {
			cs.setTitleFont(MFontUtil.setMFont(value));
		} else if (id.equals(StandardChartSettings.PROPERTY_SUBTITLE_FONT)) {
			cs.setSubtitleFont(MFontUtil.setMFont(value));
		} else if (id.equals(StandardChartSettings.PROPERTY_LEGEND_FONT)) {
			cs.setLegendFont(MFontUtil.setMFont(value));
		} else if (id.equals(StandardChartSettings.PROPERTY_TITLE_POSITION))
			cs.setTitlePosition((EdgeEnum) titlePositionD.getEnumValue(value));
		else if (id.equals(StandardChartSettings.PROPERTY_LEGEND_POSITION))
			cs.setLegendPosition((EdgeEnum) legendPositionD.getEnumValue(value));

		else if (id.equals(StandardChartSettings.PROPERTY_SHOW_LEGEND))
			cs.setShowLegend((Boolean) value);
		else if (id.equals(StandardChartSettings.PROPERTY_RENDER_TYPE)) {
			value = Misc.nullValue((String) value);
			cs.setRenderType((String) value);
		} else if (id.equals(StandardChartSettings.PROPERTY_TITLE_COLOR)) {
			if (value instanceof AlfaRGB)
				cs.setTitleColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(StandardChartSettings.PROPERTY_SUBTITLE_COLOR)) {
			if (value instanceof AlfaRGB)
				cs.setSubtitleColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(StandardChartSettings.PROPERTY_LEGEND_COLOR)) {
			if (value instanceof AlfaRGB)
				cs.setLegendColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(StandardChartSettings.PROPERTY_LEGEND_BACKGROUND_COLOR)) {
			if (value instanceof AlfaRGB)
				cs.setLegendBackgroundColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(StandardChartSettings.PROPERTY_CUSTOMIZER_CLASS)) {
			value = Misc.nullValue((String) value);
			cs.setCustomizerClass((String) value);
		} else if (id.equals(StandardChartSettings.PROPERTY_TITLE_EXPRESSION)) {
			cs.setTitleExpression(ExprUtil.setValues(cs.getTitleExpression(), value));
		} else if (id.equals(StandardChartSettings.PROPERTY_SUBTITLE_EXPRESSION)) {
			cs.setSubtitleExpression(ExprUtil.setValues(cs.getSubtitleExpression(), value));
		} else if (id.equals(StandardChartSettings.PROPERTY_ANCHOR_NAME_EXPRESSION))
			cs.setAnchorNameExpression(ExprUtil.setValues(cs.getAnchorNameExpression(), value));
		else if (id.equals(StandardChartSettings.PROPERTY_BOOKMARK_LEVEL))
			cs.setBookmarkLevel(value != null ? Integer.parseInt(value.toString()) : 0);
		else if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TARGET))
			cs.setLinkTarget((String) value);
		else if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TYPE))
			cs.setLinkType((String) value);
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION))
			cs.setHyperlinkAnchorExpression(ExprUtil.setValues(cs.getHyperlinkAnchorExpression(), value));
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION))
			cs.setHyperlinkPageExpression(ExprUtil.setValues(cs.getHyperlinkPageExpression(), value));
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION))
			cs.setHyperlinkReferenceExpression(ExprUtil.setValues(cs.getHyperlinkReferenceExpression(), value));
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION))
			cs.setHyperlinkTooltipExpression(ExprUtil.setValues(cs.getHyperlinkTooltipExpression(), value));
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION)) {
			cs.setHyperlinkWhenExpression(ExprUtil.setValues(cs.getHyperlinkWhenExpression(), value));
		} else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS)) {
			if (value instanceof ParameterDTO) {
				ParameterDTO v = (ParameterDTO) value;

				for (JRHyperlinkParameter prm : propertyDTO.getValue())
					cs.removeHyperlinkParameter(prm);

				for (JRHyperlinkParameter param : v.getValue())
					cs.addHyperlinkParameter(param);

				propertyDTO = v;
			}
		} else if (id.equals(SpiderChartComponent.PROPERTY_EVALUATION_TIME))
			component.setEvaluationTime((EvaluationTimeEnum) EnumHelper.getSetValue(EvaluationTimeEnum.values(), value, 1, false));
		else if (id.equals(SpiderChartComponent.PROPERTY_EVALUATION_GROUP)) {
			component.setEvaluationGroup((String) value);
		} else
			super.setPropertyValue(id, value);
	}

	@Override
	protected void setGroupItems(String[] items) {
		super.setGroupItems(items);
		if (evaluationGroupNameD != null)
			evaluationGroupNameD.setItems(items);
	}

	private RComboBoxPropertyDescriptor evaluationGroupNameD;
	private static JSSEnumPropertyDescriptor titlePositionD;
	private static JSSEnumPropertyDescriptor legendPositionD;
	private static JSSEnumPropertyDescriptor evaluationTimeD;
	private static JSSEnumPropertyDescriptor rotation;
	private static JSSEnumPropertyDescriptor tableOrder;

	@Override
	public void setValue(Object value) {
		if (getValue() != null) {
			SpiderChartComponent obj = getComponent();
			if (obj instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport().removePropertyChangeListener(this);
			((StandardChartSettings) obj.getChartSettings()).getEventSupport().removePropertyChangeListener(this);
			((StandardSpiderPlot) obj.getPlot()).getEventSupport().removePropertyChangeListener(this);
		}
		if (value != null) {
			SpiderChartComponent obj = getComponent(value);
			if (value instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport().addPropertyChangeListener(this);
			((StandardChartSettings) obj.getChartSettings()).getEventSupport().addPropertyChangeListener(this);
			((StandardSpiderPlot) obj.getPlot()).getEventSupport().addPropertyChangeListener(this);
		}
		super.setValue(value);
	}

	private SpiderChartComponent getComponent() {
		return getComponent(getValue());
	}

	private SpiderChartComponent getComponent(Object value) {
		if (value != null) {
			JRDesignComponentElement jrElement = (JRDesignComponentElement) value;
			return (SpiderChartComponent) jrElement.getComponent();
		}
		return null;
	}

	@Override
	public int getDefaultHeight() {
		Object defaultValue = DefaultManager.INSTANCE.getDefaultPropertiesValue(this.getClass(), JRDesignElement.PROPERTY_HEIGHT);
		return defaultValue != null ? (Integer)defaultValue : 200;
	}

	@Override
	public int getDefaultWidth() {
		Object defaultValue = DefaultManager.INSTANCE.getDefaultPropertiesValue(this.getClass(), JRDesignElement.PROPERTY_WIDTH);
		return defaultValue != null ? (Integer)defaultValue : 200;
	}


	@Override
	public JRDesignComponentElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement jrcomponent = new JRDesignComponentElement();
		jrcomponent.setWidth(getDefaultWidth());
		jrcomponent.setHeight(getDefaultHeight());

		SpiderChartComponent component = new SpiderChartComponent();
		component.setChartSettings(new StandardChartSettings());
		component.setDataset(new StandardSpiderDataset());
		StandardSpiderPlot spiderPlot = new StandardSpiderPlot();
		spiderPlot.setWebFilled(true);
		component.setPlot(spiderPlot);

		jrcomponent.setComponent(component);
		jrcomponent.setComponentKey(new ComponentKey("http://jasperreports.sourceforge.net/jasperreports/components", "sc", "spiderChart")); //$NON-NLS-1$
		
		DefaultManager.INSTANCE.applyDefault(this.getClass(), jrcomponent);
		
		return jrcomponent;
	}

	@Override
	public List<MDatasetRun> getDatasetRunList() {
		if (getValue() != null) {
			JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
			SpiderChartComponent component = (SpiderChartComponent) jrElement.getComponent();
			if (component != null && component.getDataset() != null) {
				List<MDatasetRun> datasetList = new ArrayList<MDatasetRun>();
				MDatasetRun mDatasetRun = new MDatasetRun(component.getDataset().getDatasetRun(), getJasperDesign());
				mDatasetRun.setJasperConfiguration(getJasperConfiguration());
				datasetList.add(mDatasetRun);
				return datasetList;
			}
		}
		return null;
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRDesignComponentElement jrSourceElement = (JRDesignComponentElement) getValue();
		SpiderChartComponent jrSourceComponent = (SpiderChartComponent) jrSourceElement.getComponent();
		StandardChartSettings jrSourceSetting = (StandardChartSettings) jrSourceComponent.getChartSettings();
		StandardSpiderPlot jrSourcePlot = (StandardSpiderPlot) jrSourceComponent.getPlot();
		
		JRDesignComponentElement jrTargetElement = (JRDesignComponentElement) getValue();
		SpiderChartComponent jrTargetComponent = (SpiderChartComponent) jrTargetElement.getComponent();
		StandardChartSettings jrTargetSetting = (StandardChartSettings) jrTargetComponent.getChartSettings();
		StandardSpiderPlot jrTargetPlot = (StandardSpiderPlot) jrTargetComponent.getPlot();
		
		jrTargetPlot.setAxisLineColor(getColorClone(jrSourcePlot.getAxisLineColor()));
		jrTargetPlot.setAxisLineWidth(jrSourcePlot.getAxisLineWidth());
		jrTargetPlot.setLabelColor(getColorClone(jrSourcePlot.getLabelColor()));
		jrTargetPlot.setLabelGap(jrSourcePlot.getLabelGap());
		jrTargetPlot.setLabelFont(getFontClone(jrSourcePlot.getLabelFont()));
		jrTargetPlot.setRotation(jrSourcePlot.getRotation());
		jrTargetPlot.setTableOrder(jrSourcePlot.getTableOrder());
		jrTargetPlot.setWebFilled(jrSourcePlot.getWebFilled());
		jrTargetPlot.setStartAngle(jrSourcePlot.getStartAngle());
		jrTargetPlot.setInteriorGap(jrSourcePlot.getInteriorGap());
		jrTargetPlot.setHeadPercent(jrSourcePlot.getHeadPercent());
		
		jrTargetSetting.setTitleFont(getFontClone(jrSourceSetting.getTitleFont()));
		jrTargetSetting.setSubtitleFont(getFontClone(jrSourceSetting.getSubtitleFont()));
		jrTargetSetting.setLegendFont(getFontClone(jrSourceSetting.getLegendFont()));
		jrTargetSetting.setTitlePosition(jrSourceSetting.getTitlePosition());
		jrTargetSetting.setLegendPosition(jrSourceSetting.getLegendPosition());
		jrTargetSetting.setShowLegend(jrSourceSetting.getShowLegend());
		jrTargetSetting.setRenderType(jrSourceSetting.getRenderType());
		jrTargetSetting.setTitleColor(getColorClone(jrSourceSetting.getTitleColor()));
		jrTargetSetting.setLegendColor(getColorClone(jrSourceSetting.getLegendColor()));
		jrTargetSetting.setSubtitleColor(getColorClone(jrSourceSetting.getSubtitleColor()));
		jrTargetSetting.setLegendBackgroundColor(getColorClone(jrSourceSetting.getLegendBackgroundColor()));
	}
}
