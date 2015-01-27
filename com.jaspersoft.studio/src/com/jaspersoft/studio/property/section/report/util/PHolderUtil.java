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
package com.jaspersoft.studio.property.section.report.util;

import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;

import com.jaspersoft.studio.utils.Misc;

public class PHolderUtil {
	private static final String COM_JASPERSOFT_STUDIO_UNIT = "com.jaspersoft.studio.unit.";

	public static String getUnit(JRPropertiesHolder pholder, String prop, String def) {
		return Misc.nvl(pholder.getPropertiesMap().getProperty(COM_JASPERSOFT_STUDIO_UNIT + prop), def);
	}

	public static boolean setProperty(boolean anychanges, JRPropertiesMap pmap, String prop, String unit, String defUnit) {
		String pname = COM_JASPERSOFT_STUDIO_UNIT + prop;
		String p = pmap.getProperty(pname);
		if (p != null && defUnit != null && defUnit.equals(unit)) {
			pmap.removeProperty(pname);
			return true;
		}
		if ((p != null && !p.equals(unit)) || (p == null && unit != null)) {
			pmap.setProperty(pname, unit);
			return true;
		}
		if (p != null && unit == null) {
			pmap.removeProperty(pname);
			return true;
		}
		return anychanges;
	}
}
