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
package com.jaspersoft.studio.server.publish.wizard.page;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Wizard page for the selection of a datasource for a resource being
 * created/modified into a remote JasperServer repository.
 * 
 * @author Chicu Veaceslav (schicu@jaspersoft.com)
 * 
 */
public class FileSelectionPage extends JSSHelpWizardPage {

	public static final String PAGE_NAME = "fileSelectionPage"; //$NON-NLS-1$

	public FileSelectionPage(JasperReportsConfiguration jConfig) {
		super(PAGE_NAME);
		setTitle("File Selection");
		setDescription("Select a file.");
	}

	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_SELECT_DATASOURCES;
	}

	@Override
	public void createControl(Composite parent) {
		final Composite cmp = new Composite(parent, SWT.NONE);
		setControl(cmp);
		cmp.setLayout(new GridLayout(2, false));

		Label lbl = new Label(cmp, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		lbl.setLayoutData(gd);

		final Text tfile = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		tfile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button b = new Button(cmp, SWT.PUSH);
		b.setText(Messages.common_browse);
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(UIUtils.getShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
				dialog.setTitle(Messages.ResourceCellEditor_open_resource);
				dialog.setInitialPattern("*.jrxml"); //$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					file = (IFile) dialog.getFirstResult();
					tfile.setText(file.getFullPath().toOSString());
				}
			}
		});
	}

	private IFile file;

	public IFile getFile() {
		return file;
	}
}
