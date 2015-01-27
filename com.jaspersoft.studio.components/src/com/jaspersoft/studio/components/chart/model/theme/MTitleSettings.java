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
import net.sf.jasperreports.chartthemes.simple.PaintProvider;
import net.sf.jasperreports.chartthemes.simple.TitleSettings;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
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

public class MTitleSettings extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private String displayText;

	public MTitleSettings(MChartThemeSettings parent, TitleSettings ts, String displayText) {
		super(parent, -1);
		setValue(ts);
		this.displayText = displayText;
	}

	@Override
	public TitleSettings getValue() {
		return (TitleSettings) super.getValue();
	}

	@Override
	public ImageDescriptor getImagePath() {
		return null;
	}

	@Override
	public String getDisplayText() {
		return displayText;
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
		CheckBoxPropertyDescriptor showTitleD = new CheckBoxPropertyDescriptor(TitleSettings.PROPERTY_showTitle, Messages.MTitleSettings_showTitleTitle);
		showTitleD.setDescription(Messages.MTitleSettings_showTitleDescription);
		desc.add(showTitleD);

		FontPropertyDescriptor fontD = new FontPropertyDescriptor(TitleSettings.PROPERTY_font, "Font");
		fontD.setDescription("Font");
		desc.add(fontD);

		posD = new JSSEnumPropertyDescriptor(TitleSettings.PROPERTY_position, Messages.MTitleSettings_positionTitle, EdgeEnum.class, NullEnum.NULL);
		posD.setDescription(Messages.MTitleSettings_positionDescription);
		desc.add(posD);

		hp = new JSSEnumPropertyDescriptor(TitleSettings.PROPERTY_horizontalAlignment, Messages.MTitleSettings_horizontalAlignmentTitle, JFreeChartHorizontalAlignmentEnum.class, NullEnum.NULL);
		hp.setDescription(Messages.MTitleSettings_horizontalAlignmentDescription);
		desc.add(hp);

		vp = new JSSEnumPropertyDescriptor(TitleSettings.PROPERTY_verticalAlignment, Messages.MTitleSettings_verticalAlignementTitle, JFreeChartVerticalAlignmentEnum.class, NullEnum.NULL);
		vp.setDescription(Messages.MTitleSettings_verticalAlignementDescription);
		desc.add(vp);

		PadUtil.createPropertyDescriptors(desc, defaultsMap);

		PaintProviderPropertyDescriptor fgPaint = new PaintProviderPropertyDescriptor(TitleSettings.PROPERTY_foregroundPaint, Messages.MTitleSettings_foregroundColorTitle);
		fgPaint.setDescription(Messages.MTitleSettings_foregroundColorDescription);
		desc.add(fgPaint);

		PaintProviderPropertyDescriptor bgPaint = new PaintProviderPropertyDescriptor(TitleSettings.PROPERTY_backgroundPaint, Messages.MTitleSettings_backgroundColorTitle);
		bgPaint.setDescription(Messages.MTitleSettings_backgroundColorDescription);
		desc.add(bgPaint);

		defaultsMap.put(TitleSettings.PROPERTY_backgroundPaint, null);
		defaultsMap.put(TitleSettings.PROPERTY_foregroundPaint, null);

		defaultsMap.put(TitleSettings.PROPERTY_showTitle, Boolean.TRUE);
		defaultsMap.put(TitleSettings.PROPERTY_position, EdgeEnum.TOP);
		defaultsMap.put(TitleSettings.PROPERTY_horizontalAlignment, JFreeChartHorizontalAlignmentEnum.LEFT);
		defaultsMap.put(TitleSettings.PROPERTY_verticalAlignment, JFreeChartVerticalAlignmentEnum.TOP);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/sample.reference/chartthemes/index.html#chartthemes"); //$NON-NLS-1$
	}

	private static JSSEnumPropertyDescriptor posD;
	private static JSSEnumPropertyDescriptor hp;
	private static JSSEnumPropertyDescriptor vp;
	private MFont clFont;

	@Override
	public Object getPropertyValue(Object id) {
		TitleSettings ts = getValue();
		if (id.equals(TitleSettings.PROPERTY_showTitle))
			return ts.getShowTitle();
		if (id.equals(TitleSettings.PROPERTY_font)) {
			clFont = MFontUtil.getMFont(clFont, ts.getFont(), null, this);
			return clFont;
		}
		if (id.equals(TitleSettings.PROPERTY_position))
			return posD.getEnumValue(ts.getPositionValue());
		if (id.equals(TitleSettings.PROPERTY_horizontalAlignment))
			return hp.getEnumValue(JFreeChartHorizontalAlignmentEnum.getValue(ts.getHorizontalAlignment()));
		if (id.equals(TitleSettings.PROPERTY_verticalAlignment))
			return vp.getEnumValue(JFreeChartVerticalAlignmentEnum.getValue(ts.getVerticalAlignment()));
		if (id.equals(TitleSettings.PROPERTY_backgroundPaint))
			return ts.getBackgroundPaint();
		if (id.equals(TitleSettings.PROPERTY_foregroundPaint))
			return ts.getForegroundPaint();

		Object pad = PadUtil.getPropertyValue(id, ts.getPadding());
		if (pad != null)
			return pad;
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		TitleSettings ts = getValue();
		if (id.equals(TitleSettings.PROPERTY_showTitle))
			ts.setShowTitle((Boolean) value);
		else if (id.equals(TitleSettings.PROPERTY_font))
			ts.setFont(MFontUtil.setMFont(value));
		else if (id.equals(TitleSettings.PROPERTY_position))
			ts.setPosition((EdgeEnum) posD.getEnumValue(value));
		else if (id.equals(TitleSettings.PROPERTY_horizontalAlignment))
			ts.setHorizontalAlignment(((JFreeChartHorizontalAlignmentEnum) hp.getEnumValue(value)).getJFreeChartValue());
		else if (id.equals(TitleSettings.PROPERTY_verticalAlignment))
			ts.setVerticalAlignment(((JFreeChartVerticalAlignmentEnum) vp.getEnumValue(value)).getJFreeChartValue());
		else if (id.equals(TitleSettings.PROPERTY_backgroundPaint))
			ts.setBackgroundPaint((PaintProvider) value);
		else if (id.equals(TitleSettings.PROPERTY_foregroundPaint))
			ts.setForegroundPaint((PaintProvider) value);

		RectangleInsets ri = PadUtil.setPropertyValue(id, value, ts.getPadding());
		if (ri != null)
			ts.setPadding(ri);
	}
}
