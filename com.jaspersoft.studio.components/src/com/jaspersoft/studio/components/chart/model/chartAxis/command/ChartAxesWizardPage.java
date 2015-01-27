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
package com.jaspersoft.studio.components.chart.model.chartAxis.command;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRAreaPlot;
import net.sf.jasperreports.charts.JRBar3DPlot;
import net.sf.jasperreports.charts.JRBarPlot;
import net.sf.jasperreports.charts.JRBubblePlot;
import net.sf.jasperreports.charts.JRCandlestickPlot;
import net.sf.jasperreports.charts.JRHighLowPlot;
import net.sf.jasperreports.charts.JRLinePlot;
import net.sf.jasperreports.charts.JRScatterPlot;
import net.sf.jasperreports.charts.JRTimeSeriesPlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.design.JRDesignChart;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.wizard.ChartTypeWizardPage;

public class ChartAxesWizardPage extends WizardPage {
	private static final int GALLERY_HEIGHT = 100;
	private static final int GALLERY_WIDTH = 100;
	private byte chartAxes;
	private Scale zoomFactor;
	private Gallery chartsGallery;
	private GalleryItem itemGroup;

	public byte getChartAxis() {
		return chartAxes;
	}

	protected ChartAxesWizardPage(Class<? extends JRChartPlot> chartPlot) {
		super("chartaxiswizard"); //$NON-NLS-1$
		setTitle(Messages.common_chartaxis_wizard);
		setDescription(Messages.ChartAxesWizardPage_chartaxis_wizard_description);
	}

	@Override
	public void dispose() {
		GalleryItem[] tis = chartsGallery.getSelection();
		if (tis.length > 0) {
			GalleryItem ti = tis[0];
			chartAxes = (Byte) ti.getData();
		}
		super.dispose();
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		setControl(composite);

		zoomFactor = new Scale(composite, SWT.NONE);
		zoomFactor.setMinimum(1);
		zoomFactor.setMaximum(50);
		zoomFactor.setIncrement(1);
		zoomFactor.setPageIncrement(5);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.widthHint = 150;
		zoomFactor.setLayoutData(gd);

		chartsGallery = new Gallery(composite, SWT.VIRTUAL | SWT.V_SCROLL | SWT.BORDER);
		final NoGroupRenderer gr = new NoGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemSize(GALLERY_WIDTH, GALLERY_HEIGHT);
		gr.setAutoMargin(true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 500;
		chartsGallery.setLayoutData(gd);
		chartsGallery.setGroupRenderer(gr);
		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		ir.setShowLabels(true);
		ir.setShowRoundedSelectionCorners(false);
		ir.setSelectionForegroundColor(getShell().getDisplay().getSystemColor(SWT.COLOR_BLUE));
		chartsGallery.setItemRenderer(ir);

		itemGroup = new GalleryItem(chartsGallery, SWT.NONE);

		fillTableb4j(chartsGallery, itemGroup);

		chartsGallery.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof GalleryItem)
					chartAxes = (Byte) ((GalleryItem) e.item).getData();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		zoomFactor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double c = 1 + 0.1 * zoomFactor.getSelection();
				gr.setItemSize((int) (GALLERY_WIDTH * c), (int) (GALLERY_HEIGHT * c));
			}
		});
	}

	private void fillTableb4j(Gallery table, GalleryItem rootItem) {
		table.setRedraw(false);

		for (byte ctype : plotmap.keySet()) {
			// if (chartPlot != null
			// && !plotmap.get(ctype).isAssignableFrom(chartPlot))
			// continue;
			// hmm here we should use the same from jfreechart
			ChartTypeWizardPage.getTableItem(ctype, rootItem);
		}

		table.setRedraw(true);
	}

	private static Map<Byte, Class<? extends JRChartPlot>> plotmap = new LinkedHashMap<Byte, Class<? extends JRChartPlot>>();
	static {
		plotmap.put(JRDesignChart.CHART_TYPE_LINE, JRLinePlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_XYLINE, JRLinePlot.class);

		plotmap.put(JRDesignChart.CHART_TYPE_AREA, JRAreaPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_XYAREA, JRAreaPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_STACKEDAREA, JRAreaPlot.class);

		plotmap.put(JRDesignChart.CHART_TYPE_BAR, JRBarPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_STACKEDBAR, JRBarPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_XYBAR, JRBarPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_BAR3D, JRBar3DPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_STACKEDBAR3D, JRBar3DPlot.class);

		plotmap.put(JRDesignChart.CHART_TYPE_BUBBLE, JRBubblePlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_CANDLESTICK, JRCandlestickPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_HIGHLOW, JRHighLowPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_SCATTER, JRScatterPlot.class);
		plotmap.put(JRDesignChart.CHART_TYPE_TIMESERIES, JRTimeSeriesPlot.class);
	}

}
