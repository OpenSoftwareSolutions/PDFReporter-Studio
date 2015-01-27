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
package net.sf.jasperreports.eclipse.wizard.project;

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class JRProjectWizard extends Wizard implements INewWizard,
		IExecutableExtension {
	boolean proceed=true;
	protected JRProjectPage step1;

	public JRProjectWizard() {
		super();
		setWindowTitle(Messages.JRProjectWizard_title);
		setNeedsProgressMonitor(true);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		step1 = new JRProjectPage();
		addPage(step1);
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// _configurationElement = config;
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
			UIUtils.showError(e.getCause());
		} catch (InterruptedException e) {
			UIUtils.showError(e);
		}

		return true;
	}

	protected void doFinish(IProgressMonitor monitor) {
		proceed=true;
		String prjName = step1.getName();
		if(ProjectUtil.hasExistingContent(prjName)){
			// Check on possible existing content in the workspace directory
			UIUtils.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					proceed = MessageDialog.openQuestion(UIUtils.getShell(), 
							Messages.JRProjectWizard_OverwriteQuestionTitle, 
							Messages.JRProjectWizard_OverwriteQuestionMsg);								
				}
			});
		}
		if(proceed) {
			ProjectUtil.deleteProjectFolder(prjName);
			IProject prj = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(prjName);
			if (!prj.exists()) {
				IProjectDescription desc = prj.getWorkspace()
						.newProjectDescription(prj.getName());
				try {
					prj.create(desc, null);
					if (!prj.isOpen())
						prj.open(null);
					createProject(monitor, prj);
				} catch (CoreException e) {
					UIUtils.showError(e);
				}
			}
		}
	}

	protected void createProject(IProgressMonitor monitor, IProject prj)
			throws CoreException, JavaModelException {
		ProjectUtil.createJRProject(monitor, prj);
	}

}
