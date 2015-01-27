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
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.jaspersoft.translation.resources.ISendTranslation;

/**
 * Command called when the user select the send translation on the contextual
 * menu of a translation project. It create a zip of the project and for each 
 * Contributed sender it call the sendTranslation method
 * 
 * @author Orlandin Marco
 *
 */
public class SendTranslationCommand implements IHandler {

	/**
	 * Create a zip of the project folder
	 * 
	 * @param rootPath the path of the project
	 * @return a java.io.file pointing to the zip file
	 */
	private File zipResources(String rootPath){
		String tmpDirectory = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		String zipFilePath = tmpDirectory;
		if(!(tmpDirectory.endsWith("/") || tmpDirectory.endsWith("\\"))){ //$NON-NLS-1$ //$NON-NLS-2$
			zipFilePath += System.getProperty("file.separator"); //$NON-NLS-1$
		}
		zipFilePath += "translationFiles.zip"; //$NON-NLS-1$
		
		ZipUtils zipUtils = new ZipUtils();
		boolean result = zipUtils.zipFiles(rootPath, zipFilePath);
		if (result) return new File(zipFilePath);
		else return null;
	}
	
	/**
	 * Send the translation for a specific project to all the extension qualified as sender. 
	 * The sender are loaded from an extension point
	 * 
	 * @param project 
	 */
	private void sendTranslation(IProject project){
		File content = zipResources(project.getLocation().toString());
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("com.jaspersoft.studio.babel.editor.resourceSender");
		for(IConfigurationElement element : elements){
			Object input;
			try {
				input = element.createExecutableExtension("class");
				if (input instanceof ISendTranslation){
					ISendTranslation sender = (ISendTranslation)input;
					sender.sendTranslation(content);
				}
			} catch (CoreException e) {
					e.printStackTrace();
			}
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	   ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
				}
				sendTranslation(project);
			}
		}
		return null;
	}
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}
