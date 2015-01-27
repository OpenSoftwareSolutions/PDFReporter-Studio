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
package com.jaspersoft.studio.components.chart.model.dataset;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.design.JRDesignHighLowDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MHyperLink;
import com.jaspersoft.studio.property.descriptor.JRPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;

public class MChartHighLowDataset extends MChartDataset {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MChartHighLowDataset(ANode parent, JRDesignHighLowDataset value,
			JasperDesign jasperDesign) {
		super(parent, value, jasperDesign);
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
		super.createPropertyDescriptors(desc, defaultsMap);

		JRExpressionPropertyDescriptor closeExprD = new JRExpressionPropertyDescriptor(
				JRDesignHighLowDataset.PROPERTY_CLOSE_EXPRESSION,
				Messages.MChartHighLowDataset_close_expression);
		closeExprD
				.setDescription(Messages.MChartHighLowDataset_close_expression_description);
		desc.add(closeExprD);
		closeExprD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#closeExpression"));

		JRExpressionPropertyDescriptor dateExprD = new JRExpressionPropertyDescriptor(
				JRDesignHighLowDataset.PROPERTY_DATE_EXPRESSION,
				Messages.MChartHighLowDataset_data_expression);
		dateExprD
				.setDescription(Messages.MChartHighLowDataset_data_expression_description);
		desc.add(dateExprD);
		dateExprD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#dateExpression"));

		JRExpressionPropertyDescriptor highExprD = new JRExpressionPropertyDescriptor(
				JRDesignHighLowDataset.PROPERTY_HIGH_EXPRESSION,
				Messages.MChartHighLowDataset_high_expression);
		highExprD
				.setDescription(Messages.MChartHighLowDataset_high_expression_description);
		desc.add(highExprD);
		highExprD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#highExpression"));

		JRExpressionPropertyDescriptor lowExprD = new JRExpressionPropertyDescriptor(
				JRDesignHighLowDataset.PROPERTY_LOW_EXPRESSION,
				Messages.MChartHighLowDataset_low_expression);
		lowExprD.setDescription(Messages.MChartHighLowDataset_low_expression_description);
		desc.add(lowExprD);
		lowExprD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#lowExpression"));

		JRExpressionPropertyDescriptor openExprD = new JRExpressionPropertyDescriptor(
				JRDesignHighLowDataset.PROPERTY_OPEN_EXPRESSION,
				Messages.MChartHighLowDataset_open_expression);
		openExprD
				.setDescription(Messages.MChartHighLowDataset_open_expression_description);
		desc.add(openExprD);
		openExprD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#openExpression"));

		JRExpressionPropertyDescriptor seriesExprD = new JRExpressionPropertyDescriptor(
				JRDesignHighLowDataset.PROPERTY_SERIES_EXPRESSION,
				Messages.common_series_expression);
		seriesExprD
				.setDescription(Messages.MChartHighLowDataset_series_expression_description);
		desc.add(seriesExprD);
		seriesExprD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#seriesExpression"));

		JRExpressionPropertyDescriptor volumeExprD = new JRExpressionPropertyDescriptor(
				JRDesignHighLowDataset.PROPERTY_VOLUME_EXPRESSION,
				Messages.MChartHighLowDataset_volume_expression);
		volumeExprD
				.setDescription(Messages.MChartHighLowDataset_volume_expression_description);
		desc.add(volumeExprD);
		volumeExprD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#volumeExpression"));

		JRPropertyDescriptor hyperLinkD = new JRPropertyDescriptor(
				JRDesignHighLowDataset.PROPERTY_ITEM_HYPERLINK,
				Messages.common_item_hyperlink);
		hyperLinkD
				.setDescription(Messages.MChartHighLowDataset_item_hyperlink_description);
		desc.add(hyperLinkD);
		hyperLinkD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#itemHyperlink"));

		closeExprD
				.setCategory(Messages.MChartHighLowDataset_chart_highlow_dataset_category);
		dateExprD
				.setCategory(Messages.MChartHighLowDataset_chart_highlow_dataset_category);
		highExprD
				.setCategory(Messages.MChartHighLowDataset_chart_highlow_dataset_category);
		lowExprD.setCategory(Messages.MChartHighLowDataset_chart_highlow_dataset_category);
		openExprD
				.setCategory(Messages.MChartHighLowDataset_chart_highlow_dataset_category);
		seriesExprD
				.setCategory(Messages.MChartHighLowDataset_chart_highlow_dataset_category);
		volumeExprD
				.setCategory(Messages.MChartHighLowDataset_chart_highlow_dataset_category);
		hyperLinkD
				.setCategory(Messages.MChartHighLowDataset_chart_highlow_dataset_category);

		defaultsMap.put(JRDesignHighLowDataset.PROPERTY_CLOSE_EXPRESSION, null);
		defaultsMap.put(JRDesignHighLowDataset.PROPERTY_DATE_EXPRESSION, null);
		defaultsMap.put(JRDesignHighLowDataset.PROPERTY_HIGH_EXPRESSION, null);
		defaultsMap.put(JRDesignHighLowDataset.PROPERTY_LOW_EXPRESSION, null);
		defaultsMap.put(JRDesignHighLowDataset.PROPERTY_OPEN_EXPRESSION, null);
		defaultsMap
				.put(JRDesignHighLowDataset.PROPERTY_SERIES_EXPRESSION, null);
		defaultsMap
				.put(JRDesignHighLowDataset.PROPERTY_VOLUME_EXPRESSION, null);
	}

