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


import java.util.List;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Class that implement the wizard to export a report as a template. the wizard is composed
 * of three steps but only the first two are important for the exporting process, the third is
 * only a "Congratulation" step
 * 
 * @author Orlandin Marco
 *
 */
public class TemplateExporterWizard extends Wizard implements IExportWizard {

	/**
	 * The selected report
	 */
	private IStructuredSelection selection;
	
	/**
	 * First page of the wizard: resource exporting and destination path
	 */
	private ResourcePage firstPage;
	
	/**
	 * Second step of the wizard: report type and categories
	 */
	private CategoriesPage secondPage;
	
	/**
	 * A ConfilictDetailsError, but with three buttons: Yes, No and Cancel.
	 * Take note that the code returned for the Yes button is IDialogConstants.OK_ID 
	 * and the id for the No button is IDialogConstants.CANCEL_ID
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class YesNoDetailsError extends ConflictDetailsError{

		public YesNoDetailsError(Shell parentShell, String dialogTitle, String message, IStatus status, int displayMask) {
			super(parentShell, dialogTitle, message, status, displayMask);
		}
		
		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			createButton(parent, IDialogConstants.OK_ID, IDialogConstants.YES_LABEL,	false);
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.NO_LABEL,	true);
			createDetailsButton(parent);
		}
		
		protected void setShellStyle(int newShellStyle) {
			super.setShellStyle(newShellStyle | SWT.SHEET);
		}
	}

	/**
	 * Create the three step and add them to the report, but only if a report is selected
	 */
	@Override
	public void addPages() {
		super.addPages();
		if (selection.getFirstElement() instanceof IFile){
			IFile reportFile = (IFile)selection.getFirstElement();
			try {
				firstPage = new ResourcePage(reportFile);
				secondPage = new CategoriesPage();
				
				addPage(firstPage);
				addPage(secondPage);
				addPage(createCongratPage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
	
	/**
	 * Create the congratulations page, setting the proper grid data. The last page in particular is 
	 * used here to define the size of the wizard dialog
	 * 
	 * @return
	 */
	private JSSHelpWizardPage createCongratPage(){	
		return new FinalPage();
	}
	
	/**
	 * Check if the actual selection is valid to open the wizard, so it must be not null and 
	 * not empty
	 * 
	 * @return true if the actual selection is valid to open this wizard, otherwise false
	 */
	public boolean canOpen(){
		return selection != null && !selection.isEmpty();
	}
	
	
	/**
	 * Initialize the selection of the wizard, by searching an IFile in the selected element
	 * in the Project Explorer or in the actually opened and focused editor
	 * 
	 * @param workbench 
	 * @param currentSelection the element actually selected
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
			this.selection = currentSelection;
      @SuppressWarnings("rawtypes")
			List selectedResources = IDE.computeSelectedResources(currentSelection);
      if (!selectedResources.isEmpty()) {
          this.selection = new StructuredSelection(selectedResources);
      }

      // look it up if current selection (after resource adapting) is empty
      if (selection.isEmpty() && workbench.getActiveWorkbenchWindow() != null) {
          IWorkbenchPage page = workbench.getActiveWorkbenchWindow()
                  .getActivePage();
          if (page != null) {
              IEditorPart currentEditor = page.getActiveEditor();
              if (currentEditor != null) {
                  Object selectedResource = currentEditor.getEditorInput()
                          .getAdapter(IResource.class);
                  if (selectedResource != null) {
                      selection = new StructuredSelection(selectedResource);
                  }
              }
          }
      }
			setWindowTitle(Messages.TemplateExporterWizard_title);
			setNeedsProgressMonitor(false);
	}
	

	
	/**
	 * Display a error message when the selected template type is not compatible with the exported report, in the detail
	 * section are listed the founded error
	 * 
	 * @param errors List of all the validation errors found using the selected report as template of the selected type
	 * 
	 * @return the code of the pressed button on the dialog
	 */
	@SuppressWarnings("unused")
	private int createErrorMessage(List<String> errors){
		String conf = ""; //$NON-NLS-1$
		for(String error : errors){
			conf += error.concat("\n"); //$NON-NLS-1$ 
		}

		IStatus status = new OperationStatus(IStatus.ERROR, JasperReportsPlugin
				.getDefault().getPluginID(), OperationStatus.NOTHING_TO_REDO,
				conf, null);
		int result = new YesNoDetailsError(
				UIUtils.getShell(),
				Messages.TemplateExporterWizard_errorTitle,
				Messages.TemplateExporterWizard_errorMessage,
				status, IStatus.OK | IStatus.INFO | IStatus.WARNING
						| IStatus.ERROR).open();
		return result;
	}
	
	/**
	 * Return a list of validation errors for the selected report with the selected engine
	 * the list is void if there aren't errors
	 * 
	 * @return a list of strings where every string is a validation error message
	 */
	public List<String> getValidationErrors(){
		return secondPage.validateWithSelectedEngine(firstPage.getDesign());
	}
	
	/**
	 * Return the path selected as destination for the template
	 * 
	 * @return the destination path
	 */
	public String getPath(){
		return firstPage.getDestinationPath();
	}

	/**
	 * The finish of the wizard call the finish of every single step
	 */
	@Override
	public boolean performFinish() {		
		/* The validation is done on the transition between the second and third step
		 * List<String> validationError = getValidationErrors();
		if (validationError.size()>0){
			if (createErrorMessage(validationError) == IDialogConstants.CANCEL_ID) return false;
		}*/
		firstPage.finish();
		secondPage.finish(firstPage.getDesign().getName(),firstPage.getDestinationPath());
		return true;
	}

}
