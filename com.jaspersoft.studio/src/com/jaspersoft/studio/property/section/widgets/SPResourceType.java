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
package com.jaspersoft.studio.property.section.widgets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.section.AbstractSection;

public class SPResourceType extends SPText {

	private Button btn;

	public SPResourceType(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public void setReadOnly(boolean readonly) {
		super.setReadOnly(readonly);
		btn.setEnabled(!readonly);
	}
	
	protected SelectionAdapter buttonPressed(){
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(ftext.getShell(), false,
						ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
				dialog.setTitle(Messages.ResourceCellEditor_open_resource);
				dialog.setInitialPattern("*.properties"); //$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					IFile file = (IFile) dialog.getFirstResult();
					if (file != null)
						handleTextChanged(section, pDescriptor.getId(), convertFile2Value(file));
				}
			}
		};
	}


	protected void createComponent(Composite parent) {
		super.createComponent(parent);
		btn = section.getWidgetFactory().createButton(parent, "...", SWT.PUSH);
		btn.addSelectionListener(buttonPressed());
	}

	protected String convertFile2Value(IFile f) {
		return f.getProjectRelativePath().toOSString();
	}

	protected void handleTextChanged(final AbstractSection section, final Object property, String text) {
		if (text != null && text.trim().isEmpty())
			text = null;
		section.changeProperty(property, text);
	}

}
