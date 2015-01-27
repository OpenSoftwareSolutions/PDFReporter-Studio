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
package net.sf.jasperreports.eclipse.ui;

import net.sf.jasperreports.eclipse.classpath.JavaProjectClassLoader;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.ReportLoader;
import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.convert.ReportConverter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;

/*
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperDesignPreviewView.java 27 2009-11-11 12:40:27Z teodord $
 */
public final class ReportPreviewUtil {

	public static void loadFileIntoViewer(final IFile file, final IReportViewer viewer, Display display) {
		display.asyncExec(new Runnable() {
			public void run() {
				ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

				try {
					ClassLoader projectClassLoader = createProjectClassLoader(file.getProject());
					if (projectClassLoader != null) {
						Thread.currentThread().setContextClassLoader(projectClassLoader);
					}

					try {
						JRReport report = ReportLoader.loadReport(file);
						if (report == null) {
							viewer.setReport(null);
						} else {
							viewer.setReport(new ReportConverter(DefaultJasperReportsContext.getInstance(), report, false).getJasperPrint());
						}
					} catch (Throwable t) {
						UIUtils.showError(t);
					}
				} finally {
					Thread.currentThread().setContextClassLoader(oldClassLoader);
				}
			}
		});
	}

	public static ClassLoader createProjectClassLoader(IProject project) {
		ClassLoader classLoader = null;
		try {
			IJavaProject javaProject = null;

			if (project.hasNature(JavaCore.NATURE_ID)) {
				javaProject = JavaCore.create(project);
				javaProject.open(null);
				classLoader = JavaProjectClassLoader.instance(javaProject, Thread.currentThread().getContextClassLoader());
			}
		} catch (CoreException e) {
			e.printStackTrace();// FIXME
			return null;
		}

		return classLoader;
	}
}
