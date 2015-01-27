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
package com.jaspersoft.translation.action;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.jaspersoft.translation.resources.TranslationProjectNature;

/**
 * Class to evaluate the nature of a project and check if it is a Translation Project
 * While checking it also seek the old nature id used before the change of the resource
 * bundle plugin, if the old id is found then it is replaced with the new one
 * 
 * @author Orlandin Marco
 *
 */
public class NatureTranslationTester extends PropertyTester {

	/**
	 * Check if a project is a translation project
	 * 
	 * @param receiver an IProject, if the parameter has a different type
	 * the method return false
	 * @return true if the receiver is an IProject with a translation project nature (old 
	 * or new one, in the first case the nature is also overwritten), false otherwise
	 */
	public static boolean evaluateElementNature(Object receiver){
		if (receiver instanceof IProject){
			IProject project = (IProject)receiver;  
		     try {
		    	if (project.isOpen()){
			    	if (project.hasNature(TranslationProjectNature.NATURE_ID)) return true;
			    	else if  (project.hasNature(TranslationProjectNature.COMPATIBILITY_NATURE_ID)){
			    		replaceNature(project, new NullProgressMonitor());
			    		return true;
			    	}
		    	}
				return false;
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	    return false;
	}
	

	/**
	 * Add a nature to a project
	 * 
	 * @param monitor
	 * @param prj the project where the nature will be added
	 * @param nature the id of the nature
	 * @throws CoreException
	 */
	private static void replaceNature(IProject project, IProgressMonitor monitor) throws CoreException {
		if (!project.isOpen()) project.open(monitor);
		IProjectDescription description = project.getDescription();
        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length];
        for(int i = 0; i<prevNatures.length; i++){
        	String oldNature = prevNatures[i];
        	if (oldNature.equals(TranslationProjectNature.COMPATIBILITY_NATURE_ID)){
        		oldNature = TranslationProjectNature.NATURE_ID;
        	}
        	newNatures[i] = oldNature;
        }
        description.setNatureIds(newNatures);
        project.setDescription(description, new NullProgressMonitor());
	}

	
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof Collection){
			Collection<?> selection = (Collection<?>) receiver;
			boolean allRight = !selection.isEmpty();
			for (Iterator<?> it = selection.iterator(); it.hasNext() && allRight;) {
				allRight = evaluateElementNature(it.next());
			}
			return allRight;
		} else {
			return evaluateElementNature(receiver);
		}

	}

}
