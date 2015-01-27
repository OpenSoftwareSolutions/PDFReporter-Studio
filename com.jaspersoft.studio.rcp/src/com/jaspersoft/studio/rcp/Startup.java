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
package com.jaspersoft.studio.rcp;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.rcp.heartbeat.Heartbeat;
import com.jaspersoft.studio.rcp.messages.Messages;

/**
 *
 */
public class Startup implements IStartup {

	public void earlyStartup() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(FileUtils.DEFAULT_PROJECT); //$NON-NLS-1$
		IProgressMonitor monitor = new NullProgressMonitor();
		try {
			if (!project.exists()) {
				project.create(monitor);
				project.open(monitor);
				ProjectUtil.createJRProject(monitor, project);
				IProjectDescription description = project.getDescription();
				description.setName(Messages.Startup_jss_project);
				project.setDescription(description, monitor);
			}
			IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
			registry.setDefaultEditor("*.properties", "com.essiembre.rbe.eclipse.editor.ResourceBundleEditor");
		} catch (CoreException e) {
			e.printStackTrace();
		} finally {
			monitor.done();
		}

		String devmode = System.getProperty("devmode");
		if(devmode==null || !devmode.equals("true")){
			Job job = new Job("Check New Version") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {

					Heartbeat.run();
					return Status.OK_STATUS;
				}

			};
			job.setSystem(true);
			job.schedule();
		}
		
	}

}
