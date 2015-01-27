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
package com.jaspersoft.studio.components.chart.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import com.jaspersoft.studio.utils.SelectionHelper;

public class ChartHelper {
	public static String[] getChartThemesNull() {
		String[] ct = getChartThemes();
		String[] ctn = new String[ct.length + 1];
		ctn[0] = "";
		System.arraycopy(ct, 0, ctn, 1, ct.length);
		return ctn;
	}

	public static String[] getChartThemes() {
		ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
		try {
			IEditorPart ep = SelectionHelper.getActiveJRXMLEditor();
			IFile file = ((IFileEditorInput) ep.getEditorInput()).getFile();

			SelectionHelper.setClassLoader(file, null);

			List<ChartThemeBundle> tbundles = ExtensionsEnvironment
					.getExtensionsRegistry().getExtensions(
							ChartThemeBundle.class);
			Set<String> tset = new HashSet<String>();
			for (ChartThemeBundle ctb : tbundles) {
				String[] themeNames = ctb.getChartThemeNames();
				for (String theme : themeNames)
					tset.add(theme);
			}

			String[] themes = tset.toArray(new String[tset.size()]);
			Arrays.sort(themes);
			return themes;
		} catch (Exception e) {
			e.printStackTrace();
			return new String[0];
		} finally {
			Thread.currentThread().setContextClassLoader(oldCL);
		}
	}
}
