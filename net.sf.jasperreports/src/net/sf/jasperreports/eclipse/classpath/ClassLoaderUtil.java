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
package net.sf.jasperreports.eclipse.classpath;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ClassLoaderUtil {

	public static ClassLoader getClassLoader4Project(IProgressMonitor monitor, IProject activeProject) throws CoreException, JavaModelException {
		if (activeProject.hasNature(JavaCore.NATURE_ID)) {
			IJavaProject javaProject = JavaCore.create(activeProject);
			javaProject.open(monitor);
			return JavaProjectClassLoader.instance(javaProject, Thread.currentThread().getContextClassLoader());
		}
		return activeProject.getClass().getClassLoader();
	}

	public static Set<String> packages = new HashSet<String>();
	static {
		packages.add("java");
		packages.add("java.lang");
		packages.add("java.lang.JRFillVariable");
		packages.add("java.util.Throwable");
		packages.add("java.util.Object");
		packages.add("java.lang.java");
		packages.add("java.util.java");
		packages.add("net");
		packages.add("net.sf");
		packages.add("net.sf.jasperreports");
		packages.add("net.sf.jasperreports.engine");
		packages.add("net.sf.jasperreports.engine.fill");
		packages.add("java.util");
		packages.add("java.math");
		packages.add("java.text");
		packages.add("java.io");
		packages.add("java.net");
		packages.add("Object");
		packages.add("JREvaluator");
		packages.add("JRFillParameter");
		packages.add("JRFillVariable");
		packages.add("Map");
		packages.add("Throwable");
		packages.add("java.lang.JREvaluator");
		packages.add("net.sf.jasperreports.engine.JREvaluator");
		packages.add("java.lang.package-info");
		packages.add("java.util.JREvaluator");
		packages.add("java.math.JREvaluator");
		packages.add("java.text.JREvaluator");
		packages.add("java.io.JREvaluator");
		packages.add("java.net.JREvaluator");
		packages.add("java.lang.JRFillParameter");
		packages.add("net.sf.jasperreports.engine.JRFillParameter");
		packages.add("java.util.JRFillParameter");
		packages.add("java.math.JRFillParameter");
		packages.add("java.text.JRFillParameter");
		packages.add("java.io.JRFillParameter");
		packages.add("java.net.JRFillParameter");
		packages.add("java.lang.JRFillParameter");
		packages.add("net.sf.jasperreports.engine.JRFillVariable");
		packages.add("java.util.JRFillVariable");
		packages.add("java.math.JRFillVariable");
		packages.add("java.text.JRFillVariable");
		packages.add("java.io.JRFillVariable");
		packages.add("java.net.JRFillVariable");
		packages.add("java.lang.Map");
		packages.add("net.sf.jasperreports.engine.Map");
		packages.add("net.sf.jasperreports.engine.fill.Map");
		packages.add("java.util.package-info");
		packages.add("java.math.Map");
		packages.add("java.text.Map");
		packages.add("java.io.Map");
		packages.add("java.net.Map");
		packages.add("net.sf.jasperreports.engine.Throwable");
		packages.add("net.sf.jasperreports.engine.fill.Throwable");
		packages.add("java.math.Throwable");
		packages.add("java.text.Throwable");
		packages.add("java.io.Throwable");
		packages.add("java.net.Throwable");
		packages.add("net.sf.jasperreports.engine.Object");
		packages.add("net.sf.jasperreports.engine.fill.Object");
		packages.add("java.math.Object");
		packages.add("java.text.Object");
		packages.add("java.io.Object");
		packages.add("java.net.Object");
		packages.add("net.sf.jasperreports.engine.java");
		packages.add("net.sf.jasperreports.engine.fill.java");
		packages.add("java.math.java");
		packages.add("java.text.java");
		packages.add("java.io.java");
		packages.add("java.net.java");
		packages.add("java.io.package-info");
		packages.add("net.sf.jasperreports.engine.String");
		packages.add("net.sf.jasperreports.engine.fill.String");
		packages.add("java.util.String");
		packages.add("java.math.String");
		packages.add("java.text.String");
		packages.add("java.io.String");
		packages.add("java.net.String");
		packages.add("java.lang.net.sf.jasperreports.engine.java$sql$Connection");
		packages.add("java.io.net.sf.jasperreports.engine.java$sql$Connection");
		packages.add("java.net.net.sf.jasperreports.engine.java$sql$Connection");
		packages.add("java.util.net.sf.jasperreports.engine.java$sql$Connection");
		packages.add("groovy.lang.net.sf.jasperreports.engine.java$sql$Connection");
		packages.add("groovy.util.net.sf.jasperreports.engine.java$sql$Connection");
		packages.add("net.sf.jasperreports.engine.java$sql$Connection");
		packages.add("net.sf.jasperreports.engine.java.sql$Connection");
		packages.add("net.sf.jasperreports.engine.java.sql.Connection");
		packages.add("java.lang.net.sf.jasperreports.engine.fill.java$sql$Connection");
		packages.add("java.io.net.sf.jasperreports.engine.fill.java$sql$Connection");
		packages.add("java.net.net.sf.jasperreports.engine.fill.java$sql$Connection");
		packages.add("java.util.net.sf.jasperreports.engine.fill.java$sql$Connection");
		packages.add("groovy.lang.net.sf.jasperreports.engine.fill.java$sql$Connection");
		packages.add("groovy.util.net.sf.jasperreports.engine.fill.java$sql$Connection");
		packages.add("net.sf.jasperreports.engine.fill.java$sql$Connection");
		packages.add("net.sf.jasperreports.engine.fill.java.sql$Connection");
		packages.add("net.sf.jasperreports.engine.fill.java.sql.Connection");
		packages.add("java.lang.java.util.java$sql$Connection");
		packages.add("java.io.java.util.java$sql$Connection");
		packages.add("java.net.java.util.java$sql$Connection");
		packages.add("java.util.java.util.java$sql$Connection");
		packages.add("groovy.lang.java.util.java$sql$Connection");
		packages.add("groovy.util.java.util.java$sql$Connection");
		packages.add("java.util.java$sql$Connection");
		packages.add("java.util.java.sql$Connection");
		packages.add("java.util.java.sql.Connection");
		packages.add("java.lang.java.math.java$sql$Connection");
		packages.add("java.io.java.math.java$sql$Connection");
		packages.add("java.net.java.math.java$sql$Connection");
		packages.add("java.util.java.math.java$sql$Connection");
		packages.add("groovy.lang.java.math.java$sql$Connection");
		packages.add("groovy.util.java.math.java$sql$Connection");
		packages.add("java.math.java$sql$Connection");
		packages.add("java.math.java.sql$Connection");
		packages.add("java.math.java.sql.Connection");
		packages.add("java.lang.java.text.java$sql$Connection");
		packages.add("java.io.java.text.java$sql$Connection");
		packages.add("java.net.java.text.java$sql$Connection");
		packages.add("java.util.java.text.java$sql$Connection");
		packages.add("groovy.lang.java.text.java$sql$Connection");
		packages.add("groovy.util.java.text.java$sql$Connection");
		packages.add("java.text.java$sql$Connection");
		packages.add("java.text.java.sql$Connection");
		packages.add("java.text.java.sql.Connection");
		packages.add("java.lang.java.io.java$sql$Connection");
		packages.add("java.io.java.io.java$sql$Connection");
		packages.add("java.net.java.io.java$sql$Connection");
		packages.add("java.util.java.io.java$sql$Connection");
		packages.add("groovy.lang.java.io.java$sql$Connection");
		packages.add("groovy.util.java.io.java$sql$Connection");
		packages.add("java.io.java$sql$Connection");
		packages.add("java.io.java.sql$Connection");
		packages.add("java.io.java.sql.Connection");
		packages.add("java.lang.java.net.java$sql$Connection");
		packages.add("java.io.java.net.java$sql$Connection");
		packages.add("java.net.java.net.java$sql$Connection");
		packages.add("java.util.java.net.java$sql$Connection");
		packages.add("groovy.lang.java.net.java$sql$Connection");
		packages.add("groovy.util.java.net.java$sql$Connection");
		packages.add("java.net.java$sql$Connection");
		packages.add("java.net.java.sql$Connection");
		packages.add("java.net.java.sql.Connection");
		packages.add("java.lang.java$sql$Connection");
		packages.add("groovy.lang.java$sql$Connection");
		packages.add("groovy.util.java$sql$Connection");
		packages.add("java$sql$Connection");
		packages.add("java.sql$Connection");
		packages.add("net.sf.jasperreports.engine.JRFillField");
		packages.add("java.lang.net.sf.jasperreports.engine.java$sql$Timestamp");
		packages.add("java.io.net.sf.jasperreports.engine.java$sql$Timestamp");
		packages.add("java.net.net.sf.jasperreports.engine.java$sql$Timestamp");
		packages.add("java.util.net.sf.jasperreports.engine.java$sql$Timestamp");
		packages.add("groovy.lang.net.sf.jasperreports.engine.java$sql$Timestamp");
		packages.add("groovy.util.net.sf.jasperreports.engine.java$sql$Timestamp");
		packages.add("net.sf.jasperreports.engine.java$sql$Timestamp");
		packages.add("net.sf.jasperreports.engine.java.sql$Timestamp");
		packages.add("net.sf.jasperreports.engine.java.sql.Timestamp");
		packages.add("java.lang.net.sf.jasperreports.engine.fill.java$sql$Timestamp");
		packages.add("java.io.net.sf.jasperreports.engine.fill.java$sql$Timestamp");
		packages.add("java.net.net.sf.jasperreports.engine.fill.java$sql$Timestamp");
		packages.add("java.util.net.sf.jasperreports.engine.fill.java$sql$Timestamp");
		packages.add("groovy.lang.net.sf.jasperreports.engine.fill.java$sql$Timestamp");
		packages.add("groovy.util.net.sf.jasperreports.engine.fill.java$sql$Timestamp");
		packages.add("net.sf.jasperreports.engine.fill.java$sql$Timestamp");
		packages.add("net.sf.jasperreports.engine.fill.java.sql$Timestamp");
		packages.add("net.sf.jasperreports.engine.fill.java.sql.Timestamp");
		packages.add("java.lang.java.util.java$sql$Timestamp");
		packages.add("java.io.java.util.java$sql$Timestamp");
		packages.add("java.net.java.util.java$sql$Timestamp");
		packages.add("java.util.java.util.java$sql$Timestamp");
		packages.add("groovy.lang.java.util.java$sql$Timestamp");
		packages.add("groovy.util.java.util.java$sql$Timestamp");
		packages.add("java.util.java$sql$Timestamp");
		packages.add("java.util.java.sql$Timestamp");
		packages.add("java.util.java.sql.Timestamp");
		packages.add("java.lang.java.math.java$sql$Timestamp");
		packages.add("java.io.java.math.java$sql$Timestamp");
		packages.add("java.net.java.math.java$sql$Timestamp");
		packages.add("java.util.java.math.java$sql$Timestamp");
		packages.add("groovy.lang.java.math.java$sql$Timestamp");
		packages.add("groovy.util.java.math.java$sql$Timestamp");
		packages.add("java.math.java$sql$Timestamp");
		packages.add("java.math.java.sql$Timestamp");
		packages.add("java.math.java.sql.Timestamp");
		packages.add("java.lang.java.text.java$sql$Timestamp");
		packages.add("java.io.java.text.java$sql$Timestamp");
		packages.add("java.net.java.text.java$sql$Timestamp");
		packages.add("java.util.java.text.java$sql$Timestamp");
		packages.add("groovy.lang.java.text.java$sql$Timestamp");
		packages.add("groovy.util.java.text.java$sql$Timestamp");
		packages.add("java.text.java$sql$Timestamp");
		packages.add("java.text.java.sql$Timestamp");
		packages.add("java.text.java.sql.Timestamp");
		packages.add("java.lang.java.io.java$sql$Timestamp");
		packages.add("java.io.java.io.java$sql$Timestamp");
		packages.add("java.net.java.io.java$sql$Timestamp");
		packages.add("java.util.java.io.java$sql$Timestamp");
		packages.add("groovy.lang.java.io.java$sql$Timestamp");
		packages.add("groovy.util.java.io.java$sql$Timestamp");
		packages.add("java.io.java$sql$Timestamp");
		packages.add("java.io.java.sql$Timestamp");
		packages.add("java.io.java.sql.Timestamp");
		packages.add("java.lang.java.net.java$sql$Timestamp");
		packages.add("java.io.java.net.java$sql$Timestamp");
		packages.add("java.net.java.net.java$sql$Timestamp");
		packages.add("java.util.java.net.java$sql$Timestamp");
		packages.add("groovy.lang.java.net.java$sql$Timestamp");
		packages.add("groovy.util.java.net.java$sql$Timestamp");
		packages.add("java.net.java$sql$Timestamp");
		packages.add("java.net.java.sql$Timestamp");
		packages.add("java.net.java.sql.Timestamp");
		packages.add("java.lang.java$sql$Timestamp");
		packages.add("groovy.lang.java$sql$Timestamp");
		packages.add("groovy.util.java$sql$Timestamp");
		packages.add("java$sql$Timestamp");
		packages.add("java.sql$Timestamp");
	}
}
