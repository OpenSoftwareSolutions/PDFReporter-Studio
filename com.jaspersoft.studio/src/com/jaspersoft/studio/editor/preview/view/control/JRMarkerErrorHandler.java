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

import net.sf.jasperreports.eclipse.builder.Markers;
import net.sf.jasperreports.eclipse.util.xml.SourceLocation;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.IProblem;

import com.jaspersoft.studio.utils.Console;

/**
 * Extend the classic  JRErrorHandler to handle also the marker 
 * on the project. The base error handler print the error messages
 * on a console but dosen't change the decorator of the report in the 
 * explorer if it has or not errors. 
 * 
 * @author Orlandin Marco
 *
 */
public class JRMarkerErrorHandler extends JRErrorHandler {

	/**
	 * Reference to the project
	 */
	private IFile resource;
	
	/**
	 * Create an instance of the class
	 * 
	 * @param c console where the message are printed
	 * @param resouece reference to the compiled project
	 */
	public JRMarkerErrorHandler(Console c, IFile resouece) {
		super(c);
		this.resource = resouece;
	}

	@Override
	public void reset() {
		super.reset();
		try {
			if (resource != null)
				Markers.deleteMarkers(resource);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addMarker(Throwable e) {
		super.addMarker(e);
		try {
			if (resource != null)
				Markers.addMarker(resource, e);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
	private int getLocationLine(SourceLocation location){
		return location != null ? location.getLineNumber() : 0;
	}
	
	@Override
	public void addMarker(IProblem problem, SourceLocation location) {
		super.addMarker(problem, location);
		try {
			if (resource != null)
				Markers.addMarker(resource, problem.getMessage(), getLocationLine(location), IMarker.SEVERITY_ERROR);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void addMarker(String message, SourceLocation location) {
		super.addMarker(message, location);
		try {
			if (resource != null)
				Markers.addMarker(resource, message, getLocationLine(location), IMarker.SEVERITY_ERROR);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void addMarker(IProblem problem, SourceLocation location, JRExpression expr) {
		super.addMarker(problem, location, expr);
		try {
			if (resource != null)
				Markers.addMarker(resource, problem.getMessage(), getLocationLine(location), IMarker.SEVERITY_ERROR);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void addMarker(String message, SourceLocation location, JRDesignElement element) {
		super.addMarker(message, location, element);
		try {
			if (resource != null)
				Markers.addMarker(resource, message, getLocationLine(location), IMarker.SEVERITY_ERROR);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
	
}
