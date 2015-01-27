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

/**
 * Listener can detect when something changes in the page of a wizard
 * by implementing this interface.
 * 
 * JSSWizard autoregister itself to get this event for all JSSWizardPage's
 * added to the wizard.
 * 
 * @author gtoffoli
 *
 */
public interface JSSWizardPageChangeListener {

	  /**
	   * 
	   * @param event containing the changed page
	   */
		public void pageChanged(JSSWizardPageChangeEvent event);
	
}
