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
/*
 * JasperReports - Free Java Reporting Library. Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program is part of JasperReports.
 * 
 * JasperReports is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * JasperReports is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with JasperReports. If not, see
 * <http://www.gnu.org/licenses/>.
 */

/*
 * Contributors: Eugene D - eugenedruy@users.sourceforge.net Adrian Jackson - iapetus@users.sourceforge.net David Taylor
 * - exodussystems@users.sourceforge.net Lars Kristensen - llk@users.sourceforge.net
 */
package com.jaspersoft.studio.jasper;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import net.sf.jasperreports.charts.ChartContext;
import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.charts.type.AxisPositionEnum;
import net.sf.jasperreports.charts.util.ChartUtil;
import net.sf.jasperreports.chartthemes.simple.FileImageProvider;
import net.sf.jasperreports.chartthemes.simple.ImageProvider;
import net.sf.jasperreports.chartthemes.simple.SimpleChartTheme;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRChartPlot.JRSeriesColor;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.convert.ElementConverter;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.util.JRExpressionUtil;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.Dataset;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ChartConverter.java 5878 2013-01-07 20:23:13Z teodord $
 */
public final class ChartConverter extends ElementConverter {

	private static final String CHART_THEME = "chart.theme_";
	/**
	 *
	 */
	private final static ChartConverter INSTANCE = new ChartConverter();
	private JFreeChart jfreeChart;
	private Dataset dataset;

	/**
	 *
	 */
	private ChartConverter() {
	}

	/**
	 *
	 */
	public static ChartConverter getInstance() {
		return INSTANCE;
	}

	/**
	 *
	 */
	public JRPrintElement convert(ReportConverter reportConverter, JRElement element) {
		JRBasePrintImage printImage = new JRBasePrintImage(reportConverter.getDefaultStyleProvider());
		JRChart chart = (JRChart) element;

		copyElement(reportConverter, chart, printImage);

		printImage.copyBox(chart.getLineBox());

		printImage.setAnchorName(JRExpressionUtil.getExpressionText(chart.getAnchorNameExpression()));
		printImage.setBookmarkLevel(chart.getBookmarkLevel());
		printImage.setLinkType(chart.getLinkType());
		printImage.setOnErrorType(OnErrorTypeEnum.ICON);
		printImage.setRenderable(getRenderer(reportConverter, chart));
		printImage.setScaleImage(ScaleImageEnum.CLIP);

		return printImage;
	}

