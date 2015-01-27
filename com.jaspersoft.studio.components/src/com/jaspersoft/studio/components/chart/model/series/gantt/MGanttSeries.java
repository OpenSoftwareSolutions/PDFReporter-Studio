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
package com.jaspersoft.studio.components.chart.model.series.gantt;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.design.JRDesignGanttSeries;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.ChartNodeIconDescriptor;
import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MHyperLink;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.JRPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;

public class MGanttSeries extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new ChartNodeIconDescriptor("ganttseries"); //$NON-NLS-1$
		return iconDescriptor;
	}

	public MGanttSeries() {
		super();
	}

	public MGanttSeries(ANode parent, JRDesignGanttSeries value, int newIndex) {
		super(parent, -1);
		setValue(value);
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;

	@Override
	public Map<String, Object> getDefaultsMap() {
		return defaultsMap;
	}

	@Override
	public IPropertyDescriptor[] getDescriptors() {
		return descriptors;
	}

	@Override
	public void setDescriptors(IPropertyDescriptor[] descriptors1,
			Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *            the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc,
			Map<String, Object> defaultsMap) {

		JRExpressionPropertyDescriptor endDateExpD = new JRExpressionPropertyDescriptor(
				JRDesignGanttSeries.PROPERTY_END_DATE_EXPRESSION,
				Messages.common_end_date_expression);
		endDateExpD
				.setDescription(Messages.MGanttSeries_end_date_expression_description);
		desc.add(endDateExpD);

		JRExpressionPropertyDescriptor lblExprD = new JRExpressionPropertyDescriptor(
				JRDesignGanttSeries.PROPERTY_LABEL_EXPRESSION,
				Messages.common_label_expression);
		lblExprD.setDescription(Messages.MGanttSeries_label_expression_description);
		desc.add(lblExprD);

		JRExpressionPropertyDescriptor seriesExprD = new JRExpressionPropertyDescriptor(
				JRDesignGanttSeries.PROPERTY_SERIES_EXPRESSION,
				Messages.common_series_expression);
		seriesExprD
				.setDescription(Messages.MGanttSeries_series_expression_description);
		desc.add(seriesExprD);

		JRExpressionPropertyDescriptor percExprD = new JRExpressionPropertyDescriptor(
				JRDesignGanttSeries.PROPERTY_PERCENT_EXPRESSION,
				Messages.MGanttSeries_percent_expression);
		percExprD
				.setDescription(Messages.MGanttSeries_percent_expression_description);
		desc.add(percExprD);

		JRExpressionPropertyDescriptor startDateExprD = new JRExpressionPropertyDescriptor(
				JRDesignGanttSeries.PROPERTY_START_DATE_EXPRESSION,
				Messages.common_start_date_expression);
		startDateExprD
				.setDescription(Messages.MGanttSeries_start_date_expression_description);
		desc.add(startDateExprD);

		JRExpressionPropertyDescriptor subtaskExprD = new JRExpressionPropertyDescriptor(
				JRDesignGanttSeries.PROPERTY_SUBTASK_EXPRESSION,
				Messages.MGanttSeries_subtask_expression);
		subtaskExprD
				.setDescription(Messages.MGanttSeries_subtask_expression_description);
		desc.add(subtaskExprD);

		JRExpressionPropertyDescriptor taskExprD = new JRExpressionPropertyDescriptor(
				JRDesignGanttSeries.PROPERTY_TASK_EXPRESSION,
				Messages.MGanttSeries_task_expression);
		taskExprD
				.setDescription(Messages.MGanttSeries_task_expression_description);
		desc.add(taskExprD);

		JRPropertyDescriptor itemHyperLinkD = new JRPropertyDescriptor(
				JRDesignGanttSeries.PROPERTY_ITEM_HYPERLINK,
				Messages.common_item_hyperlink);
		itemHyperLinkD
				.setDescription(Messages.MGanttSeries_item_hyperlink_description);
		desc.add(itemHyperLinkD);

		defaultsMap.put(JRDesignGanttSeries.PROPERTY_END_DATE_EXPRESSION, null);
		defaultsMap.put(JRDesignGanttSeries.PROPERTY_LABEL_EXPRESSION, null);
		defaultsMap.put(JRDesignGanttSeries.PROPERTY_SERIES_EXPRESSION, null);
		defaultsMap.put(JRDesignGanttSeries.PROPERTY_START_DATE_EXPRESSION,
				null);
		defaultsMap.put(JRDesignGanttSeries.PROPERTY_SUBTASK_EXPRESSION, null);
		defaultsMap.put(JRDesignGanttSeries.PROPERTY_TASK_EXPRESSION, null);
	}

	private MHyperLink mHyperLink;

	public Object getPropertyValue(Object id) {
		JRDesignGanttSeries jrElement = (JRDesignGanttSeries) getValue();

		if (id.equals(JRDesignGanttSeries.PROPERTY_ITEM_HYPERLINK)) {
			JRHyperlink itemHyperLink = jrElement.getItemHyperlink();
			if (itemHyperLink == null)
				itemHyperLink = new JRDesignHyperlink();
			mHyperLink = new MHyperLink(itemHyperLink);
			setChildListener(mHyperLink);
			return mHyperLink;
		}
		if (id.equals(JRDesignGanttSeries.PROPERTY_END_DATE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getEndDateExpression());
		if (id.equals(JRDesignGanttSeries.PROPERTY_PERCENT_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getPercentExpression());
		if (id.equals(JRDesignGanttSeries.PROPERTY_LABEL_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getLabelExpression());
		if (id.equals(JRDesignGanttSeries.PROPERTY_SERIES_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getSeriesExpression());
		if (id.equals(JRDesignGanttSeries.PROPERTY_START_DATE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getStartDateExpression());
		if (id.equals(JRDesignGanttSeries.PROPERTY_SUBTASK_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getSubtaskExpression());
		if (id.equals(JRDesignGanttSeries.PROPERTY_TASK_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getTaskExpression());

		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		JRDesignGanttSeries jrElement = (JRDesignGanttSeries) getValue();

		if (id.equals(JRDesignGanttSeries.PROPERTY_END_DATE_EXPRESSION))
			jrElement.setEndDateExpression(ExprUtil.setValues(
					jrElement.getEndDateExpression(), value));
		else if (id.equals(JRDesignGanttSeries.PROPERTY_LABEL_EXPRESSION))
			jrElement.setLabelExpression(ExprUtil.setValues(
					jrElement.getLabelExpression(), value));
		else if (id.equals(JRDesignGanttSeries.PROPERTY_PERCENT_EXPRESSION))
			jrElement.setPercentExpression(ExprUtil.setValues(
					jrElement.getPercentExpression(), value));
		else if (id.equals(JRDesignGanttSeries.PROPERTY_SERIES_EXPRESSION))
			jrElement.setSeriesExpression(ExprUtil.setValues(
					jrElement.getSeriesExpression(), value));
		else if (id.equals(JRDesignGanttSeries.PROPERTY_START_DATE_EXPRESSION))
			jrElement.setStartDateExpression(ExprUtil.setValues(
					jrElement.getStartDateExpression(), value));
		else if (id.equals(JRDesignGanttSeries.PROPERTY_SUBTASK_EXPRESSION))
			jrElement.setSubtaskExpression(ExprUtil.setValues(
					jrElement.getSubtaskExpression(), value));
		else if (id.equals(JRDesignGanttSeries.PROPERTY_TASK_EXPRESSION))
			jrElement.setTaskExpression(ExprUtil.setValues(
					jrElement.getTaskExpression(), value));
	}

	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

}
