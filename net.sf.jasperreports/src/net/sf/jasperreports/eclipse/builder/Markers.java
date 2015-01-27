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

import java.io.File;
import java.text.MessageFormat;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.engine.JRException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.xml.sax.SAXParseException;

public class Markers {
	public static final String MARKER_TYPE = "net.sf.jasperreports.jrxmlProblem.marker"; //$NON-NLS-1$
	
	/**
	 * Path of the markers file of a project where the parameter 0 is the path of the root of the workspace
	 * and the parameter 1 is the project name
	 */
	private static final String MARKERS_DEFAULT_FILE = "{0}/.metadata/.plugins/org.eclipse.core.resources/.projects/{1}/.markers.snap";

	public static IMarker addMarker(IResource file, Throwable e) throws CoreException {
		if (e instanceof JRException && e.getCause() instanceof SAXParseException)
			e = e.getCause();
		if (e instanceof SAXParseException) {
			SAXParseException se = (SAXParseException) e;
			return addMarker(file, e.getMessage(), se.getLineNumber(), se.getColumnNumber(), IMarker.SEVERITY_ERROR);
		}
		return addMarker(file, e.getMessage(), 0, 0, IMarker.SEVERITY_ERROR);
	}

	public static IMarker addMarker(IResource file, String message, int lineNumber, int severity) throws CoreException {
		return addMarker(file, message, lineNumber, 0, severity);
	}

	public static IMarker addMarker(IResource file, String message, int lineNumber, int colNumber, int severity) throws CoreException {
		IMarker marker = file.createMarker(Markers.MARKER_TYPE);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.SEVERITY, severity);
		marker.setAttribute(IMarker.USER_EDITABLE, false);
		marker.setAttribute(IMarker.LINE_NUMBER, Math.max(0, lineNumber));
		marker.setAttribute(IMarker.CHAR_END, Math.max(0, colNumber));
		file.getProject().createMarker(Markers.MARKER_TYPE);
		return marker;
	}
	
	/**
	 * Return a file to the markers file for a specific project
	 * 
	 * @param projectName the project  name
	 * @param workspace the root of the workspace where the project is
	 * @return a file that point to the markers for a specific project 
	 */
	private static File getMarkersFile(String projectName, IWorkspaceRoot workspace){
		File wsDirLocation = workspace.getLocation().toFile();
		String wsAbsolutePath = wsDirLocation.getAbsolutePath();
		Path path = new Path(MessageFormat.format(MARKERS_DEFAULT_FILE, new Object[]{wsAbsolutePath,projectName}));
		return path.toFile();
	}

	public static void deleteMarkers(IResource resource) throws CoreException {
		resource.deleteMarkers(Markers.MARKER_TYPE, false, IResource.DEPTH_ZERO);
		//For some reason the markers file must be delete to have it correctly updated
		//catch the exception to avoid strange behavior next to the release
		try{
			File markersFile = getMarkersFile(resource.getProject().getName(), resource.getWorkspace().getRoot());
			if (markersFile != null && markersFile.exists()) markersFile.delete();
		}catch(Exception ex){
			JasperReportsPlugin.getDefault().logError("Unable to delete the markers file", ex);
		}
	}

}
