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
package com.jaspersoft.studio.components.chart.model;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignBar3DPlot;
import net.sf.jasperreports.charts.design.JRDesignBarPlot;
import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignDataRange;
import net.sf.jasperreports.charts.design.JRDesignGanttDataset;
import net.sf.jasperreports.charts.design.JRDesignGanttSeries;
import net.sf.jasperreports.charts.design.JRDesignHighLowDataset;
import net.sf.jasperreports.charts.design.JRDesignItemLabel;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.charts.design.JRDesignPie3DPlot;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.charts.design.JRDesignPiePlot;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodDataset;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodSeries;
import net.sf.jasperreports.charts.design.JRDesignTimeSeries;
import net.sf.jasperreports.charts.design.JRDesignTimeSeriesDataset;
import net.sf.jasperreports.charts.design.JRDesignValueDataset;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;
import net.sf.jasperreports.charts.design.JRDesignXyDataset;
import net.sf.jasperreports.charts.design.JRDesignXySeries;
import net.sf.jasperreports.charts.design.JRDesignXyzDataset;
import net.sf.jasperreports.charts.design.JRDesignXyzSeries;
import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.charts.type.TimePeriodEnum;
import net.sf.jasperreports.charts.type.ValueLocationEnum;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseChart;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.CollectionElementAddedEvent;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.ChartNodeIconDescriptor;
import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.plot.MChartPlot;
import com.jaspersoft.studio.components.chart.model.plot.PlotFactory;
import com.jaspersoft.studio.components.chart.property.descriptor.PlotPropertyDescriptor;
import com.jaspersoft.studio.components.chart.util.ChartHelper;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.series.CategorySerie;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.series.GanttSeries;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.series.PieSerie;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.series.TimePeriodSerie;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.series.TimeSerie;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.series.XySerie;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.series.XyzSerie;
import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.IContainerEditPart;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.IPastable;
import com.jaspersoft.studio.model.MGraphicElementLineBox;
import com.jaspersoft.studio.model.MHyperLink;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.model.text.MFontUtil;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.classname.NClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.dialog.ParameterDTO;
import com.jaspersoft.studio.property.descriptor.text.FontPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.EdgePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.SpinnerPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.Misc;

/*
 * The Class MChart.
 */