	private MHyperLink mHyperLink;

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignHighLowDataset jrElement = (JRDesignHighLowDataset) getValue();

		if (id.equals(JRDesignHighLowDataset.PROPERTY_ITEM_HYPERLINK)) {
			if (mHyperLink == null) {
				JRHyperlink itemHyperlink = jrElement.getItemHyperlink();
				if (itemHyperlink == null)
					itemHyperlink = new JRDesignHyperlink();
				mHyperLink = new MHyperLink(itemHyperlink);
				setChildListener(mHyperLink);
			}
			return mHyperLink;
		}
		if (id.equals(JRDesignHighLowDataset.PROPERTY_CLOSE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getCloseExpression());
		if (id.equals(JRDesignHighLowDataset.PROPERTY_DATE_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getDateExpression());
		if (id.equals(JRDesignHighLowDataset.PROPERTY_HIGH_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getHighExpression());
		if (id.equals(JRDesignHighLowDataset.PROPERTY_LOW_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getLowExpression());
		if (id.equals(JRDesignHighLowDataset.PROPERTY_OPEN_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getOpenExpression());
		if (id.equals(JRDesignHighLowDataset.PROPERTY_SERIES_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getSeriesExpression());
		if (id.equals(JRDesignHighLowDataset.PROPERTY_VOLUME_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getVolumeExpression());

		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignHighLowDataset jrElement = (JRDesignHighLowDataset) getValue();

		if (id.equals(JRDesignHighLowDataset.PROPERTY_CLOSE_EXPRESSION))
			jrElement.setCloseExpression(ExprUtil.setValues(
					jrElement.getCloseExpression(), value));
		else if (id.equals(JRDesignHighLowDataset.PROPERTY_DATE_EXPRESSION))
			jrElement.setDateExpression(ExprUtil.setValues(
					jrElement.getDateExpression(), value));
		else if (id.equals(JRDesignHighLowDataset.PROPERTY_HIGH_EXPRESSION))
			jrElement.setHighExpression(ExprUtil.setValues(
					jrElement.getHighExpression(), value));
		else if (id.equals(JRDesignHighLowDataset.PROPERTY_LOW_EXPRESSION))
			jrElement.setLowExpression(ExprUtil.setValues(
					jrElement.getLowExpression(), value));
		else if (id.equals(JRDesignHighLowDataset.PROPERTY_OPEN_EXPRESSION))
			jrElement.setOpenExpression(ExprUtil.setValues(
					jrElement.getOpenExpression(), value));
		else if (id.equals(JRDesignHighLowDataset.PROPERTY_SERIES_EXPRESSION))
			jrElement.setSeriesExpression(ExprUtil.setValues(
					jrElement.getSeriesExpression(), value));
		else if (id.equals(JRDesignHighLowDataset.PROPERTY_VOLUME_EXPRESSION))
			jrElement.setVolumeExpression(ExprUtil.setValues(
					jrElement.getVolumeExpression(), value));
		else
			super.setPropertyValue(id, value);
	}

}
