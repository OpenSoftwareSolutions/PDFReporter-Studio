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
package com.jaspersoft.studio.server.wizard.find;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

final class FindWizardDialog extends WizardDialog {
	FindWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}

	@Override
	public void finishPressed() {
		super.finishPressed();
	}

	@Override
	public void run(boolean fork, boolean cancelable,
			final IRunnableWithProgress runnable)
			throws InvocationTargetException, InterruptedException {
		ProgressMonitorPart mpart = (ProgressMonitorPart) getProgressMonitor();
		mpart.setVisible(true);
		try {

			ModalContext.run(runnable, fork, getProgressMonitor(), getShell()
					.getDisplay());
		} finally {
			mpart.done();
			mpart.setVisible(false);
		}
	}
}
