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

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.CollectionElementAddedEvent;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MHyperLink;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.property.descriptor.JRPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.FloatPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;

public class MChartPieDataset extends MChartDataset {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MChartPieDataset(ANode parent, JRDesignPieDataset value,
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

		// JRExpressionPropertyDescriptor keyExprD = new
		// JRExpressionPropertyDescriptor(
		// JRDesignPieDataset.PROPERTY_KEY_EXPRESSION,
		// Messages.common_key_expression);
		// keyExprD.setDescription(Messages.MChartPieDataset_key_expression_description);
		// desc.add(keyExprD);
		//
		// JRExpressionPropertyDescriptor lblExprD = new
		// JRExpressionPropertyDescriptor(
		// JRDesignPieDataset.PROPERTY_LABEL_EXPRESSION,
		// Messages.common_label_expression);
		// lblExprD.setDescription(Messages.MChartPieDataset_label_expression_description);
		// desc.add(lblExprD);

		JRExpressionPropertyDescriptor oKeyExprD = new JRExpressionPropertyDescriptor(
				JRDesignPieDataset.PROPERTY_OTHER_KEY_EXPRESSION,
				Messages.MChartPieDataset_other_key_expression);
		oKeyExprD
				.setDescription(Messages.MChartPieDataset_other_key_expression_description);
		desc.add(oKeyExprD);
		oKeyExprD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#otherKeyExpression"));

		JRExpressionPropertyDescriptor oLblExprD = new JRExpressionPropertyDescriptor(
				JRDesignPieDataset.PROPERTY_OTHER_LABEL_EXPRESSION,
				Messages.MChartPieDataset_other_label_expression);
		oLblExprD
				.setDescription(Messages.MChartPieDataset_other_label_expression_description);
		desc.add(oLblExprD);
		oLblExprD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#otherLabelExpression"));

		// JRExpressionPropertyDescriptor valExpD = new
		// JRExpressionPropertyDescriptor(
		// JRDesignPieDataset.PROPERTY_VALUE_EXPRESSION,
		// Messages.common_value_expression);
		// valExpD.setDescription(Messages.MChartPieDataset_value_expression_description);
		// desc.add(valExpD);

		JRPropertyDescriptor oSectHyperLD = new JRPropertyDescriptor(
				JRDesignPieDataset.PROPERTY_OTHER_SECTION_HYPERLINK,
				Messages.MChartPieDataset_other_section_hyperlink);
		oSectHyperLD
				.setDescription(Messages.MChartPieDataset_other_section_hyperlink_description);
		desc.add(oSectHyperLD);
		oSectHyperLD
				.setHelpRefBuilder(new HelpReferenceBuilder(
						"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#otherSectionHyperlink"));

		// JRPropertyDescriptor sectHyperLD = new JRPropertyDescriptor(
		// JRDesignPieDataset.PROPERTY_SECTION_HYPERLINK,
		// Messages.common_section_hyperlink);
		// sectHyperLD
		// .setDescription(Messages.MChartPieDataset_section_hyperlink_description);
		// desc.add(sectHyperLD);

		IntegerPropertyDescriptor maxCountD = new IntegerPropertyDescriptor(
				JRDesignPieDataset.PROPERTY_MAX_COUNT,
				Messages.MChartPieDataset_max_count);
		maxCountD
				.setDescription(Messages.MChartPieDataset_max_count_description);
		desc.add(maxCountD);

		FloatPropertyDescriptor minPercD = new FloatPropertyDescriptor(
				JRDesignPieDataset.PROPERTY_MIN_PERCENTAGE,
				Messages.MChartPieDataset_min_percentage);
		minPercD.setDescription(Messages.MChartPieDataset_min_percentage_description);
		desc.add(minPercD);

		// keyExprD.setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);
		// lblExprD.setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);
		oKeyExprD
				.setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);
		oLblExprD
				.setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);
		// valExpD.setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);
		oSectHyperLD
				.setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);
		// sectHyperLD
		// .setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);

		maxCountD
				.setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);
		minPercD.setCategory(Messages.MChartPieDataset_chart_pie_dataset_category);

		defaultsMap.put(JRDesignPieDataset.PROPERTY_KEY_EXPRESSION, null);
		defaultsMap.put(JRDesignPieDataset.PROPERTY_LABEL_EXPRESSION, null);
		defaultsMap.put(JRDesignPieDataset.PROPERTY_OTHER_KEY_EXPRESSION, null);
		defaultsMap.put(JRDesignPieDataset.PROPERTY_OTHER_LABEL_EXPRESSION,
				null);
		defaultsMap.put(JRDesignPieDataset.PROPERTY_VALUE_EXPRESSION, null);

		setHelpPrefix(desc,
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#pieDataset");
	}

	private MHyperLink omHyperLink;

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignPieDataset jrElement = (JRDesignPieDataset) getValue();

		if (id.equals(JRDesignPieDataset.PROPERTY_MIN_PERCENTAGE))
			return jrElement.getMinPercentage();
		if (id.equals(JRDesignPieDataset.PROPERTY_MAX_COUNT))
			return jrElement.getMaxCount();
		// if (id.equals(JRDesignPieDataset.PROPERTY_SECTION_HYPERLINK)) {
		// JRHyperlink sectionHyperlink = jrElement.getSectionHyperlink();
		// if (sectionHyperlink == null)
		// sectionHyperlink = new JRDesignHyperlink();
		// mHyperLink = new MHyperLink(sectionHyperlink);
		// setChildListener(mHyperLink);
		// return mHyperLink;
		// }
		if (id.equals(JRDesignPieDataset.PROPERTY_OTHER_SECTION_HYPERLINK)) {
			JRHyperlink otherSectionHyperlink = jrElement
					.getOtherSectionHyperlink();
			if (otherSectionHyperlink == null)
				otherSectionHyperlink = new JRDesignHyperlink();
			omHyperLink = new MHyperLink(otherSectionHyperlink);
			setChildListener(omHyperLink);
			return omHyperLink;
		}
		// if (id.equals(JRDesignPieDataset.PROPERTY_KEY_EXPRESSION)) {
		// cExpression = ExprUtil.getExpression(this, cExpression,
		// jrElement.getKeyExpression());
		// return cExpression;
		// }
		// if (id.equals(JRDesignPieDataset.PROPERTY_LABEL_EXPRESSION)) {
		// dExpression = ExprUtil.getExpression(this, dExpression,
		// jrElement.getLabelExpression());
		// return dExpression;
		// }
		if (id.equals(JRDesignPieDataset.PROPERTY_OTHER_KEY_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getOtherKeyExpression());
		if (id.equals(JRDesignPieDataset.PROPERTY_OTHER_LABEL_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getOtherLabelExpression());
		// if (id.equals(JRDesignPieDataset.PROPERTY_VALUE_EXPRESSION)) {
		// oExpression = ExprUtil.getExpression(this, oExpression,
		// jrElement.getValueExpression());
		// return oExpression;
		// }

		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignPieDataset jrElement = (JRDesignPieDataset) getValue();

		if (id.equals(JRDesignPieDataset.PROPERTY_MIN_PERCENTAGE))
			jrElement.setMinPercentage((Float) value);
		else if (id.equals(JRDesignPieDataset.PROPERTY_MAX_COUNT)){
			Integer intValue = (Integer)value;
			if (intValue == 0) intValue = null;
			jrElement.setMaxCount(intValue);
		}
		// else if (id.equals(JRDesignPieDataset.PROPERTY_KEY_EXPRESSION))
		// jrElement.setKeyExpression(ExprUtil.setValues(
		// jrElement.getKeyExpression(), value));
		// else if (id.equals(JRDesignPieDataset.PROPERTY_LABEL_EXPRESSION))
		// jrElement.setLabelExpression(ExprUtil.setValues(
		// jrElement.getLabelExpression(), value));
		else if (id.equals(JRDesignPieDataset.PROPERTY_OTHER_KEY_EXPRESSION))
			jrElement.setOtherKeyExpression(ExprUtil.setValues(
					jrElement.getOtherKeyExpression(), value));
		else if (id.equals(JRDesignPieDataset.PROPERTY_OTHER_LABEL_EXPRESSION))
			jrElement.setOtherLabelExpression(ExprUtil.setValues(
					jrElement.getOtherLabelExpression(), value));
		// else if (id.equals(JRDesignPieDataset.PROPERTY_VALUE_EXPRESSION))
		// jrElement.setValueExpression(ExprUtil.setValues(
		// jrElement.getValueExpression(), value));
		else
			super.setPropertyValue(id, value);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("pieSeries")) { //$NON-NLS-1$
			if (evt.getSource() == getValue()) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					int newIndex = -1;
					if (evt instanceof CollectionElementAddedEvent) {
						newIndex = ((CollectionElementAddedEvent) evt)
								.getAddedIndex();
					}
					ReportFactory.createNode(this, evt.getNewValue(), newIndex);
				} else if (evt.getOldValue() != null
						&& evt.getNewValue() == null) {
					// delete
					for (INode n : getChildren()) {
						if (n.getValue() == evt.getOldValue()) {
							removeChild((ANode) n);
							break;
						}
					}
				} else {
					// changed
					for (INode n : getChildren()) {
						if (n.getValue() == evt.getOldValue())
							n.setValue(evt.getNewValue());
					}
				}
			}
		}
		super.propertyChange(evt);
	}

}
