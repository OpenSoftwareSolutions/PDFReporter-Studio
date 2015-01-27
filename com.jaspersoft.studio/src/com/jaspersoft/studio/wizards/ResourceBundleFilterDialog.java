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
package com.jaspersoft.studio.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

/**
 * This class extends the classic eclipse dialog to find a resource, so it now display
 * only resources with extension .properties and that belong to the passed java project 
 * or to one of its dependences
 * 
 * @author Orlandin Marco
 *
 */
public class ResourceBundleFilterDialog extends FilteredResourcesSelectionDialog{
	
	/**
	 * The displayed resources belong to this project or to one of its dependences
	 */
	private IJavaProject javaProject;
	
	/**
	 * ResourceProxyVisitor to visit resource tree and get matched resources.
	 * During visit resources it updates progress monitor and adds matched
	 * resources to ContentProvider instance.
	 * 
	 * Its essentially a refactoring of the FilteredResourcesSelectionDialog:ResourceProxyVisitor,
	 * since it was private it was exported in this class
	 */
	public class ResourceProxyVisitor implements IResourceProxyVisitor {

		private AbstractContentProvider proxyContentProvider;

		private ResourceFilter resourceFilter;

		private IProgressMonitor progressMonitor;

		/**
		 * Creates new ResourceProxyVisitor instance.
		 * 
		 * @param contentProvider
		 * @param resourceFilter
		 * @param progressMonitor
		 * @throws CoreException
		 */
		public ResourceProxyVisitor(AbstractContentProvider contentProvider, ResourceFilter resourceFilter, IProgressMonitor progressMonitor) throws CoreException {
			super();
			this.proxyContentProvider = contentProvider;
			this.resourceFilter = resourceFilter;
			this.progressMonitor = progressMonitor;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceProxyVisitor#visit(org.eclipse.core.resources.IResourceProxy)
		 */
		public boolean visit(IResourceProxy proxy) {

			if (progressMonitor.isCanceled())
				return false;

			IResource resource = proxy.requestResource();

			proxyContentProvider.add(resource, resourceFilter);

			if (resource.getType() == IResource.FOLDER && resource.isDerived()
					&& !resourceFilter.isShowDerived()) {

				return false;
			}

			if (resource.getType() == IResource.FILE) {
				return false;
			}

			return true;
		}
	}
	
	/**
	 * Shows a list of resources to the user with a text entry field for a string
	 * pattern used to filter the list of resources. The displayed resource has the 
	 * .properties extension
	 * 
	 * @param shell the parent shell
	 * @param multi the multi selection flag
	 * @param container the project containing the slectable resources
	 * 
	 */
	public ResourceBundleFilterDialog(Shell shell, boolean multi, IJavaProject container) {
		super(shell, multi, container.getProject(),  IResource.FILE);
		this.javaProject = container;
	}
	
	/**
	 * Return an array of resources of the passed container and of the dependences 
	 * of the java project
	 * 
	 * @param projectContainer
	 * @return an array of resources (can be null)
	 */
	private IResource[] getMembers(IContainer projectContainer){
		IResource[] members;
		try {
			members = projectContainer.members();
			List<IResource> resources = new ArrayList<IResource>(Arrays.asList(members)); 
			String[] dependencies = javaProject.getRequiredProjectNames();
			IWorkspaceRoot rootWorkspace = javaProject.getResource().getWorkspace().getRoot();
			for(String projectName : dependencies){
				IProject project = rootWorkspace.getProject(projectName);
				if (project.exists()) resources.addAll(Arrays.asList(project.members()));
			}
			return resources.toArray(new IResource[resources.size()]);
		} catch (CoreException e) {
			e.printStackTrace();
			return new IResource[0];
		}
	}
	
	/**
	 * Provide the elements on which the search pattern will be applied. 
	 * The element are the resources of the project container and the resources
	 * of its dependencies projects
	 */
	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor progressMonitor) throws CoreException {
		if (itemsFilter instanceof ResourceFilter) {
			IContainer projectContainer = javaProject.getProject();
			IResource[] members = getMembers(projectContainer);
			progressMonitor.beginTask("Searching", members.length);
			
			ResourceProxyVisitor visitor = new ResourceProxyVisitor(
					contentProvider, (ResourceFilter) itemsFilter,
					progressMonitor);
			
			if (visitor.visit(projectContainer.createProxy())) {
				for (int i= 0; i < members.length; i++) {
					IResource member = members[i];
					if (member.isAccessible())
						member.accept(visitor, IResource.NONE);
					progressMonitor.worked(1);
					if (progressMonitor.isCanceled())
						break;
				}
			}
			
		}
		progressMonitor.done();
	}
	
	/**
	 * Create the filter that does the pattern matching, this particular pattern is composed 
	 * of a variable part typed by the user and from a fixed part. The fixed part in particular
	 * exclude all the element that hasen't the properties extension, independently from what
	 * typed from the user. If the user dosen't type anything it is returned everything with 
	 * a .properties extension
	 */
	protected ItemsFilter createFilter() {
		return new ResourceFilter(){
			
			@Override
			public String getPattern() {
				String pattern = super.getPattern();
				if (pattern.isEmpty()) return "*";
				return pattern;
			}
			
			public boolean matchItem(Object item) {
				if (!(item instanceof IResource))	return false;
				IResource resource = (IResource) item;
				String name = resource.getName();
				if (!name.endsWith(".properties")) return false;
				return super.matchItem(item);
				
			}
		};
	}
	
}
