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
package com.jaspersoft.translation.resources;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * Define a specific nature for a translation project
 * 
 * @author Orlandin Marco
 *
 */
public class TranslationProjectNature implements IProjectNature {

	/**
	 * ID of this project nature
	 */
	public static final String NATURE_ID = "com.jaspersoft.studio.babel.editor.translationNature"; //$NON-NLS-1$
	
	/**
	 * Nature ID used before the plugin switching to the EPL version
	 */
	public static final String COMPATIBILITY_NATURE_ID = "com.essiembre.eclipse.i18n.resourcebundle.translationNature"; //$NON-NLS-1$

	/**
	 * The project
	 */
	private IProject project;

	/** 
	 * Returns the project to which this project nature applies.
	 *
	 * @return the project handle
	 */
	@Override
	public IProject getProject() {
		return project;
	}

	/**
	 * Sets the project to which this nature applies.
	 * Used when instantiating this project nature runtime.
	 * This is called by <code>IProject.create()</code> or
	 * <code>IProject.setDescription()</code>
	 * and should not be called directly by clients.
	 *
	 * @param project the project to which this nature applies
	 */
	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

	@Override
	public void configure() throws CoreException {
	}

	@Override
	public void deconfigure() throws CoreException {
	}
	
	/**
	 * Add the translation nature to a project
	 * 
	 * @param monitor
	 * @param prj the project where the nature will be added
	 * @throws CoreException
	 */
	public static void createJRProject(IProgressMonitor monitor, IProject prj) throws CoreException {
		addNature(prj, TranslationProjectNature.NATURE_ID, monitor);
	}


	/**
	 * Add a nature to a project
	 * 
	 * @param monitor
	 * @param prj the project where the nature will be added
	 * @param nature the id of the nature
	 * @throws CoreException
	 */
	public static void addNature(IProject project, String nature, IProgressMonitor monitor) throws CoreException {
		if (!project.isOpen()) project.open(monitor);
		if (!project.hasNature(nature)) {
			IProjectDescription description = project.getDescription();
	        String[] prevNatures = description.getNatureIds();
	        String[] newNatures = new String[prevNatures.length + 1];
	        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
	        newNatures[prevNatures.length] = nature;
	        description.setNatureIds(newNatures);
	        project.setDescription(description, new NullProgressMonitor());
		}
	}

	/**
	 * Check if a project exist and it is opened 
	 * 
	 * @param prj the project
	 * @return true if the project exist, is open and accessibile, false otherwise
	 */
	public static boolean isOpen(IProject prj) {
		return prj.exists() && prj.isOpen() && prj.isAccessible();
	}

}
