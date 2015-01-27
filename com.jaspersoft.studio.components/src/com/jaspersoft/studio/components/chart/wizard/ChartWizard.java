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

import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;

import org.eclipse.jface.wizard.IWizardPage;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.JSSWizard;

public class ChartWizard extends JSSWizard implements IExpressionContextSetter{
	private ChartTypeWizardPage page0;
	private ChartDataPage step1;
	private MGraphicElement chart;
	private JRDesignElementDataset edataset;
	private ExpressionContext expContext;

	private int width;
	private int height;
	private boolean skipFirstPage = false;

	public ChartWizard(MGraphicElement chart, JRDesignElementDataset edataset,
			boolean skipFirstPage) {
		this(chart, edataset);
		this.skipFirstPage = skipFirstPage;
		setNeedsProgressMonitor(false);
	}

	public ChartWizard(MGraphicElement chart, JRDesignElementDataset edataset) {
		super();
		setWindowTitle(Messages.common_chart_wizard);
		this.chart = chart;
		this.edataset = edataset;
		JRDesignElement jrChart = (JRDesignElement) chart.getValue();
		width = jrChart.getWidth();
		height = jrChart.getHeight();
	}

	@Override
	public void addPages() {
		if (chart instanceof MChart) {
			page0 = new ChartTypeWizardPage((MChart) chart);
			addPage(page0);
		}

		step1 = new ChartDataPage((JRDesignElement) chart.getValue(), edataset,
				getConfig());
		step1.setExpressionContext(expContext);
		addPage(step1);
	}

	public MGraphicElement getChart() {
		JRDesignElement jrChart = (JRDesignElement) chart.getValue();
		jrChart.setWidth(width);
		jrChart.setHeight(height);
		return chart;
	}

	@Override
	public IWizardPage getStartingPage() {
		if (skipFirstPage && page0 != null)
			return step1;
		return super.getStartingPage();
	}

	@Override
	public boolean performFinish() {
		if (page0 != null) {
			boolean finished = page0.isPageComplete() && step1.isPageComplete();
			page0.finishPage();
			return finished;
		}
		return step1.isPageComplete();
	}

	@Override
	public void init(JasperReportsConfiguration jConfig) {
		super.init(jConfig);
		if (chart != null)
			chart.setJasperConfiguration(jConfig);
	}
	
	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext=expContext;
		if(step1!=null){
			step1.setExpressionContext(expContext);
		}
	}
}
