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

import java.util.List;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateChartAxesCommand extends Command {

	private JRDesignChartAxis jrElement;

	private JRDesignMultiAxisPlot jrPlot;
	private JRDesignChart chart;
	private int index;
	private JasperDesign jDesign;

	/**
	 * Instantiates a new creates the element command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateChartAxesCommand(MChart destNode, MChartAxes srcNode, int newIndex) {
		this((JRDesignChart) destNode.getValue(), (JRDesignChartAxis) srcNode.getValue(), newIndex, destNode.getJasperDesign());
	}

	public CreateChartAxesCommand(JRDesignChart chart, JRDesignChartAxis chartAxis, int newIndex, JasperDesign jDesign) {
		super();
		this.jrElement = chartAxis;
		this.chart = chart;
		this.jrPlot = (JRDesignMultiAxisPlot) chart.getPlot();
		this.index = newIndex;
		this.jDesign = jDesign;
	}

	/**
	 * Creates the object.
	 */
	protected void createObject() {
		if (jrElement == null) {
			// here put a wizard
			List<JRChartAxis> axes = jrPlot.getAxes();
			Class<? extends JRChartPlot> chartplotclass = null;
			if (!axes.isEmpty())
				chartplotclass = axes.get(0).getChart().getPlot().getClass();
			ChartAxesWizard wizard = new ChartAxesWizard(chartplotclass);
			WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				byte type = wizard.getChartAxis();
				// JRDesignChart chart = (JRDesignChart) jrPlot.getChart();
				jrElement = new JRDesignChartAxis(this.chart);
				JRDesignChart c = MChart.createJRElement(jDesign, type);
				jrElement.setChart(c);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (jrElement != null) {
			if (index >= 0 && index < jrPlot.getAxes().size())
				jrPlot.addAxis(index, jrElement);
			else
				jrPlot.addAxis(jrElement);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrPlot == null || jrElement == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrPlot.removeAxis(jrElement);
	}

}
