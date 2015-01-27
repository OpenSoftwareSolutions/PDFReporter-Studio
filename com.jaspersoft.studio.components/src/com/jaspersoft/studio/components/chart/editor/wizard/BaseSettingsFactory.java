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
package com.jaspersoft.studio.components.chart.editor.wizard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.chartthemes.simple.AxisSettings;
import net.sf.jasperreports.chartthemes.simple.ChartSettings;
import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import net.sf.jasperreports.chartthemes.simple.ColorProvider;
import net.sf.jasperreports.chartthemes.simple.FileImageProvider;
import net.sf.jasperreports.chartthemes.simple.GradientPaintProvider;
import net.sf.jasperreports.chartthemes.simple.LegendSettings;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;
import net.sf.jasperreports.chartthemes.simple.PlotSettings;
import net.sf.jasperreports.chartthemes.simple.TitleSettings;

import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.ui.Align;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.UnitType;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: SimpleSettingsFactory.java 5876 2013-01-07 19:05:05Z teodord $
 */
public class BaseSettingsFactory
{

	public static final Color COLOR_0 = new Color(249, 166, 90);
	public static final Color COLOR_1 = new Color(121, 195, 106);
	public static final Color COLOR_2 = new Color(89, 154, 211);
	public static final Color COLOR_3 = new Color(241, 89, 95);
	public static final Color COLOR_4 = new Color(207, 137, 62);
	public static final Color COLOR_5 = new Color(158, 102, 171);
	public static final Color COLOR_6 = new Color(205, 112, 88);
	public static final Color COLOR_7 = new Color(215, 127, 179);
	
	public static final Color CHART_BACKGROUND1 = new Color(240, 240, 240);
	public static final Color CHART_BACKGROUND2 = new Color(153, 153, 153);
	
	public static final Color TITLE_BACKGROUND = new Color(230, 230, 230);
	public static final Color SUBTITLE_BACKGROUND = new Color(163, 163, 163);

	@SuppressWarnings("serial")
	public static final List<PaintProvider> COLORS = new ArrayList<PaintProvider>(){{
		add(new ColorProvider(COLOR_0));
		add(new ColorProvider(COLOR_1));
		add(new ColorProvider(COLOR_2));
		add(new ColorProvider(COLOR_3));
		add(new ColorProvider(COLOR_4));
		add(new ColorProvider(COLOR_5));
		add(new ColorProvider(COLOR_6));
		add(new ColorProvider(COLOR_7));
		}};
		
	@SuppressWarnings("serial")
	public static final List<PaintProvider> COLORS_DARKER = new ArrayList<PaintProvider>(){{
		add(new ColorProvider(COLOR_0.darker()));
		add(new ColorProvider(COLOR_1.darker()));
		add(new ColorProvider(COLOR_2.darker()));
		add(new ColorProvider(COLOR_3.darker()));
		add(new ColorProvider(COLOR_4.darker()));
		add(new ColorProvider(COLOR_5.darker()));
		add(new ColorProvider(COLOR_6.darker()));
		add(new ColorProvider(COLOR_7.darker()));
		}};
		
	@SuppressWarnings("serial")
	public static final List<GradientPaintProvider> GRADIENT_PAINTS = new ArrayList<GradientPaintProvider>(){{
		add(new GradientPaintProvider(COLOR_0, COLOR_0.darker()));
		add(new GradientPaintProvider(COLOR_1, COLOR_1.darker()));
		add(new GradientPaintProvider(COLOR_2, COLOR_2.darker()));
		add(new GradientPaintProvider(COLOR_3, COLOR_3.darker()));
		add(new GradientPaintProvider(COLOR_4, COLOR_4.darker()));
		add(new GradientPaintProvider(COLOR_5, COLOR_5.darker()));
		add(new GradientPaintProvider(COLOR_6, COLOR_6.darker()));
		add(new GradientPaintProvider(COLOR_7, COLOR_7.darker()));
	}};

	@SuppressWarnings("serial")
	public static final List<Stroke> STROKES = new ArrayList<Stroke>(){{
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		add(new BasicStroke(2f));
		}};
	
	@SuppressWarnings("serial")
	public static final List<Stroke> OUTLINE_STROKES = new ArrayList<Stroke>(){{
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		add(new BasicStroke(3f));
		}};
	

