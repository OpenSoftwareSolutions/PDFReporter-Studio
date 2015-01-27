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
package net.sf.jasperreports.eclipse.builder.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.builder.JasperReportsNature;
import net.sf.jasperreports.eclipse.classpath.container.JRClasspathContainer;
import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/*
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperCompileManager.java 1229 2006-04-19 13:27:35 +0300 (Wed, 19 Apr 2006) teodord $
 */
public class ToggleNatureAction implements IObjectActionDelegate {

	private ISelection selection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			for (Iterator it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
				}
				changeProjectSettings(project);
			}
		}
	}

	public void changeProjectSettings(final IProject project) {
		if (project != null) {
			Job job = new Job(Messages.ToggleNatureAction_JobName) {
				@Override
				protected IStatus run(final IProgressMonitor monitor) {
					try {
						toggleNature(project, monitor);
					} catch (Exception e) {
						UIUtils.showError(e);
					} finally {
						monitor.done();
					}
					return Status.OK_STATUS;
				}

			};
			job.setPriority(Job.LONG);
			job.schedule();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.
	 * action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		ISelection s = targetPart.getSite().getSelectionProvider().getSelection();
		if (s instanceof TreeSelection) {
			TreePath[] tp = ((TreeSelection) s).getPaths();
			for (int i = 0; i < tp.length; i++) {
				Object obj = tp[i].getFirstSegment();
				if (obj instanceof IProject) {
					try {
						if (((IProject) obj).hasNature(JasperReportsNature.NATURE_ID))
							action.setChecked(true);
						else {
							action.setChecked(false);
							break;
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Toggles sample nature on a project
	 * 
	 * @param project
	 *          to have sample nature added or removed
	 */
	private void toggleNature(IProject project, IProgressMonitor monitor) {
		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			IJavaProject javaProject = JavaCore.create(project);
			for (int i = 0; i < natures.length; ++i) {
				if (JasperReportsNature.NATURE_ID.equals(natures[i])) {
					// Remove the nature
					String[] newNatures = new String[natures.length - 1];
					System.arraycopy(natures, 0, newNatures, 0, i);
					System.arraycopy(natures, i + 1, newNatures, i, natures.length - i - 1);
					description.setNatureIds(newNatures);
					project.setDescription(description, monitor);

					// Path to all libraries needed

					List<IClasspathEntry> centries = new ArrayList<IClasspathEntry>();
					IClasspathEntry[] entries = javaProject.readRawClasspath();
					Set<Path> set = JasperReportsPlugin.getClasspathContainerManager().getRemovableContainers();
					for (IClasspathEntry en : entries) {
						if (en.getPath().equals(JRClasspathContainer.ID))
							continue;
						if(set.contains(en.getPath()))
							continue;
						centries.add(en);
					}
					javaProject.setRawClasspath(centries.toArray(new IClasspathEntry[centries.size()]), monitor);
					return;
				}
			}

			// Add the nature
			ProjectUtil.addNature(project, JasperReportsNature.NATURE_ID, monitor);
			ProjectUtil.createJRClasspathContainer(monitor, javaProject);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
