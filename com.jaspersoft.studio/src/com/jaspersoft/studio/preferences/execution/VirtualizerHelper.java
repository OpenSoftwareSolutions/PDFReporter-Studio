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
package com.jaspersoft.studio.preferences.execution;

import java.io.File;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class VirtualizerHelper {
	public static void setVirtualizer(JasperDesign jd, JasperReportsConfiguration jContext, Map<String, Object> parammap) {
		if (jContext.getPropertyBoolean(ReportExecutionPreferencePage.JSS_VIRTUALIZER_USE, false))
			parammap.put(JRParameter.REPORT_VIRTUALIZER, createVirtualizer(jContext));
	}

	private static JRVirtualizer createVirtualizer(JasperReportsConfiguration jContext) {
		JRVirtualizer v = null;
		String vtype = jContext.getProperty(ReportExecutionPreferencePage.JSS_VIRTUALIZER_TYPE);
		int maxSize = jContext.getPropertyInteger(ReportExecutionPreferencePage.JSS_VIRTUALIZER_MAX_SIZE, 100);

		if (vtype != null) {
			if (vtype.equals(JRFileVirtualizer.class.getName())) {
				v = new JRFileVirtualizer(maxSize);
			} else if (vtype.equals(JRGzipVirtualizer.class.getName())) {
				v = new JRGzipVirtualizer(maxSize);
			} else if (vtype.equals(JRSwapFileVirtualizer.class.getName())) {
				int blockSize = jContext.getPropertyInteger(ReportExecutionPreferencePage.JSS_VIRTUALIZER_BLOCK_SIZE, 100);
				int minGrowCount = jContext.getPropertyInteger(ReportExecutionPreferencePage.JSS_VIRTUALIZER_MIN_GROW_COUNT,
						100);
				String directory = jContext.getProperty(ReportExecutionPreferencePage.JSS_VIRTUALIZER_TMP);
				if (directory != null && !directory.trim().equals("")) {
					// check if exists
					File f = new File(directory);
					if (!f.exists())
						directory = null;
				}
				if (directory == null || directory.trim().equals("")) {
					IWorkspace workspace = ResourcesPlugin.getWorkspace();
					directory = workspace.getRoot().getLocation().toOSString();
				}

				v = new JRSwapFileVirtualizer(maxSize, new JRSwapFile(directory, blockSize, minGrowCount), false);
			}
		}
		return v;
	}
}
