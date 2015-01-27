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
package com.jaspersoft.studio.components.chart.model.theme;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.chartthemes.simple.LegendSettings;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockFrame;
import org.jfree.ui.RectangleInsets;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.enums.JFreeChartHorizontalAlignmentEnum;
import com.jaspersoft.studio.components.chart.model.enums.JFreeChartVerticalAlignmentEnum;
import com.jaspersoft.studio.components.chart.model.theme.paintprovider.PaintProviderPropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.util.PadUtil;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.model.text.MFontUtil;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.FontPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.utils.Misc;

public class MLegendSettings extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MLegendSettings(MChartThemeSettings parent, LegendSettings cts) {
		super(parent, -1);
		setValue(cts);
	}

	@Override
	public LegendSettings getValue() {
		return (LegendSettings) super.getValue();
	}

	@Override
	public ImageDescriptor getImagePath() {
		return null;
	}

	@Override
	public String getDisplayText() {
		return "Legend"; //$NON-NLS-1$
	}

	private IPropertyDescriptor[] descriptors;
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
		PropertyDescriptor pd = new CheckBoxPropertyDescriptor(LegendSettings.PROPERTY_showLegend, Messages.MLegendSettings_showLegendTitle);
		pd.setDescription(Messages.MLegendSettings_showLegendDescription);
		desc.add(pd);

		pd = new FontPropertyDescriptor(LegendSettings.PROPERTY_font, "Font");
		pd.setDescription("Font");
		desc.add(pd);

		posD = new JSSEnumPropertyDescriptor(LegendSettings.PROPERTY_position, Messages.MLegendSettings_positionTitle, EdgeEnum.class, NullEnum.NULL);
		posD.setDescription(Messages.MLegendSettings_positionDescription);
		desc.add(posD);

		hp = new JSSEnumPropertyDescriptor(LegendSettings.PROPERTY_horizontalAlignment, Messages.MLegendSettings_legendHAlignTitle, JFreeChartHorizontalAlignmentEnum.class, NullEnum.NULL);
		hp.setDescription(Messages.MLegendSettings_legendHAlignDescription);
		desc.add(hp);

		vp = new JSSEnumPropertyDescriptor(LegendSettings.PROPERTY_verticalAlignment, Messages.MLegendSettings_legendVAlignTitle, JFreeChartVerticalAlignmentEnum.class, NullEnum.NULL);
		vp.setDescription(Messages.MLegendSettings_legendVAlignDescription);
		desc.add(vp);

		PadUtil.createPropertyDescriptors(desc, defaultsMap);

		pd = new PaintProviderPropertyDescriptor(LegendSettings.PROPERTY_foregroundPaint, Messages.MLegendSettings_legendForegroundColorTitle);
		pd.setDescription(Messages.MLegendSettings_legendForegroundColorDescription);
		desc.add(pd);

		pd = new PaintProviderPropertyDescriptor(LegendSettings.PROPERTY_backgroundPaint, Messages.MLegendSettings_legendBackgroundColorTitle);
		pd.setDescription(Messages.MLegendSettings_legendBackgroundColorDescription);
		desc.add(pd);

		PadUtil.createPropertyDescriptors(desc, defaultsMap, LegendSettings.PROPERTY_blockFrame, "Block Frame"); //$NON-NLS-1$

		defaultsMap.put(LegendSettings.PROPERTY_backgroundPaint, null);
		defaultsMap.put(LegendSettings.PROPERTY_foregroundPaint, null);

		defaultsMap.put(LegendSettings.PROPERTY_showLegend, Boolean.TRUE);
		defaultsMap.put(LegendSettings.PROPERTY_position, EdgeEnum.TOP);
		defaultsMap.put(LegendSettings.PROPERTY_horizontalAlignment, JFreeChartHorizontalAlignmentEnum.LEFT);
		defaultsMap.put(LegendSettings.PROPERTY_verticalAlignment, JFreeChartVerticalAlignmentEnum.TOP);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/sample.reference/chartthemes/index.html#chartthemes"); //$NON-NLS-1$
	}

	private static JSSEnumPropertyDescriptor posD;
	private static JSSEnumPropertyDescriptor hp;
	private static JSSEnumPropertyDescriptor vp;
	private MFont clFont;

	@Override
	public Object getPropertyValue(Object id) {
		LegendSettings ts = getValue();
		if (id.equals(LegendSettings.PROPERTY_showLegend))
			return Misc.nvl(ts.getShowLegend(), Boolean.TRUE);
		if (id.equals(LegendSettings.PROPERTY_font)) {
			clFont = MFontUtil.getMFont(clFont, ts.getFont(), null, this);
			return clFont;
		}
		if (id.equals(LegendSettings.PROPERTY_position))
			return posD.getEnumValue(ts.getPositionValue());
		if (id.equals(LegendSettings.PROPERTY_horizontalAlignment))
			return hp.getEnumValue(JFreeChartHorizontalAlignmentEnum.getValue(ts.getHorizontalAlignment()));
		if (id.equals(LegendSettings.PROPERTY_verticalAlignment))
			return vp.getEnumValue(JFreeChartVerticalAlignmentEnum.getValue(ts.getVerticalAlignment()));
		if (id.equals(LegendSettings.PROPERTY_backgroundPaint))
			return ts.getBackgroundPaint();
		if (id.equals(LegendSettings.PROPERTY_foregroundPaint))
			return ts.getForegroundPaint();

		Object pad = PadUtil.getPropertyValue(id, ts.getPadding());
		if (pad != null)
			return pad;
		BlockFrame bf = ts.getBlockFrame();
		if (bf != null) {
			pad = PadUtil.getPropertyValue(id, bf.getInsets(), LegendSettings.PROPERTY_blockFrame);
			if (pad != null)
				return pad;
		}
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		LegendSettings ts = getValue();
		if (id.equals(LegendSettings.PROPERTY_showLegend))
			ts.setShowLegend((Boolean) value);
		else if (id.equals(LegendSettings.PROPERTY_font))
			ts.setFont(MFontUtil.setMFont(value));
		else if (id.equals(LegendSettings.PROPERTY_position))
			ts.setPosition((EdgeEnum) posD.getEnumValue(value));
		else if (id.equals(LegendSettings.PROPERTY_horizontalAlignment))
			ts.setHorizontalAlignment(((JFreeChartHorizontalAlignmentEnum) hp.getEnumValue(value)).getJFreeChartValue());
		else if (id.equals(LegendSettings.PROPERTY_verticalAlignment))
			ts.setVerticalAlignment(((JFreeChartVerticalAlignmentEnum) vp.getEnumValue(value)).getJFreeChartValue());
		else if (id.equals(LegendSettings.PROPERTY_backgroundPaint))
			ts.setBackgroundPaint((PaintProvider) value);
		else if (id.equals(LegendSettings.PROPERTY_foregroundPaint))
			ts.setForegroundPaint((PaintProvider) value);

		RectangleInsets ri = PadUtil.setPropertyValue(id, value, ts.getPadding());
		if (ri != null)
			ts.setPadding(ri);
		BlockFrame bf = ts.getBlockFrame();
		ri = PadUtil.setPropertyValue(id, value, bf == null ? null : bf.getInsets(), LegendSettings.PROPERTY_blockFrame);
		if (ri != null)
			ts.setBlockFrame(new BlockBorder(ri.getTop(), ri.getLeft(), ri.getBottom(), ri.getRight()));
	}
}