	private ImageProvider getCachedImageProvider(ImageProvider ip) {
		if (ip != null)
			try {
				if (ip instanceof CachedImageProvider)
					return ip;
				if (ip instanceof FileImageProvider)
					return new CachedImageProvider((FileImageProvider) ip);
				else
					return ip;
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	/**
	 * 
	 */
	private Renderable getRenderer(ReportConverter reportConverter, JRChart chart) {
		JasperReportsConfiguration jContext = (JasperReportsConfiguration) reportConverter.getJasperReportsContext();
		String renderType = null;
		Rectangle2D rectangle = null;
		ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(jContext.getClassLoader());
			if (dataset != null && jfreeChart != null)
				dataset.removeChangeListener(jfreeChart.getPlot());

			renderType = chart.getRenderType();// FIXMETHEME try reuse this sequence
			if (renderType == null) {
				renderType = JRPropertiesUtil.getInstance(jContext).getProperty(reportConverter.getReport(),
						JRChart.PROPERTY_CHART_RENDER_TYPE);
			}

			String themeName = chart.getTheme();
			if (themeName == null) {
				themeName = JRPropertiesUtil.getInstance(jContext).getProperty(reportConverter.getReport(),
						JRChart.PROPERTY_CHART_THEME);
			}
			ChartTheme theme = ChartUtil.getInstance(jContext).getTheme(themeName);
			if (theme instanceof SimpleChartTheme) {
				SimpleChartTheme sct = (SimpleChartTheme) theme;
				sct.getChartSettings().setBackgroundImage(getCachedImageProvider(sct.getChartSettings().getBackgroundImage()));
				sct.getPlotSettings().setBackgroundImage(getCachedImageProvider(sct.getPlotSettings().getBackgroundImage()));
			}
			ChartContext chartContext = null;
			// Object cc = jContext.getMap().get(chart);
			// if (cc != null && cc instanceof ChartContext)
			// chartContext = (ChartContext) cc;
			// else {
			chartContext = new ConvertChartContext(chart, jContext);
			try {
				jfreeChart = theme.createChart(chartContext);
				dataset = chartContext.getDataset();
				if (jfreeChart == null && chart.getChartType() == JRChart.CHART_TYPE_MULTI_AXIS) {
					List<JRChartAxis> axis = ((JRDesignMultiAxisPlot) chart.getPlot()).getAxes();
					Plot mainPlot = null;
					int axisNumber = 0;
					for (JRChartAxis ax : axis) {
						JRChart chartAxis = ((JRDesignChartAxis) ax).getChart();
						if (jfreeChart == null) {
							jfreeChart = getJFreeChart(reportConverter, chartAxis);
							mainPlot = jfreeChart.getPlot();
							if (mainPlot instanceof CategoryPlot)
								((CategoryPlot) mainPlot).setRangeAxisLocation(0, getChartAxisLocation(ax));
							else if (mainPlot instanceof XYPlot)
								((XYPlot) mainPlot).setRangeAxisLocation(0, getChartAxisLocation(ax));
						} else {
							axisNumber++;
							JFreeChart axisChart = getJFreeChart(reportConverter, chartAxis);
							if (mainPlot instanceof CategoryPlot) {
								CategoryPlot mainCatPlot = (CategoryPlot) mainPlot;
								if (!(axisChart.getPlot() instanceof CategoryPlot))
									continue;

								// Get the axis and add it to the multi axis chart plot
								CategoryPlot axisPlot = (CategoryPlot) axisChart.getPlot();
								mainCatPlot.setRangeAxis(axisNumber, axisPlot.getRangeAxis());
								mainCatPlot.setRangeAxisLocation(axisNumber, getChartAxisLocation(ax));

								// Add the data set and map it to the recently added axis
								mainCatPlot.setDataset(axisNumber, axisPlot.getDataset());
								mainCatPlot.mapDatasetToRangeAxis(axisNumber, axisNumber);

								// Set the renderer to use to draw the dataset.
								mainCatPlot.setRenderer(axisNumber, axisPlot.getRenderer());

								// Handle any color series for this chart
								configureAxisSeriesColors(axisPlot.getRenderer(), chartAxis.getPlot());
							} else if (mainPlot instanceof XYPlot) {
								XYPlot mainXyPlot = (XYPlot) mainPlot;
								if (!(axisChart.getPlot() instanceof XYPlot))
									continue;

								// Get the axis and add it to the multi axis chart plot
								XYPlot axisPlot = (XYPlot) axisChart.getPlot();
								mainXyPlot.setRangeAxis(axisNumber, axisPlot.getRangeAxis());
								mainXyPlot.setRangeAxisLocation(axisNumber, getChartAxisLocation(ax));

								// Add the data set and map it to the recently added axis
								mainXyPlot.setDataset(axisNumber, axisPlot.getDataset());
								mainXyPlot.mapDatasetToRangeAxis(axisNumber, axisNumber);

								// Set the renderer to use to draw the dataset.
								mainXyPlot.setRenderer(axisNumber, axisPlot.getRenderer());

								// Handle any color series for this chart
								configureAxisSeriesColors(axisPlot.getRenderer(), chartAxis.getPlot());
							}
						}
					}
				}
			} catch (JRException e) {
				throw new JRRuntimeException(e);
			}

			rectangle = new Rectangle2D.Double(0, 0, chart.getWidth(), chart.getHeight());
		} finally {
			Thread.currentThread().setContextClassLoader(oldLoader);
		}
		return ChartUtil.getInstance(jContext).getChartRenderableFactory(renderType)
				.getRenderable(jContext, jfreeChart, null, rectangle);
	}

	private JFreeChart getJFreeChart(ReportConverter reportConverter, JRChart chart) {
		JasperReportsContext jContext = reportConverter.getJasperReportsContext();
		String themeName = chart.getTheme();
		if (themeName == null) {
			themeName = JRPropertiesUtil.getInstance(jContext).getProperty(reportConverter.getReport(),
					JRChart.PROPERTY_CHART_THEME);
		}

		ChartTheme theme = ChartUtil.getInstance(jContext).getTheme(themeName);

		ChartContext chartContext = new ConvertChartContext(chart, jContext);

		JFreeChart jfreeChart = null;
		try {
			jfreeChart = theme.createChart(chartContext);
		} catch (JRException e) {
			throw new JRRuntimeException(e);
		}
		return jfreeChart;
	}

	protected AxisLocation getChartAxisLocation(JRChartAxis ca) {
		AxisPositionEnum pv = ca.getPositionValue();
		return ca.getPositionValue() != null && pv == AxisPositionEnum.RIGHT_OR_BOTTOM ? AxisLocation.BOTTOM_OR_RIGHT
				: AxisLocation.TOP_OR_LEFT;
	}

	private void configureAxisSeriesColors(CategoryItemRenderer renderer, JRChartPlot jrPlot) {
		SortedSet<JRSeriesColor> seriesColors = jrPlot.getSeriesColors();
		if (seriesColors != null) {
			Iterator<JRSeriesColor> iter = seriesColors.iterator();
			while (iter.hasNext()) {
				JRSeriesColor seriesColor = iter.next();
				renderer.setSeriesPaint(seriesColor.getSeriesOrder(), seriesColor.getColor());
			}
		}
	}

	private void configureAxisSeriesColors(XYItemRenderer renderer, JRChartPlot jrPlot) {
		SortedSet<JRSeriesColor> seriesColors = jrPlot.getSeriesColors();
		if (seriesColors != null) {
			Iterator<JRSeriesColor> iter = seriesColors.iterator();
			while (iter.hasNext()) {
				JRSeriesColor seriesColor = iter.next();
				renderer.setSeriesPaint(seriesColor.getSeriesOrder(), seriesColor.getColor());
			}
		}
	}
}
