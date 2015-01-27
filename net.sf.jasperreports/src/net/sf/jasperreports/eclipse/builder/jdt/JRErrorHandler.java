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
package net.sf.jasperreports.eclipse.builder.jdt;

import net.sf.jasperreports.eclipse.builder.JasperReportErrorHandler;
import net.sf.jasperreports.eclipse.builder.Markers;
import net.sf.jasperreports.eclipse.util.xml.SourceLocation;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.IProblem;

public class JRErrorHandler implements JasperReportErrorHandler {

	public static final String MARKER_ERROR_JREXPRESSION = "MARKER_ERROR_JREXPRESSION";
	public static final String MARKER_ERROR_JRDESIGNELEMENT = "MARKER_ERROR_JRDESIGNELEMENT";
	private IFile file;

	public JRErrorHandler(IFile file) {
		this.file = file;
	}

	public void addMarker(Throwable e) {
		try {
			Markers.addMarker(file, e);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

	public void addMarker(IProblem problem, SourceLocation location) {
		addMarker(problem.getMessage(), location);
	}

	public void addMarker(String message, SourceLocation location) {
		try {
			int line = location == null ? 1 : location.getLineNumber();
			int col = location == null ? 1 : location.getColumnNumber();
			Markers.addMarker(file, message, line, col, IMarker.SEVERITY_ERROR);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addMarker(IProblem problem, SourceLocation location, JRExpression expr) {
		try {
			int line = location == null ? 1 : location.getLineNumber();
			int col = location == null ? 1 : location.getColumnNumber();
			IMarker m = Markers.addMarker(file, problem.getMessage(), line, col, IMarker.SEVERITY_ERROR);
			m.setAttribute(MARKER_ERROR_JREXPRESSION, Integer.toString(expr.getId()));
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addMarker(String message, SourceLocation location, JRDesignElement element) {
		try {
			int line = location == null ? 1 : location.getLineNumber();
			int col = location == null ? 1 : location.getColumnNumber();
			IMarker m = Markers.addMarker(file, message, line, col, IMarker.SEVERITY_ERROR);
			m.setAttribute(MARKER_ERROR_JRDESIGNELEMENT, element);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
