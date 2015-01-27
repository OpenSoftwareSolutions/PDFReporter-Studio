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
package com.jaspersoft.translation.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.babel.messages.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.translation.resources.TranslationProjectNature;

/**
 * Wizard to create a new translation project
 * 
 * @author Orlandin Marco
 *
 */
public class TranslationProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

	/**
	 * Page where the project name is defined
	 */
	protected TranslationProjectPage step1;

	public TranslationProjectWizard() {
		super();
		setWindowTitle(Messages.translation_wiz_title);
		setNeedsProgressMonitor(true);
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		step1 = new TranslationProjectPage();
		addPage(step1);
	}
	
	@Override
	public boolean performFinish() {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
		
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					doFinish(monitor);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * If the project dosen't exist it will be created
	 */
	protected void doFinish(IProgressMonitor monitor) {
		IProject prj = ResourcesPlugin.getWorkspace().getRoot().getProject(step1.getName());
		if (!prj.exists()) {
			IProjectDescription desc = prj.getWorkspace().newProjectDescription(prj.getName());
			try {
				prj.create(desc, null);
				if (!prj.isOpen()) prj.open(null);
				createProject(monitor, prj);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Add the Translation Nature to the just created project
	 */
	protected void createProject(IProgressMonitor monitor, IProject prj) throws CoreException {
		TranslationProjectNature.createJRProject(monitor, prj);
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {}
}
