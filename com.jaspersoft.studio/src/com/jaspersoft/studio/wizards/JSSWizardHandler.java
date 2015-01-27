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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

import com.jaspersoft.studio.data.wizard.FileDataAdapterWizardHandler;

/**
 * Abstract handler for commands that launch custom wizards in Jaspersoft Studio.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 * @see FileDataAdapterWizardHandler
 */
public abstract class JSSWizardHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String wizardId = getWizardIdParameterId();

		IWorkbenchWindow activeWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		if (wizardId == null) {
			IAction wizardAction = createWizardChooserDialogAction(activeWindow);
			wizardAction.run();
		} else {

			IWizardRegistry wizardRegistry = getWizardRegistry();
			IWizardDescriptor wizardDescriptor = wizardRegistry.findWizard(wizardId);
			if (wizardDescriptor == null) {
				throw new ExecutionException("unknown wizard: " + wizardId); //$NON-NLS-1$
			}

			try {
				IWorkbenchWizard wizard = wizardDescriptor.createWizard();
				ISelection selection = activeWindow.getSelectionService().getSelection();
				IStructuredSelection ss = selection instanceof IStructuredSelection ? (IStructuredSelection) selection
						: StructuredSelection.EMPTY;
				wizard.init(PlatformUI.getWorkbench(), ss);

				if (wizardDescriptor.canFinishEarly() && !wizardDescriptor.hasPages()) {
					wizard.performFinish();
					return null;
				}

				Shell parent = activeWindow.getShell();
				createAndOpenWizardDialog(parent, wizard);

			} catch (CoreException ex) {
				throw new ExecutionException("error creating wizard", ex); //$NON-NLS-1$
			}

		}

		return null;
	}

	/**
	 * Creates and opens the wizard dialog.
	 * <p>
	 * 
	 * Default implementation uses the {@link WizardDialog} class.<br>
	 * Subclasses can for example modify the behavior contributing of the standard buttonbar contributing a new one. They
	 * can easily do this by using their one {@link WizardDialog} implementation.
	 * 
	 * @param parent
	 *          the parent shell
	 * @param wizard
	 *          the wizard to be created in the dialog
	 */
	protected void createAndOpenWizardDialog(Shell parent, IWizard wizard) {
		WizardDialog dialog = new WizardDialog(parent, wizard);
		dialog.create();
		dialog.open();
	}

	/**
	 * Returns an <code>IAction</code> that opens a dialog to allow the user to choose a wizard.
	 * 
	 * @param window
	 *          The workbench window to use when constructing the action.
	 * @return An <code>IAction</code> that opens a dialog to allow the user to choose a wizard.
	 */
	protected abstract IAction createWizardChooserDialogAction(IWorkbenchWindow window);

	/**
	 * Returns the id of the parameter used to indicate which wizard this command should launch.
	 * 
	 * @return The id of the parameter used to indicate which wizard this command should launch.
	 */
	protected abstract String getWizardIdParameterId();

	/**
	 * Returns the wizard registry for the concrete <code>WizardHandler</code> implementation class.
	 * 
	 * @return The wizard registry for the concrete <code>WizardHandler</code> implementation class.
	 */
	protected abstract IWizardRegistry getWizardRegistry();
}
