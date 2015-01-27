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
package com.jaspersoft.studio.components.chart.wizard;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.design.JRDesignChart;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.nebula.widgets.gallery.RoundedGalleryItemRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.utils.UIUtil;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizardPage;

public class ChartTypeWizardPage extends JSSWizardPage {
	private static final int GALLERY_HEIGHT = 100;
	private static final int GALLERY_WIDTH = 100;
	private MChart chart;
	private byte chartType = JRDesignChart.CHART_TYPE_LINE;
	private Scale zoomFactor;
	private Gallery chartsGallery;
	private GalleryItem itemGroup;
	private static Map<String,Image> standardImages=new HashMap<String, Image>();
	private static Map<String,Image> selectedImages=new HashMap<String, Image>();

	protected ChartTypeWizardPage(MChart chart) {
		super("chartwizard"); //$NON-NLS-1$
		setTitle(Messages.common_chart_wizard);
		setDescription(Messages.ChartWizardPage_chart_wizard_description);
		this.chart = chart;
		this.chartType = ((JRDesignChart) chart.getValue()).getChartType();
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_CHART_TYPE;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		setControl(composite);

		zoomFactor = new Scale(composite, SWT.NONE);
		zoomFactor.setMinimum(1);
		zoomFactor.setMaximum(50);
		zoomFactor.setIncrement(1);
		zoomFactor.setPageIncrement(5);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.widthHint = 150;
		zoomFactor.setLayoutData(gd);

		chartsGallery = new Gallery(composite, SWT.VIRTUAL | SWT.V_SCROLL
				| SWT.BORDER);
		final NoGroupRenderer gr = new NoGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemSize(GALLERY_WIDTH, GALLERY_HEIGHT);
		gr.setAutoMargin(true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 500;
		chartsGallery.setLayoutData(gd);
		chartsGallery.setGroupRenderer(gr);
		RoundedGalleryItemRenderer ir = new RoundedGalleryItemRenderer();
		ir.setShowLabels(true);
//		ir.setShowRoundedSelectionCorners(false);
//		ir.setSelectionForegroundColor(getShell().getDisplay().getSystemColor(
//				SWT.COLOR_BLUE));
		chartsGallery.setItemRenderer(ir);

		itemGroup = new GalleryItem(chartsGallery, SWT.NONE);

		fillTableb4j(chartsGallery, itemGroup);

		chartsGallery.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof GalleryItem) {
					chartType = (Byte) ((GalleryItem) e.item).getData();

					getContainer().updateButtons();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		setTableSelection();
		zoomFactor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double c = 1 + 0.1 * zoomFactor.getSelection();
				gr.setItemSize((int) (GALLERY_WIDTH * c),
						(int) (GALLERY_HEIGHT * c));
			}
		});
	}

	private void setTableSelection() {
		for (GalleryItem ti : itemGroup.getItems()) {
			if (((Byte) ti.getData()).intValue() == chartType) {
				chartsGallery.setSelection(new GalleryItem[] { ti });
				break;
			}
		}
	}

	@Override
	public IWizardPage getNextPage() {
		finishPage();
		return super.getNextPage();
	}

	public void finishPage() {
		JRDesignChart oldChart = (JRDesignChart) chart.getValue();
		if (chartType != oldChart.getChartType()) {
			oldChart.setChartType(chartType);
			MChart.setupChart(oldChart);
		}
	}

	@Override
	public boolean canFlipToNextPage() {
		if (chartType == JRDesignChart.CHART_TYPE_MULTI_AXIS) {
			JRDesignChart old = (JRDesignChart) chart.getValue();
			if (chartType != old.getChartType()) {
				old.setChartType(chartType);
			}
			return false;
		}
		return super.canFlipToNextPage();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			chartsGallery.setFocus();
	}

	private void fillTableb4j(Gallery table, GalleryItem rootItem) {
		table.setRedraw(false);

		getTableItem(JRDesignChart.CHART_TYPE_AREA, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_STACKEDAREA, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_XYAREA, rootItem);

		getTableItem(JRDesignChart.CHART_TYPE_BAR, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_BAR3D, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_XYBAR, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_STACKEDBAR, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_STACKEDBAR3D, rootItem);

		getTableItem(JRDesignChart.CHART_TYPE_LINE, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_XYLINE, rootItem);

		getTableItem(JRDesignChart.CHART_TYPE_PIE, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_PIE3D, rootItem);

		getTableItem(JRDesignChart.CHART_TYPE_BUBBLE, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_CANDLESTICK, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_TIMESERIES, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_HIGHLOW, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_SCATTER, rootItem);

		getTableItem(JRDesignChart.CHART_TYPE_THERMOMETER, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_METER, rootItem);

		getTableItem(JRDesignChart.CHART_TYPE_GANTT, rootItem);
		getTableItem(JRDesignChart.CHART_TYPE_MULTI_AXIS, rootItem);

		table.setRedraw(true);
	}

	public static GalleryItem getTableItem(byte chartype, GalleryItem gr) {
		switch (chartype) {
		case JRDesignChart.CHART_TYPE_AREA:
			GalleryItem ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_area_chart);
			setGallyeryItemImageInfo(ti,"/icons/area_big.png");	//$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_AREA);
			return ti;

		case JRDesignChart.CHART_TYPE_BAR:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_bar_chart);
			setGallyeryItemImageInfo(ti,"/icons/bar_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_BAR);
			return ti;
		case JRDesignChart.CHART_TYPE_BAR3D:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_bar3d_chart);
			setGallyeryItemImageInfo(ti,"/icons/bar3d_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_BAR3D);
			return ti;
		case JRDesignChart.CHART_TYPE_BUBBLE:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_bubble_chart);
			setGallyeryItemImageInfo(ti,"/icons/bubble_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_BUBBLE);
			return ti;
		case JRDesignChart.CHART_TYPE_CANDLESTICK:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_candlestick_chart);
			setGallyeryItemImageInfo(ti,"/icons/candlestick_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_CANDLESTICK);
			return ti;
		case JRDesignChart.CHART_TYPE_GANTT:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_gantt);
			setGallyeryItemImageInfo(ti,"/icons/gantt_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_GANTT);
			return ti;
		case JRDesignChart.CHART_TYPE_HIGHLOW:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_highlow_chart);
			setGallyeryItemImageInfo(ti,"/icons/highlow_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_HIGHLOW);
			return ti;
		case JRDesignChart.CHART_TYPE_LINE:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_line_chart);
			setGallyeryItemImageInfo(ti,"/icons/line_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_LINE);
			return ti;
		case JRDesignChart.CHART_TYPE_METER:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_meter_chart);
			setGallyeryItemImageInfo(ti,"/icons/meter_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_METER);
			return ti;
		case JRDesignChart.CHART_TYPE_MULTI_AXIS:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_multiaxes_chart);
			setGallyeryItemImageInfo(ti,"/icons/multiaxis_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_MULTI_AXIS);
			return ti;
		case JRDesignChart.CHART_TYPE_PIE:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_pie_chart);
			setGallyeryItemImageInfo(ti,"/icons/pie_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_PIE);
			return ti;
		case JRDesignChart.CHART_TYPE_PIE3D:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_pie3d_chart);
			setGallyeryItemImageInfo(ti,"/icons/pie3d_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_PIE3D);
			return ti;
		case JRDesignChart.CHART_TYPE_SCATTER:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_scatter_chart);
			setGallyeryItemImageInfo(ti,"/icons/scatter_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_SCATTER);
			return ti;
		case JRDesignChart.CHART_TYPE_STACKEDAREA:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_stacked_area);
			setGallyeryItemImageInfo(ti,"/icons/stackedarea_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_STACKEDAREA);
			return ti;
		case JRDesignChart.CHART_TYPE_STACKEDBAR:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_stacked_bar);
			setGallyeryItemImageInfo(ti,"/icons/stackedbar_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_STACKEDBAR);
			return ti;
		case JRDesignChart.CHART_TYPE_STACKEDBAR3D:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_stacked_bar3D);
			setGallyeryItemImageInfo(ti,"/icons/stackedbar3d_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_STACKEDBAR3D);
			return ti;
		case JRDesignChart.CHART_TYPE_THERMOMETER:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_thermometer_chart);
			setGallyeryItemImageInfo(ti,"/icons/thermometer_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_THERMOMETER);
			return ti;
		case JRDesignChart.CHART_TYPE_TIMESERIES:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_timeseries_chart);
			setGallyeryItemImageInfo(ti,"/icons/timeseries_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_TIMESERIES);
			return ti;
		case JRDesignChart.CHART_TYPE_XYAREA:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_xy_area);
			setGallyeryItemImageInfo(ti,"/icons/xyarea_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_XYAREA);
			return ti;
		case JRDesignChart.CHART_TYPE_XYBAR:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_xy_bar);
			setGallyeryItemImageInfo(ti,"/icons/xybar_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_XYBAR);
			return ti;
		case JRDesignChart.CHART_TYPE_XYLINE:
			ti = new GalleryItem(gr, SWT.NONE);
			ti.setText(Messages.common_xy_line);
			setGallyeryItemImageInfo(ti,"/icons/xyline_big.png"); //$NON-NLS-1$
			ti.setData(JRDesignChart.CHART_TYPE_XYLINE);
			return ti;
		}
		return null;
	}
	
	private static void setGallyeryItemImageInfo(GalleryItem item, String imagePath){
		UIUtil.setGallyeryItemImageInfo(
				item, Activator.PLUGIN_ID, 
				imagePath, selectedImages, standardImages);
	}
}
