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
package com.jaspersoft.studio.model.datasource.xml;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.datasource.AMFileDataSource;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;

public class MXMLDataSource extends AMFileDataSource {
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
			iconDescriptor = new NodeIconDescriptor("datasourceJDBC"); //$NON-NLS-1$
		return iconDescriptor;
	}

	public MXMLDataSource() {
		super(null, -1);
	}

	public MXMLDataSource(ANode parent, int index) {
		super(parent, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
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

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		NTextPropertyDescriptor jdbcURLD = new NTextPropertyDescriptor(PROPERTY_XPATHSELECT, Messages.common_xpath_select);
		desc.add(jdbcURLD);

		NTextPropertyDescriptor timeZoneD = new NTextPropertyDescriptor(PROPERTY_XPATHTIMEZONE,
				Messages.common_xpath_timezone);
		desc.add(timeZoneD);

		NTextPropertyDescriptor localeD = new NTextPropertyDescriptor(PROPERTY_XPATHLOCALE, Messages.common_xpath_locale);
		desc.add(localeD);

		defaultsMap.put(PROPERTY_XPATHTIMEZONE, TimeZone.getDefault());
		defaultsMap.put(PROPERTY_XPATHLOCALE, Locale.getDefault());
	}

	public static final String PROPERTY_XPATHSELECT = "PROPERTY_XPATHSELECT"; //$NON-NLS-1$
	private String xpathselect;

	public static final String PROPERTY_XPATHLOCALE = "PROPERTY_XPATHLOCALE"; //$NON-NLS-1$
	private Locale xpathlocale;

	public static final String PROPERTY_XPATHTIMEZONE = "PROPERTY_XPATHTIMEZONE"; //$NON-NLS-1$
	private TimeZone xpathTimeZone;

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(PROPERTY_XPATHSELECT))
			return xpathselect;
		if (id.equals(PROPERTY_XPATHLOCALE))
			return xpathlocale;
		if (id.equals(PROPERTY_XPATHTIMEZONE))
			return xpathTimeZone;
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(PROPERTY_XPATHSELECT)) {
			xpathselect = (String) value;
		} else if (id.equals(PROPERTY_XPATHLOCALE)) {
			xpathlocale = (Locale) value;
		} else if (id.equals(PROPERTY_XPATHTIMEZONE)) {
			xpathTimeZone = (TimeZone) value;
		} else
			super.setPropertyValue(id, value);
	}
}
