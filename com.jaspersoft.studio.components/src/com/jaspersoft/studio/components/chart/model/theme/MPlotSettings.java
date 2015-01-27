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

import net.sf.jasperreports.chartthemes.simple.FileImageProvider;
import net.sf.jasperreports.chartthemes.simple.ImageProvider;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;
import net.sf.jasperreports.chartthemes.simple.PlotSettings;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.jfree.ui.RectangleInsets;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.enums.JFreeChartAlignEnum;
import com.jaspersoft.studio.components.chart.model.enums.JFreeChartPlotOrientationEnum;
import com.jaspersoft.studio.components.chart.model.theme.imageprovider.ImageProviderPropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.paintprovider.PaintProviderPropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.paintproviders.PaintProvidersPropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.stroke.StrokePropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.strokes.StrokesPropertyDescriptor;
import com.jaspersoft.studio.components.chart.model.theme.util.PadUtil;
import com.jaspersoft.studio.jasper.CachedImageProvider;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.model.text.MFontUtil;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.FontPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.DegreePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.TransparencyPropertyDescriptor;
import com.jaspersoft.studio.utils.Misc;

public class MPlotSettings extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MPlotSettings(MChartThemeSettings parent, PlotSettings ps) {
		super(parent, -1);
		setValue(ps);
	}

	@Override
	public PlotSettings getValue() {
		return (PlotSettings) super.getValue();
	}

	@Override
	public ImageDescriptor getImagePath() {
		return null;
	}

	@Override
	public String getDisplayText() {
		return "Plot"; //$NON-NLS-1$
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
		PropertyDescriptor pd = new CheckBoxPropertyDescriptor(PlotSettings.PROPERTY_outlineVisible, Messages.MPlotSettings_outlineVisibleTitle);
		pd.setDescription(Messages.MPlotSettings_outlineVisibleDescription);
		pd.setCategory("Outline"); //$NON-NLS-1$
		desc.add(pd);

		pd = new CheckBoxPropertyDescriptor(PlotSettings.PROPERTY_domainGridlineVisible, Messages.MPlotSettings_domainGridLineVisibleTitle);
		pd.setDescription(Messages.MPlotSettings_domainGridLineVisibleDescription);
		pd.setCategory("Domain Grid Line"); //$NON-NLS-1$
		desc.add(pd);

		pd = new CheckBoxPropertyDescriptor(PlotSettings.PROPERTY_rangeGridlineVisible, Messages.MPlotSettings_rangeGridLineVisibleTitle);
		pd.setDescription(Messages.MPlotSettings_rangeGridLineVisibleDescription);
		pd.setCategory("Range Grid Line"); //$NON-NLS-1$
		desc.add(pd);

		pd = new FontPropertyDescriptor(PlotSettings.PROPERTY_displayFont, Messages.MPlotSettings_displayFontTitle);
		pd.setDescription(Messages.MPlotSettings_displayFontDescription);
		desc.add(pd);

		pd = new FontPropertyDescriptor(PlotSettings.PROPERTY_tickLabelFont, Messages.MPlotSettings_tickLabelFontTitle);
		pd.setDescription(Messages.MPlotSettings_tickLabelFontDescription);
		desc.add(pd);

		pd = new TransparencyPropertyDescriptor(PlotSettings.PROPERTY_backgroundImageAlpha, Messages.MPlotSettings_backgroundAlphaTitle);
		pd.setDescription(Messages.MPlotSettings_backgroundAlphaDescription);
		pd.setCategory(Messages.common_background);
		desc.add(pd);

		pd = new TransparencyPropertyDescriptor(PlotSettings.PROPERTY_foregroundAlpha, Messages.MPlotSettings_foregroundAlphaTitle);
		pd.setDescription(Messages.MPlotSettings_foregroundAlphaDescription);
		desc.add(pd);

		pd = new DegreePropertyDescriptor(PlotSettings.PROPERTY_labelRotation, Messages.MPlotSettings_labelRotationTitle);
		pd.setDescription(Messages.MPlotSettings_labelRotationDescription);
		desc.add(pd);

		bia = new JSSEnumPropertyDescriptor(PlotSettings.PROPERTY_backgroundImageAlignment, Messages.MPlotSettings_backgroundImgAlignTitle, JFreeChartAlignEnum.class, NullEnum.NOTNULL);
		bia.setDescription(Messages.MPlotSettings_backgroundImgAlignDescription);
		bia.setCategory(Messages.common_background);
		desc.add(bia);

		orientation = new JSSEnumPropertyDescriptor(PlotSettings.PROPERTY_orientation, Messages.MPlotSettings_orientationTitle, JFreeChartPlotOrientationEnum.class, NullEnum.NOTNULL);
		orientation.setDescription(Messages.MPlotSettings_orientationDescription);
		desc.add(orientation);

		pd = new PaintProviderPropertyDescriptor(PlotSettings.PROPERTY_backgroundPaint, Messages.MPlotSettings_backgroundColorTitle);
		pd.setDescription(Messages.MPlotSettings_backgroundColorDescription);
		pd.setCategory(Messages.common_background);
		desc.add(pd);

		pd = new PaintProviderPropertyDescriptor(PlotSettings.PROPERTY_outlinePaint, Messages.MPlotSettings_outlineColorTitle);
		pd.setDescription(Messages.MPlotSettings_outlineColorDescription);
		pd.setCategory("Outline"); //$NON-NLS-1$
		desc.add(pd);

		pd = new PaintProviderPropertyDescriptor(PlotSettings.PROPERTY_domainGridlinePaint, Messages.MPlotSettings_domainGridLineColorTitle);
		pd.setDescription(Messages.MPlotSettings_domainGridLineColorDescription);
		pd.setCategory("Domain Grid Line"); //$NON-NLS-1$
		desc.add(pd);

		pd = new PaintProviderPropertyDescriptor(PlotSettings.PROPERTY_rangeGridlinePaint, Messages.MPlotSettings_rangeGridLineColorTitle);
		pd.setDescription(Messages.MPlotSettings_rangeGridLineColorDescription);
		pd.setCategory("Range Grid Line"); //$NON-NLS-1$
		desc.add(pd);

		pd = new ImageProviderPropertyDescriptor(PlotSettings.PROPERTY_backgroundImage, Messages.MPlotSettings_backGroundImageTitle);
		pd.setDescription(Messages.MPlotSettings_backGroundImageDescription);
		pd.setCategory(Messages.common_background);
		desc.add(pd);

		pd = new StrokePropertyDescriptor(PlotSettings.PROPERTY_outlineStroke, Messages.MPlotSettings_outlineStrokeTitle);
		pd.setDescription(Messages.MPlotSettings_outlineStrokeDescription);
		pd.setCategory("Outline"); //$NON-NLS-1$
		desc.add(pd);

		pd = new StrokePropertyDescriptor(PlotSettings.PROPERTY_domainGridlineStroke, Messages.MPlotSettings_domainGridLineStrokeTitle);
		pd.setDescription(Messages.MPlotSettings_domainGridLineStrokeDescription);
		pd.setCategory("Domain Grid Line"); //$NON-NLS-1$
		desc.add(pd);

		pd = new StrokePropertyDescriptor(PlotSettings.PROPERTY_rangeGridlineStroke, Messages.MPlotSettings_rangeGridLineStrokeTitle);
		pd.setDescription(Messages.MPlotSettings_rangeGridLineStrokeDescription);
		pd.setCategory("Range Grid Line"); //$NON-NLS-1$
		desc.add(pd);

		pd = new StrokesPropertyDescriptor(PlotSettings.PROPERTY_seriesStrokeSequence, Messages.MPlotSettings_strokeSequenceTitle);
		pd.setDescription(Messages.MPlotSettings_strokeSequenceDescription);
		pd.setCategory("Series"); //$NON-NLS-1$
		desc.add(pd);

		pd = new StrokesPropertyDescriptor(PlotSettings.PROPERTY_seriesOutlineStrokeSequence, Messages.MPlotSettings_outlineStrokeSequenceTitle);
		pd.setDescription(Messages.MPlotSettings_outlineStrokeSequenceDescription);
		pd.setCategory("Series"); //$NON-NLS-1$
		desc.add(pd);

		pd = new PaintProvidersPropertyDescriptor(PlotSettings.PROPERTY_seriesColorSequence, Messages.MPlotSettings_colorSequenceTitle);
		pd.setDescription(Messages.MPlotSettings_colorSequenceDescription);
		pd.setCategory("Series"); //$NON-NLS-1$
		desc.add(pd);

		pd = new PaintProvidersPropertyDescriptor(PlotSettings.PROPERTY_seriesGradientPaintSequence, Messages.MPlotSettings_gradientPaintSequenceTitle);
		pd.setDescription(Messages.MPlotSettings_gradientPaintSequenceDescription);
		pd.setCategory("Series"); //$NON-NLS-1$
		desc.add(pd);

		pd = new PaintProvidersPropertyDescriptor(PlotSettings.PROPERTY_seriesOutlinePaintSequence, Messages.MPlotSettings_outlinePaintSequenceTitle);
		pd.setDescription(Messages.MPlotSettings_outlinePaintSequenceDescription);
		pd.setCategory("Series"); //$NON-NLS-1$
		desc.add(pd);

		PadUtil.createPropertyDescriptors(desc, defaultsMap);

		defaultsMap.put(PlotSettings.PROPERTY_backgroundPaint, null);
		defaultsMap.put(PlotSettings.PROPERTY_outlinePaint, null);
		defaultsMap.put(PlotSettings.PROPERTY_domainGridlinePaint, null);
		defaultsMap.put(PlotSettings.PROPERTY_rangeGridlinePaint, null);
		defaultsMap.put(PlotSettings.PROPERTY_backgroundPaint, null);
		defaultsMap.put(PlotSettings.PROPERTY_backgroundImage, null);
		defaultsMap.put(PlotSettings.PROPERTY_outlineStroke, null);
		defaultsMap.put(PlotSettings.PROPERTY_domainGridlineStroke, null);
		defaultsMap.put(PlotSettings.PROPERTY_rangeGridlineStroke, null);
		defaultsMap.put(PlotSettings.PROPERTY_seriesStrokeSequence, null);
		defaultsMap.put(PlotSettings.PROPERTY_seriesOutlineStrokeSequence, null);
		defaultsMap.put(PlotSettings.PROPERTY_seriesColorSequence, null);
		defaultsMap.put(PlotSettings.PROPERTY_seriesGradientPaintSequence, null);
		defaultsMap.put(PlotSettings.PROPERTY_seriesOutlinePaintSequence, null);

		defaultsMap.put(PlotSettings.PROPERTY_outlineVisible, Boolean.TRUE);
		defaultsMap.put(PlotSettings.PROPERTY_domainGridlineVisible, Boolean.TRUE);
		defaultsMap.put(PlotSettings.PROPERTY_rangeGridlineVisible, Boolean.TRUE);
		defaultsMap.put(PlotSettings.PROPERTY_backgroundImageAlignment, JFreeChartAlignEnum.TOP_LEFT);
		defaultsMap.put(PlotSettings.PROPERTY_orientation, JFreeChartPlotOrientationEnum.HORIZONTAL);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/sample.reference/chartthemes/index.html#chartthemes"); //$NON-NLS-1$
	}

	private static JSSEnumPropertyDescriptor orientation;
	private static JSSEnumPropertyDescriptor bia;
	private MFont dFont;
	private MFont tlFont;

	@Override
	public Object getPropertyValue(Object id) {
		PlotSettings ps = getValue();
		if (id.equals(PlotSettings.PROPERTY_outlineVisible))
			return ps.getOutlineVisible();
		if (id.equals(PlotSettings.PROPERTY_domainGridlineVisible))
			return ps.getDomainGridlineVisible();
		if (id.equals(PlotSettings.PROPERTY_rangeGridlineVisible))
			return ps.getRangeGridlineVisible();
		if (id.equals(PlotSettings.PROPERTY_displayFont)) {
			dFont = MFontUtil.getMFont(dFont, ps.getDisplayFont(), null, this);
			return dFont;
		}
		if (id.equals(PlotSettings.PROPERTY_tickLabelFont)) {
			tlFont = MFontUtil.getMFont(tlFont, ps.getTickLabelFont(), null, this);
			return tlFont;
		}
		if (id.equals(PlotSettings.PROPERTY_backgroundImageAlpha))
			return ps.getBackgroundAlpha();
		if (id.equals(PlotSettings.PROPERTY_labelRotation))
			return ps.getLabelRotation();
		if (id.equals(PlotSettings.PROPERTY_backgroundImageAlignment))
			return bia.getEnumValue(Misc.nvl(JFreeChartAlignEnum.getValue(ps.getBackgroundImageAlignment()), JFreeChartAlignEnum.TOP_LEFT));
		if (id.equals(PlotSettings.PROPERTY_orientation))
			return orientation.getEnumValue(JFreeChartPlotOrientationEnum.getValue(ps.getOrientation()));
		if (id.equals(PlotSettings.PROPERTY_backgroundPaint))
			return ps.getBackgroundPaint();
		if (id.equals(PlotSettings.PROPERTY_outlinePaint))
			return ps.getOutlinePaint();
		if (id.equals(PlotSettings.PROPERTY_domainGridlinePaint))
			return ps.getDomainGridlinePaint();
		if (id.equals(PlotSettings.PROPERTY_rangeGridlinePaint))
			return ps.getRangeGridlinePaint();
		if (id.equals(PlotSettings.PROPERTY_backgroundImage)) {
			ImageProvider bimg = ps.getBackgroundImage();
			if (bimg != null && bimg instanceof FileImageProvider)
				return ((FileImageProvider) bimg).getFile();
			return null;
		}
		if (id.equals(PlotSettings.PROPERTY_outlineStroke))
			return ps.getOutlineStroke();
		if (id.equals(PlotSettings.PROPERTY_domainGridlineStroke))
			return ps.getDomainGridlineStroke();
		if (id.equals(PlotSettings.PROPERTY_rangeGridlineStroke))
			return ps.getRangeGridlineStroke();
		if (id.equals(PlotSettings.PROPERTY_seriesStrokeSequence))
			return ps.getSeriesStrokeSequence();
		if (id.equals(PlotSettings.PROPERTY_seriesOutlineStrokeSequence))
			return ps.getSeriesOutlineStrokeSequence();
		if (id.equals(PlotSettings.PROPERTY_seriesColorSequence))
			return ps.getSeriesColorSequence();
		if (id.equals(PlotSettings.PROPERTY_seriesGradientPaintSequence))
			return ps.getSeriesGradientPaintSequence();
		if (id.equals(PlotSettings.PROPERTY_seriesStrokeSequence))
			return ps.getSeriesStrokeSequence();

		Object pad = PadUtil.getPropertyValue(id, ps.getPadding());
		if (pad != null)
			return pad;
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		PlotSettings ps = getValue();
		if (id.equals(PlotSettings.PROPERTY_outlineVisible))
			ps.setOutlineVisible((Boolean) value);
		else if (id.equals(PlotSettings.PROPERTY_domainGridlineVisible))
			ps.setDomainGridlineVisible((Boolean) value);
		else if (id.equals(PlotSettings.PROPERTY_rangeGridlineVisible))
			ps.setRangeGridlineVisible((Boolean) value);
		else if (id.equals(PlotSettings.PROPERTY_displayFont))
			ps.setDisplayFont(MFontUtil.setMFont(value));
		else if (id.equals(PlotSettings.PROPERTY_tickLabelFont))
			ps.setTickLabelFont(MFontUtil.setMFont(value));
		else if (id.equals(PlotSettings.PROPERTY_backgroundImageAlpha))
			ps.setBackgroundAlpha((Float) value);
		else if (id.equals(PlotSettings.PROPERTY_labelRotation))
			ps.setLabelRotation((Double) value);
		else if (id.equals(PlotSettings.PROPERTY_backgroundImageAlignment))
			ps.setBackgroundImageAlignment(JFreeChartAlignEnum.getJFreeChartValue((Integer) value));
		else if (id.equals(PlotSettings.PROPERTY_orientation))
			ps.setOrientation(((JFreeChartPlotOrientationEnum) orientation.getEnumValue(value)).getJFreeChartValue());
		else if (id.equals(PlotSettings.PROPERTY_backgroundPaint))
			ps.setBackgroundPaint((PaintProvider) value);
		else if (id.equals(PlotSettings.PROPERTY_outlinePaint))
			ps.setOutlinePaint((PaintProvider) value);
		else if (id.equals(PlotSettings.PROPERTY_domainGridlinePaint))
			ps.setDomainGridlinePaint((PaintProvider) value);
		else if (id.equals(PlotSettings.PROPERTY_rangeGridlinePaint))
			ps.setRangeGridlinePaint((PaintProvider) value);
		else if (id.equals(PlotSettings.PROPERTY_backgroundImage))
			if (value == null || ((String) value).trim().isEmpty())
				ps.setBackgroundImage(null);
			else
				ps.setBackgroundImage(new CachedImageProvider((String) value));
		else if (id.equals(PlotSettings.PROPERTY_outlineStroke))
			ps.setOutlineStroke((Stroke) value);
		else if (id.equals(PlotSettings.PROPERTY_domainGridlineStroke))
			ps.setDomainGridlineStroke((Stroke) value);
		else if (id.equals(PlotSettings.PROPERTY_rangeGridlineStroke))
			ps.setRangeGridlineStroke((Stroke) value);
		else if (id.equals(PlotSettings.PROPERTY_seriesStrokeSequence))
			ps.setSeriesStrokeSequence((List<Stroke>) value);
		else if (id.equals(PlotSettings.PROPERTY_seriesOutlineStrokeSequence))
			ps.setSeriesOutlineStrokeSequence((List<Stroke>) value);

		RectangleInsets ri = PadUtil.setPropertyValue(id, value, ps.getPadding());
		if (ri != null)
			ps.setPadding(ri);
	}
}
