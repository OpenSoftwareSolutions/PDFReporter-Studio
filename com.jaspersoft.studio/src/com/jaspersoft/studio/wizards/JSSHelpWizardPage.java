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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * Extend a simple wizard page to provide an easy way to define a contextual help
 * 
 * @author Orlandin Marco
 *
 */
public abstract class JSSHelpWizardPage extends WizardPage implements ContextData {

	private String contextName;
	
	protected JSSHelpWizardPage(String pageName) {
		super(pageName);
		 contextName = getContextName();
	}
	

  /**
   * Creates a new wizard page with the given name, title, and image.
   *
   * @param pageName the name of the page
   * @param title the title for this wizard page,
   *   or <code>null</code> if none
   * @param titleImage the image descriptor for the title of this wizard page,
   *   or <code>null</code> if none
   */
  protected JSSHelpWizardPage(String pageName, String title, ImageDescriptor titleImage) {
  	super(pageName, title, titleImage);
		 contextName = getContextName();
  }
	
	/**
	 * This method is called by the constructor of JSSHelpWizardPage to initialize the context name 
	 * of a wizard page. With the context name a contextual help will be available. This method 
	 * should be defined in the subclasses to provide a context name for each wizard page. If 
	 * this method return null the context will not used, and the behavior of this wizard page will 
	 * be a default one without help.
	 */
	protected abstract String getContextName();
	
	protected void setContextName(String contextName){
		this.contextName = contextName;
	}
	
	/**
	 * Set the root control of the wizard, and also add a listener to do the perform help action 
	 * and set the context of the top control.
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
	@Override
	public void setHelpData(){
		if (contextName != null){
			PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),contextName);
		}
	}
	
	/**
	 * Set and show the help data if a context, that bind this wizard with the data, is provided
	 */
	@Override
	public void performHelp() {
		if (contextName != null){
			PlatformUI.getWorkbench().getHelpSystem().displayHelp(contextName);
		}
	};
	

}
