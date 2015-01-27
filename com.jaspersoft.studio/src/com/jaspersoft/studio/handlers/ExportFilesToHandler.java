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
package com.jaspersoft.studio.handlers;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.datatransfer.FileSystemExportWizard;


/**
 * The command handler that gets invoked when the "Export Files to..." command is invoked.
 * It launches the {@link FileSystemExportWizard} wizard in order to allow the user to
 * save the selected files (if any) to an external file system location.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 */
public class ExportFilesToHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FileSystemExportWizard wizard = new FileSystemExportWizard();
		wizard.init(getActiveWorkbench(event), getActiveSelection(event));
		WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
		dialog.open();
		return null;
	}

	/*
	 * Gets the current active selection.
	 * 1. Verifies that all selected objects are of type IProject/IFile/IFolder/IPackageFragment
	 * in order to automatically propose the related files already selected.
	 * 2. If at least one the element is not "allowed" then it will return an empty selection
	 * leaving to the wizard the task to try recovering the proposed selection, for example from
	 * the currently active JRXML editor.
	 */
	private IStructuredSelection getActiveSelection(ExecutionEvent event) {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection instanceof IStructuredSelection && !currentSelection.isEmpty()) {
			for (Object o : ((IStructuredSelection) currentSelection).toArray()) {
				if (!(o instanceof IFile || o instanceof IFolder || 
						o instanceof IPackageFragment || o instanceof IProject)) {
					return StructuredSelection.EMPTY;
				}
			}
			return (IStructuredSelection)currentSelection;
		}
		return StructuredSelection.EMPTY;
	}

	/*
	 * Returns the current active workbench.
	 */
	private IWorkbench getActiveWorkbench(ExecutionEvent event){
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbench activeWorkbench = PlatformUI.getWorkbench();
		if(activeWorkbenchWindow!=null){
			activeWorkbench=activeWorkbenchWindow.getWorkbench();
		}
		return activeWorkbench;
	}
}
