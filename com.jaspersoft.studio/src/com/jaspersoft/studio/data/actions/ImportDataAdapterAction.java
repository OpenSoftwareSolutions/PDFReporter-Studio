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
package com.jaspersoft.studio.data.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.MDataAdapter;
import com.jaspersoft.studio.data.MDataAdapters;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.data.storage.FileDataAdapterStorage;
import com.jaspersoft.studio.messages.Messages;

public class ImportDataAdapterAction extends Action {
	public static final String ID = "importDataAdapteraction"; //$NON-NLS-1$
	private TreeViewer treeViewer;

	public ImportDataAdapterAction(TreeViewer treeViewer) {
		super();
		setId(ID);
		this.treeViewer = treeViewer;
		setText(Messages.ImportDataAdapterAction_name);
		setDescription(Messages.ImportDataAdapterAction_description);
		setToolTipText(Messages.ImportDataAdapterAction_tooltip);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif")); //$NON-NLS-1$ //$NON-NLS-2$
		setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ui", "$nl$/icons/full/dtool16/import_wiz.gif")); //$NON-NLS-1 //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Override
	public boolean isEnabled() {
		Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		return firstElement != null && (firstElement instanceof MDataAdapter || firstElement instanceof MDataAdapters);
	}

	@Override
	public void run() {
		Object obj = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		MDataAdapters mDataAdapters = null;
		if (obj instanceof MDataAdapters)
			mDataAdapters = (MDataAdapters) obj;
		if (obj instanceof MDataAdapter)
			mDataAdapters = (MDataAdapters) ((MDataAdapter) obj).getParent();
		if (mDataAdapters != null) {
			final ADataAdapterStorage storage = mDataAdapters.getValue();

			Job job = new WorkspaceJob("Searching DataAdapters") { //$NON-NLS-1$
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					IWorkspace workspace = ResourcesPlugin.getWorkspace();
					IProject[] projects = workspace.getRoot().getProjects();
					for (IProject prj : projects) {
						if (prj.isOpen())
							scanFolder(prj.members());
					}

					return Status.OK_STATUS;
				}

				protected void scanFolder(IResource[] fileResources) throws CoreException {
					for (IResource r : fileResources) {
						if (r instanceof IFolder)
							scanFolder(((IFolder) r).members());
						else if (r instanceof IFile)
							checkFile((IFile) r);
					}
				}

				protected void checkFile(IFile file) throws CoreException {
					if (file.getName().endsWith(".xml")) { //$NON-NLS-1$
						try {
							final DataAdapterDescriptor das = FileDataAdapterStorage.readDataADapter(file.getContents(),
									file.getProject());
							if (das != null) {
								Display.getDefault().asyncExec(new Runnable() {

									public void run() {
										DataAdapterDescriptor oldDas = storage.findDataAdapter(das.getName());
										if (oldDas != null)
											; // DataAdapterManager.removeDataAdapter(oldDas); replace?
										else
											storage.addDataAdapter("", das); //$NON-NLS-1$
									}
								});

							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			};
			job.schedule();
		}
	}
}
