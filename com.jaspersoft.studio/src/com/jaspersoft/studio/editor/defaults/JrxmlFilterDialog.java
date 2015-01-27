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
package com.jaspersoft.studio.editor.defaults;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

/**
 * This class extends the classic eclipse dialog to find a report, so it now display
 * only resources with extension .jrxml
 * 
 * @author Orlandin Marco
 *
 */
public class JrxmlFilterDialog extends FilteredResourcesSelectionDialog{
	
	
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
	 * .jrxml extension
	 * 
	 * @param shell the parent shell
	 * @param multi the multi selection flag
	 * @param container the project containing the slectable resources
	 * 
	 */
	public JrxmlFilterDialog(Shell shell, boolean multi) {
		super(shell, multi, ResourcesPlugin.getWorkspace().getRoot(),  IResource.FILE);
	}
	
	/**
	 * Create the filter that does the pattern matching, this particular pattern is composed 
	 * of a variable part typed by the user and from a fixed part. The fixed part in particular
	 * exclude all the element that hasen't the properties extension, independently from what
	 * typed from the user. If the user dosen't type anything it is returned everything with 
	 * a .jrxml extension
	 */
	@Override
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
				if (!name.toLowerCase().endsWith(".jrxml")) return false;
				return super.matchItem(item);
				
			}
		};
	}
	
}
