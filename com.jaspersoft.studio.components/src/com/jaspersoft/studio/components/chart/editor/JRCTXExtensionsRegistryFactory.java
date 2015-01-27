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
/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.studio.components.chart.editor;

import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.charts.ChartTheme;
import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import net.sf.jasperreports.chartthemes.simple.SimpleChartTheme;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRCTXExtensionsRegistryFactory implements ExtensionsRegistryFactory, ChartThemeBundle {
	private ChartThemeSettings cts;

	public JRCTXExtensionsRegistryFactory(ChartThemeSettings cts) {
		this.cts = cts;
	}

	public String[] getChartThemeNames() {
		return new String[] { "" };
	}

	private SimpleChartTheme sct;

	public ChartTheme getChartTheme(String themeName) {
		if (sct == null && cts != null)
			sct = new SimpleChartTheme(cts);
		return sct;
	}

	private final ExtensionsRegistry extensionsRegistry = new ExtensionsRegistry() {
		public <T> List<T> getExtensions(Class<T> extensionType) {
			if (ChartThemeBundle.class.equals(extensionType))
				return (List<T>) Collections.singletonList(JRCTXExtensionsRegistryFactory.this);
			return null;
		}
	};

	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) {
		return extensionsRegistry;
	}
}
