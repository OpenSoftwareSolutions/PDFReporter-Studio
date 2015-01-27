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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.jaspersoft.studio.messages.Messages;

/**
 * Action to export a report and it resources to use it as a template
 * 
 * @author Marco Orlandin
 *
 */
public class ExportAsTemplateHandler extends AbstractHandler {

	/**
	 * Create the wizard and if the selection is valid it will be opened, otherwise
	 * an error message is shown
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TemplateExporterWizard wizard = new TemplateExporterWizard();
		wizard.init(getActiveWorkbench(event), getActiveSelection(event));
		if (wizard.canOpen()){
			WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
			int exitCode = dialog.open();
			if (exitCode != WizardDialog.CANCEL){
				MessageBox messageBox = new MessageBox(UIUtils.getShell(), SWT.ICON_INFORMATION |SWT.OK);
				messageBox.setText(Messages.ExportAsTemplateHandler_successTitle);
			  messageBox.setMessage(Messages.ExportAsTemplateHandler_successMessage+wizard.getPath());
			  messageBox.open();
			}
		} else {
			 MessageBox messageBox = new MessageBox(UIUtils.getShell(), SWT.ICON_WARNING |SWT.OK);
			 messageBox.setText(Messages.ExportAsTemplateHandler_errorTitle);
		   messageBox.setMessage(Messages.ExportAsTemplateHandler_errorMessage);
		   messageBox.open();
		}
		return null;
	}

	/*
	 * Gets the current active selection.
	 * 1. Verifies that all selected object is of typeIFile
	 * in order to automatically propose the related files already selected.
	 * 2. If at least one the element is not "allowed" then it will return an empty selection
	 * leaving to the wizard the task to try recovering the proposed selection, for example from
	 * the currently active JRXML editor.
	 */
	private IStructuredSelection getActiveSelection(ExecutionEvent event) {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection instanceof IStructuredSelection && !currentSelection.isEmpty()) {
			for (Object o : ((IStructuredSelection) currentSelection).toArray()) {
				if (!(o instanceof IFile)) {
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
