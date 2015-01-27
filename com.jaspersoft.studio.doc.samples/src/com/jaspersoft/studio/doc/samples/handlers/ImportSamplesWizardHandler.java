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
package com.jaspersoft.studio.doc.samples.handlers;
import net.sf.jasperreports.eclipse.ui.validator.EmptyStringValidator;
import net.sf.jasperreports.eclipse.wizard.project.JRProjectPage;
import net.sf.jasperreports.samples.wizards.SampleNewWizard;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.doc.samples.messages.Messages;


/**
 * Action for the cheatsheet to open the wizard to import the sample reports into the workspace
 * 
 * @author Orlandin Marco
 *
 */
public class ImportSamplesWizardHandler extends Action {
	
		/**
		 * Redefinitions of the wizard page to adapt it to the cheatsheets, and adding also the contextual help
		 * 
		 * @author Orlandin Marco
		 *
		 */
		private class JRFixedProjectPage extends JRProjectPage{
		
			/**
			 * Default workspace directory name for the samples
			 */
			private String defaultName = "JasperReportsSamples"; //$NON-NLS-1$
			
			/**
			 * Context name for the help
			 */
			private String contextName = "com.jaspersoft.studio.doc.select_sample_location"; //$NON-NLS-1$
			
			/**
			 * 
			 * @param value a workspace folder name
			 * @return true if the folder is already present in the workspace, otherwise false
			 */
			private boolean isProjectPresent(Object value){
				IProject[] prjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				for (IProject p : prjs) {
					if (p.getName().equals(value))
						return true;
				}
				return false;
			}
			
			@Override
			public void createControl(Composite parent) {
				DataBindingContext dbc = new DataBindingContext();
				WizardPageSupport.create(this, dbc);
	
				Composite composite = new Composite(parent, SWT.NONE);
				setControl(composite);
				composite.setLayout(new GridLayout(2, false));
	
				new Label(composite, SWT.NONE).setText(Messages.ImportSamplesWizardHandler_name_label);
	
				Text tname = new Text(composite, SWT.BORDER);
				tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd);
	
				dbc.bindValue(SWTObservables.observeText(tname, SWT.Modify),PojoObservables.observeValue(this, "name"), 				
						new UpdateValueStrategy().setAfterConvertValidator(new EmptyStringValidator() {
									@Override
									public IStatus validate(Object value) {
										IStatus s = super.validate(value);
										if (s.equals(Status.OK_STATUS)) {
												if (isProjectPresent(value)){
													return ValidationStatus.error(Messages.ImportSamplesWizardHandler_plugin_exist);
												} else if (value.equals(defaultName))
													return ValidationStatus.info(Messages.ImportSamplesWizardHandler_suggested_name);
										}
										return s;
									}
								}), null);
			}
			
			/**
			 * When the main control is set, a listener for the help request is added to it
			 */
			@Override
			protected void setControl(Control newControl) {
				super.setControl(newControl);
				newControl.addListener(SWT.Help, new Listener() {			
					@Override
					public void handleEvent(Event event) {
						performHelp();	
					}
				});
				setHelpData();
			};
	
			/**
			 * Set the help data that should be seen in this step
			 */
			public void setHelpData(){
				PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),contextName);
			}
			
			/**
			 * Set and show the help data if a context, that bind this wizard with the data, is provided
			 */
			@Override
			public void performHelp() {
				PlatformUI.getWorkbench().getHelpSystem().displayHelp(contextName);
			};
			
	}
		
	@Override
	public void run() {
		SampleNewWizard wizard = new SampleNewWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(), new JRFixedProjectPage());
		WizardDialog dialogToOpen = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
		dialogToOpen.open();
	}

	
}