	/**
	 *
	 */
	public static final ChartThemeSettings createChartThemeSettings()
	{
		ChartThemeSettings settings = new ChartThemeSettings();
		
		ChartSettings chartSettings = settings.getChartSettings();
		chartSettings.setBackgroundPaint(new GradientPaintProvider(CHART_BACKGROUND1, CHART_BACKGROUND2));
		chartSettings.setBackgroundImage(new FileImageProvider("net/sf/jasperreports/chartthemes/simple/jasperreports.png"));
		chartSettings.setBackgroundImageAlignment(new Integer(Align.TOP_RIGHT));
		chartSettings.setBackgroundImageAlpha(new Float(1f));
		chartSettings.setBorderVisible(Boolean.FALSE);
		chartSettings.setBorderPaint(new ColorProvider(Color.BLACK));
		chartSettings.setBorderStroke(new BasicStroke(1f));
		chartSettings.setAntiAlias(Boolean.TRUE);
		chartSettings.setTextAntiAlias(Boolean.TRUE);
		chartSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));

		TitleSettings titleSettings = settings.getTitleSettings();
		titleSettings.setShowTitle(Boolean.TRUE);
		titleSettings.setPosition(EdgeEnum.TOP);
		titleSettings.setForegroundPaint(new ColorProvider(Color.black));
		titleSettings.setBackgroundPaint(new ColorProvider(TITLE_BACKGROUND));
		titleSettings.getFont().setBold(Boolean.TRUE);
		titleSettings.getFont().setFontSize(22);
		titleSettings.setHorizontalAlignment(HorizontalAlignment.CENTER);
		titleSettings.setVerticalAlignment(VerticalAlignment.TOP);
		titleSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));
		
		TitleSettings subtitleSettings = settings.getSubtitleSettings();
		subtitleSettings.setShowTitle(Boolean.TRUE);
		subtitleSettings.setPosition(EdgeEnum.TOP);
		subtitleSettings.setForegroundPaint(new ColorProvider(Color.BLACK));
		subtitleSettings.setBackgroundPaint(new ColorProvider(SUBTITLE_BACKGROUND));
		subtitleSettings.getFont().setBold(Boolean.TRUE);
		subtitleSettings.setHorizontalAlignment(HorizontalAlignment.CENTER);
		subtitleSettings.setVerticalAlignment(VerticalAlignment.TOP);
		subtitleSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));

		LegendSettings legendSettings = settings.getLegendSettings();
		legendSettings.setShowLegend(Boolean.TRUE);
		legendSettings.setPosition(EdgeEnum.BOTTOM);
		legendSettings.setForegroundPaint(new ColorProvider(Color.black));
		legendSettings.setBackgroundPaint(new ColorProvider(Color.white));
		legendSettings.getFont().setBold(Boolean.TRUE);
		legendSettings.getFont().setFontSize(7);
		legendSettings.setHorizontalAlignment(HorizontalAlignment.CENTER);
		legendSettings.setVerticalAlignment(VerticalAlignment.BOTTOM);
		//FIXMETHEME legendSettings.setBlockFrame();
		legendSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));
		
		PlotSettings plotSettings = settings.getPlotSettings();
		plotSettings.setOrientation(PlotOrientation.VERTICAL);
//		plotSettings.setForegroundAlpha(new Float(0.5f));
		plotSettings.setBackgroundPaint(new ColorProvider(Color.white));
		plotSettings.setBackgroundAlpha(new Float(0.0f));
		plotSettings.setBackgroundImage(new FileImageProvider("net/sf/jasperreports/chartthemes/simple/jasperreports.png"));
		plotSettings.setBackgroundImageAlpha(new Float(0.5f));
		plotSettings.setBackgroundImageAlignment(new Integer(Align.NORTH_WEST));
		plotSettings.setLabelRotation(new Double(0));
		plotSettings.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 1.1, 2.2, 3.3, 4.4));
		plotSettings.setOutlineVisible(Boolean.TRUE);
		plotSettings.setOutlinePaint(new ColorProvider(Color.BLACK));
		plotSettings.setOutlineStroke(new BasicStroke(1f));
		plotSettings.setSeriesColorSequence(COLORS);
//		plotSettings.setSeriesGradientPaintSequence(GRADIENT_PAINTS);
		plotSettings.setSeriesOutlinePaintSequence(COLORS_DARKER);
		plotSettings.setSeriesStrokeSequence(STROKES);
		plotSettings.setSeriesOutlineStrokeSequence(OUTLINE_STROKES);
		plotSettings.setDomainGridlineVisible(Boolean.TRUE);
		plotSettings.setDomainGridlinePaint(new ColorProvider(Color.BLACK));
		plotSettings.setDomainGridlineStroke(new BasicStroke(0.5f));
		plotSettings.setRangeGridlineVisible(Boolean.TRUE);
		plotSettings.setRangeGridlinePaint(new ColorProvider(Color.BLACK));
		plotSettings.setRangeGridlineStroke(new BasicStroke(0.5f));
		plotSettings.getTickLabelFont().setFontName("Arial");
		plotSettings.getTickLabelFont().setBold(Boolean.TRUE);
		plotSettings.getTickLabelFont().setFontSize(10);
		plotSettings.getDisplayFont().setFontName("Arial");
		plotSettings.getDisplayFont().setBold(Boolean.TRUE);
		plotSettings.getDisplayFont().setFontSize(12);
		
		AxisSettings domainAxisSettings = settings.getDomainAxisSettings();
		domainAxisSettings.setVisible(Boolean.TRUE);
		domainAxisSettings.setLocation(AxisLocation.BOTTOM_OR_RIGHT);
		domainAxisSettings.setLinePaint(new ColorProvider(Color.white));
		domainAxisSettings.setLineStroke(new BasicStroke(1f));
		domainAxisSettings.setLineVisible(Boolean.TRUE);
