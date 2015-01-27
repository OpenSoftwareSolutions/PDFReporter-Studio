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
package com.jaspersoft.studio.components.chart.property.section.plot;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.chart.model.plot.MAreaPlot;
import com.jaspersoft.studio.components.chart.model.plot.MBar3DPlot;
import com.jaspersoft.studio.components.chart.model.plot.MBarPlot;
import com.jaspersoft.studio.components.chart.model.plot.MBubblePlot;
import com.jaspersoft.studio.components.chart.model.plot.MCandlestickPlot;
import com.jaspersoft.studio.components.chart.model.plot.MHighLowPlot;
import com.jaspersoft.studio.components.chart.model.plot.MLinePlot;
import com.jaspersoft.studio.components.chart.model.plot.MMeterPlot;
import com.jaspersoft.studio.components.chart.model.plot.MMultiAxisPlot;
import com.jaspersoft.studio.components.chart.model.plot.MPie3DPlot;
import com.jaspersoft.studio.components.chart.model.plot.MPiePlot;
import com.jaspersoft.studio.components.chart.model.plot.MScatterPlot;
import com.jaspersoft.studio.components.chart.model.plot.MThermometerPlot;
import com.jaspersoft.studio.components.chart.model.plot.MTimeSeriesPlot;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class PlotFactory {
	public static void createPlot(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		APropertyNode plot = section.getElement();
		APlot aplot = null;
		if (plot instanceof MAreaPlot)
			aplot = new AreaPlot();
		else if (plot instanceof MBar3DPlot)
			aplot = new Bar3dPlot();
		else if (plot instanceof MBarPlot)
			aplot = new BarPlot();
		else if (plot instanceof MBubblePlot)
			aplot = new BubblePlot();
		else if (plot instanceof MCandlestickPlot)
			aplot = new CandlestickPlot();
		else if (plot instanceof MHighLowPlot)
			aplot = new HighLowPlot();
		else if (plot instanceof MLinePlot)
			aplot = new LinePlot();
		else if (plot instanceof MMeterPlot)
			aplot = new MeterPlot();
		else if (plot instanceof MMultiAxisPlot)
			aplot = new MultiAxisPlot();
		else if (plot instanceof MPie3DPlot)
			aplot = new Pie3dPlot();
		else if (plot instanceof MPiePlot)
			aplot = new PiePlot();
		else if (plot instanceof MScatterPlot)
			aplot = new ScatterPlot();
		else if (plot instanceof MThermometerPlot)
			aplot = new ThermometerPlot();
		else if (plot instanceof MTimeSeriesPlot)
			aplot = new TimeSeriesPlot();
		if (aplot != null)
			aplot.createControls(section, parent, tabbedPropertySheetPage);
	}
}
