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
package com.jaspersoft.studio.components.chart.model.chartAxis;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.JRChartAxis;
import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.charts.type.AxisPositionEnum;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.ChartNodeIconDescriptor;
import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.components.chart.model.plot.PlotFactory;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.IDragable;
import com.jaspersoft.studio.model.IPastableGraphic;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.JRPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;

public class MChartAxes extends APropertyNode implements IDragable, ICopyable {
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
			iconDescriptor = new ChartNodeIconDescriptor("chartaxis"); //$NON-NLS-1$
		return iconDescriptor;
	}

	public String getDisplayText() {
		JRChartAxis ca = (JRChartAxis) getValue();
		if (ca != null) {
			JRChart c = ca.getChart();
			String dt = PlotFactory.getChartPlot(c.getPlot()).getDisplayText();
			if (c.getKey() != null && !c.getKey().trim().isEmpty())
				dt += " (" + c.getKey() + ")";
			return dt;
		}
		return getIconDescriptor().getTitle();
	}

	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	public MChartAxes() {
		super();
	}

	public MChartAxes(ANode parent, JRChartAxis jrChart, int newIndex) {
		super(parent, newIndex);
		setValue(jrChart);
	}

	@Override
	public JRChartAxis getValue() {
		return (JRChartAxis) super.getValue();
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
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		// super.createPropertyDescriptors(desc, defaultsMap);

		positionD = new JSSEnumPropertyDescriptor(JRDesignChartAxis.PROPERTY_POSITION, Messages.MChartAxes_position, AxisPositionEnum.class, NullEnum.NOTNULL);
		positionD.setDescription(Messages.MChartAxes_position_description);
		desc.add(positionD);

		JRPropertyDescriptor chartD = new JRPropertyDescriptor(JRDesignChartAxis.PROPERTY_CHART, Messages.MChartAxes_chart);
		chartD.setDescription(Messages.MChartAxes_chart_description);
		desc.add(chartD);
		//
		// if (mChart == null) {
		// mChart = new MChart();
		// mChart.setValue(((JRChartAxis) getValue()).getChart());
		// }
		// mChart.createPropertyDescriptors(desc, defaultsMap);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#axis");
	}

	@Override
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		// initialize style
		JasperDesign jasperDesign = getJasperDesign();
		if (jasperDesign != null) {
			// initialize groups
			JRGroup[] groups = jasperDesign.getGroups();
			String[] items = new String[groups.length + 1];
			items[0] = ""; //$NON-NLS-1$
			for (int j = 0; j < groups.length; j++) {
				items[j + 1] = groups[j].getName();
			}
			setGroupItems(items);
		}
	}

	protected void setGroupItems(String[] items) {
		if (mChart != null)
			mChart.setGroupItems(items);
	}

	private MChart mChart;
	private static JSSEnumPropertyDescriptor positionD;

	public Object getPropertyValue(Object id) {
		JRDesignChartAxis jrElement = (JRDesignChartAxis) getValue();

		if (id.equals(JRDesignChartAxis.PROPERTY_POSITION))
			return positionD.getEnumValue(jrElement.getPositionValue());

		if (id.equals(JRDesignChartAxis.PROPERTY_CHART)) {
			if (mChart == null)
				mChart = new MChart();
			mChart.setJasperConfiguration(getJasperConfiguration());
			setChildListener(mChart);
			mChart.setValue(jrElement.getChart());
			return mChart;
		}
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		JRDesignChartAxis jrElement = (JRDesignChartAxis) getValue();
		if (id.equals(JRDesignChartAxis.PROPERTY_POSITION))
			jrElement.setPosition((AxisPositionEnum) positionD.getEnumValue(value));
		else if (mChart != null) {
			mChart.setPropertyValue(id, value);
		}
	}

	@Override
	public void setValue(Object value) {
		JRDesignChartAxis oldObject = (JRDesignChartAxis) getValue();
		JRDesignChartAxis newObject = (JRDesignChartAxis) value;

		if (oldObject != null)
			((JRDesignChart) oldObject.getChart()).getEventSupport().removePropertyChangeListener(this);
		if (newObject != null)
			((JRDesignChart) newObject.getChart()).getEventSupport().addPropertyChangeListener(this);
		super.setValue(value);
	}

	@Override
	public boolean isCopyable2(Object parent) {
		if (parent instanceof MChart || parent instanceof MElementGroup || parent instanceof IPastableGraphic)
			return true;
		return false;
	}

}
