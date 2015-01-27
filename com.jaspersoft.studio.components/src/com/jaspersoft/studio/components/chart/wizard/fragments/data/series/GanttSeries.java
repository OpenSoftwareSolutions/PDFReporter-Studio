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
package com.jaspersoft.studio.components.chart.wizard.fragments.data.series;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.charts.design.JRDesignGanttSeries;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;

public class GanttSeries implements ISeriesFactory<JRGanttSeries> {

	public JRDesignGanttSeries createSerie() {
		return createSerie(new JRDesignExpression("\"SERIES 1\""), null);
	}

	@Override
	public JRDesignGanttSeries createSerie(JRDesignExpression expr, JRGanttSeries prev) {
		JRDesignGanttSeries f = new JRDesignGanttSeries();
		f.setSeriesExpression(expr);
		if (prev == null) {
			f.setPercentExpression(new JRDesignExpression("0"));
			f.setStartDateExpression(new JRDesignExpression("new java.util.Date()"));
			f.setEndDateExpression(new JRDesignExpression("new java.util.Date()"));
			f.setTaskExpression(new JRDesignExpression("\"Task\""));
		} else {
			f.setPercentExpression(ExprUtil.clone(prev.getPercentExpression()));
			f.setStartDateExpression(ExprUtil.clone(prev.getStartDateExpression()));
			f.setEndDateExpression(ExprUtil.clone(prev.getEndDateExpression()));
			f.setTaskExpression(ExprUtil.clone(prev.getTaskExpression()));
			f.setLabelExpression(ExprUtil.clone(prev.getLabelExpression()));
		}
		return f;
	}

	public String getColumnText(Object element, int columnIndex) {

		JRGanttSeries dcs = (JRGanttSeries) element;
		switch (columnIndex) {
		case 0:
			if (dcs.getSeriesExpression() != null && dcs.getSeriesExpression().getText() != null)
				return dcs.getSeriesExpression().getText();
		}
		return ""; //$NON-NLS-1$
	}

	public Object getValue(JRGanttSeries element, String property) {
		JRGanttSeries prop = (JRGanttSeries) element;
		if ("NAME".equals(property)) { //$NON-NLS-1$
			return prop.getSeriesExpression();
		}
		return ""; //$NON-NLS-1$
	}

	public void modify(JRGanttSeries element, String property, Object value) {
		JRDesignGanttSeries data = (JRDesignGanttSeries) element;
		if ("NAME".equals(property) && value instanceof JRExpression) //$NON-NLS-1$
			data.setSeriesExpression((JRExpression) value);
	}

	private List<JRGanttSeries> vlist;

	public List<JRGanttSeries> getList() {
		return vlist;
	}

	public void setList(List<JRGanttSeries> vlist) {
		this.vlist = new ArrayList<JRGanttSeries>(vlist);
	}

}
