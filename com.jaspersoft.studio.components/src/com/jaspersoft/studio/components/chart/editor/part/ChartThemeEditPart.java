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
package com.jaspersoft.studio.components.chart.editor.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.design.JRDesignAreaPlot;
import net.sf.jasperreports.charts.design.JRDesignBar3DPlot;
import net.sf.jasperreports.charts.design.JRDesignBarPlot;
import net.sf.jasperreports.charts.design.JRDesignBubblePlot;
import net.sf.jasperreports.charts.design.JRDesignCandlestickPlot;
import net.sf.jasperreports.charts.design.JRDesignHighLowPlot;
import net.sf.jasperreports.charts.design.JRDesignLinePlot;
import net.sf.jasperreports.charts.design.JRDesignPie3DPlot;
import net.sf.jasperreports.charts.design.JRDesignPiePlot;
import net.sf.jasperreports.charts.design.JRDesignScatterPlot;
import net.sf.jasperreports.charts.design.JRDesignTimeSeriesPlot;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.GraphicalViewer;

import com.jaspersoft.studio.components.chart.figure.ChartFigure;
import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.components.chart.model.theme.MChartThemeSettings;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.model.MGraphicElement;

public class ChartThemeEditPart extends FigureEditPart {

	/**
	 * List of all the sample chart models, so when a property is changed the model can be easly refrshed
	 */
	private List<MChart> chartModels = new ArrayList<MChart>();
	
	private List<ChartFigure> charts = new ArrayList<ChartFigure>();

