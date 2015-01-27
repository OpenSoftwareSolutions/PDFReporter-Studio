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
package com.jaspersoft.studio.model.image.command.wizard;

import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.image.MImage;

public class WizardImagePage extends WizardPage {
	private MImage mimage;

	public void setMImage(MImage mimage) {
		this.mimage = mimage;
	}

	public MImage getMImage() {
		return mimage;
	}

	public WizardImagePage() {
		super("connectionpage"); //$NON-NLS-1$
		setTitle(Messages.common_image);
		setDescription(Messages.WizardImagePage_description);
		setImageDescriptor(MImage.getIconDescriptor().getIcon32());
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		setControl(composite);

		Label lbl = new Label(composite, SWT.NONE);
		lbl.setText(Messages.WizardImagePage_image_expression);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		lbl.setLayoutData(gd);

		Text etxt = new Text(composite, SWT.BORDER | SWT.MULTI);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 60;
		etxt.setLayoutData(gd);

		final Button fbut = new Button(composite, SWT.BORDER);
		fbut.setText(Messages.common_browse + "..."); //$NON-NLS-1$
		fbut.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		fbut.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				FilteredResourcesSelectionDialog fd = new FilteredResourcesSelectionDialog(Display.getCurrent()
						.getActiveShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
				fd.setInitialPattern("*.png"); //$NON-NLS-1$
				if (fd.open() == Dialog.OK) {
					File obj = (File) fd.getFirstResult();
					JRDesignExpression jre = new JRDesignExpression();
					jre.setText("\"" + obj.getLocationURI().getPath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
					mimage.setPropertyValue(JRDesignImage.PROPERTY_EXPRESSION, jre);
				}

			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "Jaspersoft.wizard");
	}
}
