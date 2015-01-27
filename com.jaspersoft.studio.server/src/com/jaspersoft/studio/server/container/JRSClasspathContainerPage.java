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
package com.jaspersoft.studio.server.container;

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.server.messages.Messages;

public class JRSClasspathContainerPage extends WizardPage implements IClasspathContainerPage, IClasspathContainerPageExtension {
	private ArrayList<IPath> fUsedPaths;

	public JRSClasspathContainerPage() {
		super("Jaspersoft Server Library"); //$NON-NLS-1$
		setTitle(Messages.JRSClasspathContainerPage_0);
		setDescription(Messages.JRSClasspathContainerPage_1);
		setPageComplete(true);
		fUsedPaths = new ArrayList<IPath>();
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());

		Label lbl = new Label(composite, SWT.NONE);
		lbl.setText(Messages.JRSClasspathContainerPage_2);
		lbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));

		setControl(composite);
	}

	@Override
	public void initialize(IJavaProject project, IClasspathEntry[] currentEntries) {
		JRSClasspathContainer jrcc = new JRSClasspathContainer(null, project);
		for (int i = 0; i < currentEntries.length; i++) {
			IClasspathEntry curr = currentEntries[i];
			if (curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				fUsedPaths.add(curr.getPath());
			}
		}
	}

	@Override
	public boolean finish() {
		return true;
	}

	@Override
	public void setSelection(IClasspathEntry containerEntry) {
	}

	@Override
	public IClasspathEntry getSelection() {
		return JavaCore.newContainerEntry(JRSClasspathContainer.ID);
	}

}
