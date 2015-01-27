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
package com.jaspersoft.studio.property.dataset.dialog;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

public class RunWithProgressBar implements IRunnableContext {

	private ProgressBar pb;

	public RunWithProgressBar(Composite comp) {
		pb = new ProgressBar(comp, SWT.HORIZONTAL | SWT.INDETERMINATE);
		// pb.setMinimum(0);
		// pb.setMaximum(100);
		// pb.setSelection(50);
		// pb.setBounds(10, 10, 200, 30);
		GridData gd = new GridData();
		gd.widthHint = 100;
		gd.heightHint = 20;
		pb.setLayoutData(gd);
		pb.setVisible(false);
	}

	public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws InvocationTargetException,
			InterruptedException {
		IProgressMonitor monitor = getProgressMonitor();

		ModalContext.run(runnable, true, monitor, Display.getDefault());
	}

	public IProgressMonitor getProgressMonitor() {
		return new ProgressBarMonitor(pb);
	}

	class ProgressBarMonitor implements IProgressMonitor {
		private ProgressBar progressBar;
		private boolean cancelled;

		/**
		 * Create a new ProgressBarMonitor.
		 * 
		 * @param progressBar
		 *          the ProgressBar control
		 */
		public ProgressBarMonitor(ProgressBar progressBar) {
			this.progressBar = progressBar;
		}

		public void beginTask(String name, int totalWork) {
			cancelled = false;
			if (!progressBar.isDisposed()) {
				progressBar.setSelection(0);
				progressBar.setMinimum(0);
				progressBar.setMaximum(totalWork);
				progressBar.setVisible(true);
			}
		}

		public void setTaskName(String name) {
			// nothing to do
		}

		public void subTask(String name) {
			// nothing to do
		}

		public void internalWorked(double work) {
		}

		public void worked(int work) {
			if (!progressBar.isDisposed()) {
				progressBar.setSelection(progressBar.getSelection() + work);
			}
		}

		public boolean isCanceled() {
			return cancelled;
		}

		public void setCanceled(boolean value) {
			cancelled = value;
		}

		public void done() {
			if (!progressBar.isDisposed()) {
				progressBar.setVisible(false);
			}
		}
	}
}
