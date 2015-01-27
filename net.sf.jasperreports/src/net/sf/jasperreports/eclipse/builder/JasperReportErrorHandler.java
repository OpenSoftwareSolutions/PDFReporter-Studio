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
package net.sf.jasperreports.eclipse.builder;

import net.sf.jasperreports.eclipse.util.xml.SourceLocation;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.jdt.core.compiler.IProblem;

/*
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JasperReportErrorHandler.java 23 2007-03-09 14:36:40Z lucianc $
 */
public interface JasperReportErrorHandler {

	void addMarker(Throwable e);

	void addMarker(String message, SourceLocation location);
	
	void addMarker(String message, SourceLocation location, JRDesignElement element);

	void addMarker(IProblem problem, SourceLocation location);

	void addMarker(IProblem problem, SourceLocation location, JRExpression expr);
}
