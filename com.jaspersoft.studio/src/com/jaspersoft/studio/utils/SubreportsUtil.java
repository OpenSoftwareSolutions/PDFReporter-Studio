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
package com.jaspersoft.studio.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class SubreportsUtil {
	public static Map<File, IFile> getSubreportFiles(JasperReportsConfiguration jConfig, IFile file, JasperDesign jd,
			IProgressMonitor monitor) {
		Map<File, IFile> fmap = new HashMap<File, IFile>();
		try {
			List<JRDesignElement> elements = ModelUtils.getAllElements(jd);
			for (JRDesignElement ele : elements) {
				if (ele instanceof JRDesignSubreport)
					publishSubreport(jConfig, fmap, monitor, file, jd, (JRDesignSubreport) ele);
			}
		} finally {
			jConfig.init(file);
		}
		return fmap;
	}

	private static void publishSubreport(JasperReportsConfiguration jConfig, Map<File, IFile> fmap,
			IProgressMonitor monitor, IFile file, JasperDesign parent, JRDesignSubreport ele) {
		jConfig.init(file);
		String expr = ExpressionUtil.eval(ele.getExpression(), jConfig, parent);
		if (expr == null || expr.isEmpty())
			return;
		if (expr.endsWith(FileExtension.PointJASPER))
			expr = expr.replaceAll(FileExtension.PointJASPER + "$", FileExtension.PointJRXML);
		expr = expr.replaceFirst("repo:", "");
		File f = FileUtils.findFile(file, expr);
		if (f == null)
			try {
				try {
					f = new File(expr);
				} catch (IllegalArgumentException e) {
					f = new File(file.getRawLocationURI().getPath(), expr);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		if (fmap.containsKey(f))
			return;
		if (f != null && f.exists()) {
			IFile[] fs = root.findFilesForLocationURI(f.toURI());
			if (fs != null && fs.length > 0) {
				IFile ifile = fs[0];
				fmap.put(f, ifile);
				try {
					JasperDesign jd = JRXMLUtils.getJasperDesign(jConfig, ifile.getContents(), ifile.getFileExtension());
					if (jd != null) {
						for (JRDesignElement el : ModelUtils.getAllElements(jd)) {
							if (el instanceof JRDesignSubreport)
								publishSubreport(jConfig, fmap, monitor, ifile, jd, (JRDesignSubreport) el);
							if (monitor.isCanceled())
								break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				fmap.put(f, null);
			}
		}
	}

	public static IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
}