	@Override
	protected IFigure createFigure() {
		charts.clear();

		RectangleFigure rf = new RectangleFigure();
		rf.setBorder(new LineBorder(ColorConstants.white));
		GridLayout lm = new GridLayout(3, false);
		lm.marginHeight = 10;
		lm.marginWidth = 20;
		lm.horizontalSpacing = 20;
		rf.setLayoutManager(lm);

		JRDesignChart jdc = MChart.createJRElement(
				getModel().getJasperDesign(), JRDesignChart.CHART_TYPE_AREA);
		jdc.setTitleExpression(new JRDesignExpression("\"Area Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Areas\""));
		((JRDesignAreaPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));
		((JRDesignAreaPlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Name\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_BAR);
		jdc.setTitleExpression(new JRDesignExpression("\"Bar Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Bars\""));
		((JRDesignBarPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));
		((JRDesignBarPlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Name\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_BAR3D);
		jdc.setTitleExpression(new JRDesignExpression("\"Bar 3D Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying 3D Bars\""));
		((JRDesignBar3DPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));
		((JRDesignBar3DPlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Name\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_BUBBLE);
		jdc.setTitleExpression(new JRDesignExpression("\"Bubble Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Bubbles\""));
		((JRDesignBubblePlot) jdc.getPlot())
				.setYAxisLabelExpression(new JRDesignExpression("\"Amount\""));
		((JRDesignBubblePlot) jdc.getPlot())
				.setXAxisLabelExpression(new JRDesignExpression(
						"\"Probability\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_CANDLESTICK);
		jdc.setTitleExpression(new JRDesignExpression("\"Candlestick Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Candlesticks\""));
		((JRDesignCandlestickPlot) jdc.getPlot())
				.setTimeAxisLabelExpression(new JRDesignExpression("\"Time\""));
		((JRDesignCandlestickPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_HIGHLOW);
		jdc.setTitleExpression(new JRDesignExpression("\"High Low Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying High Low Values\""));
		((JRDesignHighLowPlot) jdc.getPlot())
				.setTimeAxisLabelExpression(new JRDesignExpression("\"Time\""));
		((JRDesignHighLowPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_LINE);
		jdc.setTitleExpression(new JRDesignExpression("\"Line Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Lines\""));
		((JRDesignLinePlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Name\""));
		((JRDesignLinePlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_METER);
		jdc.setTitleExpression(new JRDesignExpression("\"Meter Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying a Meter\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_PIE);
		jdc.setTitleExpression(new JRDesignExpression("\"Pie Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying a Pie\""));
		((JRDesignPiePlot) jdc.getPlot()).setCircular(Boolean.TRUE);

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_PIE3D);
		jdc.setTitleExpression(new JRDesignExpression("\"Pie 3D Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying a Pie 3D\""));
		((JRDesignPie3DPlot) jdc.getPlot()).setCircular(Boolean.TRUE);

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_SCATTER);
		jdc.setTitleExpression(new JRDesignExpression("\"Scatter Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Scattered Dots\""));
		((JRDesignScatterPlot) jdc.getPlot())
				.setYAxisLabelExpression(new JRDesignExpression("\"Amount\""));
		((JRDesignScatterPlot) jdc.getPlot())
				.setXAxisLabelExpression(new JRDesignExpression(
						"\"Probability\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_STACKEDAREA);
		jdc.setTitleExpression(new JRDesignExpression("\"Stacked Area Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Stacked Areas\""));
		((JRDesignAreaPlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Name\""));
		((JRDesignAreaPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_STACKEDBAR);
		jdc.setTitleExpression(new JRDesignExpression("\"Stacked Bar Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Stacked Bars\""));
		((JRDesignBarPlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Name\""));
		((JRDesignBarPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_STACKEDBAR3D);
		jdc.setTitleExpression(new JRDesignExpression(
				"\"Stacked Bar 3D Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Stacked Bars 3D\""));
		((JRDesignBar3DPlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Name\""));
		((JRDesignBar3DPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_THERMOMETER);
		jdc.setTitleExpression(new JRDesignExpression("\"Thermometer Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Thermometer\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_TIMESERIES);
		jdc.setTitleExpression(new JRDesignExpression("\"Time Series Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying Time Series\""));
		((JRDesignTimeSeriesPlot) jdc.getPlot())
				.setTimeAxisLabelExpression(new JRDesignExpression("\"Time\""));
		((JRDesignTimeSeriesPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_XYAREA);
		jdc.setTitleExpression(new JRDesignExpression("\"XY Area Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying XY Area\""));
		((JRDesignAreaPlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Name\""));
		((JRDesignAreaPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_XYBAR);
		jdc.setTitleExpression(new JRDesignExpression("\"XY Bar Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying XY Bars\""));
		((JRDesignBarPlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Probability\""));
		((JRDesignBarPlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		jdc = MChart.createJRElement(getModel().getJasperDesign(),
				JRDesignChart.CHART_TYPE_XYLINE);
		jdc.setTitleExpression(new JRDesignExpression("\"XY Line Chart\""));
		jdc.setSubtitleExpression(new JRDesignExpression(
				"\"Chart Displaying XY Lines\""));
		((JRDesignLinePlot) jdc.getPlot())
				.setCategoryAxisLabelExpression(new JRDesignExpression(
						"\"Probability\""));
		((JRDesignLinePlot) jdc.getPlot())
				.setValueAxisLabelExpression(new JRDesignExpression(
						"\"Amount\""));

		addChart(rf, lm, jdc);

		rf.setSize(3 * jdc.getWidth() + 80, (jdc.getHeight() + 20)
				* rf.getChildren().size() / 3 + 50);

		setPrefsBorder(rf);
		
		//Add the property change listener to force the charts refresh when something is changed
		//FIXME: add the refresh only on the change of visual property
		getModel().getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				for (MChart child : chartModels){
					((MGraphicElement)child).setChangedProperty(true);
				}
				refresh();
			}
		});
		return rf;
	}
	
	

	protected void setupChartSize(JRDesignChart jdc, GridLayout lm,
			ChartFigure cf) {
		jdc.setX(0);
		jdc.setY(0);
		jdc.setWidth(300);
		jdc.setHeight(180);
		jdc.setTheme("");
		setupSize(jdc, lm, cf);
	}

	private void setupSize(JRDesignChart jdc, GridLayout lm, ChartFigure cf) {
		GridData gd = new GridData();
		gd.heightHint = jdc.getHeight();
		gd.widthHint = jdc.getWidth();
		lm.setConstraint(cf, gd);
	}

	protected void addChart(final RectangleFigure rf, final GridLayout lm, final JRDesignChart jdc) {
		MChart model = new MChart();
		chartModels.add(model);
		model.setValue(jdc);
		final ChartFigure cf = new ChartFigure(model);
		setupChartSize(jdc, lm, cf);
		cf.setToolTip(new Label("Click on me to zoom"));
		cf.setJRElement(jdc, getDrawVisitor());
		cf.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent me) {
				if (rf.getChildren().size() > 1) {
					for (IFigure fc : charts) {
						if (fc != cf)
							rf.remove(fc);
					}
					jdc.setWidth(655);
					jdc.setHeight(400);
					setupSize(jdc, lm, cf);

					((GraphicalViewer) getViewer())
							.reveal(ChartThemeEditPart.this);
					cf.setLocation(new org.eclipse.draw2d.geometry.Point(5, 5));
					rf.setSize(jdc.getWidth() + 50, jdc.getHeight() + 50);
				} else {
					setupChartSize(jdc, lm, cf);
					rf.removeAll();
					for (IFigure f : charts)
						rf.add(f);
					rf.setSize(3 * jdc.getWidth() + 80, (jdc.getHeight() + 20)
							* rf.getChildren().size() / 3 + 50);
				}
			}

			@Override
			public void mouseReleased(MouseEvent me) {
			}

			@Override
			public void mouseDoubleClicked(MouseEvent me) {
			}
		});

		rf.add(cf);
		charts.add(cf);
	}

	public void setPrefsBorder(IFigure rect) {
	}
	

	@Override
	public MChartThemeSettings getModel() {
		return (MChartThemeSettings) super.getModel();
	}

	@Override
	protected void setupFigure(IFigure rect) {
	}

	@Override
	protected void createEditPolicies() {
	}

}
