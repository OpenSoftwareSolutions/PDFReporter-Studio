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

import org.eclipse.jface.wizard.Wizard;

/**
 * This is interface should be implemented by all clients that want to be notified
 * about the status of wizard.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public interface WizardEndingStateListener {

	/**
	 * This method is invoked when the {@link Wizard#performFinish()} method has been called.
	 * It allows to perform additional operations if needed.
	 */
	void performFinishInvoked();
	

	/**
	 * This method is invoked when the {@link Wizard#performCancel()} method has been called.
	 * It allows to perform additional operations if needed.
	 */
	void performCancelInvoked();
	
}
