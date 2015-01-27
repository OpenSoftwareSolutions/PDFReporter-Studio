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
package com.jaspersoft.studio.outline.action.compile;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Class to evaluate if the selection contains one or more JRXML file
 * 
 * @author Orlandin Marco
 *
 */
public class NatureJrxmlTester extends PropertyTester {

	/**
	 * Check if the resource is a jrxml file
	 * 
	 * @param element a resource
	 * @return if the resource is a jrxml file it returns true.
	 * If the resource is a folder of a project it check also its
	 * children (the project must be open). Otherwise return false
	 */
	private boolean evaluateChild(Object element){
		if (element instanceof IFolder){
			IFolder folder = (IFolder)element;
			try {
				for(IResource resource : folder.members()){
					if (evaluateChild(resource)) return true;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else if (element instanceof IFile){
			IFile file = (IFile)element;
			String extension = file.getFileExtension();
			if (extension != null && extension.toLowerCase().equals("jrxml")){
				return true;
			}
		} else if (element instanceof IProject){
			IProject project = (IProject)element;
			if (project.isOpen()){
				try {
					for(IResource resource : project.members()){
						if (evaluateChild(resource)) return true;
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof Collection){
			Collection<?> selection = (Collection<?>) receiver;
			boolean allRight = !selection.isEmpty();
			for (Iterator<?> it = selection.iterator(); it.hasNext() && allRight;) {
				allRight = evaluateChild(it.next());
			}
			return allRight;
		} else {
			return evaluateChild(receiver);
		}

	}

}