public class MChart extends MGraphicElementLineBox implements IContainer, IContainerEditPart, IPastable, IDatasetContainer {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String PLOTPROPERTY = "PLOTPROPERTY";
	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new ChartNodeIconDescriptor("chart"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m chart.
	 */
	public MChart() {
		super();
	}

	public MChart(ANode parent, int newIndex) {
		super(parent, newIndex);
	}

	/**
	 * Instantiates a new m chart.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrChart
	 *          the jr chart
	 * @param newIndex
	 *          the new index
	 */
	public MChart(ANode parent, JRChart jrChart, int newIndex) {
		super(parent, newIndex);
		setValue(jrChart);
	}

	@Override
	public JRDesignChart getValue() {
		return (JRDesignChart) super.getValue();
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

		titlePositionD = new EdgePropertyDescriptor(JRBaseChart.PROPERTY_TITLE_POSITION, Messages.MChart_title_position, EdgeEnum.class, NullEnum.NULL);
		titlePositionD.setDescription(Messages.MChart_title_position_description);
		titlePositionD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chartTitle_position"));
		desc.add(titlePositionD);

		evaluationTimeD = new JSSEnumPropertyDescriptor(JRDesignChart.PROPERTY_EVALUATION_TIME, Messages.MChart_evaluation_time, EvaluationTimeEnum.class, NullEnum.NOTNULL);
		evaluationTimeD.setDescription(Messages.MChart_evaluation_time_description);
		desc.add(evaluationTimeD);

		NClassTypePropertyDescriptor classD = new NClassTypePropertyDescriptor(JRDesignChart.PROPERTY_CUSTOMIZER_CLASS, Messages.MChart_customizer_class);
		classD.setDescription(Messages.MChart_customizer_class_description);
		desc.add(classD);

		CheckBoxPropertyDescriptor showLegendD = new CheckBoxPropertyDescriptor(JRBaseChart.PROPERTY_SHOW_LEGEND, Messages.MChart_show_legend, NullEnum.NULL);
		showLegendD.setDescription(Messages.MChart_show_legend_description);
		desc.add(showLegendD);

		RWComboBoxPropertyDescriptor rendererTypeD = new RWComboBoxPropertyDescriptor(JRBaseChart.PROPERTY_RENDER_TYPE, Messages.MChart_renderer_type,
				new String[] { "", "draw", "image", "svg" }, NullEnum.NULL); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		rendererTypeD.setDescription(Messages.MChart_renderer_type_description);
		desc.add(rendererTypeD);

		RWComboBoxPropertyDescriptor themeD = new RWComboBoxPropertyDescriptor(JRBaseChart.PROPERTY_THEME, Messages.MChart_theme, ChartHelper.getChartThemesNull(), NullEnum.NULL);
		themeD.setDescription(Messages.MChart_theme_description);
		desc.add(themeD);

		evaluationGroupD = new RComboBoxPropertyDescriptor(JRDesignChart.PROPERTY_EVALUATION_GROUP, Messages.MChart_evaluation_group, new String[] { "" }); //$NON-NLS-1$
		evaluationGroupD.setDescription(Messages.MChart_evaluation_group_description);
		desc.add(evaluationGroupD);

		PlotPropertyDescriptor plotD = new PlotPropertyDescriptor(PLOTPROPERTY, Messages.MChart_plot); //$NON-NLS-1$
		plotD.setDescription(Messages.MChart_plot_description);
		desc.add(plotD);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chart");

		if (mHyperLink == null)
			mHyperLink = new MHyperLink(null);
		mHyperLink.createPropertyDescriptors(desc, defaultsMap);
		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chart");

		FontPropertyDescriptor titleFontD = new FontPropertyDescriptor(JRDesignChart.PROPERTY_TITLE_FONT, Messages.MChart_title_font);
		titleFontD.setDescription(Messages.MChart_title_font_description);
		desc.add(titleFontD);
		titleFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#font"));

		ColorPropertyDescriptor titleColorD = new ColorPropertyDescriptor(JRBaseChart.PROPERTY_TITLE_COLOR, Messages.MChart_title_color, NullEnum.INHERITED);
		titleColorD.setDescription(Messages.MChart_title_color_description);
		desc.add(titleColorD);

		JRExpressionPropertyDescriptor titleExprD = new JRExpressionPropertyDescriptor(JRDesignChart.PROPERTY_TITLE_EXPRESSION, Messages.MChart_title_expression);
		titleExprD.setDescription(Messages.MChart_title_expression_description);
		desc.add(titleExprD);
		titleExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#titleExpression"));

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chartTitle");

		FontPropertyDescriptor subtitleFontD = new FontPropertyDescriptor(JRDesignChart.PROPERTY_SUBTITLE_FONT, Messages.MChart_subtitle_font);
		subtitleFontD.setDescription(Messages.MChart_subtitle_font_description);
		desc.add(subtitleFontD);
		subtitleFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#font"));

		ColorPropertyDescriptor subtitleColorD = new ColorPropertyDescriptor(JRBaseChart.PROPERTY_SUBTITLE_COLOR, Messages.MChart_subtitle_color, NullEnum.INHERITED);
		subtitleColorD.setDescription(Messages.MChart_subtitle_color_description);
		desc.add(subtitleColorD);

		JRExpressionPropertyDescriptor subtitleExprD = new JRExpressionPropertyDescriptor(JRDesignChart.PROPERTY_SUBTITLE_EXPRESSION, Messages.MChart_subtitle_expression);
		subtitleExprD.setDescription(Messages.MChart_subtitle_expression_description);
		desc.add(subtitleExprD);
		subtitleExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#subtitleExpression"));

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chartSubtitle");

		FontPropertyDescriptor legendFontD = new FontPropertyDescriptor(JRDesignChart.PROPERTY_LEGEND_FONT, Messages.MChart_legend_font);
		legendFontD.setDescription(Messages.MChart_legend_font_description);
		desc.add(legendFontD);
		legendFontD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#font"));

		ColorPropertyDescriptor legendColorD = new ColorPropertyDescriptor(JRBaseChart.PROPERTY_LEGEND_COLOR, Messages.MChart_legend_color, NullEnum.INHERITED);
		legendColorD.setDescription(Messages.MChart_legend_color_description);
		desc.add(legendColorD);

		ColorPropertyDescriptor legendBackColorD = new ColorPropertyDescriptor(JRBaseChart.PROPERTY_LEGEND_BACKGROUND_COLOR, Messages.MChart_legend_background_color, NullEnum.INHERITED);
		legendBackColorD.setDescription(Messages.MChart_legend_background_color_description);
		desc.add(legendBackColorD);

		legendPositionD = new EdgePropertyDescriptor(JRBaseChart.PROPERTY_LEGEND_POSITION, Messages.MChart_legend_position, EdgeEnum.class, NullEnum.NULL);
		legendPositionD.setDescription(Messages.MChart_legend_position_description);
		legendPositionD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chartLegend_position"));
		desc.add(legendPositionD);

		JRExpressionPropertyDescriptor anchorNameExp = new JRExpressionPropertyDescriptor(JRDesignChart.PROPERTY_ANCHOR_NAME_EXPRESSION, com.jaspersoft.studio.messages.Messages.MTextField_anchorNameLabel);
		anchorNameExp.setHelpRefBuilder(new HelpReferenceBuilder("http://127.0.0.1:55429/help/topic/net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#anchorNameExpression")); //$NON-NLS-1$
		anchorNameExp.setDescription(com.jaspersoft.studio.messages.Messages.MTextField_anchorNameDescription);
		desc.add(anchorNameExp);

		SpinnerPropertyDescriptor bookmarkLevel = new SpinnerPropertyDescriptor(JRDesignChart.PROPERTY_BOOKMARK_LEVEL, com.jaspersoft.studio.messages.Messages.MTextField_bookmarkLevelLabel);
		bookmarkLevel.setDescription(com.jaspersoft.studio.messages.Messages.MTextField_bookmarkLevelDescription);
		bookmarkLevel.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chart_bookmarkLevel")); //$NON-NLS-1$
		desc.add(bookmarkLevel);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#chartLegend");

		titleFontD.setCategory(Messages.MChart_chart_title_category);
		titleColorD.setCategory(Messages.MChart_chart_title_category);
		titlePositionD.setCategory(Messages.MChart_chart_title_category);
		titleExprD.setCategory(Messages.MChart_chart_title_category);

		subtitleFontD.setCategory(Messages.MChart_chart_subtitle_category);
		subtitleExprD.setCategory(Messages.MChart_chart_subtitle_category);
		subtitleColorD.setCategory(Messages.MChart_chart_subtitle_category);

		plotD.setCategory(Messages.MChart_common_chart_properties_category);

		evaluationGroupD.setCategory(Messages.MChart_common_chart_properties_category);
		themeD.setCategory(Messages.MChart_common_chart_properties_category);

		classD.setCategory(Messages.MChart_common_chart_properties_category);

		legendFontD.setCategory(Messages.MChart_chart_legend_category);
		legendBackColorD.setCategory(Messages.MChart_chart_legend_category);
		legendColorD.setCategory(Messages.MChart_chart_legend_category);
		legendPositionD.setCategory(Messages.MChart_chart_legend_category);
		showLegendD.setCategory(Messages.MChart_chart_legend_category);

		evaluationTimeD.setCategory(Messages.MChart_common_chart_properties_category);

		rendererTypeD.setCategory(Messages.MChart_common_chart_properties_category);

		defaultsMap.put(JRBaseChart.PROPERTY_THEME, null);
		defaultsMap.put(JRDesignChart.PROPERTY_CUSTOMIZER_CLASS, null);
		defaultsMap.put(JRBaseChart.PROPERTY_SHOW_LEGEND, new Boolean(true));
		defaultsMap.put(JRBaseChart.PROPERTY_TITLE_COLOR, null);
		defaultsMap.put(JRBaseChart.PROPERTY_SUBTITLE_COLOR, null);
		defaultsMap.put(JRBaseChart.PROPERTY_LEGEND_COLOR, null);
		defaultsMap.put(JRBaseChart.PROPERTY_LEGEND_BACKGROUND_COLOR, null);

		defaultsMap.put(JRDesignChart.PROPERTY_TITLE_FONT, null);
		defaultsMap.put(JRDesignChart.PROPERTY_SUBTITLE_FONT, null);
		defaultsMap.put(JRDesignChart.PROPERTY_LEGEND_FONT, null);

		defaultsMap.put(JRBaseChart.PROPERTY_TITLE_POSITION, null);
		defaultsMap.put(JRBaseChart.PROPERTY_LEGEND_POSITION, null);
		defaultsMap.put(JRDesignChart.PROPERTY_EVALUATION_TIME, EnumHelper.getValue(EvaluationTimeEnum.NOW, 1, false));
	}

	@Override
	public void setGroupItems(String[] items) {
		super.setGroupItems(items);
		if (evaluationGroupD != null)
			evaluationGroupD.setItems(items);
	}

	private RComboBoxPropertyDescriptor evaluationGroupD;
	private MChartPlot mChartPlot;
	private MFont tFont;
	private MFont stFont;
	private MFont lFont;

	private MHyperLink mHyperLink;

	private ParameterDTO propertyDTO;
	private static EdgePropertyDescriptor titlePositionD;
	private static EdgePropertyDescriptor legendPositionD;
	private static JSSEnumPropertyDescriptor evaluationTimeD;

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignChart jrElement = (JRDesignChart) getValue();

		if (id.equals(JRBaseChart.PROPERTY_TITLE_POSITION))
			return titlePositionD.getEnumValue(jrElement.getTitlePositionValue());
		if (id.equals(JRBaseChart.PROPERTY_LEGEND_POSITION))
			return legendPositionD.getEnumValue(jrElement.getLegendPositionValue());
		if (id.equals(JRDesignChart.PROPERTY_EVALUATION_TIME))
			return evaluationTimeD.getEnumValue(jrElement.getEvaluationTimeValue());
		if (id.equals(JRBaseChart.PROPERTY_RENDER_TYPE))
			return jrElement.getRenderType();
		if (id.equals(JRBaseChart.PROPERTY_THEME))
			return jrElement.getTheme();
		if (id.equals(JRDesignChart.PROPERTY_EVALUATION_GROUP)) {
			if (jrElement.getEvaluationGroup() != null)
				return jrElement.getEvaluationGroup().getName();
			return ""; //$NON-NLS-1$
		}
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS)) {
			if (propertyDTO == null) {
				propertyDTO = new ParameterDTO();
				propertyDTO.setJasperDesign(getJasperDesign());
				propertyDTO.setValue(jrElement.getHyperlinkParameters());
			}
			return propertyDTO;
		}
		if (id.equals(JRBaseChart.PROPERTY_TITLE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnTitleColor());
		if (id.equals(JRBaseChart.PROPERTY_SUBTITLE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnSubtitleColor());
		if (id.equals(JRBaseChart.PROPERTY_LEGEND_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnLegendColor());
		if (id.equals(JRBaseChart.PROPERTY_LEGEND_BACKGROUND_COLOR))
			return Colors.getSWTRGB4AWTGBColor(jrElement.getOwnLegendBackgroundColor());
		if (id.equals(JRBaseChart.PROPERTY_SHOW_LEGEND))
			return jrElement.getShowLegend();

		if (id.equals(JRDesignChart.PROPERTY_CUSTOMIZER_CLASS))
			return jrElement.getCustomizerClass();

		// hyperlink --------------------------------------
		if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TARGET))
			return jrElement.getLinkTarget();
		if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TYPE))
			return jrElement.getLinkType();
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getHyperlinkAnchorExpression());
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getHyperlinkPageExpression());
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getHyperlinkReferenceExpression());
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getHyperlinkTooltipExpression());
		if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION)) {
			return ExprUtil.getExpression(jrElement.getHyperlinkWhenExpression());
		}
		if (id.equals(JRDesignChart.PROPERTY_ANCHOR_NAME_EXPRESSION)) {
			return ExprUtil.getExpression(jrElement.getAnchorNameExpression());
		}
		if (id.equals(JRDesignChart.PROPERTY_BOOKMARK_LEVEL)) {
			return jrElement.getBookmarkLevel();
		}

		if (id.equals(PLOTPROPERTY)) { //$NON-NLS-1$
			if (mChartPlot == null) {
				mChartPlot = PlotFactory.getChartPlot(jrElement.getPlot());
				setChildListener(mChartPlot);
			}
			mChartPlot.setJasperConfiguration(getJasperConfiguration());
			return mChartPlot;
		}
		if (id.equals(JRDesignChart.PROPERTY_TITLE_FONT)) {
			tFont = MFontUtil.getMFont(tFont, jrElement.getTitleFont(), jrElement.getStyle(), this);
			return tFont;
		}
		if (id.equals(JRDesignChart.PROPERTY_SUBTITLE_FONT)) {
			stFont = MFontUtil.getMFont(stFont, jrElement.getSubtitleFont(), jrElement.getStyle(), this);
			return stFont;
		}
		if (id.equals(JRDesignChart.PROPERTY_LEGEND_FONT)) {
			lFont = MFontUtil.getMFont(lFont, jrElement.getLegendFont(), jrElement.getStyle(), this);
			return lFont;
		}

		if (id.equals(JRDesignChart.PROPERTY_TITLE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getTitleExpression());
		if (id.equals(JRDesignChart.PROPERTY_SUBTITLE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getSubtitleExpression());

		return super.getPropertyValue(id);
	}

	@Override
	public Object getPropertyActualValue(Object id) {
		JRDesignChart jrElement = (JRDesignChart) getValue();
		if (id.equals(JRBaseChart.PROPERTY_TITLE_POSITION)) {
			EdgeEnum position = jrElement.getTitlePositionValue();
			return titlePositionD.getEnumValue(position != null ? position : EdgeEnum.TOP);
		}
		if (id.equals(JRBaseChart.PROPERTY_LEGEND_POSITION)) {
			EdgeEnum position = jrElement.getLegendPositionValue();
			return legendPositionD.getEnumValue(position != null ? position : EdgeEnum.BOTTOM);
		}
		if (id.equals(JRBaseChart.PROPERTY_TITLE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(JRStyleResolver.getTitleColor(jrElement));
		if (id.equals(JRBaseChart.PROPERTY_SUBTITLE_COLOR))
			return Colors.getSWTRGB4AWTGBColor(JRStyleResolver.getSubtitleColor(jrElement));
		if (id.equals(JRBaseChart.PROPERTY_LEGEND_COLOR))
			return Colors.getSWTRGB4AWTGBColor(JRStyleResolver.getLegendColor(jrElement));
		if (id.equals(JRBaseChart.PROPERTY_LEGEND_BACKGROUND_COLOR))
			return Colors.getSWTRGB4AWTGBColor(JRStyleResolver.getLegendBackgroundColor(jrElement));
		if (id.equals(JRBaseChart.PROPERTY_SHOW_LEGEND))
			return jrElement.getShowLegend() != null ? jrElement.getShowLegend() : true;
		if (id.equals(JRDesignChart.PROPERTY_TITLE_FONT)) {
			tFont = MFontUtil.getMFont(tFont, jrElement.getTitleFont(), jrElement.getStyle(), this);
			return tFont;
		}
		return super.getPropertyActualValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignChart jrElement = (JRDesignChart) getValue();

		if (id.equals(JRDesignChart.PROPERTY_TITLE_FONT)) {
			jrElement.setTitleFont(MFontUtil.setMFont(value));
		} else if (id.equals(JRDesignChart.PROPERTY_SUBTITLE_FONT)) {
			jrElement.setSubtitleFont(MFontUtil.setMFont(value));
		} else if (id.equals(JRDesignChart.PROPERTY_LEGEND_FONT)) {
			jrElement.setLegendFont(MFontUtil.setMFont(value));
		} else if (id.equals(JRBaseChart.PROPERTY_TITLE_POSITION))
			jrElement.setTitlePosition((EdgeEnum) titlePositionD.getEnumValue(value));
		else if (id.equals(JRBaseChart.PROPERTY_LEGEND_POSITION))
			jrElement.setLegendPosition((EdgeEnum) legendPositionD.getEnumValue(value));
		else if (id.equals(JRDesignChart.PROPERTY_EVALUATION_TIME))
			jrElement.setEvaluationTime((EvaluationTimeEnum) evaluationTimeD.getEnumValue(value));
		else if (id.equals(JRBaseChart.PROPERTY_SHOW_LEGEND))
			jrElement.setShowLegend((Boolean) value);
		else if (id.equals(JRBaseChart.PROPERTY_RENDER_TYPE)) {
			value = Misc.nullValue((String) value);
			jrElement.setRenderType((String) value);
		} else if (id.equals(JRBaseChart.PROPERTY_THEME)) {
			value = Misc.nullValue((String) value);
			jrElement.setTheme((String) value);
		} else if (id.equals(JRDesignChart.PROPERTY_EVALUATION_GROUP)) {
			if (value!=null && !value.equals("")) { //$NON-NLS-1$
				JRGroup group = (JRGroup) getJasperDesign().getGroupsMap().get(value);
				jrElement.setEvaluationGroup(group);
			}
			else {
				jrElement.setEvaluationGroup(null);
			}
		}

		else if (id.equals(JRBaseChart.PROPERTY_TITLE_COLOR)) {
			if (value instanceof AlfaRGB)
				jrElement.setTitleColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(JRBaseChart.PROPERTY_SUBTITLE_COLOR)) {
			if (value instanceof AlfaRGB)
				jrElement.setSubtitleColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(JRBaseChart.PROPERTY_LEGEND_COLOR)) {
			if (value instanceof AlfaRGB)
				jrElement.setLegendColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(JRBaseChart.PROPERTY_LEGEND_BACKGROUND_COLOR)) {
			if (value instanceof AlfaRGB)
				jrElement.setLegendBackgroundColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		} else if (id.equals(JRDesignChart.PROPERTY_CUSTOMIZER_CLASS)) {
			value = Misc.nullValue((String) value);
			jrElement.setCustomizerClass((String) value);
		} else if (id.equals(JRDesignChart.PROPERTY_TITLE_EXPRESSION)) {
			jrElement.setTitleExpression(ExprUtil.setValues(jrElement.getTitleExpression(), value));
		} else if (id.equals(JRDesignChart.PROPERTY_SUBTITLE_EXPRESSION)) {
			jrElement.setSubtitleExpression(ExprUtil.setValues(jrElement.getSubtitleExpression(), value));
		} else if (id.equals(JRDesignChart.PROPERTY_ANCHOR_NAME_EXPRESSION))
			jrElement.setAnchorNameExpression(ExprUtil.setValues(jrElement.getAnchorNameExpression(), value));
		else if (id.equals(JRDesignChart.PROPERTY_BOOKMARK_LEVEL))
			jrElement.setBookmarkLevel(value != null ? Integer.parseInt(value.toString()) : 0);
		else if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TARGET))
			jrElement.setLinkTarget((String) value);
		else if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TYPE))
			jrElement.setLinkType((String) value);
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION))
			jrElement.setHyperlinkAnchorExpression(ExprUtil.setValues(jrElement.getHyperlinkAnchorExpression(), value));
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION))
			jrElement.setHyperlinkPageExpression(ExprUtil.setValues(jrElement.getHyperlinkPageExpression(), value));
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION))
			jrElement.setHyperlinkReferenceExpression(ExprUtil.setValues(jrElement.getHyperlinkReferenceExpression(), value));
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION))
			jrElement.setHyperlinkTooltipExpression(ExprUtil.setValues(jrElement.getHyperlinkTooltipExpression(), value));
		else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION)) {
			jrElement.setHyperlinkWhenExpression(ExprUtil.setValues(jrElement.getHyperlinkWhenExpression(), value));
		} else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS)) {
			if (value instanceof ParameterDTO) {
				ParameterDTO v = (ParameterDTO) value;

				for (JRHyperlinkParameter prm : propertyDTO.getValue())
					jrElement.removeHyperlinkParameter(prm);

				for (JRHyperlinkParameter param : v.getValue())
					jrElement.addHyperlinkParameter(param);

				propertyDTO = v;
			}
		} else
			super.setPropertyValue(id, value);
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

	public static JRDesignChart createJRElement(JasperDesign jasperDesign, byte chartType) {
		JRDesignChart jrChart = new JRDesignChart(jasperDesign, chartType);
		setupChart(jrChart);
			
		DefaultManager.INSTANCE.applyDefault(MChart.class, jrChart);
		
		return jrChart;
	}

	public static void setupChart(JRDesignChart jrChart) {
		jrChart.setEvaluationTime(EvaluationTimeEnum.REPORT);
		if (jrChart.getChartType() == JRDesignChart.CHART_TYPE_XYBAR)
			jrChart.setDataset(new JRDesignXyDataset(null));
		// dataset initialisation
		if (jrChart.getDataset() instanceof JRDesignHighLowDataset) {
			JRDesignHighLowDataset jds = (JRDesignHighLowDataset) jrChart.getDataset();
			if (jds.getCloseExpression() == null)
				jds.setCloseExpression(ExprUtil.setValues(new JRDesignExpression(), "100"));
			if (jds.getOpenExpression() == null)
				jds.setOpenExpression(ExprUtil.setValues(new JRDesignExpression(), "100"));
			if (jds.getVolumeExpression() == null)
				jds.setVolumeExpression(ExprUtil.setValues(new JRDesignExpression(), "100"));
			if (jds.getHighExpression() == null)
				jds.setHighExpression(ExprUtil.setValues(new JRDesignExpression(), "100"));
			if (jds.getLowExpression() == null)
				jds.setLowExpression(ExprUtil.setValues(new JRDesignExpression(), "100"));
			if (jds.getDateExpression() == null)
				jds.setDateExpression(ExprUtil.setValues(new JRDesignExpression(), "new Date()"));
			jds.setSeriesExpression(new JRDesignExpression("\"CHANGE_ME\""));
		} else if (jrChart.getDataset() instanceof JRDesignPieDataset) {
			JRDesignPieSeries pieSeries = new PieSerie().createSerie();
			((JRDesignPieDataset) jrChart.getDataset()).addPieSeries(pieSeries);
		} else if (jrChart.getDataset() instanceof JRDesignCategoryDataset) {
			JRCategorySeries catSeries = new CategorySerie().createSerie();
			((JRDesignCategoryDataset) jrChart.getDataset()).addCategorySeries(catSeries);
		} else if (jrChart.getDataset() instanceof JRDesignValueDataset) {
			JRDesignValueDataset valueDataset = (JRDesignValueDataset) jrChart.getDataset();
			if (jrChart.getChartType() == JRDesignChart.CHART_TYPE_METER) {
				valueDataset.setValueExpression(new JRDesignExpression("50"));
			} else {
				valueDataset.setValueExpression(new JRDesignExpression("\"CHANGE_ME\""));
			}
		} else if (jrChart.getDataset() instanceof JRDesignXyDataset) {
			JRDesignXySeries series = new XySerie().createSerie();
			((JRDesignXyDataset) jrChart.getDataset()).addXySeries(series);
		} else if (jrChart.getDataset() instanceof JRDesignXyzDataset) {
			JRDesignXyzSeries series = new XyzSerie().createSerie();
			((JRDesignXyzDataset) jrChart.getDataset()).addXyzSeries(series);
		} else if (jrChart.getDataset() instanceof JRDesignTimeSeriesDataset) {
			JRDesignTimeSeries series = new TimeSerie().createSerie();
			((JRDesignTimeSeriesDataset) jrChart.getDataset()).addTimeSeries(series);
			((JRDesignTimeSeriesDataset) jrChart.getDataset()).setTimePeriod(TimePeriodEnum.DAY.getTimePeriod());
		} else if (jrChart.getDataset() instanceof JRDesignTimePeriodDataset) {
			JRDesignTimePeriodSeries series = new TimePeriodSerie().createSerie();
			((JRDesignTimePeriodDataset) jrChart.getDataset()).addTimePeriodSeries(series);
		} else if (jrChart.getDataset() instanceof JRDesignGanttDataset) {
			JRDesignGanttSeries series = new GanttSeries().createSerie();
			((JRDesignGanttDataset) jrChart.getDataset()).addGanttSeries(series);
		}
		// plot initialisation
		JRChartPlot plot = jrChart.getPlot();
		if (plot instanceof JRDesignBar3DPlot) {
			JRDesignBar3DPlot jrPlot = (JRDesignBar3DPlot) plot;
			if (jrPlot.getItemLabel() == null || !(jrPlot.getItemLabel() instanceof JRDesignItemLabel))
				jrPlot.setItemLabel(new JRDesignItemLabel(null, jrChart));
		} else if (plot instanceof JRDesignPiePlot) {
			JRDesignPiePlot jrPlot = (JRDesignPiePlot) plot;
			if (jrPlot.getItemLabel() == null || !(jrPlot.getItemLabel() instanceof JRDesignItemLabel))
				jrPlot.setItemLabel(new JRDesignItemLabel(null, jrChart));
		} else if (plot instanceof JRDesignPie3DPlot) {
			JRDesignPie3DPlot jrPlot = (JRDesignPie3DPlot) plot;
			if (jrPlot.getItemLabel() == null || !(jrPlot.getItemLabel() instanceof JRDesignItemLabel))
				jrPlot.setItemLabel(new JRDesignItemLabel(null, jrChart));
		} else if (plot instanceof JRDesignBarPlot) {
			JRDesignBarPlot jrPlot = (JRDesignBarPlot) plot;
			if (jrPlot.getItemLabel() == null || !(jrPlot.getItemLabel() instanceof JRDesignItemLabel))
				jrPlot.setItemLabel(new JRDesignItemLabel(null, jrChart));
		} else if (plot instanceof JRDesignThermometerPlot) {
			JRDesignThermometerPlot tplot = (JRDesignThermometerPlot) plot;
			if (tplot.getHighRange() == null)
				tplot.setHighRange(getDummyDataRange());
			if (tplot.getLowRange() == null)
				tplot.setLowRange(getDummyDataRange());
			if (tplot.getMediumRange() == null)
				tplot.setMediumRange(getDummyDataRange());
			if (tplot.getDataRange() == null) {
				tplot.setDataRange(getDummyDataRange());
			}
			if (tplot.getValueLocationValue() == null)
				tplot.setValueLocation(ValueLocationEnum.LEFT);
			if (tplot.getValueDisplay() == null)
				tplot.setValueDisplay(new JRDesignValueDisplay(null, jrChart));
		} else if (plot instanceof JRDesignMeterPlot) {
			JRDesignMeterPlot tplot = (JRDesignMeterPlot) plot;
			try {
				if (tplot.getValueDisplay() == null)
					tplot.setValueDisplay(new JRDesignValueDisplay(null, jrChart));
				if (tplot.getDataRange() == null)
					tplot.setDataRange(getDummyDataRange());
			} catch (JRException e) {
				e.printStackTrace();
			}
		} else if (plot instanceof JRDesignMultiAxisPlot) {
			((JRDesignMultiAxisPlot) plot).setChart(jrChart);
		}
	}

	private static JRDesignDataRange getDummyDataRange() {
		JRDesignDataRange dataRange = new JRDesignDataRange(null);
		dataRange.setHighExpression(new JRDesignExpression("100"));
		dataRange.setLowExpression(new JRDesignExpression("1"));
		return dataRange;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		if (getValue() != null) {
			JRDesignChart chart = (JRDesignChart) getValue();
			String chartype = "";
			switch (chart.getChartType()) {
			case JRDesignChart.CHART_TYPE_AREA:
				chartype = "Area";
				break;
			case JRDesignChart.CHART_TYPE_BAR:
				chartype = "Bar";
				break;
			case JRDesignChart.CHART_TYPE_BAR3D:
				chartype = "Bar 3D";
				break;
			case JRDesignChart.CHART_TYPE_BUBBLE:
				chartype = "Bubble";
				break;
			case JRDesignChart.CHART_TYPE_CANDLESTICK:
				chartype = "Candlestick";
				break;
			case JRDesignChart.CHART_TYPE_HIGHLOW:
				chartype = "High Low";
				break;
			case JRDesignChart.CHART_TYPE_LINE:
				chartype = "Line";
				break;
			case JRDesignChart.CHART_TYPE_METER:
				chartype = "Meter";
				break;
			case JRDesignChart.CHART_TYPE_MULTI_AXIS:
				chartype = "Multi Axis";
				break;
			case JRDesignChart.CHART_TYPE_PIE:
				chartype = "Pie";
				break;
			case JRDesignChart.CHART_TYPE_PIE3D:
				chartype = "Pie 3D";
				break;
			case JRDesignChart.CHART_TYPE_SCATTER:
				chartype = "Scatter";
				break;
			case JRDesignChart.CHART_TYPE_STACKEDBAR:
				chartype = "Stacked Bar";
				break;
			case JRDesignChart.CHART_TYPE_STACKEDBAR3D:
				chartype = "Stacked Bar 3D";
				break;
			case JRDesignChart.CHART_TYPE_THERMOMETER:
				chartype = "Thermometer";
				break;
			case JRDesignChart.CHART_TYPE_TIMESERIES:
				chartype = "Time Series";
				break;
			case JRDesignChart.CHART_TYPE_XYAREA:
				chartype = "XY Area";
				break;
			case JRDesignChart.CHART_TYPE_XYBAR:
				chartype = "XY Bar";
				break;
			case JRDesignChart.CHART_TYPE_XYLINE:
				chartype = "XY Line";
				break;
			case JRDesignChart.CHART_TYPE_STACKEDAREA:
				chartype = "Stacked Area";
				break;
			case JRDesignChart.CHART_TYPE_GANTT:
				chartype = "Gantt";
				break;
			default:
				throw new JRRuntimeException("Chart type not supported.");
			}
			return chartype;
		}
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

	@Override
	public void setValue(Object value) {
		JRChart oldObject = (JRChart) getValue();
		JRChart newObject = (JRChart) value;

		if (oldObject != null) {
			((JRBaseChartPlot) oldObject.getPlot()).getEventSupport().removePropertyChangeListener(this);
			if (oldObject.getLegendFont() != null)
				((JRBaseFont) oldObject.getLegendFont()).getEventSupport().removePropertyChangeListener(this);
			if (oldObject.getSubtitleFont() != null)
				((JRBaseFont) oldObject.getSubtitleFont()).getEventSupport().removePropertyChangeListener(this);
			if (oldObject.getTitleFont() != null)
				((JRBaseFont) oldObject.getTitleFont()).getEventSupport().removePropertyChangeListener(this);
		}
		if (newObject != null) {
			((JRBaseChartPlot) newObject.getPlot()).getEventSupport().addPropertyChangeListener(this);
			if (newObject.getLegendFont() != null)
				((JRBaseFont) newObject.getLegendFont()).getEventSupport().addPropertyChangeListener(this);
			if (newObject.getSubtitleFont() != null)
				((JRBaseFont) newObject.getSubtitleFont()).getEventSupport().addPropertyChangeListener(this);
			if (newObject.getTitleFont() != null)
				((JRBaseFont) newObject.getTitleFont()).getEventSupport().addPropertyChangeListener(this);
		}
		super.setValue(value);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		setChangedProperty(true);
		if (evt.getPropertyName().equals(JRDesignElementGroup.PROPERTY_CHILDREN)) {
			if (evt.getSource() == getValue()) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					int newIndex = -1;
					if (evt instanceof CollectionElementAddedEvent) {
						newIndex = ((CollectionElementAddedEvent) evt).getAddedIndex();
					}
					// add the node to this parent
					ANode n = ReportFactory.createNode(this, evt.getNewValue(), newIndex);
					if (evt.getNewValue() instanceof JRElementGroup) {
						JRElementGroup jrFrame = (JRElementGroup) evt.getNewValue();
						ReportFactory.createElementsForBand(n, jrFrame.getChildren());
					}
				} else if (evt.getOldValue() != null && evt.getNewValue() == null) {
					// delete
					for (INode n : getChildren()) {
						if (n.getValue() == evt.getOldValue()) {
							removeChild((ANode) n);
							break;
						}
					}
				} else {
					// changed
					for (INode n : getChildren()) {
						if (n.getValue() == evt.getOldValue())
							n.setValue(evt.getNewValue());
					}
				}
			}
		} else if (evt.getPropertyName().equals("axes")) { //$NON-NLS-1$
			if (evt.getOldValue() == null && evt.getNewValue() != null) {
				int newIndex = -2;
				if (evt instanceof CollectionElementAddedEvent) {
					newIndex = ((CollectionElementAddedEvent) evt).getAddedIndex();
				}
				// add the node to this parent
				ReportFactory.createNode(this, evt.getNewValue(), newIndex);
				// if (evt.getNewValue() instanceof JRElementGroup) {
				// JRElementGroup jrFrame = (JRElementGroup) evt.getNewValue();
				// ReportFactory.createElementsForBand(n,
				// jrFrame.getChildren());
				// }
			} else if (evt.getOldValue() != null && evt.getNewValue() == null) {
				// delete
				for (INode n : getChildren()) {
					if (n.getValue() == evt.getOldValue()) {
						removeChild((ANode) n);
						break;
					}
				}
			} else {
				// changed
				for (INode n : getChildren()) {
					if (n.getValue() == evt.getOldValue())
						n.setValue(evt.getNewValue());
				}
			}

		}
		PropertyChangeEvent newEvent = evt;
		if (!(evt.getSource() instanceof ANode))
			newEvent = new PropertyChangeEvent(this, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		getPropertyChangeSupport().firePropertyChange(newEvent);
	}

	@Override
	public boolean isCopyable2(Object parent) {
		if (parent instanceof MChart)
			return true;
		return super.isCopyable2(parent);
	}

	@Override
	public List<MDatasetRun> getDatasetRunList() {
		JRChart oldObject = (JRChart) getValue();
		if (oldObject != null && oldObject.getDataset() != null) {
			List<MDatasetRun> datasetList = new ArrayList<MDatasetRun>();
			MDatasetRun mDatasetRun = new MDatasetRun(oldObject.getDataset().getDatasetRun(), getJasperDesign());
			mDatasetRun.setJasperConfiguration(getJasperConfiguration());
			datasetList.add(mDatasetRun);
			return datasetList;
		} else
			return null;
	}
	

	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRDesignChart jrSource = (JRDesignChart) getValue();
		if (jrSource != null){
			JRDesignChart jrTarget = (JRDesignChart)target;

			jrTarget.setTitleFont(getFontClone(jrSource.getTitleFont()));
			jrTarget.setSubtitleFont(getFontClone(jrSource.getSubtitleFont()));
			jrTarget.setLegendFont(getFontClone(jrSource.getLegendFont()));
			jrTarget.setTitlePosition(jrSource.getTitlePositionValue());
			jrTarget.setLegendPosition(jrSource.getLegendPositionValue());
			jrTarget.setShowLegend(jrSource.getShowLegend());
			jrTarget.setRenderType(jrSource.getRenderType());
			jrTarget.setTitleColor(getColorClone(jrSource.getOwnTitleColor()));
			jrTarget.setSubtitleColor(getColorClone(jrSource.getOwnSubtitleColor()));
			jrTarget.setLegendColor(getColorClone(jrSource.getOwnLegendColor()));
			jrTarget.setLegendBackgroundColor(getColorClone(jrSource.getOwnLegendBackgroundColor()));
		}
	}
}
