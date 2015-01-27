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


import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRMeterPlot;
import net.sf.jasperreports.charts.JRMultiAxisPlot;
import net.sf.jasperreports.charts.JRPie3DPlot;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRThermometerPlot;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.engine.JRChartPlot;

public class PlotFactory {
	public static MChartPlot getChartPlot(JRChartPlot plot) {
		if (plot instanceof JRAreaPlot)
			return new MAreaPlot((JRAreaPlot) plot);
		if (plot instanceof JRBar3DPlot)
			return new MBar3DPlot((JRBar3DPlot) plot);
		if (plot instanceof JRBarPlot)
			return new MBarPlot((JRBarPlot) plot);
		if (plot instanceof JRBubblePlot)
			return new MBubblePlot((JRBubblePlot) plot);
		if (plot instanceof JRCandlestickPlot)
			return new MCandlestickPlot((JRCandlestickPlot) plot);
		if (plot instanceof JRHighLowPlot)
			return new MHighLowPlot((JRHighLowPlot) plot);
		if (plot instanceof JRLinePlot)
			return new MLinePlot((JRLinePlot) plot);
		if (plot instanceof JRMeterPlot)
			return new MMeterPlot((JRMeterPlot) plot);
		if (plot instanceof JRMultiAxisPlot)
			return new MMultiAxisPlot((JRMultiAxisPlot) plot);
		if (plot instanceof JRPie3DPlot)
			return new MPie3DPlot((JRPie3DPlot) plot);
		if (plot instanceof JRPiePlot)
			return new MPiePlot((JRPiePlot) plot);
		if (plot instanceof JRScatterPlot)
			return new MScatterPlot((JRScatterPlot) plot);
		if (plot instanceof JRThermometerPlot)
			return new MThermometerPlot((JRThermometerPlot) plot);
		if (plot instanceof JRTimeSeriesPlot)
			return new MTimeSeriesPlot((JRTimeSeriesPlot) plot);

		return new MChartPlot(plot);
	}
}
