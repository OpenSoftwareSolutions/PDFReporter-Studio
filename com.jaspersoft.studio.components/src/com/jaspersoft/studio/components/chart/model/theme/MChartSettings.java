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

import java.awt.Stroke;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.chartthemes.simple.ChartSettings;
import net.sf.jasperreports.chartthemes.simple.FileImageProvider;
import net.sf.jasperreports.chartthemes.simple.ImageProvider;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jfree.ui.RectangleInsets;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.enums.JFreeChartAlignEnum;
import com.jaspersoft.studio.components.chart.model.theme.imageprovider.ImageProviderPropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.paintprovider.PaintProviderPropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.stroke.StrokePropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.util.PadUtil;
import com.jaspersoft.studio.jasper.CachedImageProvider;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.TransparencyPropertyDescriptor;
import com.jaspersoft.studio.utils.Misc;

public class MChartSettings extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MChartSettings(MChartThemeSettings parent, ChartSettings cs) {
		super(parent, -1);
		setValue(cs);
	}

	@Override
	public ChartSettings getValue() {
		return (ChartSettings) super.getValue();
	}

	@Override
	public ImageDescriptor getImagePath() {
		return null;
	}

	@Override
	public String getDisplayText() {
		return "Chart";
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
		PropertyDescriptor pd = new CheckBoxPropertyDescriptor(ChartSettings.PROPERTY_textAntiAlias, Messages.MChartSettings_textAATitle);
		pd.setDescription(Messages.MChartSettings_textAADescription);
		desc.add(pd);

		pd = new CheckBoxPropertyDescriptor(ChartSettings.PROPERTY_antiAlias, Messages.MChartSettings_antiAliasTitle);
		pd.setDescription(Messages.MChartSettings_antiAliasDescription);
		desc.add(pd);

		pd = new CheckBoxPropertyDescriptor(ChartSettings.PROPERTY_borderVisible, Messages.MChartSettings_borderVisibleTitle);
		pd.setDescription(Messages.MChartSettings_borderVisibleDescription);
		pd.setCategory("Borders"); //$NON-NLS-1$
		desc.add(pd);

		pd = new TransparencyPropertyDescriptor(ChartSettings.PROPERTY_backgroundImageAlpha, Messages.MChartSettings_imageAlphaTitle);
		pd.setDescription(Messages.MChartSettings_imageAlphaDescription);
		pd.setCategory("Background"); //$NON-NLS-1$
		desc.add(pd);

		bia = new JSSEnumPropertyDescriptor(ChartSettings.PROPERTY_backgroundImageAlignment, Messages.MChartSettings_imageAlignTitle, JFreeChartAlignEnum.class, NullEnum.NOTNULL);
		bia.setDescription(Messages.MChartSettings_imageAlignDescription);
		bia.setCategory("Background"); //$NON-NLS-1$
		desc.add(bia);

		PadUtil.createPropertyDescriptors(desc, defaultsMap);

		pd = new PaintProviderPropertyDescriptor(ChartSettings.PROPERTY_backgroundPaint, Messages.MChartSettings_paintTitle);
		pd.setDescription(Messages.MChartSettings_paintDescription);
		pd.setCategory("Background"); //$NON-NLS-1$
		desc.add(pd);

		pd = new PaintProviderPropertyDescriptor(ChartSettings.PROPERTY_borderPaint, Messages.MChartSettings_borderColorTitle);
		pd.setDescription(Messages.MChartSettings_borderColorDescription);
		pd.setCategory("Borders"); //$NON-NLS-1$
		desc.add(pd);

		pd = new ImageProviderPropertyDescriptor(ChartSettings.PROPERTY_backgroundImage, Messages.MChartSettings_backgroundImageTitle);
		pd.setDescription(Messages.MChartSettings_backgroundImageDescription);
		pd.setCategory("Background"); //$NON-NLS-1$
		desc.add(pd);

		pd = new StrokePropertyDescriptor(ChartSettings.PROPERTY_borderStroke, Messages.MChartSettings_borderStrokeTitle);
		pd.setDescription(Messages.MChartSettings_borderStrokeDescription);
		pd.setCategory("Borders"); //$NON-NLS-1$
		desc.add(pd);

		defaultsMap.put(ChartSettings.PROPERTY_backgroundPaint, null);
		defaultsMap.put(ChartSettings.PROPERTY_borderPaint, null);
		defaultsMap.put(ChartSettings.PROPERTY_backgroundImage, null);
		defaultsMap.put(ChartSettings.PROPERTY_borderStroke, null);

		defaultsMap.put(ChartSettings.PROPERTY_textAntiAlias, Boolean.TRUE);
		defaultsMap.put(ChartSettings.PROPERTY_antiAlias, Boolean.TRUE);
		defaultsMap.put(ChartSettings.PROPERTY_borderVisible, Boolean.TRUE);
		defaultsMap.put(ChartSettings.PROPERTY_backgroundImageAlignment, JFreeChartAlignEnum.TOP_LEFT);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/sample.reference/chartthemes/index.html#chartthemes"); //$NON-NLS-1$
	}

	private static JSSEnumPropertyDescriptor bia;

	@Override
	public Object getPropertyValue(Object id) {
		ChartSettings cs = getValue();
		if (id.equals(ChartSettings.PROPERTY_textAntiAlias))
			return cs.getTextAntiAlias();
		if (id.equals(ChartSettings.PROPERTY_antiAlias))
			return cs.getAntiAlias();
		if (id.equals(ChartSettings.PROPERTY_borderVisible))
			return cs.getBorderVisible();
		if (id.equals(ChartSettings.PROPERTY_backgroundImageAlpha))
			return cs.getBackgroundImageAlpha();
		if (id.equals(ChartSettings.PROPERTY_backgroundImageAlignment))
			return Misc.nvl(cs.getBackgroundImageAlignment(), new Integer(JFreeChartAlignEnum.TOP_LEFT.getValue()));

		Object pad = PadUtil.getPropertyValue(id, cs.getPadding());
		if (pad != null)
			return pad;
		if (id.equals(ChartSettings.PROPERTY_backgroundPaint))
			return cs.getBackgroundPaint();
		if (id.equals(ChartSettings.PROPERTY_borderPaint))
			return cs.getBorderPaint();
		if (id.equals(ChartSettings.PROPERTY_backgroundImage)) {
			ImageProvider bimg = cs.getBackgroundImage();
			if (bimg != null && bimg instanceof FileImageProvider)
				return ((FileImageProvider) bimg).getFile();
			return null;
		}
		if (id.equals(ChartSettings.PROPERTY_borderStroke))
			return cs.getBorderStroke();

		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		ChartSettings cs = getValue();
		if (id.equals(ChartSettings.PROPERTY_textAntiAlias))
			cs.setTextAntiAlias((Boolean) value);
		else if (id.equals(ChartSettings.PROPERTY_antiAlias))
			cs.setAntiAlias((Boolean) value);
		else if (id.equals(ChartSettings.PROPERTY_borderVisible))
			cs.setBorderVisible((Boolean) value);
		else if (id.equals(ChartSettings.PROPERTY_backgroundImageAlpha))
			cs.setBackgroundImageAlpha((Float) value);
		else if (id.equals(ChartSettings.PROPERTY_backgroundImageAlignment))
			cs.setBackgroundImageAlignment((Integer) value);
		else if (id.equals(ChartSettings.PROPERTY_backgroundPaint))
			cs.setBackgroundPaint((PaintProvider) value);
		else if (id.equals(ChartSettings.PROPERTY_borderPaint))
			cs.setBorderPaint((PaintProvider) value);
		else if (id.equals(ChartSettings.PROPERTY_backgroundImage))
			if (value == null || ((String) value).trim().isEmpty())
				cs.setBackgroundImage(null);
			else
				cs.setBackgroundImage(new CachedImageProvider((String) value));
		else if (id.equals(ChartSettings.PROPERTY_borderStroke))
			cs.setBorderStroke((Stroke) value);

		RectangleInsets ri = PadUtil.setPropertyValue(id, value, cs.getPadding());
		if (ri != null)
			cs.setPadding(ri);
	}
}
