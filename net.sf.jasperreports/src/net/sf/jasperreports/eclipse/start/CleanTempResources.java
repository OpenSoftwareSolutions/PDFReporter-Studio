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
package net.sf.jasperreports.eclipse.start;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class CleanTempResources implements IStartup {
	private Job wjob;

	private final class ResourceVisitor implements IResourceProxyVisitor {
		private IProgressMonitor monitor;

		public ResourceVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		public boolean visit(IResourceProxy proxy) throws CoreException {
			IResource r = proxy.requestResource();
			if (proxy != null && proxy.isLinked()) {
				if ((r instanceof IFolder || r.getFileExtension() != null && r.getFileExtension().equalsIgnoreCase(FileExtension.JRXML)) && r.getLocation() != null && !r.getLocation().toFile().exists())
					r.delete(true, monitor);
			}
			if (monitor.isCanceled())
				return false;
			return true;
		}
	}

	@Override
	public void earlyStartup() {
		wjob = new WorkspaceJob(Messages.CleanTempResources_SearchBrokenLinksTask) {
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				monitor.beginTask(wjob.getName(), projects.length);
				for (IProject prj : projects) {
					if (ProjectUtil.isOpen(prj)) {
						monitor.subTask(Messages.CleanTempResources_SearchProjectTask + prj.getName());
						prj.accept(new ResourceVisitor(monitor), IContainer.INCLUDE_PHANTOMS | 16);
						// IContainer.DO_NOT_CHECK_EXISTENCE this constant is
						// since 3.8

						if (monitor.isCanceled())
							return Status.CANCEL_STATUS;
						monitor.internalWorked(1);
					}
				}

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						if (workbenchWindow != null) {
							IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							IEditorReference[] refs = activePage.getEditorReferences();
							List<IEditorReference> toclose = new ArrayList<IEditorReference>();
							for (IEditorReference er : refs) {
								try {
									IEditorInput input = er.getEditorInput();
									if (input instanceof FileEditorInput) {
										if (!((FileEditorInput) input).getFile().exists())
											toclose.add(er);
									}
								} catch (PartInitException pie) {
									toclose.add(er);
								}
							}
							if (!toclose.isEmpty())
								activePage.closeEditors(toclose.toArray(new IEditorReference[toclose.size()]), false);
						}
					}
				});
				return Status.OK_STATUS;
			}

		};
		wjob.schedule();
	}
}
