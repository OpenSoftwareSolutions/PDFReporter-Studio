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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.translation.resources.TranslationFile;
import com.jaspersoft.translation.resources.TranslationInformation;
import com.jaspersoft.translation.resources.TranslationPackage;
import com.jaspersoft.translation.resources.TranslationProjectNature;

/**
 * This method return an object that has inside all the resources and information of the selected
 * translation project. If the selection is not a project or a translation project it return null
 * 
 * @author Orlandin Marco
 *
 */
public class ProvideSelectedTranslation  {

	/**
	 * Create an entry in the TranslationInformation for a package and all the files inside it
	 * 
	 * @param pluginDir the package path
	 * @param pluginContainer the TranslationInformation of the plugin where this package is contained
	 */
	private void createPackageInformation(File pluginDir, TranslationInformation pluginContainer){
		for (File pluginFile : pluginDir.listFiles()){
			if (pluginFile.isDirectory()){
				TranslationPackage packageResource = new TranslationPackage(pluginFile.getAbsolutePath(), pluginFile.getName());
				for (File packageFile : pluginFile.listFiles()){
					if (!packageFile.isDirectory()){
						packageResource.addFile(new TranslationFile(packageFile.getAbsolutePath(), packageFile.getName()));
					}
				}
				pluginContainer.addResource(packageResource);
			} else {
				pluginContainer.addResource(new TranslationFile(pluginFile.getAbsolutePath(), pluginFile.getName()));
			}
		}
	}
	
	/**
	 * From a path to a translation project build a list with all its resources. Since a translation project is intended
	 * for the plugin translation we assume that all the folder inside the translation project represents a pluging, and 
	 * all the folder inside the represent a package. For this reason this method dosen't consider the file inside the project 
	 * folder and the subfolder under the second level
	 * 
	 * @param rootPath the path of the project
	 * @return not null list of all the project resources
	 */
	private List<TranslationInformation> getResources(String rootPath)
	{
		List<TranslationInformation> projectInfo = new ArrayList<TranslationInformation>();
		File folder = new File(rootPath);
		for (File plugin : folder.listFiles()){
			if (plugin.isDirectory()){
				TranslationInformation pluginResource = new TranslationInformation(plugin.getName());
				createPackageInformation(plugin, pluginResource);
				projectInfo.add(pluginResource);
			}
		}
		return projectInfo;
	}

	/**
	 * Return the resources of the selected project
	 * 
	 * @return a list with all the resources of the translation project actually selected. Can be null
	 * if the actual selection dosen't contain a translation project
	 */
	public List<TranslationInformation> execute(){
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null && activeWorkbenchWindow.getActivePage() != null) {
		 ISelection selection = activeWorkbenchWindow.getActivePage().getSelection();
			if (selection instanceof IStructuredSelection) {
				for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
					Object element = it.next();
					IProject project = null;
					if (element instanceof IProject) {
						project = (IProject) element;
					} else if (element instanceof IAdaptable) {
						project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
					}
					try {
						if (project.hasNature(TranslationProjectNature.NATURE_ID)) return getResources(project.getLocation().toString());
						else return null;
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
