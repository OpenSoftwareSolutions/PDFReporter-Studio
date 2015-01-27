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
package net.sf.jasperreports.eclipse.wizard.project;

import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.ui.validator.EmptyStringValidator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class JRProjectPage extends WizardPage {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JRProjectPage() {
		super("JRPROJECTPAGE"); //$NON-NLS-1$
		setTitle(Messages.JRProjectPage_Title);
		setDescription(Messages.JRProjectPage_Description);
	}

	public void createControl(Composite parent) {
		DataBindingContext dbc = new DataBindingContext();
		WizardPageSupport.create(this, dbc);

		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout(2, false));

		new Label(composite, SWT.NONE).setText(Messages.JRProjectPage_LblName);

		Text tname = new Text(composite, SWT.BORDER);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd);

		dbc.bindValue(SWTObservables.observeText(tname, SWT.Modify),
				PojoObservables.observeValue(this, "name"), //$NON-NLS-1$
				new UpdateValueStrategy()
						.setAfterConvertValidator(new EmptyStringValidator() {
							@Override
							public IStatus validate(Object value) {
								IStatus s = super.validate(value);
								if (s.equals(Status.OK_STATUS)) {
									IProject[] prjs = ResourcesPlugin
											.getWorkspace().getRoot()
											.getProjects();
									for (IProject p : prjs) {
										if (p.getName().equals(value))
											return ValidationStatus
													.error(Messages.JRProjectPage_ErrorExistingProject);
									}
								}
								return s;
							}
						}), null);
	}
}