//		domainAxisSettings.setLabel("Domain Axis");
		domainAxisSettings.setLabelAngle(new Double(0.0));
		domainAxisSettings.setLabelPaint(new ColorProvider(Color.black));
		domainAxisSettings.getLabelFont().setBold(Boolean.TRUE);
		domainAxisSettings.getLabelFont().setItalic(Boolean.FALSE);
		domainAxisSettings.getLabelFont().setFontName("Times New Roman");
		domainAxisSettings.getLabelFont().setFontSize(10);
		domainAxisSettings.setLabelInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.5, 0.5, 1, 1));
		domainAxisSettings.setLabelVisible(Boolean.TRUE);
		domainAxisSettings.setTickLabelPaint(new ColorProvider(Color.cyan));
		domainAxisSettings.getTickLabelFont().setBold(Boolean.TRUE);
		domainAxisSettings.getTickLabelFont().setItalic(Boolean.FALSE);
		domainAxisSettings.getTickLabelFont().setFontName("Times New Roman");
		domainAxisSettings.getTickLabelFont().setFontSize(7);
		domainAxisSettings.getTickLabelFont().setItalic(Boolean.TRUE);
		domainAxisSettings.setTickLabelInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.5, 0.5, 0.5, 0.5));
		domainAxisSettings.setTickLabelsVisible(Boolean.TRUE);
		domainAxisSettings.setTickMarksInsideLength(new Float(0.1f));
		domainAxisSettings.setTickMarksOutsideLength(new Float(0.2f));
		domainAxisSettings.setTickMarksPaint(new ColorProvider(Color.black));
		domainAxisSettings.setTickMarksStroke(new BasicStroke(1f));
		domainAxisSettings.setTickMarksVisible(Boolean.TRUE);
		domainAxisSettings.setTickCount(new Integer(5));

		
		AxisSettings rangeAxisSettings = settings.getRangeAxisSettings();
		rangeAxisSettings.setVisible(Boolean.TRUE);
		rangeAxisSettings.setLocation(AxisLocation.TOP_OR_RIGHT);
		rangeAxisSettings.setLinePaint(new ColorProvider(Color.white));
		rangeAxisSettings.setLineStroke(new BasicStroke(1f));
		rangeAxisSettings.setLineVisible(Boolean.TRUE);
//		rangeAxisSettings.setLabel("Range Axis");
		rangeAxisSettings.setLabelAngle(new Double(Math.PI/2.0));
		rangeAxisSettings.setLabelPaint(new ColorProvider(Color.black));
		rangeAxisSettings.getLabelFont().setBold(Boolean.TRUE);
		rangeAxisSettings.getLabelFont().setItalic(Boolean.FALSE);
		rangeAxisSettings.getLabelFont().setFontName("Times New Roman");
		rangeAxisSettings.getLabelFont().setFontSize(10);
		rangeAxisSettings.setLabelInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.5, 0.5, 1, 1));
		rangeAxisSettings.setLabelVisible(Boolean.TRUE);
		rangeAxisSettings.setTickLabelPaint(new ColorProvider(Color.pink));
		rangeAxisSettings.getTickLabelFont().setBold(Boolean.FALSE);
		rangeAxisSettings.getTickLabelFont().setItalic(Boolean.FALSE);
		rangeAxisSettings.getTickLabelFont().setFontName("Times New Roman");
		rangeAxisSettings.getTickLabelFont().setFontSize(7);
		rangeAxisSettings.getTickLabelFont().setItalic(Boolean.TRUE);
		rangeAxisSettings.setTickLabelInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.5, 0.5, 0.5, 0.5));
		rangeAxisSettings.setTickLabelsVisible(Boolean.TRUE);
		rangeAxisSettings.setTickMarksInsideLength(new Float(0.2f));
		rangeAxisSettings.setTickMarksOutsideLength(new Float(0.1f));
		rangeAxisSettings.setTickMarksPaint(new ColorProvider(Color.black));
		rangeAxisSettings.setTickMarksStroke(new BasicStroke(1f));
		rangeAxisSettings.setTickMarksVisible(Boolean.TRUE);
		rangeAxisSettings.setTickCount(new Integer(6));
		
		return settings;
	}
}
