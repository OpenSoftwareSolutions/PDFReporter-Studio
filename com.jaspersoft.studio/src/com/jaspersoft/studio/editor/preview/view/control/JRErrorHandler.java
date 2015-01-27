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
package com.jaspersoft.studio.editor.preview.view.control;

import net.sf.jasperreports.eclipse.builder.JasperReportErrorHandler;
import net.sf.jasperreports.eclipse.util.xml.SourceLocation;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.jdt.core.compiler.IProblem;

import com.jaspersoft.studio.utils.Console;

public class JRErrorHandler implements JasperReportErrorHandler {
	private boolean errors = false;
	private Console c;

	public JRErrorHandler(Console c) {
		this.c = c;
	}

	public void reset() {
		errors = false;
	}

	public boolean hasErrors() {
		return errors;
	}
	
	public void addMarker(Throwable e) {
		errors = true;
		if (c != null)
			c.addError(e, null);
	}

	public void addMarker(IProblem problem, SourceLocation location) {
		errors = true;
		if (c != null)
			c.addProblem(problem, location);
	}

	public void addMarker(String message, SourceLocation location) {
		errors = true;
		if (c != null)
			c.addProblem(message, location);
	}

	@Override
	public void addMarker(IProblem problem, SourceLocation location, JRExpression expr) {
		errors = true;
		if (c != null)
			c.addProblem(problem, location, expr);
	}

	@Override
	public void addMarker(String message, SourceLocation location, JRDesignElement element) {
		errors = true;
		if (c != null)
			c.addProblem(message, location, element);
	}
}
