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

import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.engine.design.JRDesignChart;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;
import com.jaspersoft.studio.model.ANode;
/*
 * The Class ReorderElementCommand.
 */
public class ReorderChartAxesCommand extends Command {

	/** The new index. */
	private int oldIndex, newIndex;

	/** The jr element. */
	private JRDesignChartAxis jrElement;

	/** The jr group. */
	private JRDesignMultiAxisPlot jrGroup;

	/**
	 * Instantiates a new reorder element command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public ReorderChartAxesCommand(MChartAxes child, ANode parent, int newIndex) {
		super(Messages.common_reorder_elements);
		this.newIndex = Math.max(0, newIndex);
		this.jrElement = (JRDesignChartAxis) child.getValue();
		this.jrGroup = (JRDesignMultiAxisPlot) ((JRDesignChart) parent.getValue()).getPlot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldIndex = jrGroup.getAxes().indexOf(jrElement);

		jrGroup.removeAxis(jrElement);
		if (newIndex >= 0 && newIndex < jrGroup.getAxes().size())
			jrGroup.addAxis(newIndex, jrElement);
		else
			jrGroup.addAxis(jrElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrGroup.removeAxis(jrElement);
		if (oldIndex >= 0 && oldIndex < jrGroup.getAxes().size())
			jrGroup.addAxis(oldIndex, jrElement);
		else
			jrGroup.addAxis(jrElement);
	}

}
